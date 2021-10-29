import org.jooq.meta.jaxb.ForcedType

// Properties for DB access
val tresorier_db_driver: String by project
val tresorier_db_url_dflt: String by project
val tresorier_db_usr_dflt: String by project
val tresorier_db_pwd_dflt: String by project
val tresorier_db_version: String by project

val integration_db_url_dflt: String by project
val integration_db_usr_dflt: String by project
val integration_db_pwd_dflt: String by project

val test_db_driver: String by project
val test_db_url: String by project
val test_db_usr: String by project
val test_db_pwd: String by project
val test_db_version: String by project

val herokuTresorierDB: String by project
val herokuIntegrationDB: String by project

val tresorier_db_url = System.getenv(herokuTresorierDB +"_URL") ?: tresorier_db_url_dflt
val tresorier_db_usr = System.getenv(herokuTresorierDB + "_USERNAME") ?: tresorier_db_usr_dflt
val tresorier_db_pwd = System.getenv(herokuTresorierDB + "_PASSWORD") ?: tresorier_db_pwd_dflt

val integration_db_url = System.getenv(herokuIntegrationDB +"_URL") ?: integration_db_url_dflt
val integration_db_usr = System.getenv(herokuIntegrationDB + "_USERNAME") ?: integration_db_usr_dflt
val integration_db_pwd = System.getenv(herokuIntegrationDB + "_PASSWORD") ?: integration_db_pwd_dflt

buildscript {
    dependencies {
        classpath("org.postgresql:postgresql:42.2.12")
        classpath("com.h2database:h2:1.4.200")

    }
}

plugins {
    kotlin("jvm") version "1.4.10"
    id("org.jetbrains.dokka") version "1.4.0-rc"
    id("org.flywaydb.flyway") version "7.5.3"
    id("nu.studer.jooq") version "5.2"
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
val h2_version="1.4.200"
val jooq_version="3.13.4"
val mock_version="1.10.5"
val slf4j_version="1.7.30"
val logback_version="1.2.3"
val javalin_version="3.11.0"
val jackson_version="2.10.3"
val supertoken_version="1.4.+"
val argon_version="2.7"
val stripe_version="20.85.0"

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
    implementation("org.slf4j:slf4j-simple:$slf4j_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("ch.qos.logback:logback-core:$logback_version")

    // Billing
    implementation("com.stripe:stripe-java:$stripe_version")

}

tasks.clean {
    doLast { delete(project.file(generatedDir)) }
}

tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("migrateTresorierDatabase") {
    url = tresorier_db_url
    user = tresorier_db_usr
    password = tresorier_db_pwd
    locations = arrayOf(tresorier_db_version)
}


tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("migrateIntegrationDatabase") {
    url = integration_db_url
    user = integration_db_usr
    password = integration_db_pwd
    locations = arrayOf(tresorier_db_version)
}

tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("migrateTestDatabase") {
    driver = test_db_driver
    url = test_db_url
    user = test_db_usr
    password = test_db_pwd
    locations = arrayOf(test_db_version)
}

tasks.register("migrate") {
    dependsOn("migrateTresorierDatabase")
    dependsOn("migrateTestDatabase")
    dependsOn("migrateIntegrationDatabase")
}

tasks.register<org.flywaydb.gradle.task.FlywayCleanTask>("cleanIntegrationDatabase") {
    url = integration_db_url
    user = integration_db_usr
    password = integration_db_pwd
    locations = arrayOf(tresorier_db_version)
}

tasks.register<org.flywaydb.gradle.task.FlywayCleanTask>("cleanTresorierDatabase") {
    url = tresorier_db_url
    user = tresorier_db_usr
    password = tresorier_db_pwd
    locations = arrayOf(tresorier_db_version)
}

tasks.register<org.flywaydb.gradle.task.FlywayCleanTask>("cleanTestDatabase") {
    driver = test_db_driver
    url = test_db_url
    user = test_db_usr
    password = test_db_pwd
    locations = arrayOf(test_db_version)
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
                     driver = tresorier_db_driver
                     url = tresorier_db_url
                     user = tresorier_db_usr
                     password = tresorier_db_pwd
                 }
                 generator.apply {
                     name = "org.jooq.codegen.JavaGenerator"
                     database.apply {
                         name = "org.jooq.meta.postgres.PostgresDatabase"
                         inputSchema = "public"
                         forcedTypes.addAll(
                             arrayOf(
                                 ForcedType()
                                     .withName("varchar")
                                     .withIncludeExpression(".*")
                                     .withIncludeTypes("JSONB?"),
                                 ForcedType()
                                     .withName("varchar")
                                     .withIncludeExpression(".*")
                                     .withIncludeTypes("INET")
                             ).toList()
                         )
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
                    driver = test_db_driver
                    url = test_db_url
                    user = test_db_usr
                    password = test_db_pwd
                }
                generator.apply {
                    name = "org.jooq.codegen.JavaGenerator"
                    database.apply {
                        name = "org.jooq.meta.h2.H2Database"
                        forcedTypes.addAll(
                            arrayOf(
                                ForcedType()
                                    .withName("varchar")
                                    .withIncludeExpression(".*")
                                    .withIncludeTypes("JSONB?"),
                                ForcedType()
                                    .withName("varchar")
                                    .withIncludeExpression(".*")
                                    .withIncludeTypes("INET")
                            ).toList()
                        )
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

tasks.named("compileJava") {
    dependsOn("compileKotlin")
}

tasks.named("compileKotlin") {
    dependsOn("generateJooq")
}


tasks.register("generateJooq") {
    dependsOn("generateTresorierJooq")
    dependsOn("generateTestJooq")
    dependsOn("migrate")
}

tasks.named("generateTresorierJooq") {mustRunAfter("migrateTresorierDatabase")}
tasks.named("generateTestJooq") {mustRunAfter("migrateTestDatabase")}

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
    failFast = true
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
