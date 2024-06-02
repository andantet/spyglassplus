package com.teamfusion.spyglassplus.entity

import com.teamfusion.spyglassplus.item.SpyglassPlusItems
import com.teamfusion.spyglassplus.sound.SpyglassPlusSoundEvents
import net.minecraft.block.Block
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityPose
import net.minecraft.entity.EntityStatuses
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ItemStackParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.registry.tag.DamageTypeTags
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Arm
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent

abstract class StandEntity(type: EntityType<out LivingEntity>, world: World) : LivingEntity(type, world) {
    var marker: Boolean
        get() = dataTracker.get(MARKER)
        set(value) = dataTracker.set(MARKER, value)

    var lastHitTime: Long = 0L

    init {
        stepHeight = 0.0f
    }

    abstract fun asItemStack(): ItemStack

    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(MARKER, false)
    }

    override fun handleAttack(attacker: Entity): Boolean {
        return attacker is PlayerEntity && !world.canPlayerModifyAt(attacker, blockPos)
    }

    override fun damage(source: DamageSource, amount: Float): Boolean {
        if (world.isClient || isRemoved) {
            return false
        }

        if (source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            kill()
            return false
        }

        if (isInvulnerableTo(source) || isInvisible || marker) {
            return false
        }

        if (source.isIn(DamageTypeTags.IS_EXPLOSION)) {
            onBreak(source)
            kill()
            return false
        }

        if (source.isIn(DamageTypeTags.IGNITES_ARMOR_STANDS)) {
            if (isOnFire) {
                updateHealth(source, 0.15f)
            } else {
                setOnFireFor(5)
            }

            return false
        }

        if (source.isIn(DamageTypeTags.BURNS_ARMOR_STANDS) && health > 0.5f) {
            updateHealth(source, 4.0f)
            return false
        }

        val attackerEntity = source.attacker
        val sourceProjectile = source.source as? PersistentProjectileEntity

        if (attackerEntity is PlayerEntity) {
            if (!attackerEntity.abilities.allowModifyWorld) {
                return false
            }

            if (source.isSourceCreativePlayer) {
                playBreakSound()
                spawnBreakParticles()
                kill()

                return sourceProjectile != null && sourceProjectile.pierceLevel > 0
            }
        }

        val time = world.time
        if (time - lastHitTime <= 5L || sourceProjectile != null) {
            breakAndDropItem(source)
            spawnBreakParticles()
            kill()
        } else {
            world.sendEntityStatus(this, EntityStatuses.HIT_ARMOR_STAND)
            emitGameEvent(GameEvent.ENTITY_DAMAGE, attackerEntity)
            lastHitTime = time
        }

        return true
    }

    override fun kill() {
        remove(RemovalReason.KILLED)
        emitGameEvent(GameEvent.ENTITY_DIE)
    }

    override fun getPistonBehavior(): PistonBehavior {
        return if (marker) PistonBehavior.IGNORE else super.getPistonBehavior()
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putBoolean(MARKER_KEY, marker)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        marker = nbt.getBoolean(MARKER_KEY)
    }

    fun updateHealth(source: DamageSource, amount: Float) {
        val newHealth = health - amount
        if (newHealth <= 0.5f) {
            onBreak(source)
            kill()
        } else {
            health = newHealth
            emitGameEvent(GameEvent.ENTITY_DAMAGE, source.attacker)
        }
    }

    fun onBreak(source: DamageSource) {
        playBreakSound()
        drop(source)
    }

    fun playBreakSound() {
        world.playSound(
            null,
            this.x,
            this.y,
            this.z, SpyglassPlusSoundEvents.ENTITY_SPYGLASS_STAND_BREAK,
            this.soundCategory, 1.0f, 1.0f
        )
    }

    fun breakAndDropItem(source: DamageSource) {
        val stack = asItemStack()
        Block.dropStack(world, blockPos, stack)

        onBreak(source)
    }

    fun spawnBreakParticles() {
        val world = world
        if (world is ServerWorld) {
            world.spawnParticles(
                ItemStackParticleEffect(ParticleTypes.ITEM, ItemStack(SpyglassPlusItems.SPYGLASS_STAND)),
                this.x,
                this.getBodyY(0.6),
                this.z,
                10,
                (this.width / 4.0f).toDouble(),
                (this.height / 4.0f).toDouble(),
                (this.width / 4.0f).toDouble(),
                0.05
            )
        }
    }

    override fun isImmuneToExplosion(): Boolean {
        return isInvisible
    }

    private fun canClip(): Boolean {
        return !marker && !this.hasNoGravity()
    }

    override fun canMoveVoluntarily(): Boolean {
        return super.canMoveVoluntarily() && canClip()
    }

    override fun canHit(): Boolean {
        return super.canHit() && !marker
    }

    override fun isPartOfGame(): Boolean {
        return !isInvisible && !marker
    }

    override fun getDimensions(pose: EntityPose): EntityDimensions {
        return if (marker) MARKER_DIMENSIONS else type.dimensions
    }

    override fun calculateDimensions() {
        val x = x
        val y = y
        val z = z

        super.calculateDimensions()

        setPosition(x, y, z)
    }

    override fun isPushable(): Boolean {
        return false
    }

    override fun pushAway(entity: Entity) {
    }

    override fun getArmorItems(): Iterable<ItemStack> {
        return mutableListOf()
    }

    override fun equipStack(slot: EquipmentSlot, stack: ItemStack) {
    }

    override fun getEquippedStack(slot: EquipmentSlot): ItemStack {
        return ItemStack.EMPTY
    }

    override fun getMainArm(): Arm {
        return Arm.RIGHT
    }

    companion object {
        const val MARKER_KEY = "marker"
        val MARKER: TrackedData<Boolean> = DataTracker.registerData(StandEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
        val MARKER_DIMENSIONS: EntityDimensions = EntityDimensions(0.0f, 0.0f, false)
    }
}
