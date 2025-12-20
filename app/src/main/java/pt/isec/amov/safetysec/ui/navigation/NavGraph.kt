package pt.isec.amov.safetysec.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pt.isec.amov.safetysec.auth.login.LoginScreen
import pt.isec.amov.safetysec.auth.register.RegisterScreen
import pt.isec.amov.safetysec.dashboard.monitor.MonitorDashboardScreen
import pt.isec.amov.safetysec.dashboard.protected_.ProtectedDashboardScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Login.route
    ) {
        composable(Routes.Login.route) {
            LoginScreen(
                navController = navController,
//                onLoginClick = { email, password ->
//                    // Call LoginViewModel.login()
//                },
//                onRegisterClick = {
//                    navController.navigate(Routes.Register.route)
//                }
            )
        }
        composable(Routes.Register.route) { RegisterScreen(navController) }
        composable(Routes.ProtectedDashboard.route) { ProtectedDashboardScreen() }
        composable(Routes.MonitorDashboard.route) { MonitorDashboardScreen() }
    }
}
