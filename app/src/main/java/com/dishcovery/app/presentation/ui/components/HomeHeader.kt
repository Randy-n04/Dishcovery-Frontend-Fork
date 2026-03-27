package com.dishcovery.app.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dishcovery.app.R
import com.dishcovery.app.presentation.ui.theme.Rojo

@Composable
fun HomeHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(56.dp),
            shape = CircleShape,
            color = Rojo.copy(alpha = 0.8f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.chef_icon),
                contentDescription = "Chef Icon",
                modifier = Modifier.size(55.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Dishcovery",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Rojo
        )
    }
}
