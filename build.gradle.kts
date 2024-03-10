plugins {
    id("java")
}

group = "org.devster"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.formdev:flatlaf:3.4")
    implementation("org.drjekyll:colorpicker:1.4.5")
}

tasks.test {
    useJUnitPlatform()
}