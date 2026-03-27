package com.dishcovery.app.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dishcovery.app.R
import com.dishcovery.app.presentation.ui.theme.Rojo

@Composable
fun AdminHeader(onLogout: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().background(Rojo).padding(16.dp, 12.dp),
         Arrangement.SpaceBetween, Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).background(Color.White.copy(.3f)), Alignment.Center) {
                Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(28.dp))
            }
            Column {
                Text("James S.", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(stringResource(R.string.admin_panel_title), color = Color.White.copy(.85f), fontSize = 12.sp)
            }
        }
        IconButton(onLogout, Modifier.size(40.dp).clip(CircleShape).background(Color.White.copy(.2f))) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.logout_button_desc), tint = Color.White)
        }
    }
}
