@file:Suppress("RedundantSuspendModifier", "unused")

package codes.jakob.tstse.example.idiomatic.collections_vs_sequences.shared

import codes.jakob.tstse.example.common.Developer
import codes.jakob.tstse.example.common.DeveloperType
import codes.jakob.tstse.example.common.Notification
import java.time.Clock
import java.util.*
import javax.mail.Message
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

abstract class DeveloperNotificationService(
    private val emailSession: Session,
    private val clock: Clock,
) {
    abstract suspend fun notifyDevelopersByEmail(developerType: DeveloperType, notification: Notification)

    protected fun generateEmail(developer: Developer, notification: Notification): Message {
        return MimeMessage(emailSession).apply {
            setRecipients(Message.RecipientType.TO, InternetAddress.parse(developer.emailAddress, false))
            replyTo = InternetAddress.parse(REPLY_TO_ADDRESS, false)
            subject = generateEmailSubject(developer, notification)
            setText(generateEmailBody(developer, notification))
        }
    }

    protected suspend fun sendEmail(email: Message) {
        email.apply {
            sentDate = Date.from(clock.instant())
        }
        println("Sending email '${email.subject}' to ${email.allRecipients.map { it.toString() }.first()}")
    }

    private fun generateEmailSubject(developer: Developer, notification: Notification): String {
        return notification.info
    }

    private fun generateEmailBody(developer: Developer, notification: Notification): String {
        return "Dear ${developer.name}"
    }

    companion object {
        private const val REPLY_TO_ADDRESS = "noreply@example.com"
    }
}
