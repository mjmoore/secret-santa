package io.github.mjmoore.model

import io.github.mjmoore.config

class Person(
        val name: String,
        val email: String,
        var santacount: Int = 0,
        var santanames: Set<String> = emptySet(),
        val exclusions: Set<String> = emptySet()
) {
    fun needsMoreSantas(): Boolean = santacount < config.maxSantasPerPerson

    fun isIncluded(santa: Person): Boolean = !exclusions.contains(santa.name)

    fun incrementSantaCount(santa: Person) {
        santacount++
        santanames = this.santanames.plus(santa.name)
        println(message = santa.name + " got " + name + ". Santa names are " + santanames)
        println(message = "The other santa is " + otherSanta(santa))
    }

    fun otherSanta(santa: Person): String = santanames.filter {
        !(it.equals(santa.name))
    }.toString()

      companion object {
        private const val listPrefix = "\n\t* "

        fun Iterable<Person>.formattedList() =
           map(Person::name).joinToString(prefix = listPrefix, separator = listPrefix)
          // todo - add "The other Santa is XXX" into the above
        fun Iterable<Person>.exclude(person: Person): List<Person> = filter { it != person }
        fun Iterable<Person>.filterAllowed(santa: Person): List<Person> = filter { it.isIncluded(santa) }
    }
}
