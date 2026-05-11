package com.dishcovery.app.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dishcovery.app.R

@Composable
fun ProgressSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.progress_label),
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Text(
                text = stringResource(R.string.sessions_count_label, 0, 3),
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { 0f },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(CircleShape),
            color = Color(0xFFFF6347),
            trackColor = Color(0xFFE0E0E0)
        )
    }
}
