package pt.isec.amov.safetysec.dashboard.monitor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pt.isec.amov.safetysec.data.repository.AssociationRepository

data class MonitorDashboardState(
    val recentAlerts: List<String> = emptyList(),
    val protectedStatus: Map<String, String> = emptyMap(),
    val statistics: Map<String, String> = emptyMap()
)

class MonitorDashboardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MonitorDashboardState())
    val uiState: StateFlow<MonitorDashboardState> = _uiState

    private val associationRepository = AssociationRepository()

    var otpInput by mutableStateOf("")
        private set

    var otpSuccess by mutableStateOf<String?>(null)
        private set

    var otpError by mutableStateOf<String?>(null)
        private set

    fun loadData() {
        loadProtectedUsers()
        // alertas e estatísticas entram depois
    }

    private fun loadProtectedUsers() {
        viewModelScope.launch {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val protectedIds = associationRepository.getProtectedUsers(uid)

            _uiState.value = _uiState.value.copy(
                protectedStatus = protectedIds.associateWith { "Unknown" }
            )
        }
    }

    fun onOtpChange(value: String) {
        otpInput = value
    }

    fun linkProtectedWithOtp() {
        if (otpInput.isBlank()) {
            otpError = "OTP required"
            return
        }

        viewModelScope.launch {
            try {
                associationRepository.linkWithOtp(otpInput)
                otpSuccess = "Association successful"
                otpError = null
                otpInput = ""
                loadProtectedUsers()
            } catch (e: Exception) {
                otpError = e.message
                otpSuccess = null
            }
        }
    }
}

