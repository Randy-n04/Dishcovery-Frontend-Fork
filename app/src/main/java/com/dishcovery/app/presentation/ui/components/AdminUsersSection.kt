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
import androidx.compose.ui.unit.sp
import com.dishcovery.app.presentation.ui.theme.TextPrimary

@Composable
fun AdminUsersSection() {
    AdminSection("Gestión de Usuarios") {
        Box(
            Modifier.fillMaxWidth().height(48.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(TextPrimary.copy(alpha = 0.06f)),
            Alignment.Center
        ) {
            Text("Sin usuarios por mostrar", fontSize = 13.sp, color = TextPrimary.copy(.4f))
        }
    }
}