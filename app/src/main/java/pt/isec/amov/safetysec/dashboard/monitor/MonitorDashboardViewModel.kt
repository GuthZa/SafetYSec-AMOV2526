package pt.isec.amov.safetysec.dashboard.monitor


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class MonitorDashboardState(
    val recentAlerts: List<String> = emptyList(),
    val protectedStatus: Map<String, String> = emptyMap(),
    val statistics: Map<String, String> = emptyMap()
)

class MonitorDashboardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MonitorDashboardState())
    val uiState: StateFlow<MonitorDashboardState> = _uiState

    fun loadData() {
        _uiState.value = MonitorDashboardState(
            recentAlerts = listOf("John: Fall detected", "Mary: Speed limit exceeded"),
            protectedStatus = mapOf("John" to "Safe", "Mary" to "Inactive"),
            statistics = mapOf("Total Alerts" to "10", "Avg Response Time" to "15s")
        )
    }
}
