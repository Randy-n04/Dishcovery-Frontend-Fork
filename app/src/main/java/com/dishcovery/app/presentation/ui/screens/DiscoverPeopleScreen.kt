package com.dishcovery.app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dishcovery.app.R
import com.dishcovery.app.presentation.ui.theme.Cafe
import com.dishcovery.app.presentation.ui.theme.Primary

data class DiscoverUser(
    val name: String,
    val rating: Double,
    val location: String,
    val canDo: String,
    val wantToLearn: String,
    val matchPercentage: Int,
    val imageUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverPeopleScreen(
    paddingValues: PaddingValues,
    onBack: () -> Unit,
    onConnect: (DiscoverUser) -> Unit
) {
    // Datos quemados de ejemplo
    val users = listOf(
        DiscoverUser(
            name = "Javier Rodriguez",
            rating = 4.9,
            location = "San José, Costa Rica",
            canDo = "Cocina italiana",
            wantToLearn = "Cocina Mexicana",
            matchPercentage = 95,
            imageUrl = "https://api.dicebear.com/7.x/avataaars/svg?seed=Javier"
        ),
        DiscoverUser(
            name = "Maria Lopez",
            rating = 4.7,
            location = "Alajuela, Costa Rica",
            canDo = "Reposteria",
            wantToLearn = "Cocina Japonesa",
            matchPercentage = 88,
            imageUrl = "https://api.dicebear.com/7.x/avataaars/svg?seed=Maria"
        )
    )

    var currentIndex by remember { mutableIntStateOf(0) }
    val currentUser = users[currentIndex]

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.discover_people_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Cafe,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        modifier = Modifier.padding(paddingValues)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF9F9F2)) 
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Imagen y Match Badge
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF8BBD0)) 
                    ) {
                        AsyncImage(
                            model = currentUser.imageUrl,
                            contentDescription = currentUser.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                        
                        Surface(
                            modifier = Modifier
                                .padding(12.dp)
                                .align(Alignment.TopEnd),
                            color = Primary.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "${currentUser.matchPercentage}% ${stringResource(R.string.match_percentage_suffix)}",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Información del Usuario
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${currentUser.name} - ${currentUser.rating}",
                                style = MaterialTheme.typography.titleLarge,
                                color = Cafe,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFB300),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentUser.location,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        SkillSection(
                            title = stringResource(R.string.can_do_label),
                            skill = currentUser.canDo,
                            icon = "🍳"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        SkillSection(
                            title = stringResource(R.string.want_to_learn_label),
                            skill = currentUser.wantToLearn,
                            icon = "💡"
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Botones de acción
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = { 
                                    currentIndex = (currentIndex + 1) % users.size
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBDBDBD)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Close, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(stringResource(R.string.next_button), fontWeight = FontWeight.Bold)
                                }
                            }

                            Button(
                                onClick = { onConnect(currentUser) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Favorite, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(stringResource(R.string.connect_button), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SkillSection(title: String, skill: String, icon: String) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Surface(
            color = Color(0xFFF1F4E6), 
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = skill,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = Cafe
            )
        }
    }
}
