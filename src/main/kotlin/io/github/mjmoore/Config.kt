package io.github.mjmoore

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.mjmoore.model.Person
import java.lang.RuntimeException

data class Config(
    val maxSantasPerPerson: Int,
    val people: Set<Person> = emptySet(),
    val email: EmailConfig,
    val smtp: SmtpConfig
) {

    data class EmailConfig(
        val fromAddress: String?,
        val ccAddress: String?,
        val subject: String
    )

    data class SmtpConfig(
        val host: String,
        val port: Int,
        val username: String,
        val password: String
    )

    companion object {
        private const val configFileName = "application.yml"
        private val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

        fun get(): Config = Config::class
            .java
            .classLoader
            .getResource(configFileName)
            ?.let { mapper.readValue(it) }
            ?: throw RuntimeException("Could not find file $configFileName")
    }
}
