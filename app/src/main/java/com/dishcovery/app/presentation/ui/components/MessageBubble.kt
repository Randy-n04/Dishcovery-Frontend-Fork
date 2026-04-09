package com.dishcovery.app.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.dishcovery.app.domain.model.Message
import com.dishcovery.app.presentation.ui.theme.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dishcovery.app.R

@Composable
fun MessageBubble(
    message: Message,
    isFirstChatHighlight: Boolean = false,
    screenHeightPx: Float,
    onLongClick: (yFraction: Float) -> Unit = {}
) {
    val isMe = message.senderId == 2L
    val bubbleShape = RoundedCornerShape(18.dp)

    var isPressed by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (isPressed) 0.6f else 1f,
        animationSpec = tween(150),
        label = "bubbleAlpha"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .widthIn(max = 290.dp)
                .alpha(alpha)
                .border(
                    width = 1.dp,
                    color = if (!isMe) Color(0xFFCCCCCC) else Color.Transparent,
                    shape = bubbleShape
                )
                .background(
                    brush = when {
                        isMe -> Brush.horizontalGradient(listOf(Primary, Primary))
                        isFirstChatHighlight -> Brush.linearGradient(
                            colors = listOf(Color(0xFFF3F3F5), Color(0xFFE0E1E6))
                        )
                        else -> Brush.horizontalGradient(listOf(Gris, Gris))
                    },
                    shape = bubbleShape
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            isPressed = true
                            tryAwaitRelease()
                            isPressed = false
                        },
                        onLongPress = { offset ->
                            onLongClick(offset.y / screenHeightPx)
                        }
                    )
                }
                .padding(12.dp)
        ) {
            Text(
                text = message.message,
                color = if (isMe) Color.White else TextPrimary
            )
        }
    }
}