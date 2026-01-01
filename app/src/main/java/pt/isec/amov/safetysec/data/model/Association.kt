package pt.isec.amov.safetysec.data.model

data class Association(
    val monitorId: String = "",
    val protectedId: String = "",
    val createdAt: Long = System.currentTimeMillis()
)