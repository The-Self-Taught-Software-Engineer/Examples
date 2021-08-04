package codes.jakob.tstse.example.common

enum class AssignmentType(val sign: String) {
    FEATURE("F"),
    MAINTENANCE_SCHEDULED("MS"),
    MAINTENANCE_EXTRAORDINARY("ME"),
    UPGRADE("U"),
    BUGFIX("B"),
}
