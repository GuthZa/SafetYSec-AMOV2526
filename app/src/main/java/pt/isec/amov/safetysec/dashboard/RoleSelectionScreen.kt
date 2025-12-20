package pt.isec.amov.safetysec.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.isec.amov.safetysec.ui.navigation.Routes

@Composable
fun RoleSelectionScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Choose a role to continue", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { navController.navigate(Routes.ProtectedDashboard.route) }) {
            Text("Protected")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate(Routes.MonitorDashboard.route) }) {
            Text("Monitor")
        }
    }
}
