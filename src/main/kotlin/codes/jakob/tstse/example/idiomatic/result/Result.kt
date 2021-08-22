@file:Suppress("unused")

package codes.jakob.tstse.example.idiomatic.result

import codes.jakob.tstse.example.common.*
import codes.jakob.tstse.example.idiomatic.scopefunctions.DeveloperRepository
import mu.KLogger
import mu.KotlinLogging
import java.time.Clock
import java.time.LocalDate
import java.time.Month
import java.time.Year

class PaycheckService(
    private val humanResourcesClient: HumanResourcesClient,
    private val paycheckRepository: PaycheckRepository,
    private val developerRepository: DeveloperRepository,
    private val clock: Clock,
) {
    /**
     * Returns an ordered list of [Paycheck]; sorted lexicographically by the [Name] of the [Developer].
     */
    fun generatePaychecks(developerType: DeveloperType, year: Year, month: Month): List<Paycheck> {
        val today: LocalDate = LocalDate.now(clock)
        val paycheckPeriod: LocalDateRange = determinePeriod(year, month)

        return developerRepository.findDevelopersByType(developerType)
            .filterNot { paycheckRepository.exists(it, paycheckPeriod) }
            .mapNotNull { generateAndPersistPaycheck(today, paycheckPeriod, it) }
            .sortedBy { it.developer.name }
            .also { logger.info { "Generated and persisted paychecks for ${it.count()} developers" } }

//        val (successfulPaychecks: List<Paycheck>, failedPaychecks: List<Developer>) =
//            developerRepository.findDevelopersByType(developerType)
//                .asSequence()
//                .filterNot { paycheckRepository.exists(it, paycheckPeriod) }
//                .map { dev ->
//                    dev to Paycheck.Builder().apply {
//                        date = today
//                        period = paycheckPeriod
//                        developer = dev
//                    }
//                }
//                .map { (dev, paycheckBuilder) ->
//                    try {
//                        dev to paycheckBuilder.apply {
        //                        hourlyRate = humanResourcesClient.getHourlyRate(dev)
//                        }
//                    } catch (exception: HumanResourcesClient.HumanResourcesException) {
//                        logger.error(exception) {
//                            "Generation of ${Paycheck::class.simpleName} for '$dev' in period '$paycheckPeriod' failed"
//                        }
//                        dev to null
//                    }
//                }.map { (dev, paycheckBuilder) ->
//                    try {
//                        dev to paycheckBuilder?.apply {
//                            hourlyRate = humanResourcesClient.getHoursWorked(dev, paycheckPeriod)
//                        }
//                    } catch (exception: HumanResourcesClient.HumanResourcesException) {
//                        logger.error(exception) {
//                            "Generation of ${Paycheck::class.simpleName} for '$dev' in period '$paycheckPeriod' failed"
//                        }
//                        dev to null
//                    }
//                }
//                .map { (dev, paycheckBuilder) ->
//                    try {
//                        dev to paycheckBuilder?.build()
//                    } catch (exception: IllegalStateException) {
//                        logger.error(exception) {
//                            "Generation of ${Paycheck::class.simpleName} for '$dev' in period '$paycheckPeriod' failed"
//                        }
//                        dev to null
//                    }
//                }
//                .partition { (dev, paycheckBuilder) ->
//                    paycheckBuilder != null
//                }
//                .let { (success, failure) ->
//                    success.map { it.second!! } to failure.map { it.first }
//                }
//
//        successfulPaychecks.forEach { paycheckRepository.save(it) }
//        failedPaychecks.forEach { paycheckRepository.saveFailed(it, paycheckPeriod) }
//
//        return successfulPaychecks
//            .sortedBy { it.developer.name }
//            .also { logger.info { "Generated and persisted paychecks for ${it.count()} developers" } }
    }

    private fun generateAndPersistPaycheck(
        today: LocalDate,
        paycheckPeriod: LocalDateRange,
        developer: Developer
    ): Paycheck? {
        val paycheckBuilder = Paycheck.Builder().apply {
            this.date = today
            this.period = paycheckPeriod
            this.developer = developer
        }
        return Result.success(paycheckBuilder)
            .mapCatching { paycheck ->
                paycheck.apply {
                    hourlyRate = humanResourcesClient.getHourlyRate(paycheck.developer!!)
                }
            }
            .mapCatching { paycheck ->
                paycheck.apply {
                    hoursWorked = humanResourcesClient.getHoursWorked(paycheck.developer!!, paycheckPeriod)
                }
            }
            .mapCatching { paycheck ->
                paycheck.build()
            }
            .onSuccess { paycheck ->
                logger.info {
                    "Generation of ${Paycheck::class.simpleName} for '$developer' in period '$paycheckPeriod' succeeded"
                }
                paycheckRepository.save(paycheck)
            }
            .onFailure { exception ->
                logger.error(exception) {
                    "Generation of ${Paycheck::class.simpleName} for '$developer' in period '$paycheckPeriod' failed"
                }
                paycheckRepository.saveFailed(developer, paycheckPeriod)
            }
            .getOrNull()
    }

    private fun determinePeriod(year: Year, month: Month): LocalDateRange {
        val firstDayInPeriod: LocalDate = LocalDate.of(year.value, month, 1)
        val lastDayInPeriod: LocalDate = firstDayInPeriod.withDayOfMonth(firstDayInPeriod.lengthOfMonth())
        return LocalDateRange(firstDayInPeriod.rangeTo(lastDayInPeriod))
    }

    companion object {
        private val logger: KLogger = KotlinLogging.logger {}
    }
}
