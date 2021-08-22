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
    private val humanResourcesService: HumanResourcesClient,
    private val paycheckRepository: PaycheckRepository,
    private val developerRepository: DeveloperRepository,
    private val clock: Clock,
) {
    /**
     * Returns an ordered list of [Paycheck]; sorted lexicographically by the [Name] of the [Developer].
     */
    fun generatePaychecks(developerType: DeveloperType, year: Year, month: Month): List<Paycheck> {
        val today: LocalDate = LocalDate.now(clock)
        val paycheckPeriod: Pair<LocalDate, LocalDate> = determinePeriod(year, month)

        val (successfulPaychecks: List<Paycheck>, failedPaychecks: List<Developer>) =
            developerRepository.findDevelopersByType(developerType)
                .asSequence()
                .map { dev ->
                    val paycheckBuilder = Paycheck.Builder().apply {
                        date = today
                        period = paycheckPeriod
                        developer = dev
                    }
                    dev to Result.success(paycheckBuilder)
                }
                .map { (dev, paycheckBuilder) ->
                    dev to paycheckBuilder.mapCatching { paycheck ->
                        paycheck.apply { hourlyRate = humanResourcesService.getHourlyRate(paycheck.developer!!) }
                    }
                }
                .map { (dev, paycheckBuilder) ->
                    dev to paycheckBuilder.mapCatching { paycheck ->
                        paycheck.apply {
                            hoursWorked = humanResourcesService.getHoursWorked(paycheck.developer!!, paycheckPeriod)
                        }
                    }
                }
                .map { (dev, paycheckBuilder) ->
                    dev to paycheckBuilder.mapCatching { paycheck ->
                        paycheck.build()
                    }
                }
                .map { (dev, paycheckBuilder) ->
                    dev to paycheckBuilder
                        .onSuccess {
                            logger.info {
                                "Generation of ${Paycheck::class.simpleName} for '$dev' in period '$paycheckPeriod' succeeded"
                            }
                        }
                        .onFailure { exception ->
                            logger.error(exception) {
                                "Generation of ${Paycheck::class.simpleName} for '$dev' in period '$paycheckPeriod' failed"
                            }
                        }
                }
                .partition { (_, paycheckBuilder) ->
                    paycheckBuilder.isSuccess
                }
                .let { (success, failure) ->
                    success.map { it.second.getOrThrow() } to failure.map { it.first }
                }

        paycheckRepository.save(successfulPaychecks)
        paycheckRepository.saveFailed(failedPaychecks, paycheckPeriod)

        return successfulPaychecks.sortedBy { it.developer.name }
    }

    private fun determinePeriod(year: Year, month: Month): Pair<LocalDate, LocalDate> {
        val firstDayInPeriod: LocalDate = LocalDate.of(year.value, month, 1)
        val lastDayInPeriod: LocalDate = firstDayInPeriod.withDayOfMonth(firstDayInPeriod.lengthOfMonth())
        return firstDayInPeriod to lastDayInPeriod
    }

    companion object {
        private val logger: KLogger = KotlinLogging.logger {}
    }
}
