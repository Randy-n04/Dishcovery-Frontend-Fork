package com.dishcovery.app.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.dishcovery.app.presentation.ui.theme.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.sp

@Composable
fun MessageContextMenu(
    visible: Boolean,
    showAbove: Boolean,
    actions: List<Pair<String, () -> Unit>>,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(150)) + scaleIn(
            tween(150),
            transformOrigin = if (showAbove)
                TransformOrigin(0.5f, 1f)
            else
                TransformOrigin(0.5f, 0f)
        ),
        exit = fadeOut(tween(100)) + scaleOut(
            tween(100),
            transformOrigin = if (showAbove)
                TransformOrigin(0.5f, 1f)
            else
                TransformOrigin(0.5f, 0f)
        ),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = Color.Black.copy(alpha = 0.12f),
                    spotColor = Color.Black.copy(alpha = 0.12f)
                )
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            actions.forEachIndexed { index, (label, onClick) ->

                Text(
                    text = label,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onClick() }
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF555555)
                )

                if (index < actions.lastIndex) {
                    Box(
                        modifier = Modifier
                            .height(20.dp)
                            .width(0.5.dp)
                            .background(Color(0xFFE4E4E4))
                    )
                }
            }
        }
    }
}