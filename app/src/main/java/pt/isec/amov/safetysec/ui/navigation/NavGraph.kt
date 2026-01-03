package pt.isec.amov.safetysec.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pt.isec.amov.safetysec.auth.login.LoginScreen
import pt.isec.amov.safetysec.auth.login.LoginViewModel
import pt.isec.amov.safetysec.auth.register.RegisterScreen
import pt.isec.amov.safetysec.dashboard.RoleSelectionScreen
import pt.isec.amov.safetysec.dashboard.monitor.MonitorDashboardScreen
import pt.isec.amov.safetysec.dashboard.protected.ProtectedDashboardScreen
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.Login.route
    ) {
        composable(Routes.Login.route) {
            LoginScreen(
                navController = navController,
                viewModel = loginViewModel
            )
        }
        composable(Routes.Register.route) { RegisterScreen(navController) }
        composable(Routes.ProtectedDashboard.route) { ProtectedDashboardScreen() }
        composable(Routes.MonitorDashboard.route) { MonitorDashboardScreen() }
        composable(Routes.RoleSelection.route) {
            // Use the same shared ViewModel instance here
            val user by loginViewModel.currentUser.collectAsState()

            // This will now have the user data from the login screen
            user?.let {
                RoleSelectionScreen(navController, it)
            }
        }
    }
}

