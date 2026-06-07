package com.dishcovery.app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dishcovery.app.R
import com.dishcovery.app.domain.model.User
import com.dishcovery.app.domain.model.UserRole
import com.dishcovery.app.presentation.ui.components.FaqAdminDropdown
import com.dishcovery.app.presentation.ui.components.EditProfileDialog
import com.dishcovery.app.presentation.ui.components.ProfileAvatar
import com.dishcovery.app.presentation.ui.components.ProfileHeader
import com.dishcovery.app.presentation.ui.components.ProfileMenuSection
import com.dishcovery.app.presentation.ui.components.ProfilePublicData
import com.dishcovery.app.presentation.ui.theme.Beige
import com.dishcovery.app.presentation.ui.theme.Rojo
import com.dishcovery.app.presentation.ui.theme.TextPrimary
import com.dishcovery.app.presentation.viewmodel.AdminViewModel

import com.dishcovery.app.presentation.viewmodel.ProfileViewModel
import com.dishcovery.app.presentation.ui.components.EditAccountDialog
import com.dishcovery.app.presentation.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    paddingValues: PaddingValues,
    user: User?,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel,
    authViewModel: AuthViewModel
) {
    val profile by viewModel.profile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val error by viewModel.error.collectAsState()
    val kitchenCategories by viewModel.kitchenCategories.collectAsState()
    val isUpdatingAccount by authViewModel.isUpdatingAccount.collectAsState()
    var showAccountDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    val isAdmin = user?.role is UserRole.Admin

    LaunchedEffect(user?.id, isAdmin) {
        if (!isAdmin) {
            user?.id?.let { viewModel.loadProfile(it) }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Beige)
            .padding(paddingValues)
    ) {
        ProfileHeader(onBack = onBack)

        when {
            isAdmin -> {
                val faqViewModel: AdminViewModel = hiltViewModel()
                val faq         by faqViewModel.faq.collectAsState()
                val editContent by faqViewModel.editContent.collectAsState()
                val faqLoading  by faqViewModel.isLoading.collectAsState()
                val faqError    by faqViewModel.error.collectAsState()
                val saveSuccess by faqViewModel.saveSuccess.collectAsState()

                // Controla si el dropdown está abierto
                var faqExpanded by remember { mutableStateOf(false) }

                val adminDisplayName = if (user?.username.equals("admin", ignoreCase = true)) {
                    stringResource(R.string.admin_display_name)
                } else {
                    user?.username.orEmpty()
                }

                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProfileAvatar(
                        photoUrl   = "",
                        publicName = adminDisplayName,
                        bio        = "",
                        showBio    = false
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
                            Triple(
                                stringResource(R.string.faq_item_admin),
                                { faqExpanded = !faqExpanded },   // toggle dropdown
                                TextPrimary
                            ),
                            Triple(stringResource(R.string.logout_item), onLogout, Rojo)
                        )
                    )

                    // Dropdown FAQ — solo visible cuando faqExpanded = true
                    if (faqExpanded) {
                        FaqAdminDropdown(
                            faq             = faq,
                            editContent     = editContent,
                            isLoading       = faqLoading,
                            error           = faqError,
                            saveSuccess     = saveSuccess,
                            onContentChange = faqViewModel::onContentChange,
                            onSave          = faqViewModel::saveChanges,
                            onDismissSaveSuccess = faqViewModel::resetSaveSuccess
                        )
                    }

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
                    Text(
                        "No se encontró el perfil.",
                        color = TextPrimary.copy(.5f),
                        fontSize = 14.sp
                    )
                }
            }

            else -> {
                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProfileAvatar(
                        photoUrl = profile!!.photoUrl,
                        publicName = profile!!.publicName,
                        bio = profile!!.bio
                    )

                    ProfilePublicData(
                        offeredSkills = profile!!.offeredSkills,
                        language = profile!!.language,
                        location = profile!!.location
                    )

                    ProfileMenuSection(
                        title = stringResource(R.string.settings_section_title),
                        items = listOf(
                            Triple(
                                stringResource(R.string.personal_data_item),
                                { showEditDialog = true },
                                TextPrimary
                            ),
                            Triple(
                                stringResource(R.string.account_settings_item),
                                { showAccountDialog = true },
                                TextPrimary
                            )
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
        }
    }

    if (showEditDialog && profile != null) {
        EditProfileDialog(
            profile = profile!!,
            categories = kitchenCategories,
            isSaving = isSaving,
            onDismiss = { showEditDialog = false },
            onSave = { publicName, bio, photoUrl, offeredSkills, skillsToLearn, language, location ->
                viewModel.updateProfile(
                    publicName = publicName,
                    bio = bio,
                    photoUrl = photoUrl,
                    offeredSkills = offeredSkills,
                    skillsToLearn = skillsToLearn,
                    language = language,
                    location = location
                ) {
                    showEditDialog = false
                }
            }
        )
    }
    if (showAccountDialog && user != null) {
        EditAccountDialog(
            currentUsername = user.username,
            isSaving = isUpdatingAccount,
            onDismiss = { showAccountDialog = false },
            onSave = { username, currentPassword, newPassword ->
                authViewModel.updateAccount(
                    username = username,
                    currentPassword = currentPassword,
                    newPassword = newPassword
                ) {
                    showAccountDialog = false
                }
            }
        )
    }


}