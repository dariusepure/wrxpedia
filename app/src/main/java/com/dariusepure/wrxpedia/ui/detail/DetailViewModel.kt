package com.dariusepure.wrxpedia.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dariusepure.wrxpedia.data.model.ColorCode
import com.dariusepure.wrxpedia.data.model.Generation
import com.dariusepure.wrxpedia.data.model.TorqueSpec
import com.dariusepure.wrxpedia.data.repository.WrxRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class VariantTab {
    WRX,
    STI,
    SPECIAL_EDITIONS
}

enum class DetailSection {
    ENGINE_PERFORMANCE,
    TRANSMISSION_DRIVETRAIN,
    MAINTENANCE_FLUIDS,
    TORQUE_SPECS,
    PAINT_COLOR_CODES,
    CHASSIS_BRAKES_DIMENSIONS
}

data class DetailUiState(
    val generationId: String = "",
    val generation: Generation? = null,
    val torqueSpecs: List<TorqueSpec> = emptyList(),
    val colorCodes: List<ColorCode> = emptyList(),
    val isLoading: Boolean = true,
    val selectedTab: VariantTab = VariantTab.WRX,
    val expandedSections: Set<DetailSection> = setOf(DetailSection.ENGINE_PERFORMANCE)
)

class DetailViewModel(
    private val generationId: String,
    private val repository: WrxRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState(generationId = generationId))
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadGenerationDetails()
    }

    private fun loadGenerationDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val gen = repository.getGenerationById(generationId)
            val torques = repository.getTorqueSpecs()
            val colors = repository.getColorCodes()

            // Filter paint colors relevant to this generation
            val genName = gen?.name?.lowercase() ?: ""
            val genNickname = gen?.nickname?.lowercase() ?: ""
            val relevantColors = colors.filter { color ->
                val years = color.applicableYears.lowercase()
                years.contains(genName) || years.contains(genNickname) ||
                genNickname.split(" ", "/", "(", ")", "-").any { word -> word.length >= 3 && years.contains(word) }
            }.ifEmpty { colors }

            _uiState.update { state ->
                val initialTab = when {
                    gen?.variants?.wrx != null -> VariantTab.WRX
                    gen?.variants?.sti != null -> VariantTab.STI
                    else -> VariantTab.SPECIAL_EDITIONS
                }
                state.copy(
                    isLoading = false,
                    generation = gen,
                    torqueSpecs = torques,
                    colorCodes = relevantColors,
                    selectedTab = initialTab
                )
            }
        }
    }

    fun selectTab(tab: VariantTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun toggleSection(section: DetailSection) {
        _uiState.update { state ->
            val current = state.expandedSections
            val next = if (current.contains(section)) {
                current - section
            } else {
                current + section
            }
            state.copy(expandedSections = next)
        }
    }

    class Factory(
        private val generationId: String,
        private val repository: WrxRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DetailViewModel(generationId, repository) as T
        }
    }
}
