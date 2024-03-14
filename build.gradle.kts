import org.jooq.meta.jaxb.* 

// Properties for DB access
val TRESORIER_DB_DRIVER: String by project
val TRESORIER_DB_URL: String by project
val TRESORIER_DB_USR: String by project
val TRESORIER_DB_PWD: String by project
val TRESORIER_DB_VERSION: String by project

val INTEGRATION_DB_URL: String by project
val INTEGRATION_DB_USR: String by project
val INTEGRATION_DB_PWD: String by project

val TEST_DB_DRIVER: String by project
val TEST_DB_URL: String by project
val TEST_DB_USR: String by project
val TEST_DB_PWD: String by project
val TEST_DB_VERSION: String by project


// Lib Versions
val kotlin_version="1.9.22" // decembre 2023 - when updated also change kotlin("jvm")
val koin_version= "3.5.0" // septembre 2023
val junit_version="5.10.0" // juillet 2023
val postgres_version="42.6.0" // mars 2023
val flyway_version="9.22.1" // septembre 2023
val h2_version="2.2.224" // septembre 2023
val jooq_version="3.19.6" //aout 2023 - update in plugin too
val mock_version="1.13.7" //aout 2023
val logback_version="1.4.11" // aout 2023
val javalin_version="5.6.3" // juillet 2023
val jackson_version="2.15.2" // mai 2023
val argon_version="2.11" // octobre 2021
val stripe_version="23.5.0" // septembre 2023 
val json_version="20230618" // juin 2023

plugins {
    kotlin("jvm") version "1.9.22" // cf kotlin_version
    id("org.jetbrains.dokka") version "1.9.0" // aout 2023
    id("org.flywaydb.flyway") version "9.22.1" // septembre 2023
    id("org.sonarqube") version "4.4.1.3373"
    id("org.jooq.jooq-codegen-gradle") version "3.19.6"
    jacoco
    application
}

repositories {
    mavenCentral();
}

val generatedDir = "src/main/generated"
val generatedDirMain = generatedDir + "/tresorier"
val generatedDirTest = generatedDir + "/test"

sourceSets {
    main {
        java {
            setSrcDirs(listOf(generatedDirMain, generatedDirTest, "src/main/kotlin"))
        }
    }
    create("intTest") {
        java {
            compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
            runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
            setSrcDirs(listOf("src/testIntegration/kotlin"))
        }
    }
}

val intTestImplementation by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

val intTestRuntimeOnly by configurations.getting {
    extendsFrom(configurations.runtimeOnly.get())
}

dependencies {
    // Kotlin
    implementation(kotlin("script-runtime"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")

    // API and Serialisation
    implementation("io.javalin:javalin:$javalin_version") 
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jackson_version")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jackson_version")

    // password encryption
    implementation("de.mkammerer:argon2-jvm:$argon_version")

    // koin
    intTestImplementation("io.insert-koin:koin-test-junit5:$koin_version")
    testImplementation ("io.insert-koin:koin-test-junit5:$koin_version")
    implementation ("io.insert-koin:koin-core:$koin_version")

    // DB
    implementation("org.postgresql:postgresql:$postgres_version")
    intTestImplementation("org.postgresql:postgresql:$postgres_version")
    testImplementation("com.h2database:h2:$h2_version")
    jooqCodegen("org.postgresql:postgresql:$postgres_version")
    jooqCodegen("com.h2database:h2:$h2_version")

    // Junit
    intTestImplementation("org.junit.jupiter:junit-jupiter-api:$junit_version")
    intTestRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit_version")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit_version")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit_version")

    // Mock
    testImplementation("io.mockk:mockk:$mock_version")
    intTestImplementation("io.mockk:mockk:$mock_version")

    // Jooq
    implementation("org.jooq:jooq:$jooq_version")
    implementation("org.jooq:jooq-meta:$jooq_version")
    implementation("org.jooq:jooq-codegen:$jooq_version")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("ch.qos.logback:logback-core:$logback_version")

    // Billing
    implementation("com.stripe:stripe-java:$stripe_version")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")

    implementation("org.json:json:$json_version")
}

tasks.clean {
    doLast { delete(project.file(generatedDir)) }
}

flyway {
    cleanDisabled = false
}

tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("migrateTresorierDatabase") {
    url = TRESORIER_DB_URL
    user = TRESORIER_DB_USR
    password = TRESORIER_DB_PWD
    locations = arrayOf(TRESORIER_DB_VERSION)
}


tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("migrateIntegrationDatabase") {
    url = INTEGRATION_DB_URL
    user = INTEGRATION_DB_USR
    password = INTEGRATION_DB_PWD
    locations = arrayOf(TRESORIER_DB_VERSION)
}

tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("migrateTestDatabase") {
    driver = TEST_DB_DRIVER
    url = TEST_DB_URL
    user = TEST_DB_USR
    password = TEST_DB_PWD
    locations = arrayOf(TEST_DB_VERSION)
}

tasks.register("migrate") {
    dependsOn("migrateTresorierDatabase")
    dependsOn("migrateTestDatabase")
    dependsOn("migrateIntegrationDatabase")
}

tasks.register<org.flywaydb.gradle.task.FlywayCleanTask>("cleanIntegrationDatabase") {
    url = INTEGRATION_DB_URL
    user = INTEGRATION_DB_USR
    password = INTEGRATION_DB_PWD
    locations = arrayOf(TRESORIER_DB_VERSION)
}

tasks.register<org.flywaydb.gradle.task.FlywayCleanTask>("cleanTresorierDatabase") {
    url = TRESORIER_DB_URL
    user = TRESORIER_DB_USR
    password = TRESORIER_DB_PWD
    locations = arrayOf(TRESORIER_DB_VERSION)
}

tasks.register<org.flywaydb.gradle.task.FlywayCleanTask>("cleanTestDatabase") {
    driver = TEST_DB_DRIVER
    url = TEST_DB_URL
    user = TEST_DB_USR
    password = TEST_DB_PWD
    locations = arrayOf(TEST_DB_VERSION)
}

tasks.register("cleanAllDB") {
    dependsOn("cleanTresorierDatabase")
    dependsOn("cleanTestDatabase")
    dependsOn("cleanIntegrationDatabase")
}

jooq {
    executions {
        create("tresorier") { // name of the jOOQ configuration
             configuration  {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc {
                    driver = TRESORIER_DB_DRIVER
                    url = TRESORIER_DB_URL
                    user = TRESORIER_DB_USR
                    password = TRESORIER_DB_PWD
                }
                generator {
                    database {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                    }
                    generate {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                        isDaos = true
                    }
                    target {
                        packageName = "open.tresorier.generated.jooq.main"
                        directory= generatedDirMain
                    }
                }
             }
        }
        create("test") { // name of the jOOQ configuration
            configuration {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc {
                    driver = TEST_DB_DRIVER
                    url = TEST_DB_URL
                    user = TEST_DB_USR
                    password = TEST_DB_PWD
                }
                generator {
                    database {
                        name = "org.jooq.meta.h2.H2Database"
                    }
                    generate {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                        isDaos = true
                    }
                    target {
                        packageName = "open.tresorier.generated.jooq.test"
                        directory= generatedDirTest
                    }
                }
            }
        }
    }
}

tasks.named("jooqCodegenTresorier") {mustRunAfter("migrateTresorierDatabase")}
tasks.named("jooqCodegenTest") {mustRunAfter("migrateTestDatabase")}
tasks.named("compileKotlin") {
    mustRunAfter("jooqCodegen")
}

tasks.named("test") {
    dependsOn("migrateTestDatabase")
    dependsOn("cleanTestDatabase")
}

tasks.named("migrateTestDatabase") {
    dependsOn("cleanTestDatabase")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    doLast {
        println("file://${project.rootDir}/build/reports/jacoco/test/html/index.html")
    }
    classDirectories.setFrom(
            files(classDirectories.files.map {
                fileTree(it) {
                    exclude("open/tresorier/generated/**",
                            "open/tresorier/dependenciesinjection/**",
                            "open/tresorier/api/**",
                            "open/tresorier/dao/jooq/pgsql/**"
                    )
                }
            })
    )
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
    //failFast = true
    finalizedBy(tasks.jacocoTestReport)
}


val integrationTest = task<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"

    testClassesDirs = sourceSets["intTest"].output.classesDirs
    classpath = sourceSets["intTest"].runtimeClasspath
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
    shouldRunAfter("test")
}

tasks.named("integrationTest") {
    dependsOn("migrate")
    finalizedBy("cleanIntegrationDatabase")
}

tasks.check { dependsOn(integrationTest) }

application {
    mainClass.set("open.tresorier.api.ApiKt")
}

tasks.register<Jar>("uberJar") {
    manifest {
        attributes(
            "Main-Class" to "open.tresorier.api.ApiKt"
        )
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveClassifier.set("uber")

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath, "jooqCodegen")
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

tasks.register("stage") {
    dependsOn("uberJar")
    dependsOn("clean")
}

tasks.named("uberJar") {
    mustRunAfter("clean")
}

tasks.register("printInfo") {
    doLast {
        println("hello")
        println(sourceSets.getByName("main").runtimeClasspath.first())
    }
}

sonar {
    properties {
        property("sonar.projectKey", "agatha-budget_back")
        property("sonar.organization", "agatha-budget")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}