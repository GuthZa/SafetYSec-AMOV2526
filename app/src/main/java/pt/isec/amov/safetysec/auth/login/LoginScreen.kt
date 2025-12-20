package pt.isec.amov.safetysec.auth.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import pt.isec.amov.safetysec.ui.navigation.Routes

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "SafetYSec",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Filled.VisibilityOff
                            else
                                Icons.Filled.Visibility,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
//                onClick = {
//                    viewModel.login {
//                        navController.navigate("dashboard") {
//                            popUpTo("login") { inclusive = true }
//                        }
//                    }
//                },
                onClick = {
                    viewModel.login { profile ->
                        // Role-based routing
                        when {
                            profile.roles.contains("Protected") && profile.roles.contains("Monitor") -> {
                                // TODO: route to role selection screen if both
                                navController.navigate("roleSelection") {
                                    popUpTo(Routes.Login.route) { inclusive = true }
                                }
                            }
                            profile.roles.contains("Protected") -> {
                                navController.navigate(Routes.ProtectedDashboard.route) {
                                    popUpTo(Routes.Login.route) { inclusive = true }
                                }
                            }
                            profile.roles.contains("Monitor") -> {
                                navController.navigate(Routes.MonitorDashboard.route) {
                                    popUpTo(Routes.Login.route) { inclusive = true }
                                }
                            }
                            else -> {
                                // Default fallback
                                navController.navigate(Routes.Login.route) {
                                    popUpTo(Routes.Login.route) { inclusive = true }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Login")
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Don't have an account?")
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(
                    onClick = {
                        navController.navigate(Routes.Register.route)
                    }
                ) {
                    Text("Register")
                }
            }

        }
    }
}
