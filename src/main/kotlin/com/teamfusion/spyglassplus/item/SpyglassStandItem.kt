package com.teamfusion.spyglassplus.item

import com.teamfusion.spyglassplus.entity.SpyglassPlusEntityTypes
import com.teamfusion.spyglassplus.entity.SpyglassStandEntity
import com.teamfusion.spyglassplus.sound.SpyglassPlusSoundEvents
import net.minecraft.block.DispenserBlock
import net.minecraft.block.dispenser.ItemDispenserBehavior
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.item.Item
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPointer
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper.wrapDegrees
import net.minecraft.util.math.Vec3d
import net.minecraft.world.event.GameEvent
import kotlin.math.floor

class SpyglassStandItem(settings: Settings) : Item(settings) {
    init {
        DispenserBlock.registerBehavior(this, SpyglassStandDispenserBehavior)
    }

    override fun getTranslationKey(stack: ItemStack): String {
        val key = super.getTranslationKey(stack)
        return if (isSmall(stack)) "$key.small" else key
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        if (context.side == Direction.DOWN) {
            return ActionResult.FAIL
        }

        val type = SpyglassPlusEntityTypes.SPYGLASS_STAND
        val placement = ItemPlacementContext(context)
        val pos = placement.blockPos

        val world = context.world
        val vec = Vec3d.ofBottomCenter(pos)
        val box = type.dimensions.getBoxAt(vec.x, vec.y, vec.z)
        if (!world.isSpaceEmpty(null, box) || world.getOtherEntities(null, box).isNotEmpty()) {
            return ActionResult.FAIL
        }

        val stack = context.stack
        if (world is ServerWorld) {
            val entity = type.create(world, stack.nbt, null, pos, SpawnReason.SPAWN_EGG, true, true) ?: return ActionResult.FAIL

            val yaw = floor((wrapDegrees(context.playerYaw) + 22.5f) / 45.0f) * 45.0f
            // entity.spyglassYaw = yaw TODO
            entity.refreshPositionAndAngles(entity.x, entity.y, entity.z, yaw, 0.0f)
            world.spawnEntityAndPassengers(entity)

            world.playSound(null, entity.x, entity.y, entity.z, SpyglassPlusSoundEvents.ENTITY_SPYGLASS_STAND_PLACE, SoundCategory.BLOCKS, 0.75f, 0.8f)
            entity.emitGameEvent(GameEvent.ENTITY_PLACE, context.player)
        }

        stack.decrement(1)
        return ActionResult.success(world.isClient)
    }

    object SpyglassStandDispenserBehavior : ItemDispenserBehavior() {
        public override fun dispenseSilently(pointer: BlockPointer, stack: ItemStack): ItemStack {
            val direction = pointer.blockState.get(DispenserBlock.FACING)
            val pos = pointer.pos.offset(direction)
            val world = pointer.world
            val entity = SpyglassStandEntity(world, pos.x.toDouble() + 0.5, pos.y.toDouble(), pos.z.toDouble() + 0.5)
            EntityType.loadFromEntityNbt(world, null, entity, stack.nbt)
            entity.yaw = direction.asRotation()
            world.spawnEntity(entity)
            stack.decrement(1)
            return stack
        }
    }

    companion object {
        fun isSmall(stack: ItemStack): Boolean {
            val nbtEntityTag = stack.getSubNbt(EntityType.ENTITY_TAG_KEY)
            return nbtEntityTag != null && nbtEntityTag.getBoolean(SpyglassStandEntity.SMALL_KEY)
        }
    }
}
