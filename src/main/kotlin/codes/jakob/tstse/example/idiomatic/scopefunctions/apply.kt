@file:Suppress("unused")

package codes.jakob.tstse.example.idiomatic.scopefunctions

import codes.jakob.tstse.example.common.Developer
import codes.jakob.tstse.example.common.DeveloperType
import codes.jakob.tstse.example.common.Notification
import java.time.Clock
import java.util.*
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport.send
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class DeveloperNotificationService(
    private val emailSession: Session,
    private val repository: DeveloperRepository,
    private val clock: Clock,
) {
    fun notifyRandomDeveloperByEmail(developerType: DeveloperType, notification: Notification) {
        val developer: Developer = repository.findRandomDeveloperByType(developerType)
            ?: error("No developer with type '$developerType' exists")

//        val email: Message = MimeMessage(emailSession)
//        setRecipients(Message.RecipientType.TO, InternetAddress.parse(developer.emailAddress, false))
//        replyTo = InternetAddress.parse(replyToAddress, false)
//        sentDate = Date.from(clock.instant())
//        subject = generateEmailSubject(developer, notification)
//        setText(generateEmailBody(developer, notification))

        val email: Message = MimeMessage(emailSession).apply {
            setRecipients(Message.RecipientType.TO, InternetAddress.parse(developer.emailAddress, false))
            replyTo = InternetAddress.parse(replyToAddress, false)
            sentDate = Date.from(clock.instant())
            subject = generateEmailSubject(developer, notification)
            setText(generateEmailBody(developer, notification))
        }

        send(email)
    }

    private fun generateEmailSubject(developer: Developer, notification: Notification): String {
        TODO("Not yet implemented")
    }

    private fun generateEmailBody(developer: Developer, notification: Notification): String {
        TODO("Not yet implemented")
    }

    companion object {
        private const val replyToAddress = "noreply@example.com"
    }
}
