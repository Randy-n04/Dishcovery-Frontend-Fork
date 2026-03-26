package com.dishcovery.app.presentation.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dishcovery.app.presentation.ui.theme.Rojo
import com.dishcovery.app.presentation.ui.theme.TextPrimary

@Composable
fun AdminStatsSection() {
    AdminSection("Estadísticas") {
        listOf("Usuarios Activos", "Matches Realizados").forEachIndexed { i, title ->
            if (i > 0) HorizontalDivider(color = TextPrimary.copy(.1f))
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Rojo)
            Box(
                Modifier.fillMaxWidth().height(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(TextPrimary.copy(alpha = 0.06f)),
                Alignment.Center
            ) {
                Text("Sin datos disponibles", fontSize = 13.sp, color = TextPrimary.copy(.4f))
            }
        }
    }
}