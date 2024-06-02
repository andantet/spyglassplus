package com.teamfusion.spyglassplus.data.client

import com.teamfusion.spyglassplus.item.SpyglassPlusItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Models

class ModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
    }

    override fun generateItemModels(generator: ItemModelGenerator) {
        generator.register(SpyglassPlusItems.SPYGLASS_STAND, Models.GENERATED)
    }
}
