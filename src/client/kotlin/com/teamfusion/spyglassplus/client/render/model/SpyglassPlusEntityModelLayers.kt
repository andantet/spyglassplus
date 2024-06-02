package com.teamfusion.spyglassplus.client.render.model

import com.teamfusion.spyglassplus.SpyglassPlus
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry.TexturedModelDataProvider
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.util.Identifier

object SpyglassPlusEntityModelLayers {
    val SPYGLASS_STAND = registerSpyglass("main", SpyglassStandEntityModel::getTexturedModelData)
    val SPYGLASS_STAND_SMALL = registerSpyglass("small", SpyglassStandEntityModel::getSmallTexturedModelData)
    val SPYGLASS_STAND_SPYGLASS = registerSpyglass("spyglass", SpyglassStandEntityModel::getSpyglassTexturedModelData)
    val SPYGLASS_STAND_SPYGLASS_SMALL = registerSpyglass("spyglass_small", SpyglassStandEntityModel::getSpyglassSmallTexturedModelData)

    private fun register(id: String, name: String, provider: TexturedModelDataProvider): EntityModelLayer {
        val layer = EntityModelLayer(Identifier(SpyglassPlus.MOD_ID, id), name)
        EntityModelLayerRegistry.registerModelLayer(layer, provider)
        return layer
    }

    private fun registerSpyglass(id: String, provider: TexturedModelDataProvider): EntityModelLayer {
        return register("spyglass", id, provider)
    }

    private fun main(id: String, provider: TexturedModelDataProvider): EntityModelLayer {
        return register(id, "main", provider)
    }
}
