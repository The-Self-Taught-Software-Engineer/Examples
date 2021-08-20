package codes.jakob.tstse.example.idiomatic.collections_vs_sequences

import codes.jakob.tstse.example.common.*
import codes.jakob.tstse.example.idiomatic.scopefunctions.DeveloperRepository
import com.github.javafaker.Faker
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.time.Clock
import java.util.*
import javax.mail.Session
import javax.mail.Transport
import kotlin.random.Random

internal class DeveloperNotificationServiceTest {
    private val faker = Faker()
    private val assigned: List<Boolean> = List(100) { Random.nextBoolean() }
    private val developers: Set<Developer> =
        assigned.map { assigned -> backendDeveloper(faker.name().firstName(), assigned) }.toSet()

    @Test
    fun notifyDevelopersByEmail() {
        // Given
        val notification = Notification(
            NotificationType.PRODUCTION_FAILURE_WARN,
            "TEST",
        )

        val emailTransport = mockk<Transport>(relaxed = true)
        val emailSession = mockk<Session>()
        every { emailSession.transport } returns emailTransport
        every { emailSession.properties } returns Properties()

        val repository = mockk<DeveloperRepository>()
        every {
            repository.findDevelopersByType(DeveloperType.BACK_END)
        } returns developers

        // When
        println("\n\nDevelopers:\n")
        developers.forEach { dev -> println("Developer: " + dev.emailAddress to dev.assigned) }
        println("\nCollections:\n")
        val collection = CollectionsDeveloperNotificationService(emailSession, Clock.systemUTC(), repository)
        runBlocking {
            collection.notifyDevelopersByEmail(DeveloperType.BACK_END, notification)
        }

        println("\nvs.")

        println("\nSequences:\n")
        val sequence = SequencesDeveloperNotificationService(emailSession, Clock.systemUTC(), repository)
        runBlocking {
            sequence.notifyDevelopersByEmail(DeveloperType.BACK_END, notification)
        }

        println("\n")
    }

    private fun backendDeveloper(firstName: String, assigned: Boolean): Developer {
        return Developer(
            id = UUID.randomUUID(),
            name = Name("$firstName Testinger"),
            type = DeveloperType.BACK_END,
            emailAddress = "${firstName.lowercase(Locale.getDefault())}@example.com",
            assignment = null
        ).apply {
            assignment = if (assigned) Assignment("", setOf(this), AssignmentType.BUGFIX, "") else null
        }
    }
}
