package pt.isec.amov.safetysec.dashboard.protected_


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProtectedDashboardScreen(
    viewModel: ProtectedDashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val otp by remember { derivedStateOf { viewModel.otp } }
    val otpError by remember { derivedStateOf { viewModel.otpError } }

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Protected Dashboard") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Text("Recent Alerts:", style = MaterialTheme.typography.titleMedium)
            }
            items(uiState.recentAlerts) { alert ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(alert, modifier = Modifier.padding(8.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Authorized Monitors:", style = MaterialTheme.typography.titleMedium)
            }
            items(uiState.authorizedMonitors) { monitor ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(monitor, modifier = Modifier.padding(8.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Active Rules:", style = MaterialTheme.typography.titleMedium)
            }
            items(uiState.activeRules) { rule ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(rule, modifier = Modifier.padding(8.dp))
                }
            }

            // Gerar a one time pass
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Generate Association Code:", style = MaterialTheme.typography.titleMedium)
                Button(
                    onClick = { viewModel.generateOtp() },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Generate OTP")
                }
                otp?.let {
                    Text("Your code: $it", modifier = Modifier.padding(top = 8.dp))
                }
                otpError?.let {
                    Text("Error: $it", color = Color.Red, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}
