package com.teamfusion.spyglassplus.entity

import com.teamfusion.spyglassplus.item.SpyglassPlusItems
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityPose
import net.minecraft.entity.EntityType
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandler
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.SpyglassItem
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World
import java.util.Optional
import java.util.UUID
import kotlin.math.max
import kotlin.math.min

class SpyglassStandEntity(type: EntityType<out SpyglassStandEntity>, world: World) : StandEntity(type, world) {
    var small: Boolean
        get() = dataTracker.get(SMALL)
        set(value) = dataTracker.set(SMALL, value)

    var user: UUID?
        get() = dataTracker.get(USER).orElse(null)
        set(value) = dataTracker.set(USER, Optional.ofNullable(value))

    var spyglassStack: ItemStack
        get() = dataTracker.get(SPYGLASS_STACK)
        set(value) = dataTracker.set(SPYGLASS_STACK, value)

    constructor(world: World, x: Double, y: Double, z: Double) : this(SpyglassPlusEntityTypes.SPYGLASS_STAND, world) {
        setPosition(x, y, z)
    }

    override fun initDataTracker() {
        super.initDataTracker()

        dataTracker.startTracking(SMALL, false)

        dataTracker.startTracking(USER, Optional.empty())
        dataTracker.startTracking(SPYGLASS_STACK, ItemStack.EMPTY)
    }

    override fun tick() {
        super.tick()

        val user = user
        if (user != null) {
            val player = world.getPlayerByUuid(user)
            tickUser(player)
        } else {
            headYaw = yaw
        }
    }

    private fun tickUser(player: PlayerEntity?) {
        // player not found
        if (player == null) {
            unscopeSpyglass()
            return
        }

        // not true user
        if (player is ServerPlayerEntity && player.cameraEntity != this) {
            if (unscopeSpyglass()) {
                return
            }
        }

        // tick real player
        val playerPitch = player.pitch
        val playerYaw = player.yaw

        val diff = 70.0f
        val newPitch = max(-diff, min(diff, playerPitch))
        val newYaw = max(yaw - diff, min(yaw + diff, playerYaw))

        player.pitch = newPitch
        player.yaw = newYaw

        pitch = newPitch
        headYaw = newYaw
    }

    override fun interact(player: PlayerEntity, hand: Hand): ActionResult {
        val stack = player.getStackInHand(hand)

        if (hasSpyglassStack()) {
            if (user == null) {
                // try remove spyglass stack
                if (player.shouldCancelInteraction()) {
                    player.giveItemStack(spyglassStack)
                    spyglassStack = ItemStack.EMPTY
                    return ActionResult.SUCCESS
                } else {
                    if (scopeSpyglass(player)) {
                        return ActionResult.SUCCESS
                    }
                }
            }
        } else {
            // equip spyglass stack
            if (stack.item is SpyglassItem) {
                spyglassStack = stack.copy()
                stack.decrement(1)
                return ActionResult.SUCCESS
            }
        }

        return super.interact(player, hand)
    }

    fun scopeSpyglass(player: PlayerEntity): Boolean {
        // suspend to development
        if (!FabricLoader.getInstance().isDevelopmentEnvironment) {
            return false
        }

        // check for active user
        if (user != null) {
            return false
        }

        // scope
        val playerUuid = player.uuid
        user = playerUuid

        headYaw = yaw
        pitch = 0.0f

        player.yaw = yaw
        player.pitch = pitch

        if (player is ServerPlayerEntity) {
            player.cameraEntity = this
        }

        return true
    }

    fun unscopeSpyglass(): Boolean {
        // verify player
        val player = user?.let(world::getPlayerByUuid) ?: return false

        // unscope
        user = null

        if (player is ServerPlayerEntity && player.cameraEntity == this) {
            player.cameraEntity = null
        }

        headYaw = yaw
        pitch = 0.0f

        player.yaw = yaw
        player.pitch = pitch

        return true
    }

    override fun dropInventory() {
        super.dropInventory()
        dropStack(spyglassStack)
    }

    fun hasSpyglassStack(): Boolean {
        return !spyglassStack.isEmpty
    }

    override fun asItemStack(): ItemStack {
        val stack = ItemStack(SpyglassPlusItems.SPYGLASS_STAND)

        if (small) {
            val nbtEntityTag = stack.getOrCreateSubNbt(EntityType.ENTITY_TAG_KEY)
            nbtEntityTag.putBoolean(SMALL_KEY, true)
        }

        return stack
    }

    override fun isBaby(): Boolean {
        return small
    }

    override fun getDimensions(pose: EntityPose): EntityDimensions {
        return if (marker) MARKER_DIMENSIONS else {
            if (isBaby) SMALL_DIMENSIONS else type.dimensions
        }
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)

        nbt.putBoolean(SMALL_KEY, small)

        user?.let { user -> nbt.putUuid(USER_KEY, user) }
        nbt.put(SPYGLASS_STACK_KEY, spyglassStack.writeNbt(NbtCompound()))
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)

        small = nbt.getBoolean(SMALL_KEY)

        if (nbt.containsUuid(USER_KEY)) {
            val uuid = nbt.getUuid(USER_KEY)
            user = uuid
        }

        spyglassStack = ItemStack.fromNbt(nbt.getCompound(SPYGLASS_STACK_KEY))
    }

    companion object {
        const val SMALL_KEY = "small"

        const val USER_KEY = "user"
        const val SPYGLASS_STACK_KEY = "spyglass_stack"

        val SMALL: TrackedData<Boolean> = registerDataTracker(TrackedDataHandlerRegistry.BOOLEAN)

        val USER: TrackedData<Optional<UUID>> = registerDataTracker(TrackedDataHandlerRegistry.OPTIONAL_UUID)
        val SPYGLASS_STACK: TrackedData<ItemStack> = registerDataTracker(TrackedDataHandlerRegistry.ITEM_STACK)
        val SPYGLASS_YAW: TrackedData<Float> = registerDataTracker(TrackedDataHandlerRegistry.FLOAT)
        val SPYGLASS_PITCH: TrackedData<Float> = registerDataTracker(TrackedDataHandlerRegistry.FLOAT)

        val SMALL_DIMENSIONS: EntityDimensions by lazy { SpyglassPlusEntityTypes.SPYGLASS_STAND.dimensions.scaled(0.5f) }

        private fun <T> registerDataTracker(handler: TrackedDataHandler<T>): TrackedData<T> {
            return DataTracker.registerData(SpyglassStandEntity::class.java, handler)
        }

        fun createAttributes(): DefaultAttributeContainer.Builder {
            return createLivingAttributes()
        }
    }
}
