package pt.isec.amov.safetysec.dashboard.monitor


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonitorDashboardScreen(
    viewModel: MonitorDashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val otpInput by remember { derivedStateOf { viewModel.otpInput } }
    val otpMessage by remember { derivedStateOf { viewModel.otpMessage } }

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Monitor Dashboard") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item { Text("Recent Alerts:", style = MaterialTheme.typography.titleMedium) }
            items(uiState.recentAlerts) { alert ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(alert, modifier = Modifier.padding(8.dp))
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { Text("Protected Individuals Status:", style = MaterialTheme.typography.titleMedium) }
            items(uiState.protectedStatus.entries.toList()) { entry ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text("${entry.key}: ${entry.value}", modifier = Modifier.padding(8.dp))
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { Text("Statistics:", style = MaterialTheme.typography.titleMedium) }
            items(uiState.statistics.entries.toList()) { entry ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text("${entry.key}: ${entry.value}", modifier = Modifier.padding(8.dp))
                }
            }

            // Associar um protegido com a pass
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Associate Protected User:", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = otpInput,
                    onValueChange = viewModel::onOtpChange,
                    label = { Text("Enter OTP") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = { viewModel.linkProtectedWithOtp() },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Associate Protected")
                }
                otpMessage?.let {
                    Text(it, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}
