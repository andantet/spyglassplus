package com.teamfusion.spyglassplus.entity

import com.teamfusion.spyglassplus.item.SpyglassPlusItems
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityPose
import net.minecraft.entity.EntityType
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandler
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.MathHelper.lerp
import net.minecraft.world.World
import java.util.Optional
import java.util.UUID

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

    var spyglassYaw: Float
        get() = dataTracker.get(SPYGLASS_YAW)
        set(value) = dataTracker.set(SPYGLASS_YAW, value)

    var spyglassPitch: Float
        get() = dataTracker.get(SPYGLASS_PITCH)
        set(value) = dataTracker.set(SPYGLASS_PITCH, value)

    var prevSpyglassYaw: Float = 0.0f

    var prevSpyglassPitch: Float = 0.0f

    constructor(world: World, x: Double, y: Double, z: Double) : this(SpyglassPlusEntityTypes.SPYGLASS_STAND, world) {
        setPosition(x, y, z)
    }

    override fun initDataTracker() {
        super.initDataTracker()

        dataTracker.startTracking(SMALL, false)

        dataTracker.startTracking(USER, Optional.empty())
        dataTracker.startTracking(SPYGLASS_STACK, ItemStack.EMPTY)
        dataTracker.startTracking(SPYGLASS_YAW, prevSpyglassYaw)
        dataTracker.startTracking(SPYGLASS_PITCH, prevSpyglassPitch)
    }

    override fun asItemStack(): ItemStack {
        val stack = ItemStack(SpyglassPlusItems.SPYGLASS_STAND)

        if (small) {
            val nbtEntityTag = stack.getOrCreateSubNbt(EntityType.ENTITY_TAG_KEY)
            nbtEntityTag.putBoolean(SMALL_KEY, true)
        }

        return stack
    }

    override fun tickMovement() {
        prevSpyglassYaw = spyglassYaw
        prevSpyglassPitch = spyglassPitch

        super.tickMovement()
    }

    override fun dropInventory() {
        super.dropInventory()
        dropStack(spyglassStack)
    }

    fun hasSpyglassStack(): Boolean {
        return !spyglassStack.isEmpty
    }

    override fun isBaby(): Boolean {
        return small
    }

    override fun getDimensions(pose: EntityPose): EntityDimensions {
        return if (marker) MARKER_DIMENSIONS else {
            if (isBaby) SMALL_DIMENSIONS else type.dimensions
        }
    }

    fun getInterpolatedSpyglassYaw(tickDelta: Float): Float {
        return lerp(tickDelta, prevSpyglassYaw, spyglassYaw)
    }

    fun getInterpolatedSpyglassPitch(tickDelta: Float): Float {
        return lerp(tickDelta, prevSpyglassPitch, spyglassPitch)
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)

        nbt.putBoolean(SMALL_KEY, small)

        user?.let { user -> nbt.putUuid(USER_KEY, user) }
        nbt.put(SPYGLASS_STACK_KEY, spyglassStack.writeNbt(NbtCompound()))
        nbt.putFloat(SPYGLASS_YAW_KEY, spyglassYaw)
        nbt.putFloat(SPYGLASS_PITCH_KEY, spyglassPitch)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)

        small = nbt.getBoolean(SMALL_KEY)

        if (nbt.containsUuid(USER_KEY)) {
            val uuid = nbt.getUuid(USER_KEY)
            user = uuid
        }

        spyglassStack = ItemStack.fromNbt(nbt.getCompound(SPYGLASS_STACK_KEY))
        spyglassYaw = nbt.getFloat(SPYGLASS_YAW_KEY)
        spyglassPitch = nbt.getFloat(SPYGLASS_PITCH_KEY)
    }

    companion object {
        const val SMALL_KEY = "small"

        const val USER_KEY = "user"
        const val SPYGLASS_STACK_KEY = "spyglass_stack"
        const val SPYGLASS_YAW_KEY = "spyglass_yaw"
        const val SPYGLASS_PITCH_KEY = "spyglass_pitch"

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
