package pt.isec.amov.safetysec.data.model

enum class AssociationStatus {
    PENDING, ACTIVE, REVOKED
}
data class Association(
    val monitorId: String = "",
    val protectedId: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val status: String = AssociationStatus.ACTIVE.name

)