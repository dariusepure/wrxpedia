package com.dariusepure.wrxpedia.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dariusepure.wrxpedia.data.model.Generation
import com.dariusepure.wrxpedia.data.model.SearchResultItem
import com.dariusepure.wrxpedia.data.repository.WrxRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val generations: List<Generation> = emptyList(),
    val searchResults: List<SearchResultItem> = emptyList(),
    val isSearching: Boolean = false
)

class HomeViewModel(
    private val repository: WrxRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadGenerations()
    }

    private fun loadGenerations() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.getGenerations().collect { list ->
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            generations = list
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Failed to load generations", e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query, isSearching = query.isNotBlank()) }
        viewModelScope.launch {
            if (query.isBlank()) {
                _uiState.update { it.copy(searchResults = emptyList()) }
            } else {
                val results = repository.search(query)
                _uiState.update { it.copy(searchResults = results) }
            }
        }
    }

    fun clearSearch() {
        _uiState.update { it.copy(searchQuery = "", searchResults = emptyList(), isSearching = false) }
    }

    class Factory(private val repository: WrxRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(repository) as T
        }
    }
}
