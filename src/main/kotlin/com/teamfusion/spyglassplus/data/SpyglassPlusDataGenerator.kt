package com.teamfusion.spyglassplus.data

import com.teamfusion.spyglassplus.data.client.LanguageProvider
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

object SpyglassPlusDataGenerator : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(generator: FabricDataGenerator) {
        val pack = generator.createPack()
        pack.addProvider(::LanguageProvider)
    }
}
