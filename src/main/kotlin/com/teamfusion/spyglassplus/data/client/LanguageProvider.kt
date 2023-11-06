package com.teamfusion.spyglassplus.data.client

import com.teamfusion.spyglassplus.SpyglassPlus
import com.teamfusion.spyglassplus.enchantment.SpyglassPlusEnchantments
import com.teamfusion.spyglassplus.item.SpyglassPlusItemGroups
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider

class LanguageProvider(output: FabricDataOutput) : FabricLanguageProvider(output) {
    override fun generateTranslations(builder: TranslationBuilder) {
        builder.add(SpyglassPlusItemGroups.ALL_TRANSLATION_KEY, SpyglassPlus.MOD_NAME)

        builder.add(SpyglassPlusEnchantments.SCRUTINY, "Scrutiny")
        builder.add(SpyglassPlusEnchantments.ILLUMINATE, "Illuminate")
        builder.add(SpyglassPlusEnchantments.INDICATE, "Indicate")
        builder.add(SpyglassPlusEnchantments.DISCOVERY, "Discovery")
        builder.add(SpyglassPlusEnchantments.COMMAND, "Command")
    }
}
