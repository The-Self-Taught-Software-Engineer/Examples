package codes.jakob.tstse.example.idiomatic.result

import codes.jakob.tstse.example.common.*
import codes.jakob.tstse.example.idiomatic.scopefunctions.DeveloperRepository
import com.github.javafaker.Faker
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.net.SocketTimeoutException
import java.time.Clock
import java.time.Month
import java.time.Year
import java.util.*

internal class PaycheckServiceTest {
    private val faker = Faker()
    private val exists: List<Boolean> = listOf(true, false, true, false, true, true, false, false)
    private val willFail: List<Boolean> = listOf(false, false, false, false, false, false, true, false)
    private val state: Map<Developer, Pair<Boolean, Boolean>> =
        exists.zip(willFail).map { (paycheckExists, willFail) ->
            Triple(backendDeveloper(), paycheckExists, willFail)
        }.groupBy({ it.first }, { it.second to it.third }).mapValues { it.value.first() }

    @Test
    fun generatePaychecks() {
        // Given
        val humanResourcesClient = mockk<HumanResourcesClient>()
        every {
            humanResourcesClient.getHourlyRate(any())
        } returns BigDecimal("21.00")
        every {
            humanResourcesClient.getHoursWorked(any(), any())
        } answers {
            val developer = firstArg<Developer>()
            if (state[developer]!!.second) {
                throw HumanResourcesClient.TimeoutException(
                    developer = developer,
                    message = "Could not retrieve worked hours for '$developer'",
                    cause = SocketTimeoutException("Request to external HR service timed out after 10000ms"),
                )
            } else BigDecimal("155.0")
        }

        val paycheckRepository = mockk<PaycheckRepository>(relaxed = true)
        every {
            paycheckRepository.exists(any(), any())
        } answers { state[firstArg()]!!.first }

        val developerRepository = mockk<DeveloperRepository>()
        every {
            developerRepository.findDevelopersByType(DeveloperType.BACK_END)
        } returns state.keys.toSet()

        // When
        println("\n\nDevelopers:\n")
        state.keys.forEach { dev ->
            println("Developer: ${dev.name} (exists: ${state[dev]!!.first}, fails: ${state[dev]!!.second})")
        }

        println("\nResults:\n")
        val sut = PaycheckService(humanResourcesClient, paycheckRepository, developerRepository, Clock.systemUTC())
        sut.generateAndPersistPaychecks(DeveloperType.BACK_END, Year.of(2021), Month.JULY)

        println("\n")
    }

    private fun backendDeveloper(): Developer {
        val firstName = faker.name().firstName()
        val lastName = faker.name().lastName()
        return Developer(
            id = UUID.randomUUID(),
            name = Name("$firstName $lastName"),
            type = DeveloperType.BACK_END,
            emailAddress = "${firstName.lowercase(Locale.getDefault())}@example.com",
            assignment = null
        )
    }
}
