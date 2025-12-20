package pt.isec.amov.safetysec.data.model

sealed class Rule {
    data class Fall(val enabled: Boolean) : Rule()
    data class Accident(val enabled: Boolean) : Rule()
    //data class GeoFence(val areas: List<GeoFenceArea>) : Rule()
    data class Speed(val maxSpeed: Int) : Rule()
    data class Inactivity(val minutes: Int) : Rule()
    object Panic : Rule()
}
