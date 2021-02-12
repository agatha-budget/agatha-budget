import org.jooq.meta.jaxb.ForcedType

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


val tresorierDB = "HEROKU_POSTGRESQL_AQUA_JDBC"
val integrationDB = "HEROKU_POSTGRESQL_GREEN_JDBC"

val tresorier_db_url = System.getenv(tresorierDB +"_URL") ?: tresorier_db_url_dflt
val tresorier_db_usr = System.getenv(tresorierDB + "_USERNAME") ?: tresorier_db_usr_dflt
val tresorier_db_pwd = System.getenv(tresorierDB + "_PASSWORD") ?: tresorier_db_pwd_dflt

val integration_db_url = System.getenv(integrationDB +"_URL") ?: integration_db_url_dflt
val integration_db_usr = System.getenv(integrationDB + "_USERNAME") ?: integration_db_usr_dflt
val integration_db_pwd = System.getenv(integrationDB + "_PASSWORD") ?: integration_db_pwd_dflt



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
    application
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("script-runtime"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.10")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.4.10")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.10")
    implementation("io.javalin:javalin:3.11.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.10.3")
    implementation("org.koin:koin-core:2.2.1")
    implementation("io.supertokens:javalin:1.4.+")

    implementation("de.mkammerer:argon2-jvm:2.7")

    testImplementation("org.koin:koin-test:2.2.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.1.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.1.1")
    testImplementation("io.mockk:mockk:1.10.5")
    testImplementation("com.h2database:h2:1.4.200")

    implementation("org.jooq:jooq:3.13.4")
    implementation("org.jooq:jooq-meta:3.13.4")
    implementation("org.jooq:jooq-codegen:3.13.4")
    jooqGenerator("org.postgresql:postgresql:42.2.12")
    jooqGenerator("com.h2database:h2:1.4.200")
    implementation("org.postgresql:postgresql:42.2.12")
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("org.slf4j:slf4j-simple:1.7.30")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("ch.qos.logback:logback-core:1.2.3")
}

val generatedDir = "src/main/generated"

sourceSets {
    main {
        java {
            setSrcDirs(listOf(generatedDir, "src/main/kotlin"))
        }
    }
}

tasks.named("compileJava") {
    dependsOn("generateJooq")
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
                         packageName = "open.tresorier.generated.jooq.tresorier"
                         directory= generatedDir + "/tresorier"

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
                        directory= generatedDir + "/test"
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
}


tasks.named("test") {dependsOn("migrate")}
tasks.named("generateJooq") {dependsOn("migrateTresorierDatabase")}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.register<Jar>("uberJar") {
    manifest {
        attributes(
            "Main-Class" to "open.tresorier.api.ApiKt"
        )
    }

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
