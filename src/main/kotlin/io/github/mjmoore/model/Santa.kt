package io.github.mjmoore.model

import io.github.mjmoore.config
import io.github.mjmoore.model.Person.Companion.formattedList

class Santa(
    val person: Person,
    var receivers: List<Person> = emptyList()
) {
    fun isValid(): Boolean = receivers.size == config.maxSantasPerPerson

    fun generateEmailTxtOnly() { println(this.receivers.formattedList()) }

}
