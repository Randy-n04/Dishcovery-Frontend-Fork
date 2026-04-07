package com.dishcovery.app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dishcovery.app.R
import com.dishcovery.app.domain.model.User
import com.dishcovery.app.domain.model.UserRole
import com.dishcovery.app.presentation.ui.components.ProfileAvatar
import com.dishcovery.app.presentation.ui.components.ProfileHeader
import com.dishcovery.app.presentation.ui.components.ProfileMenuSection
import com.dishcovery.app.presentation.ui.components.ProfilePublicData
import com.dishcovery.app.presentation.ui.theme.Beige
import com.dishcovery.app.presentation.ui.theme.Rojo
import com.dishcovery.app.presentation.ui.theme.TextPrimary

import com.dishcovery.app.presentation.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    paddingValues: PaddingValues,
    user: User?,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel
) {
    val profile   by viewModel.profile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error     by viewModel.error.collectAsState()
    val isAdmin = user?.role is UserRole.Admin

    LaunchedEffect(user?.id, isAdmin) {
        if (!isAdmin) {
            user?.id?.let { viewModel.loadProfile(it) }
        }
    }

    Column(Modifier.fillMaxSize().background(Beige).padding(paddingValues)) {
        ProfileHeader(onBack = onBack)

        when {
            isAdmin && user != null -> {
                Column(
                    Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProfileAvatar(
                        photoUrl = "",
                        publicName = user.username,
                        bio = "",
                        showBio = false
                    )
                    ProfileMenuSection(
                        title = stringResource(R.string.settings_section_title),
                        items = listOf(
                            Triple(stringResource(R.string.account_settings_item), { }, TextPrimary)
                        )
                    )
                    ProfileMenuSection(
                        title = stringResource(R.string.other_section_title),
                        items = listOf(
                            Triple(stringResource(R.string.faq_item), { }, TextPrimary),
                            Triple(stringResource(R.string.logout_item), onLogout, Rojo)
                        )
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
            isLoading -> {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator(color = Rojo)
                }
            }
            error != null -> {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text(error!!, color = Rojo, fontSize = 14.sp)
                }
            }
            profile == null -> {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text("No se encontró el perfil.", color = TextPrimary.copy(.5f), fontSize = 14.sp)
                }
            }
            else -> {
                Column(
                    Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProfileAvatar(
                        photoUrl   = profile!!.photoUrl,
                        publicName = profile!!.publicName,
                        bio        = profile!!.bio
                    )
                    ProfilePublicData(
                        offeredSkills = profile!!.offeredSkills,
                        language      = profile!!.language,
                        location      = profile!!.location
                    )
                    ProfileMenuSection(
                        title = stringResource(R.string.settings_section_title),
                        items = listOf(
                            Triple(stringResource(R.string.personal_data_item), { }, TextPrimary),
                            Triple(stringResource(R.string.account_settings_item), { }, TextPrimary)
                        )
                    )
                    ProfileMenuSection(
                        title = stringResource(R.string.other_section_title),
                        items = listOf(
                            Triple(stringResource(R.string.faq_item), { }, TextPrimary),
                            Triple(stringResource(R.string.logout_item),   onLogout, Rojo)
                        )
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}