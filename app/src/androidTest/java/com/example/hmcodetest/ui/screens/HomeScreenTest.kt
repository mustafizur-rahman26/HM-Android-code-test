package com.example.hmcodetest.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.hmcodetest.domain.model.Product
import com.example.hmcodetest.domain.model.Swatch
import com.example.hmcodetest.ui.theme.HmcodetestTheme
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleProducts = listOf(
        Product(
            id = "1",
            name = "Slim Fit Jeans",
            brand = "H&M",
            price = "24.99 Kr.",
            thumbnail = "https://example.com/image1.jpg",
            swatches = listOf(Swatch("666666"), Swatch("000000"))
        ),
        Product(
            id = "2",
            name = "Cotton T-shirt",
            brand = "H&M",
            price = "10.39 Kr.",
            thumbnail = "https://example.com/image2.jpg",
            swatches = listOf(Swatch("FFFFFF"), Swatch("FF0000"))
        ),
        Product(
            id = "3",
            name = "Hooded Sweatshirt",
            brand = "H&M",
            price = "29.99 Kr.",
            thumbnail = "https://example.com/image3.jpg",
            swatches = listOf(Swatch("003366"))
        )
    )

    val sampleErrorMessage = "Failed to load products"

    @Test
    fun loadingState_displaysLoadingIndicator() {
        // Given
        val uiState = UiState(
            products = emptyList(),
            isLoading = true,
            errorMessage = null
        )

        // When
        composeTestRule.setContent {
            HmcodetestTheme {
                ProductsContent(
                    uiState = uiState,
                    onLoadMore = {},
                    onRetryLoadMore = {},
                    onScrollToTop = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Loading products")
            .assertIsDisplayed()
    }

    @Test
    fun errorState_displaysErrorMessageAndRetryButton() {
        // Given
        val uiState = UiState(
            products = emptyList(),
            isLoading = false,
            errorMessage = sampleErrorMessage
        )

        // When
        composeTestRule.setContent {
            HmcodetestTheme {
                ProductsContent(
                    uiState = uiState,
                    onLoadMore = {},
                    onRetryLoadMore = {},
                    onScrollToTop = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("Error: $sampleErrorMessage")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription("Retry loading products")
            .assertIsDisplayed()
    }

    @Test
    fun emptyState_displaysEmptyMessage() {
        // Given
        val uiState = UiState(
            products = emptyList(),
            isLoading = false,
            errorMessage = null
        )

        // When
        composeTestRule.setContent {
            HmcodetestTheme {
                ProductsContent(
                    uiState = uiState,
                    onLoadMore = {},
                    onRetryLoadMore = {},
                    onScrollToTop = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("No products available")
            .assertIsDisplayed()
    }

    @Test
    fun successState_displaysProductList() {
        // Given
        val uiState = UiState(
            products = sampleProducts,
            isLoading = false,
            errorMessage = null
        )

        // When
        composeTestRule.setContent {
            HmcodetestTheme {
                ProductsContent(
                    uiState = uiState,
                    onLoadMore = {},
                    onRetryLoadMore = {},
                    onScrollToTop = {}
                )
            }
        }

        // Then - Verify each product's details
        // Product 1
        composeTestRule.onAllNodesWithText("H&M")[0].assertIsDisplayed()
        composeTestRule.onNodeWithText("SLIM FIT JEANS", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("24.99 Kr.").assertIsDisplayed()

        // Product 2
        composeTestRule.onAllNodesWithText("H&M")[1].assertIsDisplayed()
        composeTestRule.onNodeWithText("COTTON T-SHIRT", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("10.39 Kr.").assertIsDisplayed()

        // Product 3
        composeTestRule.onAllNodesWithText("H&M")[2].assertIsDisplayed()
        composeTestRule.onNodeWithText("HOODED SWEATSHIRT", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("29.99 Kr.").assertIsDisplayed()
    }

    @Test
    fun scrollToTopButton_notDisplayed_whenCurrentPageIsLessThanThreshold() {
        // Given
        val uiState = UiState(
            products = sampleProducts,
            isLoading = false,
            errorMessage = null,
            currentPage = 2 // Less than threshold of 3
        )

        // When
        composeTestRule.setContent {
            HmcodetestTheme {
                ProductsContent(
                    uiState = uiState,
                    onLoadMore = {},
                    onRetryLoadMore = {},
                    onScrollToTop = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Scroll to top of product list")
            .assertDoesNotExist()
    }

    @Test
    fun scrollToTopButton_displayed_whenCurrentPageIsGreaterThanThreshold() {
        // Given
        val uiState = UiState(
            products = sampleProducts,
            isLoading = false,
            errorMessage = null,
            currentPage = 5 // Greater than threshold of 3
        )

        // When
        composeTestRule.setContent {
            HmcodetestTheme {
                ProductsContent(
                    uiState = uiState,
                    onLoadMore = {},
                    onRetryLoadMore = {},
                    onScrollToTop = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Scroll to top of product list")
            .assertIsDisplayed()
    }


    @Test
    fun productWithNoPrice_displaysNA() {
        // Given
        val productWithoutPrice = Product(
            id = "1",
            name = "Free Item",
            brand = "Brand",
            price = null,
            thumbnail = "https://example.com/image.jpg",
            swatches = emptyList()
        )
        val uiState = UiState(
            products = listOf(productWithoutPrice),
            isLoading = false,
            errorMessage = null
        )

        // When
        composeTestRule.setContent {
            HmcodetestTheme {
                ProductsContent(
                    uiState = uiState,
                    onLoadMore = {},
                    onRetryLoadMore = {},
                    onScrollToTop = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithText("N/A")
            .assertIsDisplayed()
    }

    @Test
    fun productWithMoreThanThreeSwatches_displaysOnlyThreeSwatchesWithCounter() {
        // Given
        val productWithManySwatches = Product(
            id = "1",
            name = "Colorful Shirt",
            brand = "Brand",
            price = "$19.99",
            thumbnail = "https://example.com/image.jpg",
            swatches = listOf(
                Swatch("FF0000"),
                Swatch("00FF00"),
                Swatch("0000FF"),
                Swatch("FFFF00"),
                Swatch("FF00FF"),
                Swatch("00FFFF")
            )
        )
        val uiState = UiState(
            products = listOf(productWithManySwatches),
            isLoading = false,
            errorMessage = null
        )

        // When
        composeTestRule.setContent {
            HmcodetestTheme {
                ProductsContent(
                    uiState = uiState,
                    onLoadMore = {},
                    onRetryLoadMore = {},
                    onScrollToTop = {}
                )
            }
        }

        // Then - Should show +3 indicator for remaining colors
        composeTestRule
            .onNodeWithText("+3")
            .assertExists()
        composeTestRule
            .onNodeWithContentDescription("Available in 6 colors", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun productWithNoSwatches_doesNotDisplaySwatchRow() {
        // Given
        val productWithoutSwatches = Product(
            id = "1",
            name = "Simple Item",
            brand = "Brand",
            price = "$19.99",
            thumbnail = "https://example.com/image.jpg",
            swatches = emptyList()
        )
        val uiState = UiState(
            products = listOf(productWithoutSwatches),
            isLoading = false,
            errorMessage = null
        )

        // When
        composeTestRule.setContent {
            HmcodetestTheme {
                ProductsContent(
                    uiState = uiState,
                    onLoadMore = {},
                    onRetryLoadMore = {},
                    onScrollToTop = {}
                )
            }
        }

        // Then - Should not have any color-related content descriptions
        composeTestRule
            .onNodeWithContentDescription("Available in", substring = true)
            .assertDoesNotExist()
    }

    @Test
    fun productImage_hasCorrectAccessibilityDescription() {
        // Given
        val uiState = UiState(
            products = sampleProducts.take(1),
            isLoading = false,
            errorMessage = null
        )

        // When
        composeTestRule.setContent {
            HmcodetestTheme {
                ProductsContent(
                    uiState = uiState,
                    onLoadMore = {},
                    onRetryLoadMore = {},
                    onScrollToTop = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("Product image for H&M Slim Fit Jeans")
            .assertIsDisplayed()
    }

    @Test
    fun successState_productItem_hasCorrectAccessibilityDescription() {
        // Given
        val uiState = UiState(
            products = sampleProducts.take(1),
            isLoading = false,
            errorMessage = null
        )

        // When
        composeTestRule.setContent {
            HmcodetestTheme {
                ProductsContent(
                    uiState = uiState,
                    onLoadMore = {},
                    onRetryLoadMore = {},
                    onScrollToTop = {}
                )
            }
        }

        // Then
        composeTestRule
            .onNodeWithContentDescription("H&M Slim Fit Jeans, priced at 24.99 Kr.", substring = true)
            .assertIsDisplayed()
    }
}
