package codes.jakob.tstse.example.common

interface AssignmentRepository {
    fun findByDeveloper(developer: Developer): Set<Assignment>
}
