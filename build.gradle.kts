plugins {
   kotlin("jvm") version "2.2.21"
}

group = "de.hawkiel.oop"
version = "1.0"

repositories {
   mavenCentral()
}

dependencies {
   implementation("org.processing:core:4.5.3")
}

kotlin {
   jvmToolchain(21)
}