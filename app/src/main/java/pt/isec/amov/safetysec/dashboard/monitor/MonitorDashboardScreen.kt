package pt.isec.amov.safetysec.dashboard.monitor


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.isec.amov.safetysec.dashboard.monitor.rules.MonitorRulesViewModel
import pt.isec.amov.safetysec.data.model.RuleType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonitorDashboardScreen(
    viewModel: MonitorDashboardViewModel = viewModel(),
    monitorRulesViewModel: MonitorRulesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val otpInput by remember { derivedStateOf { viewModel.otpInput } }

    // Regras por Protegido
    val rulesByProtected by monitorRulesViewModel.rulesByProtected.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadData()
        // Carregar regras para cada Protegido
        uiState.protectedStatus.keys.forEach { monitorRulesViewModel.loadRulesForProtegido(it) }
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

            // Protected Individuals + Rules
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { Text("Protected Individuals Status:", style = MaterialTheme.typography.titleMedium) }
            items(uiState.protectedStatus.entries.toList()) { entry ->
                val protectedId = entry.key
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text("${entry.key}: ${entry.value}", modifier = Modifier.padding(8.dp))

                    Spacer(modifier = Modifier.height(8.dp))
                    // Listar regras existentes
                    val protectedRules = rulesByProtected[protectedId] ?: emptyList()
                    protectedRules.forEach { rule ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(rule.type.name)
                            Text(if (rule.authorized) "Authorized" else "Pending")
                        }
                    }

                    // Botões de criação de regras
                    Button(
                        onClick = { monitorRulesViewModel.createRuleForProtegido(protectedId, RuleType.FALL) },
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text("Add Fall Rule")
                    }
                    Button(
                        onClick = { monitorRulesViewModel.createRuleForProtegido(protectedId, RuleType.ACCIDENT) },
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text("Add Accident Rule")
                    }
                    Button(
                        onClick = { monitorRulesViewModel.createRuleForProtegido(protectedId, RuleType.GEOFENCE) },
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text("Add Geofence Rule")
                    }
                    Button(
                        onClick = { monitorRulesViewModel.createRuleForProtegido(protectedId, RuleType.SPEED) },
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text("Add Speed Rule")
                    }
                    Button(
                        onClick = { monitorRulesViewModel.createRuleForProtegido(protectedId, RuleType.INACTIVITY) },
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text("Add Inactivity Rule")
                    }
                    Button(
                        onClick = { monitorRulesViewModel.createRuleForProtegido(protectedId, RuleType.PANIC) },
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text("Add Panic Rule")
                    }
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
                Text("Associate Protected User", style = MaterialTheme.typography.titleMedium)

                OutlinedTextField(
                    value = otpInput,
                    onValueChange = viewModel::onOtpChange,
                    label = { Text("Enter OTP") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Button(
                    onClick = { viewModel.linkProtectedWithOtp() },
                    modifier = Modifier.padding(top = 8.dp),
                    enabled = otpInput.isNotBlank()
                ) {
                    Text("Associate")
                }

                viewModel.otpSuccess?.let {
                    Text(it, color = Color(0xFF2E7D32), modifier = Modifier.padding(top = 4.dp))
                }

                viewModel.otpError?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 4.dp))
                }
            }

        }
    }
}
