package io.github.mjmoore.model

import io.github.mjmoore.config

class Person(
    val name: String,
    val email: String,
    val exclusions: Set<String> = emptySet()
) {
    var santaCount: Int = 0
        private set

    private val santaNames: MutableSet<String> = mutableSetOf()

    fun needsMoreSantas(): Boolean = santaCount < config.maxSantasPerPerson

    fun isIncluded(santa: Person): Boolean = !exclusions.contains(santa.name)

    fun addSanta(santa: Person) {
        santaCount++
        santaNames.add(santa.name)
    }

    /** Clears assignments so a fresh attempt can be made. */
    fun reset() {
        santaCount = 0
        santaNames.clear()
    }

    /** Names of this person's santas, other than [santa]. */
    fun otherSantas(santa: Person): List<String> = santaNames.filter { it != santa.name }

    companion object {
        private const val listPrefix = "\n\t* "

        fun Iterable<Person>.formattedList(santa: Person): String =
            joinToString(prefix = listPrefix, separator = listPrefix) { receiver ->
                val others = receiver.otherSantas(santa)
                when (others.size) {
                    0 -> receiver.name
                    1 -> "${receiver.name} (the other Santa is ${others.single()})"
                    else -> "${receiver.name} (the other Santas are ${others.joinToString(", ")})"
                }
            }

        fun Iterable<Person>.exclude(person: Person): List<Person> = filter { it != person }
        fun Iterable<Person>.filterAllowed(santa: Person): List<Person> = filter { it.isIncluded(santa) }
    }
}
