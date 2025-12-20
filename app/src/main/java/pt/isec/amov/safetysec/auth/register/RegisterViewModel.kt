package pt.isec.amov.safetysec.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pt.isec.amov.safetysec.data.repository.UserProfile
import pt.isec.amov.safetysec.data.repository.UserRepository

class RegisterViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userRepository = UserRepository()

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun onMonitorChecked(checked: Boolean) {
        _uiState.value = _uiState.value.copy(isMonitor = checked)
    }

    fun onProtectedChecked(checked: Boolean) {
        _uiState.value = _uiState.value.copy(isProtected = checked)
    }

    fun register(onSuccess: () -> Unit) {
        val state = _uiState.value

        if (
            state.name.isBlank() ||
            state.email.isBlank() ||
            state.password.length < 6
        ) {
            _uiState.value = state.copy(
                errorMessage = "Please fill all fields (password ≥ 6 characters)"
            )
            return
        }

        if (!state.isMonitor && !state.isProtected) {
            _uiState.value = state.copy(
                errorMessage = "Select at least one role"
            )
            return
        }

        _uiState.value = state.copy(isLoading = true, errorMessage = null)

//        firebaseAuth
//            .createUserWithEmailAndPassword(state.email, state.password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    // Firestore profile creation comes next
//                    onSuccess()
//                } else {
//                    _uiState.value = _uiState.value.copy(
//                        isLoading = false,
//                        errorMessage = task.exception?.localizedMessage
//                            ?: "Registration failed"
//                    )
//                }
//            }
        firebaseAuth.createUserWithEmailAndPassword(state.email, state.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Firestore profile creation
                    viewModelScope.launch {
                        try {
                            val profile = UserProfile(
                                name = state.name,
                                roles = buildList {
                                    if (state.isMonitor) add("Monitor")
                                    if (state.isProtected) add("Protected")
                                },
                                authorizedMonitors = emptyList(),
                                activeRules = emptyList(),
                                alertCancelCode = "0000"
                            )

                            userRepository.createUserProfile(profile)
                            onSuccess()

                        } catch (e: Exception) {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                errorMessage = e.message ?: "Failed to create profile"
                            )
                        }
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = task.exception?.localizedMessage ?: "Registration failed"
                    )
                }
            }
    }
}
