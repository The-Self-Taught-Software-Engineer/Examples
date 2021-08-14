import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
}

group = "codes.jakob.tstse.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.javafaker:javafaker:1.0.2")
    implementation("com.sun.mail:javax.mail:1.5.5")
    implementation("io.github.microutils:kotlin-logging:2.0.6")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation("org.slf4j:slf4j-simple:1.7.30")

    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.12.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
