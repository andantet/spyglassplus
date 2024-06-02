package com.teamfusion.spyglassplus.sound

import com.teamfusion.spyglassplus.SpyglassPlus
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier

object SpyglassPlusSoundEvents {
    val ENTITY_SPYGLASS_STAND_PLACE = register("entity.spyglass_stand.place")
    val ENTITY_SPYGLASS_STAND_BREAK = register("entity.spyglass_stand.break")

    private fun register(id: String): SoundEvent {
        val identifier = Identifier(SpyglassPlus.MOD_ID, id)
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier))
    }
}
