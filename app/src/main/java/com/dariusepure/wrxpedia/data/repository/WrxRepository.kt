package com.dariusepure.wrxpedia.data.repository

import android.content.Context
import android.util.Log
import com.dariusepure.wrxpedia.data.model.ColorCode
import com.dariusepure.wrxpedia.data.model.FluidCapacity
import com.dariusepure.wrxpedia.data.model.Generation
import com.dariusepure.wrxpedia.data.model.SearchResultItem
import com.dariusepure.wrxpedia.data.model.SpecialEditionWithGeneration
import com.dariusepure.wrxpedia.data.model.TorqueSpec
import com.dariusepure.wrxpedia.data.model.WrxDataPayload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

interface WrxRepository {
    fun getGenerations(): Flow<List<Generation>>
    suspend fun getGenerationById(id: String): Generation?
    suspend fun getTorqueSpecs(): List<TorqueSpec>
    suspend fun getFluidCapacities(): List<FluidCapacity>
    suspend fun getColorCodes(): List<ColorCode>
    suspend fun getAllSpecialEditions(): List<SpecialEditionWithGeneration>
    suspend fun search(query: String): List<SearchResultItem>
}

class LocalWrxRepositoryImpl(
    private val context: Context
) : WrxRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private var cachedPayload: WrxDataPayload? = null

    private suspend fun getPayload(): WrxDataPayload {
        cachedPayload?.let { return it }
        return withContext(Dispatchers.IO) {
            try {
                val jsonString = context.assets.open("wrx_data.json").bufferedReader().use { it.readText() }
                val payload = json.decodeFromString<WrxDataPayload>(jsonString)
                cachedPayload = payload
                payload
            } catch (e: Exception) {
                Log.e("WrxRepository", "Error reading or parsing wrx_data.json", e)
                WrxDataPayload(emptyList(), emptyList(), emptyList(), emptyList())
            }
        }
    }

    override fun getGenerations(): Flow<List<Generation>> = flow {
        emit(getPayload().generations)
    }.flowOn(Dispatchers.IO)

    override suspend fun getGenerationById(id: String): Generation? {
        return getPayload().generations.find { it.id.equals(id, ignoreCase = true) }
    }

    override suspend fun getTorqueSpecs(): List<TorqueSpec> {
        return getPayload().torqueSpecs
    }

    override suspend fun getFluidCapacities(): List<FluidCapacity> {
        return getPayload().fluidCapacities
    }

    override suspend fun getColorCodes(): List<ColorCode> {
        return getPayload().colorCodes
    }

    override suspend fun getAllSpecialEditions(): List<SpecialEditionWithGeneration> {
        val payload = getPayload()
        val list = mutableListOf<SpecialEditionWithGeneration>()
        for (gen in payload.generations) {
            gen.variants.specialEditions?.forEach { se ->
                list.add(SpecialEditionWithGeneration(se, gen.name))
            }
        }
        return list
    }

    override suspend fun search(query: String): List<SearchResultItem> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return emptyList()

        val payload = getPayload()
        val results = mutableListOf<SearchResultItem>()

        // 1. Search in Generations (name, nickname, chassisCodes, description)
        for (gen in payload.generations) {
            if (gen.name.lowercase().contains(q) ||
                gen.nickname.lowercase().contains(q) ||
                gen.chassisCodes.lowercase().contains(q) ||
                gen.years.lowercase().contains(q)
            ) {
                results.add(SearchResultItem.GenerationItem(gen))
            }

            // Search WRX variant
            gen.variants.wrx?.let { spec ->
                if (spec.engineCode.lowercase().contains(q) ||
                    spec.chassisCode.lowercase().contains(q) ||
                    spec.turboModel.lowercase().contains(q)
                ) {
                    results.add(SearchResultItem.EngineMatch(gen, "WRX", spec))
                }
            }

            // Search STI variant
            gen.variants.sti?.let { spec ->
                if (spec.engineCode.lowercase().contains(q) ||
                    spec.chassisCode.lowercase().contains(q) ||
                    spec.turboModel.lowercase().contains(q)
                ) {
                    results.add(SearchResultItem.EngineMatch(gen, "STI", spec))
                }
            }
        }

        // 2. Search in Color Codes
        for (color in payload.colorCodes) {
            if (color.code.lowercase().contains(q) ||
                color.nameEn.lowercase().contains(q) ||
                color.nameRo.lowercase().contains(q)
            ) {
                results.add(SearchResultItem.ColorCodeMatch(color))
            }
        }

        return results.distinct()
    }
}
