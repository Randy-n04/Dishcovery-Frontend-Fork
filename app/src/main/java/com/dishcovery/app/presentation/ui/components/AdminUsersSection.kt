package com.dishcovery.app.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dishcovery.app.R
import com.dishcovery.app.domain.model.User
import com.dishcovery.app.presentation.ui.theme.Rojo
import com.dishcovery.app.presentation.ui.theme.TextPrimary
import com.dishcovery.app.presentation.viewmodel.AdminViewModel


@Composable
fun AdminUsersSection(
    viewModel: AdminViewModel = hiltViewModel()
) {
    val users       by viewModel.users.collectAsState()
    val isLoading   by viewModel.usersLoading.collectAsState()
    val error       by viewModel.usersError.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()
    val totalPages  by viewModel.totalPages.collectAsState()

    AdminSection(stringResource(R.string.user_management_title)) {
        when {
            isLoading -> {
                Box(Modifier.fillMaxWidth().height(80.dp), Alignment.Center) {
                    CircularProgressIndicator(color = Rojo, modifier = Modifier.size(24.dp))
                }
            }
            error != null -> {
                Text(error!!, color = Rojo, fontSize = 13.sp)
            }
            users.isEmpty() -> {
                Box(
                    Modifier.fillMaxWidth().height(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(TextPrimary.copy(alpha = 0.06f)),
                    Alignment.Center
                ) {
                    Text(
                        stringResource(R.string.no_users_to_show),
                        fontSize = 13.sp,
                        color    = TextPrimary.copy(.4f)
                    )
                }
            }
            else -> {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    users.forEach { user ->
                        AdminUserItem(
                            user    = user,
                            onBlock = { viewModel.blockUser(user.id) }
                        )
                    }
                }

                if (totalPages > 1) {
                    Row(
                        Modifier.fillMaxWidth(),
                        Arrangement.SpaceBetween,
                        Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { viewModel.prevPage() },
                            enabled = currentPage > 0
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = null,
                                tint = if (currentPage > 0) Rojo else TextPrimary.copy(.3f)
                            )
                        }
                        Text(
                            "${currentPage + 1} / $totalPages",
                            fontSize = 13.sp,
                            color    = TextPrimary.copy(.6f)
                        )
                        IconButton(
                            onClick = { viewModel.nextPage() },
                            enabled = currentPage < totalPages - 1
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = if (currentPage < totalPages - 1) Rojo else TextPrimary.copy(.3f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminUserItem(
    user   : User,
    onBlock: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(12.dp, 10.dp),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text       = user.username,
                fontSize   = 14.sp,
                color      = TextPrimary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text     = if (user.isBlocked) stringResource(R.string.user_blocked)
                else stringResource(R.string.user_active),
                fontSize = 12.sp,
                color    = if (user.isBlocked) Rojo else Color(0xFF4CAF50)
            )
        }
        IconButton(onClick = onBlock) {
            Icon(
                imageVector = if (user.isBlocked) Icons.Default.Lock      // bloqueado = cerrado
                else Icons.Default.LockOpen,                // activo = abierto
                contentDescription = null,
                tint = if (user.isBlocked) Rojo else Color(0xFF4CAF50)    // bloqueado = rojo, activo = verde
            )
        }

    }
}