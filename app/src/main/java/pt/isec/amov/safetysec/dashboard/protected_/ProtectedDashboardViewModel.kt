package pt.isec.amov.safetysec.dashboard.protected_

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pt.isec.amov.safetysec.data.model.Rule
import pt.isec.amov.safetysec.data.repository.AssociationRepository

data class ProtectedDashboardState(
    val recentAlerts: List<String> = emptyList(),
    val authorizedMonitors: List<String> = emptyList(),
    val rules: List<Rule> = emptyList()
)

class ProtectedDashboardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProtectedDashboardState())
    val uiState: StateFlow<ProtectedDashboardState> = _uiState

    private val associationRepository = AssociationRepository()

    var otp by mutableStateOf<String?>(null)
        private set

    var otpError by mutableStateOf<String?>(null)
        private set

    fun loadData() {
        loadAuthorizedMonitors()
        // alertas e regras entram depois
    }

    private fun loadAuthorizedMonitors() {
        viewModelScope.launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val monitors = associationRepository.getMonitors(uid)

            _uiState.value = _uiState.value.copy(
                authorizedMonitors = monitors
            )
        }
    }

    fun generateOtp() {
        viewModelScope.launch {
            try {
                otp = associationRepository.generateOtp()
                otpError = null
            } catch (e: Exception) {
                otpError = e.message
            }
        }
    }
}

