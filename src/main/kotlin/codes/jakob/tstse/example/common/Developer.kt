package codes.jakob.tstse.example.common

import java.util.*

data class Developer(
    val id: UUID,
    val name: Name,
    val type: DeveloperType,
    val emailAddress: String,
    var assigned: Boolean,
)
