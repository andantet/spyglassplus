package com.teamfusion.spyglassplus.client.render.entity

import com.teamfusion.spyglassplus.SpyglassPlus
import com.teamfusion.spyglassplus.client.render.model.SpyglassPlusEntityModelLayers
import com.teamfusion.spyglassplus.client.render.model.SpyglassStandEntityModel
import com.teamfusion.spyglassplus.entity.SpyglassStandEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory.Context
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.RotationAxis
import kotlin.math.PI
import kotlin.math.sin

class SpyglassStandEntityRenderer(context: Context) : EntityRenderer<SpyglassStandEntity>(context) {
    private val model: SpyglassStandEntityModel = SpyglassStandEntityModel(context.getPart(SpyglassPlusEntityModelLayers.SPYGLASS_STAND))
    private val modelSmall: SpyglassStandEntityModel = SpyglassStandEntityModel(context.getPart(SpyglassPlusEntityModelLayers.SPYGLASS_STAND_SMALL))
    private val spyglassModel: SpyglassStandEntityModel = SpyglassStandEntityModel(context.getPart(SpyglassPlusEntityModelLayers.SPYGLASS_STAND_SPYGLASS))
    private val spyglassModelSmall: SpyglassStandEntityModel = SpyglassStandEntityModel(context.getPart(SpyglassPlusEntityModelLayers.SPYGLASS_STAND_SPYGLASS_SMALL))

    override fun render(
        entity: SpyglassStandEntity,
        yaw: Float,
        tickDelta: Float,
        matrices: MatrixStack,
        vertices: VertexConsumerProvider,
        light: Int
    ) {
        val client = MinecraftClient.getInstance()
        val visible = !entity.isInvisible
        val invisible = !visible && !entity.isInvisibleTo(client.player)
        val outline: Boolean = client.hasOutline(entity)

        val alpha = if (invisible) 0.15f else 1.0f

        matrices.push()

        matrices.scale(-1.0f, -1.0f, 1.0f)
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-180f))

        val shake = (entity.world.time - entity.lastHitTime).toFloat() + tickDelta
        if (shake < 5.0f) {
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(sin((shake / 1.5f) * PI.toFloat()) * 3.0f))
        }

        matrices.translate(0.0, (-1.501f).toDouble(), 0.0)


        // render model
        val layer = getRenderLayer(entity, texture, visible, invisible, outline) ?: return
        val baseModel = getModel(entity, false)
        render(baseModel, layer, alpha, entity, tickDelta, matrices, vertices, light)

        // render spyglass, with glint conditionally
        val spyglassModel = getModel(entity, true)
        render(spyglassModel, layer, alpha, entity, tickDelta, matrices, vertices, light)
        if (entity.spyglassStack.hasEnchantments()) {
            render(
                spyglassModel,
                RenderLayer.getEntityGlint(),
                alpha,
                entity,
                tickDelta,
                matrices,
                vertices,
                light
            )
        }

        matrices.pop()

        if (this.hasLabel(entity)) {
            this.renderLabelIfPresent(entity, entity.displayName, matrices, vertices, light)
        }
    }

    fun render(
        model: SpyglassStandEntityModel,
        layer: RenderLayer,
        alpha: Float,
        entity: SpyglassStandEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertices: VertexConsumerProvider,
        light: Int
    ) {
        model.riding = entity.hasVehicle()
        model.child = entity.small

        model.animateModel(entity, 0.0f, 0.0f, tickDelta)
        model.setAngles(entity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)

        model.render(matrices, vertices.getBuffer(layer), light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, alpha)
    }

    fun getModel(entity: SpyglassStandEntity, spyglass: Boolean): SpyglassStandEntityModel {
        return if (entity.small) (if (spyglass) this.spyglassModelSmall else this.modelSmall) else (if (spyglass) this.spyglassModel else this.model)
    }

    fun getRenderLayer(
        entity: SpyglassStandEntity,
        texture: Identifier,
        showBody: Boolean,
        translucent: Boolean,
        showOutline: Boolean
    ): RenderLayer? {
        if (!entity.marker) {
            if (translucent) return RenderLayer.getItemEntityTranslucentCull(texture)
            if (showBody) return getModel(entity, false).getLayer(texture)
            if (showOutline) return RenderLayer.getOutline(texture)
        } else {
            if (translucent) return RenderLayer.getEntityTranslucent(texture, false)
            if (showBody) return RenderLayer.getEntityCutoutNoCull(texture, false)
        }

        return null
    }

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
