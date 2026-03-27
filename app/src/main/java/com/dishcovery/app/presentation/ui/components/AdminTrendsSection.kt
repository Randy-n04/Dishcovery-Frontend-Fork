package com.dishcovery.app.presentation.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dishcovery.app.R
import com.dishcovery.app.presentation.ui.theme.Naranja
import com.dishcovery.app.presentation.ui.theme.Rojo
import com.dishcovery.app.presentation.ui.theme.TextPrimary



@Composable
fun AdminTrendsSection() {
    var baseWeight by remember { mutableStateOf(0.5f) }
    var seasonWeight by remember { mutableStateOf(0.3f) }
    var sevenDayActive by remember { mutableStateOf(false) }

    AdminSection(stringResource(R.string.add_trend_title)) {
        Text(stringResource(R.string.enter_name_label), fontSize = 13.sp, color = TextPrimary.copy(.7f))
        Row(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp))
                .border(1.dp, TextPrimary.copy(.2f), RoundedCornerShape(6.dp))
                .padding(12.dp, 10.dp),
            Arrangement.SpaceBetween, Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.no_trends_available), color = TextPrimary.copy(.4f), fontSize = 14.sp)
            Icon(Icons.Default.KeyboardArrowDown, null, tint = TextPrimary.copy(.4f))
        }
        listOf(
            stringResource(R.string.base_weight_label) to baseWeight,
            stringResource(R.string.season_weight_label) to seasonWeight
        ).forEachIndexed { i, (label, value) ->
            Row(Modifier.fillMaxWidth(),  Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Text(label, fontSize = 13.sp, color = TextPrimary.copy(.7f), modifier = Modifier.width(120.dp))
                Slider(value, { if (i == 0) baseWeight = it else seasonWeight = it }, Modifier.width(140.dp),
                    colors = SliderDefaults.colors(thumbColor = Naranja, activeTrackColor = Naranja, inactiveTrackColor = Naranja.copy(.3f)))
            }
        }
        Row(Modifier.fillMaxWidth(),  Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Text(stringResource(R.string.activate_7_days_label), fontSize = 13.sp, color = TextPrimary.copy(.6f))
            Switch(sevenDayActive, { sevenDayActive = it },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Rojo,
                    uncheckedThumbColor = Color.White, uncheckedTrackColor = TextPrimary.copy(.3f)))
        }
        Button({ }, Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Rojo)) {
            Text(stringResource(R.string.add_button), color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }
    }
}