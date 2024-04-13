package com.teamfusion.spyglassplus.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.item.ItemStack
import net.minecraft.util.Arm
import net.minecraft.world.World

class SpyglassStandEntity(type: EntityType<out SpyglassStandEntity>, world: World) : LivingEntity(type, world) {
    override fun getArmorItems(): Iterable<ItemStack> {
        return listOf()
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
        fun createAttributes(): DefaultAttributeContainer.Builder {
            return createLivingAttributes()
        }
    }
}
