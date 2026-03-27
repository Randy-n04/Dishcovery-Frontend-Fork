package com.dishcovery.app.presentation.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dishcovery.app.presentation.ui.theme.TextPrimary

/**
 * A labeled group of tappable menu rows.
 */
@Composable
fun ProfileMenuSection(title: String, items: List<Triple<String, () -> Unit, Color>>) {
    Column(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextPrimary)
        Surface(Modifier.fillMaxWidth(), RoundedCornerShape(10.dp), color = TextPrimary.copy(.06f)) {
            Column {
                items.forEachIndexed { i, (label, onClick, color) ->
                    if (i > 0) HorizontalDivider(color = TextPrimary.copy(.08f))
                    ProfileMenuItem(label = label, onClick = onClick, color = color)
                }
            }
        }
    }
}

@Composable
private fun ProfileMenuItem(label: String, onClick: () -> Unit, color: Color = TextPrimary) {
    Row(
        Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 14.dp),
        Arrangement.SpaceBetween, Alignment.CenterVertically
    ) {
        Text(label, fontSize = 14.sp, color = color)
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight, null,
            tint = color.copy(alpha = .4f),
            modifier = Modifier.size(18.dp)
        )
    }
}