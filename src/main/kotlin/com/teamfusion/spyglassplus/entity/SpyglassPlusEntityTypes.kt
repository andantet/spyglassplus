package com.teamfusion.spyglassplus.entity

import com.teamfusion.spyglassplus.SpyglassPlus
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object SpyglassPlusEntityTypes {
    val SPYGLASS_STAND = register(
        "spyglass_stand",
        FabricEntityTypeBuilder.createLiving<SpyglassStandEntity>()
            .spawnGroup(SpawnGroup.MISC)
            .entityFactory(::SpyglassStandEntity)
            .defaultAttributes(SpyglassStandEntity::createAttributes)
            .dimensions(EntityDimensions.changing(0.6f, 1.9f))
            .trackRangeChunks(8)
    )

    private fun <T : Entity> register(id: String, builder: FabricEntityTypeBuilder<T>): EntityType<T> {
        return Registry.register(Registries.ENTITY_TYPE, Identifier(SpyglassPlus.MOD_ID, id), builder.build())
    }
}
