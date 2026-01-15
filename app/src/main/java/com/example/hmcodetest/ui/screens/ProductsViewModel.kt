package com.example.hmcodetest.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hmcodetest.domain.repository.ProductsRepository
import com.example.hmcodetest.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class UiEvent {
    data object ScrollToTop : UiEvent()
}

data class UiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val hasMorePages: Boolean = true,
    val currentPage: Int = 0,
    val nextPage: Int? = null
) {
    val shouldShowScrollToTopButton: Boolean
        get() = currentPage > 3
}

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productsRepository: ProductsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()
    private val uiStateVal: UiState
        get() = _uiState.value

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    private var loadingJob: Job? = null

    init {
        loadInitialProducts()
    }

    private fun loadInitialProducts() {
        if (uiStateVal.isLoading || loadingJob?.isActive == true) {
            return
        }

        loadingJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            loadProducts(page = 1)
        }
    }


    fun loadMoreProducts() {
        if (uiStateVal.isLoadingMore || !uiStateVal.hasMorePages || loadingJob?.isActive == true) {
            return
        }

        loadingJob = viewModelScope.launch {
            val nextPage = uiStateVal.nextPage ?: (uiStateVal.currentPage + 1)

            _uiState.update { it.copy(isLoadingMore = true, error = null) }
            loadProducts(page = nextPage)
        }
    }


    private suspend fun loadProducts(page: Int) {
        productsRepository.getProducts(page = page)
            .onSuccess { data ->
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        products = (currentUiState.products + data.products).distinctBy { it.id },
                        isLoading = false,
                        isLoadingMore = false,
                        error = null,
                        hasMorePages = data.hasMorePages,
                        currentPage = data.currentPage,
                        nextPage = data.nextPage
                    )
                }
            }
            .onError { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        error = error
                    )
                }
            }
    }

    fun onScrollToTopClicked() {
        loadingJob?.cancel()

        _uiState.update {
            it.copy(
                currentPage = 1,
                isLoading = false,
                isLoadingMore = false,
                error = null
            )
        }

        viewModelScope.launch {
            _events.emit(UiEvent.ScrollToTop)
        }
    }
}
