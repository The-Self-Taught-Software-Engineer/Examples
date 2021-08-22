@file:Suppress("RedundantSuspendModifier", "unused")

package codes.jakob.tstse.example.idiomatic.collections_vs_sequences

import codes.jakob.tstse.example.common.Developer
import codes.jakob.tstse.example.common.DeveloperType
import codes.jakob.tstse.example.common.Notification
import codes.jakob.tstse.example.common.DeveloperNotificationService
import codes.jakob.tstse.example.idiomatic.collections_vs_sequences.shared.SpyCollection.Companion.toSpyCollection
import codes.jakob.tstse.example.idiomatic.scopefunctions.DeveloperRepository
import mu.KLogger
import mu.KotlinLogging
import java.time.Clock
import javax.mail.Session

class SequencesDeveloperNotificationService(
    emailSession: Session,
    clock: Clock,
    private val repository: DeveloperRepository,
) : DeveloperNotificationService(emailSession, clock) {
    override suspend fun notifyDevelopersByEmail(developerType: DeveloperType, notification: Notification) {
        var operation = 0

        val developers: Set<Developer> = repository.findDevelopersByType(developerType)

        val unassignedDevelopers = developers
            .asSequence()
            .filterNot { dev ->
                dev.assigned
                    .also { logger.debug("${++operation}. Filtered: ${dev.emailAddress to dev.assigned}") }
            }
        val emails = unassignedDevelopers
            .map { dev ->
                generateEmail(dev, notification)
                    .also { logger.debug("${++operation}. Mapped: ${dev.emailAddress to dev.assigned}") }
            }
            .take(unassignedDevelopers.count() / 2)
            .toSpyCollection()

        emails
            .forEach { email ->
                sendEmail(email)
                    .also { logger.debug("${++operation}. Sending email to ${email.firstRecipient()}") }
            }
    }

    companion object {
        private val logger: KLogger = KotlinLogging.logger {}
    }
}
