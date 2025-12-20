package pt.isec.amov.safetysec.data.model

data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val roles: List<UserRole> = emptyList(),
    val cancelAlertCode: String? = null,
    val monitors: List<String> = emptyList(),
    val protectedUsers: List<String> = emptyList()
)

enum class UserRole {
    MONITOR,
    PROTECTED
}
