import org.jooq.meta.jaxb.ForcedType

// Properties for DB access
val TRESORIER_DB_DRIVER: String by project
val TRESORIER_DB_URL_DFLT: String by project
val TRESORIER_DB_USR_DFLT: String by project
val TRESORIER_DB_PWD_DFLT: String by project
val TRESORIER_DB_VERSION: String by project

val INTEGRATION_DB_URL_DFLT: String by project
val INTEGRATION_DB_USR_DFLT: String by project
val INTEGRATION_DB_PWD_DFLT: String by project

val TEST_DB_DRIVER: String by project
val TEST_DB_URL: String by project
val TEST_DB_USR: String by project
val TEST_DB_PWD: String by project
val TEST_DB_VERSION: String by project

val TRESORIER_DB_ID: String by project
val INTEGRATION_DB_ID: String by project

val TRESORIER_DB_URL = System.getenv(TRESORIER_DB_ID +"_URL") ?: TRESORIER_DB_URL_DFLT
val TRESORIER_DB_USR = System.getenv(TRESORIER_DB_ID + "_USERNAME") ?: TRESORIER_DB_USR_DFLT
val TRESORIER_DB_PWD = System.getenv(TRESORIER_DB_ID + "_PASSWORD") ?: TRESORIER_DB_PWD_DFLT

val INTEGRATION_DB_URL = System.getenv(INTEGRATION_DB_ID +"_URL") ?: INTEGRATION_DB_URL_DFLT
val INTEGRATION_DB_USR = System.getenv(INTEGRATION_DB_ID + "_USERNAME") ?: INTEGRATION_DB_USR_DFLT
val INTEGRATION_DB_PWD = System.getenv(INTEGRATION_DB_ID + "_PASSWORD") ?: INTEGRATION_DB_PWD_DFLT

buildscript {
    dependencies {
        classpath("org.postgresql:postgresql:42.2.12")
        classpath("com.h2database:h2:2.0.206")

    }
}

plugins {
    kotlin("jvm") version "1.4.10"
    id("org.jetbrains.dokka") version "1.4.0-rc"
    id("org.flywaydb.flyway") version "9.3.1"
    id("nu.studer.jooq") version "7.0+"  // https://github.com/etiennestuder/gradle-jooq-plugin#compatibility
    jacoco
    application
}

repositories {
    mavenCentral();
    jcenter()
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


// Lib Versions
val kotlin_version="1.4.10"
val koin_version= "3.0.1-beta-2"
val junit_version="5.1.1"
val postgres_version="42.2.12"
val h2_version="2.0.206"
val jooq_version="3.17.4"
val mock_version="1.10.5"
val slf4j_version="1.7.30"
val logback_version="1.2.3"
val javalin_version="3.11.0"
val jackson_version="2.10.3"
val supertoken_version="1.4.+"
val argon_version="2.7"
val stripe_version="20.85.0"
val json_version="20220320"


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

    // Authentication
    implementation("io.supertokens:javalin:$supertoken_version")

    // password encryption
    implementation("de.mkammerer:argon2-jvm:$argon_version")

    // koin
    intTestImplementation("io.insert-koin:koin-test-junit5:$koin_version")
    testImplementation ("io.insert-koin:koin-test-junit5:$koin_version")
    implementation ("io.insert-koin:koin-core-ext:$koin_version")

    // DB
    implementation("org.postgresql:postgresql:$postgres_version")
    intTestImplementation("org.postgresql:postgresql:$postgres_version")
    testImplementation("com.h2database:h2:$h2_version")
    jooqGenerator("org.postgresql:postgresql:$postgres_version")
    jooqGenerator("com.h2database:h2:$h2_version")

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
    implementation("org.slf4j:slf4j-api:$slf4j_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("ch.qos.logback:logback-core:$logback_version")

    // Billing
    implementation("com.stripe:stripe-java:$stripe_version")

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
    configurations {
        create("tresorier") { // name of the jOOQ configuration
             jooqConfiguration.apply {
                 logging = org.jooq.meta.jaxb.Logging.WARN
                 jdbc.apply {
                     driver = TRESORIER_DB_DRIVER
                     url = TRESORIER_DB_URL
                     user = TRESORIER_DB_USR
                     password = TRESORIER_DB_PWD
                 }
                 generator.apply {
                     name = "org.jooq.codegen.JavaGenerator"
                     database.apply {
                         name = "org.jooq.meta.postgres.PostgresDatabase"
                         inputSchema = "public"
                     }
                     generate.apply {
                         isDeprecated = false
                         isRecords = true
                         isImmutablePojos = true
                         isFluentSetters = true
                         isDaos = true
                     }
                     target.apply {
                         packageName = "open.tresorier.generated.jooq.main"
                         directory= generatedDirMain

                     }
                     strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                 }
             }
        }
        create("test") { // name of the jOOQ configuration
            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc.apply {
                    driver = TEST_DB_DRIVER
                    url = TEST_DB_URL
                    user = TEST_DB_USR
                    password = TEST_DB_PWD
                }
                generator.apply {
                    name = "org.jooq.codegen.JavaGenerator"
                    database.apply {
                        name = "org.jooq.meta.h2.H2Database"
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                        isDaos = true
                    }
                    target.apply {
                        packageName = "open.tresorier.generated.jooq.test"
                        directory= generatedDirTest
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

tasks.register("generateJooq") {
    dependsOn("generateTresorierJooq")
    dependsOn("generateTestJooq")
    dependsOn("migrate")
}

tasks.named("generateTresorierJooq") {mustRunAfter("migrateTresorierDatabase")}
tasks.named("generateTestJooq") {mustRunAfter("migrateTestDatabase")}
tasks.named("compileKotlin") {
    mustRunAfter("generateTresorierJooq")
    mustRunAfter("generateTestJooq")
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

tasks.register<Jar>("uberJar") {
    manifest {
        attributes(
            "Main-Class" to "open.tresorier.api.ApiKt"
        )
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveClassifier.set("uber")

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath, "generateJooq")
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
