package codes.jakob.tstse.example.common

import java.util.*

data class Developer(
    val id: UUID,
    val name: Name,
    val type: DeveloperType,
    val emailAddress: String,
    var assignment: Assignment?,
) {
    val assigned: Boolean
        get() {
            return assignment != null
        }

    override fun toString(): String {
        return "Developer(id=$id, name=$name, type=$type, emailAddress='$emailAddress', assigned=$assigned)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Developer

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
