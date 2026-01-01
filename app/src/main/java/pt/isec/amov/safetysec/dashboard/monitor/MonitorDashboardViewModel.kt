package pt.isec.amov.safetysec.dashboard.monitor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    // Novo: OTP input
    var otpInput by mutableStateOf("")
        private set

    var otpMessage by mutableStateOf<String?>(null)
        private set

    fun loadData() {
        _uiState.value = MonitorDashboardState(
            recentAlerts = listOf("John: Fall detected", "Mary: Speed limit exceeded"),
            protectedStatus = mapOf("John" to "Safe", "Mary" to "Inactive"),
            statistics = mapOf("Total Alerts" to "10", "Avg Response Time" to "15s")
        )
    }

    // Atualiza valor da one time pass
    fun onOtpChange(value: String) {
        otpInput = value
    }

    // Associa a um protegido
    fun linkProtectedWithOtp() {
        viewModelScope.launch {
            try {
                associationRepository.linkWithOtp(otpInput)
                otpMessage = "Association successful"
            } catch (e: Exception) {
                otpMessage = e.message
            }
        }
    }
}
