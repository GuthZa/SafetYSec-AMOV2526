package pt.isec.amov.safetysec.auth.register


data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isMonitor: Boolean = false,
    val isProtected: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
