package com.dishcovery.app.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dishcovery.app.R
import com.dishcovery.app.domain.model.Chat
import coil.compose.AsyncImage
import com.dishcovery.app.presentation.ui.theme.Primary

@Composable
fun ChatItem(
    chat: Chat,
    displayName: String,
    onClick: () -> Unit = {}
) {
    // Formatear para mostrar solo HH:mm, manejando si viene con fecha (ej. "2023-10-10 14:30:00")
    val timeOnly = chat.time.split(" ").last()
    val formattedTime = if (timeOnly.contains(":")) {
        val parts = timeOnly.split(":")
        if (parts.size >= 2) "${parts[0]}:${parts[1]}" else timeOnly
    } else {
        timeOnly
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = chat.avatarUrl,
            contentDescription = stringResource(R.string.avatar_desc),
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(displayName, style = MaterialTheme.typography.titleMedium)
            Text(
                chat.lastMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(formattedTime, style = MaterialTheme.typography.labelSmall)
            // Se eliminó el indicador de notificaciones/mensajes no leídos por solicitud
        }
    }
}
