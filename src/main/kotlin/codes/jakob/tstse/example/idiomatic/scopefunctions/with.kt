@file:Suppress("unused")

package codes.jakob.tstse.example.idiomatic.scopefunctions

import codes.jakob.tstse.example.common.Developer

class DeveloperFormatter {
    fun formatGreetingLine(developer: Developer): String {
//        val greeting = "Dear ${developer.name.givenNames.joinToString(" ")} ${developer.name.familyName},"
//        if (!greeting.matches(GREETING_REGEX)) {
//            error("Greeting generated for $developer does not conform to the rules: '$greeting'")
//        }
//        return greeting

        return with(developer) {
            "Dear ${name.givenNames.joinToString(" ")} ${name.familyName},".also {
                if (!it.matches(GREETING_REGEX)) {
                    error("Greeting generated for $developer does not conform to the rules: '$it'")
                }
            }
        }
    }

    companion object {
        private val GREETING_REGEX = Regex("[\\w+\\s*]+,")
    }
}
