@file:Suppress("RedundantSuspendModifier", "unused")

package codes.jakob.tstse.example.idiomatic.collections_vs_sequences

import codes.jakob.tstse.example.common.Developer
import codes.jakob.tstse.example.common.DeveloperType
import codes.jakob.tstse.example.common.Notification
import codes.jakob.tstse.example.idiomatic.collections_vs_sequences.shared.DeveloperNotificationService
import codes.jakob.tstse.example.idiomatic.scopefunctions.DeveloperRepository
import java.time.Clock
import javax.mail.Session

class CollectionsDeveloperNotificationService(
    emailSession: Session,
    clock: Clock,
    private val repository: DeveloperRepository,
) : DeveloperNotificationService(emailSession, clock) {
    override suspend fun notifyDevelopersByEmail(developerType: DeveloperType, notification: Notification) {
        val developers: Set<Developer> = repository.findDevelopersByType(developerType)

        developers
            .filterNot { dev ->
                dev.assigned.also { println("Filtered: " + dev.emailAddress to dev.assigned) }
            }
            .map { dev ->
                generateEmail(dev, notification).also { println("Mapped: " + dev.emailAddress to dev.assigned) }
            }
            .take(2)
            .forEach { email -> sendEmail(email) }
    }
}
