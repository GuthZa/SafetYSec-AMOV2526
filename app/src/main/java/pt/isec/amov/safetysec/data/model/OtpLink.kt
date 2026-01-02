package pt.isec.amov.safetysec.data.model

data class OtpLink( //apenas um helper para guardar
    val protectedId: String = "",
    val expiresAt: Long = 0L
)