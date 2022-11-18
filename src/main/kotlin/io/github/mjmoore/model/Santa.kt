package io.github.mjmoore.model

import io.github.mjmoore.config

class Santa(
    val person: Person,
    var receivers: List<Person> = emptyList()
) {
    fun isValid(): Boolean = receivers.size == config.maxSantasPerPerson
}
