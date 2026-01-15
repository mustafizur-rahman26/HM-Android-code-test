package com.example.hmcodetest.data.repository

import com.example.hmcodetest.data.mapper.toPaginatedProducts
import com.example.hmcodetest.data.remote.ApiService
import com.example.hmcodetest.di.IoDispatcher
import com.example.hmcodetest.domain.repository.ProductsRepository
import com.example.hmcodetest.domain.model.PaginatedProducts
import com.example.hmcodetest.util.Async
import com.example.hmcodetest.util.toErrorMessage
import com.example.hmcodetest.util.toErrorType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ProductsRepository {
    
    companion object {
        private const val DEFAULT_QUERY = "jeans"
    }
    
    override suspend fun getProducts(
        page: Int
    ): Async<PaginatedProducts> = withContext(ioDispatcher) {
        runCatching {
            apiService.searchProducts(
                query = DEFAULT_QUERY,
                page = page
            )
        }.fold(
            onSuccess = { response ->
                Async.Success(data = response.toPaginatedProducts())
            },
            onFailure = { error ->
                Async.Error(
                    errorMessage = error.toErrorMessage(),
                    errorType = error.toErrorType()
                )
            }
        )
    }
}