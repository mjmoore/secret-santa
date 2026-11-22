plugins {
    kotlin("jvm") version "2.0.21"
    application
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

kotlin {
    jvmToolchain(21)
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

application {
    mainClass.set("io.github.mjmoore.MainKt")
}
