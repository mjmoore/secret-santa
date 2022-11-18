package io.github.mjmoore.model

import io.github.mjmoore.config

class Person(
    val name: String,
    val email: String,
    var santas: Int = 0,
    val exclusions: Set<String> = emptySet()
) {
    fun needsMoreSantas(): Boolean = santas < config.maxSantasPerPerson

    fun isIncluded(santa: Person): Boolean = !exclusions.contains(santa.name)

    fun incrementSantaCount() = santas++

    companion object {
        private const val listPrefix = "\n\t* "

        fun Iterable<Person>.formattedList() =
            map(Person::name).joinToString(prefix = listPrefix, separator = listPrefix)

        fun Iterable<Person>.exclude(person: Person): List<Person> = filter { it != person }
        fun Iterable<Person>.filterAllowed(santa: Person): List<Person> = filter { it.isIncluded(santa) }
    }
}
