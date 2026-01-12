package com.example.hmcodetest.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    Text(
        modifier = Modifier
            .padding(20.dp),
        text = "Welcome to Home Screen"
    )
}