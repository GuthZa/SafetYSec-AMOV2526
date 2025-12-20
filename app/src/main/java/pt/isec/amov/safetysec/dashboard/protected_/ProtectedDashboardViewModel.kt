package pt.isec.amov.safetysec.dashboard.protected_


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ProtectedDashboardState(
    val recentAlerts: List<String> = emptyList(),
    val authorizedMonitors: List<String> = emptyList(),
    val activeRules: List<String> = emptyList()
)

class ProtectedDashboardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProtectedDashboardState())
    val uiState: StateFlow<ProtectedDashboardState> = _uiState

    // TODO: Connect to Firebase to fetch alerts, rules, monitors
    fun loadData() {
        // Example placeholder
        _uiState.value = ProtectedDashboardState(
            recentAlerts = listOf("Fall detected - 12:03", "Speed limit exceeded - 09:30"),
            authorizedMonitors = listOf("Alice", "Bob"),
            activeRules = listOf("Fall", "Geofencing")
        )
    }
}
