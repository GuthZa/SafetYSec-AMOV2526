package pt.isec.amov.safetysec.data.model

data class Rule(
    val id: String = "",
    val type: RuleType = RuleType.FALL,
    val monitorId: String = "",
    val protectedId: String = "",
    val parameters: Map<String, Any> = emptyMap(),
    val authorized: Boolean = false,
    val active: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

enum class RuleType {
    FALL,
    ACCIDENT,
    GEOFENCE,
    SPEED,
    INACTIVITY,
    PANIC
}
