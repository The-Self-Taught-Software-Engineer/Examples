@file:Suppress("RemoveExplicitTypeArguments", "unused", "MemberVisibilityCanBePrivate")

package codes.jakob.tstse.example.idiomatic.scopefunctions

import codes.jakob.tstse.example.common.CrudDatabase
import codes.jakob.tstse.example.common.Developer
import codes.jakob.tstse.example.common.DeveloperType
import codes.jakob.tstse.example.common.Query
import mu.KLogger
import mu.KotlinLogging

class DeveloperRepository(private val database: CrudDatabase) {
    fun findDevelopersByType(type: DeveloperType): Set<Developer> {
        return database.read<DeveloperType, List<Developer>>(
            query = Query.where(Developer::type).isEqualTo(type),
        ).toSet()
    }

    fun findRandomDeveloperByType(type: DeveloperType): Developer? {
//        val developers: Set<Developer> = findDevelopersByType(type)
//        val randomDeveloper: Developer? = developers.randomOrNull()
//
//        if (randomDeveloper != null) logger.info { "Randomly selected developer $randomDeveloper by their type '$type'" }
//        else logger.warn { "No developer with type '$type' could be randomly selected" }
//
//        return randomDeveloper

        val developers: Set<Developer> = findDevelopersByType(type)
        return developers.randomOrNull().also {
            if (it != null) logger.info { "Randomly selected developer $it by their type '$type'" }
            else logger.warn { "No developer with type '$type' could be randomly selected" }
        }
    }

    fun save(developer: Developer) {
        TODO("Not yet implemented")
    }

    companion object {
        private val logger: KLogger = KotlinLogging.logger {}
    }
}
