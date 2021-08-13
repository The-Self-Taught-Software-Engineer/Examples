@file:Suppress("unused")

package codes.jakob.tstse.example.idiomatic.scopefunctions

import codes.jakob.tstse.example.common.Assignment
import codes.jakob.tstse.example.common.AssignmentType
import codes.jakob.tstse.example.common.DeveloperType
import codes.jakob.tstse.example.common.Notification

class AssignmentService1(private val developerRepository: DeveloperRepository) {
    fun createNotificationForMaintainenance(
        developerType: DeveloperType,
        assignmentType: AssignmentType
    ): Notification {
//        val developer: Developer = developerRepository.findRandomDeveloperByType(developerType)
//            ?: error("No developer with type '$developerType' exists to assign for maintenance")
//        developer.assigned = true
//        developerRepository.save(developer)
//        return Assignment(setOf(developer), assignmentType, MAINTAINENANCE_DEFAULT_BRIEFING).generateNotification()

        return developerRepository.findRandomDeveloperByType(developerType)
            ?.run {
                assignment = Assignment(
                    name = generateName(assignmentType),
                    developers = setOf(this),
                    type = assignmentType,
                    briefing = MAINTAINENANCE_DEFAULT_BRIEFING,
                )
                assignment!!.generateNotification().also {
                    developerRepository.save(this)
                }
            }
            ?: error("No developer with type '$developerType' exists to assign for maintenance")
    }

    private fun generateName(assignmentType: AssignmentType): String {
        TODO("Not yet implemented")
    }

    companion object {
        private const val MAINTAINENANCE_DEFAULT_BRIEFING = "!!! Prepare for maintainenance with manager !!! "

        /**
         * Evaluate this regular expression yourself [over here](https://regex101.com/r/d5pnfN/1).
         */
        private val ASSIGNMENT_NAME_REGEX: Regex = run {
            val prefix = "ASS"
            val typeSigns = AssignmentType.values().joinToString("|") { it.sign }
            val digits = "\\d+"

            Regex("$prefix-($typeSigns)-$digits")
        }
    }
}
