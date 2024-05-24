package com.teamfusion.spyglassplus.client.render.entity

import com.teamfusion.spyglassplus.SpyglassPlus
import com.teamfusion.spyglassplus.entity.SpyglassStandEntity
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory.Context
import net.minecraft.util.Identifier

class SpyglassStandEntityRenderer(context: Context) : EntityRenderer<SpyglassStandEntity>(context) {
    override fun hasLabel(entity: SpyglassStandEntity): Boolean {
        val distance = dispatcher.getSquaredDistanceToCamera(entity)
        val bound = if (entity.isInSneakingPose) 32.0f else 64.0f
        return !(distance >= (bound * bound).toDouble()) && entity.isCustomNameVisible
    }

    override fun getTexture(entity: SpyglassStandEntity): Identifier {
        return texture
    }

    companion object {
        val texture: Identifier = Identifier(SpyglassPlus.MOD_ID, "textures/entity/spyglass_stand/spyglass_stand.png")
    }
}
