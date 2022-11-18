package io.github.mjmoore

import io.github.mjmoore.model.Person.Companion.formattedList
import io.github.mjmoore.model.Santa
import net.axay.simplekotlinmail.data.SMTPLoginInfo
import net.axay.simplekotlinmail.delivery.mailerBuilder
import net.axay.simplekotlinmail.delivery.sendSync
import net.axay.simplekotlinmail.email.emailBuilder
import org.simplejavamail.api.email.Email
import org.simplejavamail.api.mailer.Mailer

class Emailer private constructor(
    smtpConfig: Config.SmtpConfig,
    private val mailer: Mailer = with(smtpConfig) {
        mailerBuilder(
            smtpLoginInfo = SMTPLoginInfo(
                host = host,
                port = port,
                username = username,
                password = password
            )
        )
    }
) {

    constructor(config: Config) : this(config.smtp)

    fun sendEmails(santas: Set<Santa>): Unit = santas
        .map(::generateEmail)
        .forEach { email -> email.sendSync(mailer) }


    private fun generateEmail(santa: Santa): Email = emailBuilder {
        config.email.fromAddress?.let(::from)
        to(santa.person.email)
        config.email.ccAddress?.let(::cc)

        config.email.subject.let(::withSubject)

        withPlainText(
            """
            It's Christmas time!
                    
            You've been selected to be secret santa for: 
            ${santa.receivers.formattedList()}

            Enjoy bringing these people a smile on Christmas day.

            HO HO HOOOOOOOOOOOOOOOOOOOO!

            Warm Regards,
            Santa
            """.trimIndent()
        )

    }
}
