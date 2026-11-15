package com.dariusepure.wrxpedia.ui.compare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dariusepure.wrxpedia.data.model.CarSpec
import com.dariusepure.wrxpedia.data.repository.WrxRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CompareOption(
    val id: String,
    val generationName: String,
    val variantTitle: String,
    val iconName: String,
    val spec: CarSpec
)

data class CompareUiState(
    val availableOptions: List<CompareOption> = emptyList(),
    val option1: CompareOption? = null,
    val option2: CompareOption? = null,
    val isLoading: Boolean = true
)

class CompareViewModel(
    private val repository: WrxRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompareUiState())
    val uiState: StateFlow<CompareUiState> = _uiState.asStateFlow()

    init {
        loadOptions()
    }

    private fun loadOptions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getGenerations().collect { generations ->
                val options = mutableListOf<CompareOption>()
                for (gen in generations) {
                    gen.variants.wrx?.let { spec ->
                        options.add(
                            CompareOption(
                                id = "${gen.id}_wrx",
                                generationName = gen.name,
                                variantTitle = "${gen.nickname} WRX",
                                iconName = gen.iconName,
                                spec = spec
                            )
                        )
                    }
                    gen.variants.sti?.let { spec ->
                        options.add(
                            CompareOption(
                                id = "${gen.id}_sti",
                                generationName = gen.name,
                                variantTitle = "${gen.nickname} STI",
                                iconName = gen.iconName,
                                spec = spec
                            )
                        )
                    }
                    gen.variants.specialEditions?.forEach { se ->
                        val seSpec = CarSpec(
                            engineCode = se.engineCode,
                            powerHp = se.powerHp,
                            torqueNm = se.torqueNm,
                            turboModel = "STI Custom",
                            stockBoost = "N/A",
                            intercooler = "STI Intercooler",
                            transmission = "6-Speed Manual",
                            driveType = "DCCD AWD",
                            finalDrive = "N/A",
                            engineOilCapacity = "4.3 L",
                            gearboxOilCapacity = "4.1 L",
                            chassisCode = gen.chassisCodes,
                            pcd = "5x114.3",
                            brakes = "Brembo High Performance"
                        )
                        options.add(
                            CompareOption(
                                id = "${gen.id}_${se.id}",
                                generationName = gen.name,
                                variantTitle = se.name,
                                iconName = gen.iconName,
                                spec = seSpec
                            )
                        )
                    }
                }

                val opt1 = options.find { it.id.contains("classic") && it.id.contains("wrx") } ?: options.firstOrNull()
                val opt2 = options.find { it.id.contains("va") && it.id.contains("sti") } ?: options.lastOrNull()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        availableOptions = options,
                        option1 = opt1,
                        option2 = opt2
                    )
                }
            }
        }
    }

    fun selectOption1(option: CompareOption) {
        _uiState.update { it.copy(option1 = option) }
    }

    fun selectOption2(option: CompareOption) {
        _uiState.update { it.copy(option2 = option) }
    }

    fun swapOptions() {
        _uiState.update {
            it.copy(option1 = it.option2, option2 = it.option1)
        }
    }

    class Factory(private val repository: WrxRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CompareViewModel(repository) as T
        }
    }
}
