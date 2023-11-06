package com.teamfusion.spyglassplus.enchantment

import com.teamfusion.spyglassplus.enchantment.target.ScopingEnchantmentTarget
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.Item

/**
 * Represents an enchantment applicable to an [Item] of [ISpyglass].
 */
open class ScopingEnchantment(weight: Rarity) : Enchantment(
    weight,
    ScopingEnchantmentTarget.INSTANCE,
    arrayOf(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND)
)
