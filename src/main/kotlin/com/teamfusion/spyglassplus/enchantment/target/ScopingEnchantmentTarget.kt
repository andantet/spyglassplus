package com.teamfusion.spyglassplus.enchantment.target

import com.chocohead.mm.api.ClassTinkerers
import com.teamfusion.spyglassplus.mixin.EnchantmentTargetMixin
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.item.Item
import net.minecraft.item.Items

class ScopingEnchantmentTarget : EnchantmentTargetMixin() {
    override fun isAcceptableItem(item: Item): Boolean {
        return item == Items.SPYGLASS
    }

    companion object {
        /**
         * The enum instance of [ScopingEnchantmentTarget].
         */
        val INSTANCE: EnchantmentTarget by lazy { ClassTinkerers.getEnum(EnchantmentTarget::class.java, "SCOPING") }
    }
}
