package com.example.hmcodetest.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.hmcodetest.domain.model.Product
import com.example.hmcodetest.domain.model.Swatch
import com.example.hmcodetest.ui.theme.Dimensions

@Composable
fun HomeScreen(
    productsViewModel: ProductsViewModel = hiltViewModel()
) {
    val uiState by productsViewModel.uiState.collectAsStateWithLifecycle()
    val gridState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        productsViewModel.events.collect { event ->
            when (event) {
                is UiEvent.ScrollToTop -> {
                    gridState.animateScrollToItem(0)
                }
            }
        }
    }

    ProductsContent(
        uiState = uiState,
        gridState = gridState,
        onLoadMore = { productsViewModel.loadMoreProducts() },
        onRetryLoadMore = { productsViewModel.retryLoadMore() },
        onScrollToTop = { productsViewModel.onScrollToTopClicked() }
    )
}

@Composable
fun ProductsContent(
    uiState: UiState,
    gridState: LazyGridState = rememberLazyGridState(),
    onLoadMore: () -> Unit,
    onRetryLoadMore: () -> Unit,
    onScrollToTop: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = Dimensions.controlQuadrupleSpace)
    ) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            uiState.errorMessage != null && uiState.products.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Dimensions.controlDoubleSpace),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = Dimensions.controlDoubleSpace),
                        text = "Error: ${uiState.errorMessage}",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                    )
                    Button(onClick = onRetryLoadMore) {
                        Text("Retry")
                    }
                }
            }

            uiState.products.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Dimensions.controlDoubleSpace),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No products available",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            else -> {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Fixed(2),
                    state = gridState,
                    verticalArrangement = Arrangement.spacedBy(Dimensions.controlHalfSpace)
                ) {
                    items(items = uiState.products, key = { it.id }) {
                        ProductGridItem(
                            product = it
                        )
                    }

                    // Loading indicator at bottom when loading more
                    if (uiState.isLoadingMore) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(Dimensions.controlDoubleSpace),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    
                    // Error at bottom with retry button
                    if (uiState.errorMessage != null && uiState.products.isNotEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(Dimensions.controlDoubleSpace),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = uiState.errorMessage,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(bottom = Dimensions.controlSpace)
                                )
                                Button(onClick = onRetryLoadMore) {
                                    Text("Retry")
                                }
                            }
                        }
                    }
                    
                    if (uiState.shouldLoadMore) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            LaunchedEffect(Unit) {
                                onLoadMore()
                            }
                        }
                    }
                }
            }
        }

        // Floating Action Button - Scroll to top
        if (uiState.shouldShowScrollToTopButton) {
            FloatingActionButton(
                modifier = Modifier
                    .padding(Dimensions.controlDoubleSpace)
                    .align(Alignment.BottomEnd),
                shape = CircleShape,
                onClick = onScrollToTop,
            ) {
                Text(
                    text = "↑",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun ProductGridItem(
    product: Product
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        // Product Image
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(product.thumbnail)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .networkCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = product.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            placeholder = ColorPainter(Color(0xFFF5F5F5)),
            error = ColorPainter(Color(0xFFEEEEEE))
        )

        Spacer(modifier = Modifier.height(Dimensions.controlSpace))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimensions.controlDoubleSpace)
                .padding(top = Dimensions.controlDoubleSpace, bottom = Dimensions.controlTripleSpace)
        ) {
            // Brand / Collection
            Text(
                text = product.brand.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Product Name
            Text(
                text = product.name.uppercase(),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Price with currency
            Text(
                text = product.price ?: "N/A",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(Dimensions.controlHalfSpace))

            // Color Swatches
            if (product.swatches.isNotEmpty()) {
                ColorSwatchesRow(swatches = product.swatches)
            }
        }
    }
}

@Composable
private fun ColorSwatchesRow(
    swatches: List<Swatch>,
    maxVisibleSwatches: Int = 3
) {
    val visibleSwatches = swatches.take(maxVisibleSwatches)
    val remainingCount = (swatches.size - maxVisibleSwatches).coerceAtLeast(0)
    val hasMoreSwatches = remainingCount > 0

    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimensions.controlHalfSpace),
        verticalAlignment = Alignment.CenterVertically
    ) {
        visibleSwatches.forEach { swatch ->
            ColorSwatchBox(swatch = swatch)
        }

        if (hasMoreSwatches) {
            Text(
                text = "+$remainingCount",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(start = 2.dp)
            )
        }
    }
}

@Composable
private fun ColorSwatchBox(swatch: Swatch) {
    Box(
        modifier = Modifier
            .size(12.dp)
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            )
            .background(
                color = parseColor(colorCode = swatch.colorCode)
            )
    )
}

private fun parseColor(colorCode: String): Color {
    return try {
        val colorString = if (colorCode.startsWith("#")) colorCode else "#$colorCode"
        Color(android.graphics.Color.parseColor(colorString))
    } catch (e: Exception) {
        Color.Gray // Fallback color
    }
}

// Preview Functions
@Preview(showBackground = true, name = "Products Grid - Light")
@Composable
private fun ProductsContentPreview() {
    MaterialTheme {
        ProductsContent(
            uiState = UiState(
                products = getSampleProducts(),
                isLoading = false,
                errorMessage = null
            ),
            onLoadMore = {},
            onRetryLoadMore = {},
            onScrollToTop = {}
        )
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
private fun ProductsContentLoadingPreview() {
    MaterialTheme {
        ProductsContent(
            uiState = UiState(
                products = emptyList(),
                isLoading = true,
                errorMessage = null
            ),
            onLoadMore = {},
            onRetryLoadMore = {},
            onScrollToTop = {}
        )
    }
}

@Preview(showBackground = true, name = "Error State")
@Composable
private fun ProductsContentErrorPreview() {
    MaterialTheme {
        ProductsContent(
            uiState = UiState(
                products = emptyList(),
                isLoading = false,
                errorMessage = "Failed to load products"
            ),
            onLoadMore = {},
            onRetryLoadMore = {},
            onScrollToTop = {}
        )
    }
}

@Preview(showBackground = true, name = "Empty State")
@Composable
private fun ProductsContentEmptyPreview() {
    MaterialTheme {
        ProductsContent(
            uiState = UiState(
                products = emptyList(),
                isLoading = false,
                errorMessage = null
            ),
            onLoadMore = {},
            onRetryLoadMore = {},
            onScrollToTop = {}
        )
    }
}

@Preview(showBackground = true, name = "Product Grid Item")
@Composable
private fun ProductGridItemPreview() {
    MaterialTheme {
        ProductGridItem(
            product = getSampleProducts().first()
        )
    }
}

// Sample data for previews using current domain model
private fun getSampleProducts() = listOf(
    Product(
        id = "0685816001",
        name = "Slim Fit Jeans",
        brand = "H&M",
        price = "$24.99",
        thumbnail = "https://image.hm.com/assets/hm/e9/c7/e9c7f0e4d8f1c8b8e4f8b8c7e4f8b8c7.jpg",
        swatches = listOf(Swatch("666666"), Swatch("000000"), Swatch("FFFFFF"), Swatch("003366"))
    ),
    Product(
        id = "0714026050",
        name = "Cotton T-shirt",
        brand = "H&M",
        price = "$10.39",
        thumbnail = "https://image.hm.com/assets/hm/c8/7e/c87ef0e4d8f1c8b8e4f8b8c7e4f8b8c7.jpg",
        swatches = listOf(Swatch("666666"), Swatch("000000"), Swatch("FFFFFF"), Swatch("003366"))
    ),
    Product(
        id = "0970819001",
        name = "Hooded Sweatshirt",
        brand = "H&M",
        price = "$29.99",
        thumbnail = "https://image.hm.com/assets/hm/a1/2f/a12ff0e4d8f1c8b8e4f8b8c7e4f8b8c7.jpg",
        swatches = listOf(Swatch("666666"), Swatch("000000"), Swatch("FFFFFF"), Swatch("003366"))
    ),
    Product(
        id = "0608945015",
        name = "Relaxed Fit Hoodie",
        brand = "H&M",
        price = "$24.49",
        thumbnail = "https://image.hm.com/assets/hm/f3/4b/f34bf0e4d8f1c8b8e4f8b8c7e4f8b8c7.jpg",
        swatches = listOf(Swatch("666666"), Swatch("000000"), Swatch("FFFFFF"), Swatch("003366"))
    )
)