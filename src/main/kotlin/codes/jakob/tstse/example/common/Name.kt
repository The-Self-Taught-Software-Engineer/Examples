package codes.jakob.tstse.example.common

data class Name(
    val givenNames: List<String>,
    val familyName: String,
) : Comparable<Name> {
    constructor(fullName: String) : this(
        givenNames = fullName.substringBeforeLast(' ').split(' '),
        familyName = fullName.substringAfterLast(' '),
    )

    override fun compareTo(other: Name): Int {
        val familyNameComparison: Int = this.familyName.compareTo(other.familyName)
        return if (familyNameComparison != 0) {
            familyNameComparison
        } else {
            this.givenNames.zip(other.givenNames)
                .map { it.first.compareTo(it.second) }
                .firstOrNull { it != 0 }
                ?: 0
        }
    }

    override fun toString(): String {
        return "${givenNames.joinToString(" ")} $familyName"
    }
}
