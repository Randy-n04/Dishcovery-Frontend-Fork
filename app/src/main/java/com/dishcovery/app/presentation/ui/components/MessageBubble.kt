package com.dishcovery.app.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.dishcovery.app.domain.model.Message
import com.dishcovery.app.presentation.ui.theme.*
import androidx.compose.ui.graphics.Color

@Composable
fun MessageBubble(
    message: Message,
    isFirstChatHighlight: Boolean = false
) {

    val isMe = message.senderId == 2L

    val bubbleShape = RoundedCornerShape(18.dp)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {

        Box(
            modifier = Modifier
                .padding(8.dp)
                .widthIn(max = 290.dp)
                .background(
                    brush = when {
                        isMe -> Brush.horizontalGradient(listOf(Primary, Primary))
                        isFirstChatHighlight -> Brush.linearGradient(
                            colors = listOf(Color(0xFFF3F3F5), Color(0xFFE0E1E6))
                        )
                        else -> Brush.horizontalGradient(listOf(BackgroundLight, BackgroundLight))
                    },
                    shape = bubbleShape
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