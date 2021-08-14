package codes.jakob.tstse.example.common

data class Name(
    val givenNames: List<String>,
    val familyName: String,
) {
    constructor(fullName: String) : this(
        givenNames = fullName.substringBeforeLast(' ').split(' '),
        familyName = fullName.substringAfterLast(' '),
    )

    override fun toString(): String {
        return "${givenNames.joinToString { " " }} $familyName"
    }
}
