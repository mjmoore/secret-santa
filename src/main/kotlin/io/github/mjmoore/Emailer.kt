package io.github.mjmoore

import io.github.mjmoore.model.Person.Companion.formattedList
import io.github.mjmoore.model.Santa
import net.axay.simplekotlinmail.data.SMTPLoginInfo
import net.axay.simplekotlinmail.delivery.mailerBuilder
import net.axay.simplekotlinmail.delivery.sendSync
import net.axay.simplekotlinmail.email.emailBuilder
import org.simplejavamail.api.email.Email
import org.simplejavamail.api.mailer.Mailer
import java.io.File
import java.time.LocalDate

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

    fun sendEmails(santas: Set<Santa>) {
        try {
            val emails = santas.map { it to generateEmail(it) }
            // Write all backups before sending anything, so a backup failure aborts the run cleanly.
            config.email.backupDirectory?.let { dir -> emails.forEach { (santa, email) -> backup(dir, santa, email) } }
            emails.forEach { (_, email) -> email.sendSync(mailer) }
        } finally {
            // The SMTP connection pool runs on non-daemon threads; without this the JVM never exits.
            mailer.shutdownConnectionPool().get()
        }
    }


    private fun generateEmail(santa: Santa): Email = emailBuilder {
        config.email.fromAddress?.let(::from)
        to(santa.person.email)
        config.email.ccAddress?.let(::cc)

        config.email.subject.let(::withSubject)

        withPlainText(body(santa))
    }

    // trimIndent() runs before the list is inserted; inserting first would break the indentation.
    private fun body(santa: Santa): String =
        """
        It's Christmas time!

        You've been selected to be secret santa for:
        RECEIVERS

        Enjoy bringing these people a smile on Christmas day.

        HO HO HOOOOOOOOOOOOOOOOOOOO!

        Warm Regards,
        Santa
        """.trimIndent().replace("RECEIVERS", santa.receivers.formattedList(santa.person))

    private fun backup(directory: String, santa: Santa, email: Email) {
        val dir = File(directory).apply { mkdirs() }
        val safeName = santa.person.name.replace(Regex("[^A-Za-z0-9._-]+"), "_")
        val file = File(dir, "${safeName}_${LocalDate.now()}.msg")
        file.writeText(
            buildString {
                appendLine("Date: ${LocalDate.now()}")
                email.fromRecipient?.let { appendLine("From: ${it.address}") }
                email.recipients.forEach { appendLine("${it.type}: ${it.address}") }
                appendLine("Subject: ${email.subject}")
                appendLine()
                appendLine(email.plainText)
            }
        )
        println("Saved ${file.path}")
    }
}
