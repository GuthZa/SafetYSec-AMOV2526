package pt.isec.amov.safetysec.data.model

data class OtpLink( //apenas um helper para guardar
    val code: String = "",
    val protectedId: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val expiresAt: Long = 0L,
    val used: Boolean = false
)