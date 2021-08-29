@file:Suppress("unused")

package codes.jakob.tstse.example.bytesized

import codes.jakob.tstse.example.common.Assignment
import codes.jakob.tstse.example.common.AssignmentRepository

class AssignmentService(private val assignmentRepository: AssignmentRepository) {
    fun estimateEpic(assignments: Set<Assignment>): Double {
        var averageHours = 0.0

        assignments@ for (assignment in assignments) {
            for (developer in assignment.developers) {
                val developerAssignments: Set<Assignment> = assignmentRepository.findByDeveloper(developer)

                averageHours = (averageHours + developerAssignments.map { it.hoursBooked }.average()) / 2

                if (averageHours > MAX_EPIC_AVERAGE_HOURS) {
                    break@assignments
                }
            }
        }

        return averageHours
    }

    companion object {
        private const val MAX_EPIC_AVERAGE_HOURS = 100.0
    }
}
