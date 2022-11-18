import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    kotlin("jvm") version "1.7.21"
}

group = "io.github.mjmoore"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val jackson = "2.14.0"
val kmail = "1.4.0"

dependencies {
    implementation("net.axay:simplekotlinmail-core:$kmail")
    implementation("net.axay:simplekotlinmail-client:$kmail")

    implementation("com.fasterxml.jackson.core:jackson-core:$jackson")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jackson")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jackson")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "16"
    }
}
