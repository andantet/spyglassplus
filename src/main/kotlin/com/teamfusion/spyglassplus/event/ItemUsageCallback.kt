package com.teamfusion.spyglassplus.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.world.World

fun interface ItemUsageCallback {
    /**
     * Called every tick an item is being used.
     */
    fun onUsageTick(world: World, user: LivingEntity, stack: ItemStack, remainingUseTicks: Int)

    companion object {
        val EVENT: Event<ItemUsageCallback> = EventFactory.createArrayBacked(ItemUsageCallback::class.java) { callbacks ->
            ItemUsageCallback { world, user, stack, remainingUseTicks ->
                callbacks.forEach { callback ->
                    callback.onUsageTick(world, user, stack, remainingUseTicks)
                }
            }
        }
    }
}
