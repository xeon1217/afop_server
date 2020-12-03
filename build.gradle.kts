import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.3.1.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
    kotlin("plugin.jpa") version "1.3.72"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    implementation(group = "org.mariadb.jdbc", name = "mariadb-java-client")
    implementation("io.jsonwebtoken:jjwt:0.9.1") //JsonWebToken library
    implementation("net.rakugakibox.util:yaml-resource-bundle:1.1") //yml library

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor") //spring-boot-processor
    implementation("org.springframework.boot:spring-boot-configuration-processor") //spring-boot-processor
    implementation("org.springframework.boot:spring-boot-devtools") //spring-boot-devtools
    implementation("org.springframework.boot:spring-boot-starter-data-jpa") //spring-boot-jpa
    implementation("org.springframework.boot:spring-boot-starter-data-rest") //spring-boot-rest-api
    implementation("org.springframework.boot:spring-boot-starter-security") //spring-boot-security
    implementation("org.springframework.boot:spring-boot-starter-mail") //spring-boot-mail
    implementation("org.springframework.boot:spring-boot-starter-websocket")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}
