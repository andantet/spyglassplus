package com.teamfusion.spyglassplus.asm

import com.chocohead.mm.api.ClassTinkerers
import net.fabricmc.loader.api.FabricLoader

class SpyglassPlusASM : Runnable {
    override fun run() {
        // register custom enchantment targets
        ClassTinkerers.enumBuilder(getIntermediaryClass("net.minecraft.class_1886"), *emptyArray<String>())
            .addEnumSubclass(
                "SCOPING",
                "com.teamfusion.spyglassplus.enchantment.target.ScopingEnchantmentTarget"
            ).build();
    }

    private fun getIntermediaryClass(path: String): String {
        return FabricLoader.getInstance().mappingResolver.mapClassName("intermediary", path)
    }
}
