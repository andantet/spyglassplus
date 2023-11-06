package com.teamfusion.spyglassplus.item

import com.teamfusion.spyglassplus.SpyglassPlus
import com.teamfusion.spyglassplus.enchantment.target.ScopingEnchantmentTarget
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.enchantment.EnchantmentLevelEntry
import net.minecraft.item.EnchantedBookItem
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.text.Text
import net.minecraft.util.Identifier

object SpyglassPlusItemGroups {
    const val ALL_KEY = "all"
    const val ALL_TRANSLATION_KEY = "itemGroup.${SpyglassPlus.MOD_ID}.$ALL_KEY"

    val ALL = register(
        ALL_KEY,
        FabricItemGroup.builder()
            .displayName(Text.translatable(ALL_TRANSLATION_KEY))
            .icon { ItemStack(Items.SPYGLASS) }
            .entries { _, entries ->
                entries.add(Items.SPYGLASS)

                Registries.ENCHANTMENT.forEach { enchantment ->
                    if (enchantment.target == ScopingEnchantmentTarget.INSTANCE) {
                        for (level in enchantment.minLevel..enchantment.maxLevel) {
                            entries.add(
                                EnchantedBookItem.forEnchantment(EnchantmentLevelEntry(enchantment, level)),
                                ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS
                            )
                        }
                    }
                }
            }
            .build()
    )

    private fun register(id: String, group: ItemGroup): ItemGroup {
        return Registry.register(Registries.ITEM_GROUP, Identifier(SpyglassPlus.MOD_ID, id), group)
    }
}
