package com.dishcovery.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dishcovery.app.domain.error.DomainError
import com.dishcovery.app.domain.model.KitchenCategory
import com.dishcovery.app.domain.model.Profile
import com.dishcovery.app.domain.repository.ProfileRepository
import com.dishcovery.app.domain.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val recipeRepository: RecipeRepository,
    private val globalErrorViewModel: GlobalErrorViewModel
) : ViewModel() {

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile.asStateFlow()

    private val _kitchenCategories = MutableStateFlow<List<KitchenCategory>>(emptyList())
    val kitchenCategories: StateFlow<List<KitchenCategory>> = _kitchenCategories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadProfile(userId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getProfileByUserId(userId)
                .onSuccess { profile -> _profile.value = profile }
                .onFailure { e ->
                    _error.value = DomainError.fromThrowable(e).message
                    globalErrorViewModel.showError(e)
                }

            recipeRepository.getKitchenCategories()
                .onSuccess { categories -> _kitchenCategories.value = categories }
                .onFailure { e -> globalErrorViewModel.showError(e) }

            _isLoading.value = false
        }
    }

    fun updateProfile(
        publicName: String,
        bio: String,
        photoUrl: String,
        offeredSkills: List<KitchenCategory>,
        skillsToLearn: List<KitchenCategory>,
        language: String,
        location: String,
        onSuccess: () -> Unit = {}
    ) {
        val currentProfile = _profile.value ?: return
        viewModelScope.launch {
            _isSaving.value = true
            _error.value = null

            repository.updateProfile(
                profileId = currentProfile.id,
                publicName = publicName,
                bio = bio,
                photoUrl = photoUrl,
                offeredSkills = offeredSkills,
                skillsToLearn = skillsToLearn,
                language = language,
                location = location
            ).onSuccess { updated ->
                _profile.value = updated
                onSuccess()
            }.onFailure { e ->
                _error.value = DomainError.fromThrowable(e).message
                globalErrorViewModel.showError(e)
            }
            _isSaving.value = false
        }
    }
}
