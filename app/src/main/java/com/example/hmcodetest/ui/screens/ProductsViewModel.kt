package com.example.hmcodetest.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hmcodetest.domain.repository.ProductsRepository
import com.example.hmcodetest.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UI State for the products screen with pagination support.
 */
data class UiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val hasMorePages: Boolean = true,
    val currentPage: Int = 0
)

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productsRepository: ProductsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val uiStateVal: UiState
        get() = _uiState.value

    private var loadMoreJob: Job? = null

    init {
        loadInitialProducts()
    }

    /**
     * Loads the first page of products
     */
    private fun loadInitialProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            productsRepository.getProducts(page = 1)
                .onSuccess { data ->
                    _uiState.update {
                        it.copy(
                            products = data.products,
                            isLoading = false,
                            error = null,
                            hasMorePages = data.hasMorePages,
                            currentPage = data.currentPage
                        )
                    }
                }
                .onError { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error
                        )
                    }
                }
        }
    }

    /**
     * Loads the next page of products and appends to existing list
     */
    fun loadMoreProducts() {
        // Prevent multiple simultaneous requests
        if (uiStateVal.isLoadingMore || !uiStateVal.hasMorePages || loadMoreJob?.isActive == true) {
            return
        }

        loadMoreJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true, error = null) }
            
            val nextPage = uiStateVal.currentPage + 1
            
            productsRepository.getProducts(page = nextPage)
                .onSuccess { data ->
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            products = currentUiState.products + data.products,
                            isLoadingMore = false,
                            error = null,
                            hasMorePages = data.hasMorePages,
                            currentPage = data.currentPage
                        )
                    }
                }
                .onError { error ->
                    _uiState.update {
                        it.copy(
                            isLoadingMore = false,
                            error = error
                        )
                    }
                }
        }
    }

}
