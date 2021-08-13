@file:Suppress("unused")

package codes.jakob.tstse.example.idiomatic.scopefunctions

import codes.jakob.tstse.example.common.Assignment
import codes.jakob.tstse.example.common.AssignmentType
import codes.jakob.tstse.example.common.DeveloperType
import codes.jakob.tstse.example.common.Notification

class AssignmentService2(private val developerRepository: DeveloperRepository) {
    fun createNotificationForMaintainenance(
        developerType: DeveloperType,
        assignmentType: AssignmentType
    ): Notification {
        return developerRepository.findRandomDeveloperByType(developerType)
            ?.let { developer ->
                developer.assignment = Assignment(
                    name = generateName(assignmentType),
                    developers = setOf(developer),
                    type = assignmentType,
                    briefing = MAINTAINENANCE_DEFAULT_BRIEFING,
                )
                developer.assignment!!.generateNotification().also {
                    developerRepository.save(developer)
                }
            }
            ?: error("No developer with type '$developerType' exists to assign for maintenance")
    }

    private fun generateName(assignmentType: AssignmentType): String {
        TODO("Not yet implemented")
    }

    companion object {
        private const val MAINTAINENANCE_DEFAULT_BRIEFING = "!!! Prepare for maintainenance with manager !!!"
    }
}
