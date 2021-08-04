package codes.jakob.tstse.example.common

data class Assignment(
    val name: String,
    val developers: Set<Developer>,
    val type: AssignmentType,
    val briefing: String,
) {
    fun generateNotification(): Notification {
        TODO("Not yet implemented")
    }
}
