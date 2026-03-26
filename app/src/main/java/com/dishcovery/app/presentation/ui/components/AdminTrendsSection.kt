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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dishcovery.app.presentation.ui.theme.Naranja
import com.dishcovery.app.presentation.ui.theme.Rojo
import com.dishcovery.app.presentation.ui.theme.TextPrimary



@Composable
fun AdminTrendsSection() {
    var baseWeight by remember { mutableStateOf(0.5f) }
    var seasonWeight by remember { mutableStateOf(0.3f) }
    var sevenDayActive by remember { mutableStateOf(false) }

    AdminSection("Agregar Tendencia") {
        Text("Ingrese el nombre", fontSize = 13.sp, color = TextPrimary.copy(.7f))
        Row(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp))
                .border(1.dp, TextPrimary.copy(.2f), RoundedCornerShape(6.dp))
                .padding(12.dp, 10.dp),
            Arrangement.SpaceBetween, Alignment.CenterVertically
        ) {
            Text("Sin tendencias disponibles", color = TextPrimary.copy(.4f), fontSize = 14.sp)
            Icon(Icons.Default.KeyboardArrowDown, null, tint = TextPrimary.copy(.4f))
        }
        listOf("Peso Base" to baseWeight, "Peso Temporada" to seasonWeight).forEachIndexed { i, (label, value) ->
            Row(Modifier.fillMaxWidth(),  Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Text(label, fontSize = 13.sp, color = TextPrimary.copy(.7f), modifier = Modifier.width(120.dp))
                Slider(value, { if (i == 0) baseWeight = it else seasonWeight = it }, Modifier.width(140.dp),
                    colors = SliderDefaults.colors(thumbColor = Naranja, activeTrackColor = Naranja, inactiveTrackColor = Naranja.copy(.3f)))
            }
        }
        Row(Modifier.fillMaxWidth(),  Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Text("Activar por 7 días", fontSize = 13.sp, color = TextPrimary.copy(.6f))
            Switch(sevenDayActive, { sevenDayActive = it },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Rojo,
                    uncheckedThumbColor = Color.White, uncheckedTrackColor = TextPrimary.copy(.3f)))
        }
        Button({ }, Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Rojo)) {
            Text("Agregar", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }
    }
}