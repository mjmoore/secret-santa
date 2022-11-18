package io.github.mjmoore

import io.github.mjmoore.model.Person
import io.github.mjmoore.model.Person.Companion.exclude
import io.github.mjmoore.model.Person.Companion.filterAllowed
import io.github.mjmoore.model.Santa

val config = Config.get()

fun main() {

    val personGenerator: () -> Set<Person> = { config.people }

    val emailer = Emailer(config)

    while (true) {

        val people = personGenerator()

        val santas = generateSantas(people)
        val isValid = santas.map(Santa::isValid).reduce(Boolean::and)

        if (isValid) {
            emailer.sendEmails(santas)
            break
        }
    }

    println("Complete.")
}


private fun generateSantas(people: Set<Person>): Set<Santa> = people
    .sortedByDescending { it.exclusions.size }
    .asSequence()
    .map { santa ->
        val candidates = people
            .exclude(santa)
            .filter(Person::needsMoreSantas)
            .filterAllowed(santa)
            .shuffled()
        val selected = candidates
            .take(config.maxSantasPerPerson)
            .onEach(Person::incrementSantaCount)
        Santa(santa, selected)
    }
    .toSet()

