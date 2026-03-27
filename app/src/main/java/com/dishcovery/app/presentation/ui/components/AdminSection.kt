package com.dishcovery.app.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dishcovery.app.R
import com.dishcovery.app.presentation.ui.theme.Rojo



@Composable
fun AdminSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    var expanded by remember { mutableStateOf(true) }
    Surface(Modifier.fillMaxWidth(), RoundedCornerShape(12.dp), Color.White, shadowElevation = 2.dp) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(Modifier.fillMaxWidth().clickable { expanded = !expanded },
                 Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Add, null, tint = Rojo, modifier = Modifier.size(20.dp))
                    Text(title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Rojo)
                }
                Icon(if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    if (expanded) stringResource(R.string.collapse_desc) else stringResource(R.string.expand_desc), tint = Rojo)
            }
            AnimatedVisibility(expanded, enter = expandVertically(), exit = shrinkVertically()) {
                Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp), content = content)
            }
        }
    }
}