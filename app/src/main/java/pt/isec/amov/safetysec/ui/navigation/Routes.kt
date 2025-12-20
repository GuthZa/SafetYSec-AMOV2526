package pt.isec.amov.safetysec.ui.navigation

sealed class Routes(val route: String) {
    object Login : Routes("login")
    object Register : Routes("register")
    object ProtectedDashboard : Routes("protected_dashboard")
    object MonitorDashboard : Routes("monitor_dashboard")
    object Rules : Routes("rules")
    object Alerts : Routes("alerts")
    object Profile : Routes("profile")
}
