package com.teamfusion.spyglassplus.raycast

import com.teamfusion.spyglassplus.tag.SpyglassPlusEntityTypeTags.IGNORE_DISCOVERY
import com.teamfusion.spyglassplus.tag.SpyglassPlusEntityTypeTags.IGNORE_MARGIN_EXPANSION_DISCOVERY
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import net.minecraft.world.RaycastContext
import java.util.function.Predicate

/**
 * Responsible for spyglass raycasting functionality.
 */
object SpyglassRaycasting {
    /**
     * Retrieves the entity that the camera is looking at.
     */
    @JvmOverloads
    fun raycast(
        camera: Entity,
        rotation: Vec2f = getRotation(camera),
        tickDelta: Float = 1.0f,
        distance: Double = MAX_RAYCAST_DISTANCE,
        predicate: Predicate<Entity?> = Predicate { entity: Entity? -> true }
    ): Entity? {
        // calculate a position vector from the camera's rotation
        var d = distance
        val vector = camera.getRotationVector(rotation.y, rotation.x)

        // calculate minimum and maximum points of raycast
        val min = camera.getCameraPosVec(tickDelta)
        val max = min.add(vector.x * d, vector.y * d, vector.z * d)

        // grab default hit result
        val hit: HitResult? = camera.world.raycast(
            RaycastContext(
                min,
                max,
                RaycastContext.ShapeType.VISUAL,
                RaycastContext.FluidHandling.NONE,
                camera
            )
        )
        if (hit != null) {
            d = hit.pos.squaredDistanceTo(min)
        }

        // calculate entity hit result
        val net = camera.boundingBox.stretch(vector.multiply(d)).expand(1.0)
        val entityHit = raycast(
            camera,
            min,
            max,
            net,
            { entity: Entity ->
                isVisibleToRaycast(
                    entity,
                    if (camera is PlayerEntity) camera else null
                ) && predicate.test(entity)
            },
            d
        )

        if (entityHit != null) {
            val entity = entityHit.entity
            val pos = entityHit.pos
            val entityDistance = min.squaredDistanceTo(pos)
            if (entityDistance < d || hit == null) {
                return entity
            }
        }

        return null
    }

    fun raycast(camera: Entity, predicate: Predicate<Entity?>): Entity? {
        return raycast(camera, getRotation(camera), 1.0f, MAX_RAYCAST_DISTANCE, predicate)
    }

    fun isVisibleToRaycast(entity: Entity, viewer: PlayerEntity?): Boolean {
        return !entity.isSpectator && !entity.type.isIn(IGNORE_DISCOVERY) && (viewer == null || !entity.isInvisibleTo(
            viewer
        ))
    }

    /**
     * Modified and mapped version of [ProjectileUtil.raycast].
     *
     * Modifies the result of [Entity.getTargetingMargin].
     */
    fun raycast(
        entity: Entity,
        min: Vec3d,
        max: Vec3d?,
        box: Box?,
        predicate: Predicate<Entity>?,
        distance: Double
    ): EntityHitResult? {
        var runningDistance = distance
        var resultEntity: Entity? = null
        var resultPos: Vec3d? = null

        for (candidate in entity.world.getOtherEntities(entity, box, predicate)) {
            val margin = candidate.targetingMargin
            val candidateBoundingBox = candidate.boundingBox.expand(
                (if (margin == 0.0f && !candidate.type.isIn(IGNORE_MARGIN_EXPANSION_DISCOVERY)
                ) 0.175f else margin).toDouble()
            )

            val optional = candidateBoundingBox.raycast(min, max)
            if (candidateBoundingBox.contains(min)) {
                if (!(runningDistance >= 0.0)) continue
                resultEntity = candidate
                resultPos = optional.orElse(min)
                runningDistance = 0.0
                continue
            }

            var squaredDistance = 0.0
            var runningPos: Vec3d? = null
            if (optional.isEmpty || !((min.squaredDistanceTo(optional.get().also { runningPos = it })
                    .also { squaredDistance = it }) < runningDistance) && runningDistance != 0.0
            ) {
                continue
            }
            if (candidate.rootVehicle === entity.rootVehicle) {
                if (runningDistance != 0.0) continue
                resultEntity = candidate
                resultPos = runningPos
                continue
            }

            resultEntity = candidate
            resultPos = runningPos
            runningDistance = squaredDistance
        }

        return if (resultEntity == null) null else EntityHitResult(resultEntity, resultPos)
    }

    fun getRotation(camera: Entity): Vec2f {
        return  /*camera instanceof SpyglassStandEntity stand
            ? new Vec2f(stand.getSpyglassYaw(), stand.getSpyglassPitch())
            : */Vec2f(camera.yaw, camera.pitch)
    }

    private const val MAX_RAYCAST_DISTANCE: Double = 64.0
}
