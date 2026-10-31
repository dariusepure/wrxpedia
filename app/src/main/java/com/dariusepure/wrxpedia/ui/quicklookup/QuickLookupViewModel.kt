package com.dariusepure.wrxpedia.ui.quicklookup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dariusepure.wrxpedia.data.model.ColorCode
import com.dariusepure.wrxpedia.data.model.FluidCapacity
import com.dariusepure.wrxpedia.data.model.SpecialEditionWithGeneration
import com.dariusepure.wrxpedia.data.model.TorqueSpec
import com.dariusepure.wrxpedia.data.repository.WrxRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class QuickLookupType {
    TORQUE_SPECS,
    FLUID_CAPACITIES,
    COLOR_CODES,
    SPECIAL_EDITIONS
}

data class QuickLookupUiState(
    val type: QuickLookupType = QuickLookupType.TORQUE_SPECS,
    val isLoading: Boolean = true,
    val torqueSpecs: List<TorqueSpec> = emptyList(),
    val fluidCapacities: List<FluidCapacity> = emptyList(),
    val colorCodes: List<ColorCode> = emptyList(),
    val specialEditions: List<SpecialEditionWithGeneration> = emptyList()
)

class QuickLookupViewModel(
    private val type: QuickLookupType,
    private val repository: WrxRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuickLookupUiState(type = type))
    val uiState: StateFlow<QuickLookupUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (type) {
                QuickLookupType.TORQUE_SPECS -> {
                    val specs = repository.getTorqueSpecs()
                    _uiState.update { it.copy(isLoading = false, torqueSpecs = specs) }
                }
                QuickLookupType.FLUID_CAPACITIES -> {
                    val fluids = repository.getFluidCapacities()
                    _uiState.update { it.copy(isLoading = false, fluidCapacities = fluids) }
                }
                QuickLookupType.COLOR_CODES -> {
                    val colors = repository.getColorCodes()
                    _uiState.update { it.copy(isLoading = false, colorCodes = colors) }
                }
                QuickLookupType.SPECIAL_EDITIONS -> {
                    val editions = repository.getAllSpecialEditions()
                    _uiState.update { it.copy(isLoading = false, specialEditions = editions) }
                }
            }
        }
    }

    class Factory(
        private val type: QuickLookupType,
        private val repository: WrxRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return QuickLookupViewModel(type, repository) as T
        }
    }
}
