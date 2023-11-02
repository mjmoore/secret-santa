package io.github.mjmoore.model

import io.github.mjmoore.config

class Person(
    val name: String,
    val email: String,
    var santaCount: Int = 0,
    val santaNames: Set<String> = emptySet(),
    val exclusions: Set<String> = emptySet()
) {
    fun needsMoreSantas(): Boolean = santaCount < config.maxSantasPerPerson

    fun isIncluded(santa: Person): Boolean = !exclusions.contains(santa.name)

    fun incrementSantaCount() = santaCount++

    fun addSanta(santa: Person): santaNames.add(santa.name)

    fun otherSanta(santa: Person): String = ${santaNames.filter {!(x.equals(santa.name))}}

    companion object {
        private const val listPrefix = "\n\t* "

        fun Iterable<Person>.formattedList(santa: Person) =
            map(Person::name + " (The other Santa is " + Person::otherSanta(santa) + ")").joinToString(prefix = listPrefix, separator = listPrefix)

        fun Iterable<Person>.exclude(person: Person): List<Person> = filter { it != person }
        fun Iterable<Person>.filterAllowed(santa: Person): List<Person> = filter { it.isIncluded(santa) }
    }
}
