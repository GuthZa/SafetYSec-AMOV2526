package pt.isec.amov.safetysec.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pt.isec.amov.safetysec.data.model.User
import pt.isec.amov.safetysec.data.repository.UserRepository

class LoginViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userRepository = UserRepository()

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser


    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun login(onSuccess: (User) -> Unit) {
        val state = _uiState.value

        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.value = state.copy(
                errorMessage = "Email and password cannot be empty"
            )
            return
        }

        _uiState.value = state.copy(
            isLoading = true,
            errorMessage = null
        )

//        firebaseAuth
//            .signInWithEmailAndPassword(state.email, state.password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    onSuccess()
//                } else {
//                    _uiState.value = _uiState.value.copy(
//                        isLoading = false,
//                        errorMessage = task.exception?.localizedMessage
//                            ?: "Login failed"
//                    )
//                }
//            }
        firebaseAuth.signInWithEmailAndPassword(state.email, state.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Fetch profile
                    viewModelScope.launch {
                        try {
                            val user = userRepository.getCurrentUser()
                            if (user != null) {
                                _currentUser.value = user
                                _uiState.value = _uiState.value.copy(isLoading = false)
                                onSuccess(user)
                            } else {
                                _uiState.value = _uiState.value.copy(
                                    isLoading = false,
                                    errorMessage = "User profile not found"
                                )
                            }
                        } catch (e: Exception) {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                errorMessage = e.message ?: "Failed to fetch profile"
                            )
                        }
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = task.exception?.localizedMessage ?: "Login failed"
                    )
                }
            }
    }
}
