package com.dishcovery.app.presentation.ui.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.ui.res.vectorResource
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dishcovery.app.domain.model.KitchenCategory
import com.dishcovery.app.presentation.ui.theme.Rojo
import com.dishcovery.app.presentation.ui.theme.TextPrimary
import com.dishcovery.app.R


/**
 * Card showing publicly visible user data: skills, language and location.
 */
@Composable
fun ProfilePublicData(offeredSkills: List<KitchenCategory>, language: String, location: String) {
    Column(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Datos Públicos:", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextPrimary)
        Surface(Modifier.fillMaxWidth(), RoundedCornerShape(10.dp), color = TextPrimary.copy(.06f)) {

            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ProfileDataRow(
                    icon = Icons.Default.Star,
                    text = offeredSkills.joinToString(", ") { it.name }.ifEmpty { "Sin habilidades" }
                )
                ProfileDataRow(
                    iconRes = R.drawable.public_language,
                    text = language
                )
                ProfileDataRow(icon = Icons.Default.LocationOn, text = location)
            }
        }
    }
}

@Composable
private fun ProfileDataRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Icon(icon, null, tint = Rojo, modifier = Modifier.size(18.dp))
        Text(text, fontSize = 14.sp, color = TextPrimary.copy(.7f))
    }
}

//Operator overload, public_language xml picture won't stop creating errors
@Composable
private fun ProfileDataRow(iconRes: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Icon(painterResource(iconRes), null, tint = Rojo, modifier = Modifier.size(18.dp))
        Text(text, fontSize = 14.sp, color = TextPrimary.copy(.7f))
    }
}