package com.dishcovery.app.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dishcovery.app.domain.model.Message
import com.dishcovery.app.presentation.ui.theme.*
import androidx.compose.ui.graphics.Color

@Composable
fun MessageBubble(message: Message) {

    val isMe = message.senderId == 2L

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {

        Box(
            modifier = Modifier
                .padding(8.dp)
                .background(
                    color = if (isMe) Primary else BackgroundLight,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
        ) {
            Text(
                text = message.message,
                color = if (isMe) Color.White else TextPrimary
            )
        }
    }
}