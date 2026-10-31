package com.dariusepure.wrxpedia.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WrxDataPayload(
    val generations: List<Generation>,
    val torqueSpecs: List<TorqueSpec>,
    val fluidCapacities: List<FluidCapacity>,
    val colorCodes: List<ColorCode>
)

@Serializable
data class Generation(
    val id: String,
    val name: String,
    val nickname: String,
    val years: String,
    val chassisCodes: String,
    val description: String,
    val iconName: String = "car",
    val variants: Variants
)

@Serializable
data class Variants(
    val wrx: CarSpec? = null,
    val sti: CarSpec? = null,
    val specialEditions: List<SpecialEdition>? = null
)

@Serializable
data class CarSpec(
    val engineCode: String,
    val powerHp: Int,
    val torqueNm: Int,
    val turboModel: String,
    val stockBoost: String = "N/A",
    val intercooler: String = "Top Mount Intercooler (TMIC)",
    val transmission: String = "Manual",
    val driveType: String = "Symmetrical AWD",
    val finalDrive: String = "N/A",
    val engineOilCapacity: String,
    val engineOilViscosity: String = "5W-30 / 10W-40",
    val gearboxOilCapacity: String,
    val diffOilCapacity: String = "0.8 L (75W-90)",
    val sparkPlugs: String = "NGK Iridium",
    val coolantCapacity: String = "7.7 L",
    val chassisCode: String,
    val pcd: String,
    val brakes: String,
    val knownIssues: List<String> = emptyList()
)

@Serializable
data class SpecialEdition(
    val id: String,
    val name: String,
    val year: String,
    val engineCode: String,
    val powerHp: Int,
    val torqueNm: Int = 0,
    val unitsProduced: String,
    val description: String,
    val keyFeatures: List<String> = emptyList(),
    val knownIssues: List<String> = emptyList()
)

@Serializable
data class TorqueSpec(
    val id: String,
    val component: String,
    val category: String,
    val torqueNm: String,
    val torqueFtLb: String,
    val notes: String = ""
)

@Serializable
data class FluidCapacity(
    val id: String,
    val system: String,
    val capacity: String,
    val recommendedViscosity: String,
    val interval: String,
    val notes: String = ""
)

@Serializable
data class ColorCode(
    val code: String,
    val nameEn: String,
    val nameRo: String,
    val hex: String,
    val applicableYears: String
)

data class SpecialEditionWithGeneration(
    val specialEdition: SpecialEdition,
    val generationName: String
)

sealed interface SearchResultItem {
    data class GenerationItem(val generation: Generation) : SearchResultItem
    data class EngineMatch(val generation: Generation, val variantName: String, val carSpec: CarSpec) : SearchResultItem
    data class ColorCodeMatch(val colorCode: ColorCode) : SearchResultItem
}
