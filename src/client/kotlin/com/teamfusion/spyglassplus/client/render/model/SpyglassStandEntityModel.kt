package com.teamfusion.spyglassplus.client.render.model

import com.teamfusion.spyglassplus.entity.SpyglassStandEntity
import com.teamfusion.spyglassplus.math.MathUtil.DEGREES_TO_RADIANS
import net.minecraft.client.model.Dilation
import net.minecraft.client.model.ModelData
import net.minecraft.client.model.ModelPart
import net.minecraft.client.model.ModelPartBuilder
import net.minecraft.client.model.ModelTransform
import net.minecraft.client.model.TexturedModelData
import net.minecraft.client.render.entity.model.EntityModelPartNames.LEFT_LEG
import net.minecraft.client.render.entity.model.EntityModelPartNames.RIGHT_LEG
import net.minecraft.client.render.entity.model.SinglePartEntityModel
import net.minecraft.util.math.MathHelper

class SpyglassStandEntityModel(private val root: ModelPart) : SinglePartEntityModel<SpyglassStandEntity>() {
    private val holder: ModelPart = root.getChild(HOLDER)
    private val spyglass: ModelPart = holder.getChild(SPYGLASS)

    private val tripod: ModelPart = root.getChild(TRIPOD)
    private val leftLeg: ModelPart = tripod.getChild(LEFT_LEG)
    private val rightLeg: ModelPart = tripod.getChild(RIGHT_LEG)
    private val backLeg: ModelPart = tripod.getChild(BACK_LEG)

    override fun setAngles(
        entity: SpyglassStandEntity,
        limbAngle: Float,
        limbDistance: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float
    ) {
        spyglass.visible = entity.hasSpyglassStack()
    }

    override fun animateModel(entity: SpyglassStandEntity, limbAngle: Float, limbDistance: Float, tickDelta: Float) {
        val yaw = if (tickDelta == 1.0f) entity.yaw else MathHelper.lerp(tickDelta, entity.prevYaw, entity.yaw)
        this.tripod.yaw = yaw * DEGREES_TO_RADIANS;
        this.holder.yaw = entity.getInterpolatedSpyglassYaw(tickDelta) * DEGREES_TO_RADIANS;
        this.spyglass.pitch = entity.getInterpolatedSpyglassPitch(tickDelta) * DEGREES_TO_RADIANS;
    }

    override fun getPart(): ModelPart {
        return root
    }

    @Suppress("UNUSED_VARIABLE")
    companion object {
        const val HOLDER: String = "holder"
        const val SPYGLASS: String = "spyglass"
        const val TRIPOD: String = "tripod"
        const val BACK_LEG: String = "back_leg"

        fun getTexturedModelData(): TexturedModelData {
            val data = ModelData()
            val root = data.root

            val holder = root.addChild(
                HOLDER,
                ModelPartBuilder.create()
                    .uv(0, 0)
                    .cuboid(-2.0f, -4.0f, 0.0f, 4.0f, 4.0f, 1.0f),
                ModelTransform.pivot(0.0f, -2.0f, 0.0f)
            )

            val spyglass = holder.addChild(SPYGLASS, ModelPartBuilder.create(), ModelTransform.NONE)

            val tripod = root.addChild(
                TRIPOD,
                ModelPartBuilder.create(),
                ModelTransform.pivot(0.0f, 24.0f, 0.0f)
            )

            val leftLeg = tripod.addChild(
                LEFT_LEG,
                ModelPartBuilder.create()
                    .uv(0, 26)
                    .cuboid(7.0f, -24.5f, 0.0f, 2.0f, 27.0f, 2.0f),
                ModelTransform.rotation(-0.2094f, 0.6196f, -0.3752f)
            )

            val rightLeg = tripod.addChild(
                RIGHT_LEG,
                ModelPartBuilder.create()
                    .uv(0, 26)
                    .cuboid(-9.0f, -24.5f, 0.0f, 2.0f, 27.0f, 2.0f),
                ModelTransform.rotation(-0.2094f, -0.6196f, 0.3752f)
            )

            val backLeg = tripod.addChild(
                BACK_LEG,
                ModelPartBuilder.create()
                    .uv(0, 26)
                    .cuboid(-1.0f, -24.5f, 6.5f, 2.0f, 27.0f, 2.0f),
                ModelTransform.rotation(0.2618f, 0.0f, 0.0f)
            )

            return TexturedModelData.of(data, 64, 64)
        }

        fun getSmallTexturedModelData(): TexturedModelData {
            val data = ModelData()
            val root = data.root

            val holder = root.addChild(
                HOLDER,
                ModelPartBuilder.create()
                    .uv(0, 0)
                    .cuboid(-2.0f, -4.0f, 0.0f, 4.0f, 4.0f, 1.0f),
                ModelTransform.pivot(0.0f, 15.0f, 0.0f)
            )

            val spyglass = holder.addChild(SPYGLASS, ModelPartBuilder.create(), ModelTransform.NONE)

            val tripod = root.addChild(
                TRIPOD,
                ModelPartBuilder.create(),
                ModelTransform.of(0.0f, 41.0f, 0.0f, 0.0f, 0.0f, 0.0f)
            )

            val leftLeg = tripod.addChild(
                LEFT_LEG,
                ModelPartBuilder.create()
                    .uv(0, 26)
                    .cuboid(7.0f, -24.5f, 0.0f, 2.0f, 9.0f, 2.0f),
                ModelTransform.rotation(-0.2094f, 0.6196f, -0.3752f)
            )

            val rightLeg = tripod.addChild(
                RIGHT_LEG,
                ModelPartBuilder.create()
                    .uv(0, 26)
                    .cuboid(-9.0f, -24.5f, 0.0f, 2.0f, 9.0f, 2.0f),
                ModelTransform.rotation(-0.2094f, -0.6196f, 0.3752f)
            )

            val backLeg = tripod.addChild(
                BACK_LEG,
                ModelPartBuilder.create()
                    .uv(0, 26)
                    .cuboid(-1.0f, -24.5f, 6.0f, 2.0f, 9.0f, 2.0f),
                ModelTransform.rotation(0.2618f, 0.0f, 0.0f)
            )

            return TexturedModelData.of(data, 64, 64)
        }

        fun getSpyglassTexturedModelData(): TexturedModelData {
            val data = ModelData()
            val root = data.root

            val holder = root.addChild(HOLDER, ModelPartBuilder.create(), ModelTransform.pivot(0.0f, -2.0f, 0.0f))

            val spyglass = holder.addChild(
                SPYGLASS,
                ModelPartBuilder.create()
                    .uv(0, 0)
                    .cuboid(-1.0f, -1.0f, -6.5f, 2.0f, 2.0f, 11.0f)
                    .uv(5, 18)
                    .cuboid(-1.0f, -1.0f, -6.5f, 2.0f, 2.0f, 6.0f, Dilation(0.2f)),
                ModelTransform.pivot(0.0f, -2.0f, 0.5f)
            )

            val tripod = root.addChild(TRIPOD, ModelPartBuilder.create(), ModelTransform.pivot(0.0f, 24.0f, 0.0f))

            val leftLeg = tripod.addChild(
                LEFT_LEG,
                ModelPartBuilder.create(),
                ModelTransform.rotation(-0.2094f, 0.6196f, -0.3752f)
            )
            val rightLeg = tripod.addChild(
                RIGHT_LEG,
                ModelPartBuilder.create(),
                ModelTransform.rotation(-0.2094f, -0.6196f, 0.3752f)
            )
            val backLeg =
                tripod.addChild(BACK_LEG, ModelPartBuilder.create(), ModelTransform.rotation(0.2618f, 0.0f, 0.0f))

            return TexturedModelData.of(data, 64, 64)
        }

        fun getSpyglassSmallTexturedModelData(): TexturedModelData {
            val data = ModelData()
            val root = data.root

            val holder = root.addChild(HOLDER, ModelPartBuilder.create(), ModelTransform.pivot(0.0f, 15.0f, 0.0f))

            val spyglass = holder.addChild(
                SPYGLASS,
                ModelPartBuilder.create()
                    .uv(0, 0)
                    .cuboid(-1.0f, -1.0f, -6.5f, 2.0f, 2.0f, 11.0f)
                    .uv(5, 18)
                    .cuboid(-1.0f, -1.0f, -6.5f, 2.0f, 2.0f, 6.0f, Dilation(0.2f)),
                ModelTransform.pivot(0.0f, -2.0f, 0.0f)
            )

            val tripod =
                root.addChild(TRIPOD, ModelPartBuilder.create(), ModelTransform.of(0.0f, 41.0f, 0.0f, 0.0f, 0.0f, 0.0f))

            val leftLeg = tripod.addChild(
                LEFT_LEG,
                ModelPartBuilder.create(),
                ModelTransform.rotation(-0.2094f, 0.6196f, -0.3752f)
            )
            val rightLeg = tripod.addChild(
                RIGHT_LEG,
                ModelPartBuilder.create(),
                ModelTransform.rotation(-0.2094f, -0.6196f, 0.3752f)
            )
            val backLeg =
                tripod.addChild(BACK_LEG, ModelPartBuilder.create(), ModelTransform.rotation(0.2618f, 0.0f, 0.0f))

            return TexturedModelData.of(data, 64, 64)
        }
    }
}
