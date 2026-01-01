package pt.isec.amov.safetysec.dashboard.protected_

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pt.isec.amov.safetysec.data.repository.AssociationRepository

data class ProtectedDashboardState(
    val recentAlerts: List<String> = emptyList(),
    val authorizedMonitors: List<String> = emptyList(),
    val activeRules: List<String> = emptyList(),
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
        // TODO: Connect to Firebase to fetch alerts, rules, monitors
        _uiState.value = ProtectedDashboardState(
            recentAlerts = listOf("Fall detected - 12:03", "Speed limit exceeded - 09:30"),
            authorizedMonitors = listOf("Alice", "Bob"),
            activeRules = listOf("Fall", "Geofencing")
        )
    }

    // Novo: gerar a One time pass para associação
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
