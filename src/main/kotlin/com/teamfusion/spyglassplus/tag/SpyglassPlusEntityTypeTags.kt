package com.teamfusion.spyglassplus.tag

import com.teamfusion.spyglassplus.SpyglassPlus
import net.minecraft.entity.EntityType
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object SpyglassPlusEntityTypeTags {
    val IGNORE_MARGIN_EXPANSION_DISCOVERY = register("ignore_margin_expansion_discovery")
    val IGNORE_DISCOVERY = register("ignore_discovery")

    private fun register(id: String): TagKey<EntityType<*>> {
        return TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier(SpyglassPlus.MOD_ID, id))
    }
}
