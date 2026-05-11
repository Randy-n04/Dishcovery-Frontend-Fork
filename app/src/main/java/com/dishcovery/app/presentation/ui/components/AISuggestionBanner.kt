package com.dishcovery.app.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dishcovery.app.R

@Composable
fun AISuggestionBanner() {
    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFFFFA07A), Color(0xFFFF6347))
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(gradient)
            .padding(16.dp)
    ) {
        Row {
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.ai_plan_suggestion),
                color = Color.White,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}
