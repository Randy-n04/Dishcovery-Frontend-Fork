package com.dishcovery.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dishcovery.app.domain.model.Faq
import com.dishcovery.app.domain.model.User
import com.dishcovery.app.domain.repository.FaqRepository
import com.dishcovery.app.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.map


@HiltViewModel
class AdminViewModel @Inject constructor(
    private val faqRepository: FaqRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    // --- FAQ ---
    private val _faq       = MutableStateFlow<Faq?>(null)
    val faq: StateFlow<Faq?> = _faq.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error     = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _editContent = MutableStateFlow("")
    val editContent: StateFlow<String> = _editContent.asStateFlow()

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess.asStateFlow()

    // --- Usuarios ---
    private val _users        = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _usersLoading = MutableStateFlow(false)
    val usersLoading: StateFlow<Boolean> = _usersLoading.asStateFlow()

    private val _usersError   = MutableStateFlow<String?>(null)
    val usersError: StateFlow<String?> = _usersError.asStateFlow()

    private val _currentPage  = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _totalPages   = MutableStateFlow(1)
    val totalPages: StateFlow<Int> = _totalPages.asStateFlow()

    init {
        loadFaq()
        loadUsers()
    }

    // --- FAQ ---
    fun loadFaq() {
        viewModelScope.launch {
            _isLoading.value = true
            faqRepository.getFaq()
                .onSuccess { faq ->
                    _faq.value         = faq
                    _editContent.value = faq.content
                    _error.value       = null
                }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun onContentChange(newText: String) { _editContent.value = newText }

    fun saveChanges() {
        viewModelScope.launch {
            _isLoading.value = true
            faqRepository.updateFaq(_editContent.value)
                .onSuccess { updated ->
                    _faq.value         = updated
                    _editContent.value = updated.content
                    _saveSuccess.value = true
                    _error.value       = null
                }
                .onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun resetSaveSuccess() { _saveSuccess.value = false }

    // --- Usuarios ---
    fun loadUsers(page: Int = 0) {
        viewModelScope.launch {
            _usersLoading.value = true
            userRepository.findAllPaginated(page, 10)
                .onSuccess { result ->
                    _users.value       = result.content
                    _currentPage.value = result.number
                    _totalPages.value  = result.totalPages
                    _usersError.value  = null
                }
                .onFailure { _usersError.value = it.message }
            _usersLoading.value = false
        }
    }

    fun blockUser(id: Long) {
        viewModelScope.launch {
            userRepository.blockUser(id)
                .onSuccess { updated ->
                    _users.value = _users.value.map {
                        if (it.id == updated.id) updated else it
                    }
                    _usersError.value = null
                }
                .onFailure { _usersError.value = it.message }
        }
    }

    fun nextPage() { if (_currentPage.value < _totalPages.value - 1) loadUsers(_currentPage.value + 1) }
    fun prevPage() { if (_currentPage.value > 0) loadUsers(_currentPage.value - 1) }
}