package com.dishcovery.app.presentation.ui.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dishcovery.app.presentation.ui.components.HomeHeader

@Composable
fun HomeLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        HomeHeader()
        content()
    }
}
