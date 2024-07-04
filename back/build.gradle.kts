import org.jooq.meta.jaxb.* 

// Properties for DB access
val DB_DRIVER: String by project
val DB_URL: String by project
val DB_USR: String by project
val DB_PWD: String by project
val DB_VERSION: String by project

val TEST_DB_URL: String by project

// Lib Versions
val kotlin_version="1.9.24" // mai 2024 - when updated also change kotlin("jvm")
val koin_version= "3.5.6" // avril 2024
val junit_version="5.10.3" // juin 2024
val postgres_version="42.7.3" // mars 2024
val flywaydb_version="10.15.2" // juillet 2024 - update in plugin too
val jooq_version="3.19.10" //juin 2024 - update in plugin too
val mock_version="1.13.11" //mai 2024
val logback_version="1.5.6" // avril 2024
val javalin_version="6.1.6" // mai 2024
val jackson_version="2.17.1" // mai 2024
val stripe_version="26.1.0" // juin 2024 
val json_version="20240303" // mars 2024
val jjwt_version="0.12.3"


buildscript {
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:10.15.2") // cf flywaydb
    }
}

plugins {
    kotlin("jvm") version "1.9.24" // cf kotlin_version
    id("org.flywaydb.flyway") version "10.15.2" // cf flywaydb
    id("org.jooq.jooq-codegen-gradle") version "3.19.10" // cf jooq version
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

    // koin
    testImplementation ("io.insert-koin:koin-test-junit5:$koin_version")
    implementation ("io.insert-koin:koin-core:$koin_version")

    // DB
    implementation("org.postgresql:postgresql:$postgres_version")
    testImplementation("org.postgresql:postgresql:$postgres_version")

    jooqCodegen("org.postgresql:postgresql:$postgres_version")
    jooqCodegen("org.postgresql:postgresql:$postgres_version")

    // Junit
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit_version")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit_version")

    // Mock
    testImplementation("io.mockk:mockk:$mock_version")

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
    implementation("io.jsonwebtoken:jjwt-api:$jjwt_version")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwt_version")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwt_version")

    implementation("org.json:json:$json_version")
}

tasks.clean {
    doLast { delete(project.file(generatedDir)) }
}

flyway {
    user = DB_USR
    password = DB_PWD
    locations = arrayOf(DB_VERSION)
    cleanDisabled=false
}

tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("migrateDatabase") {
    url = DB_URL
    }

tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("migrateTestDatabase") {
    url = TEST_DB_URL
}

tasks.register("migrate") {
    dependsOn("migrateDatabase")
    dependsOn("migrateTestDatabase")
}


tasks.register<org.flywaydb.gradle.task.FlywayCleanTask>("cleanDatabase") {
    url = DB_URL
}

tasks.register<org.flywaydb.gradle.task.FlywayCleanTask>("cleanTestDatabase") {
    url = TEST_DB_URL
}

tasks.register("cleanAllDB") {
    dependsOn("cleanDatabase")
    dependsOn("cleanTestDatabase")
}

jooq {
    executions {
        create("tresorier") { // name of the jOOQ configuration
             configuration  {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc {
                    driver = DB_DRIVER
                    url = DB_URL
                    user = DB_USR
                    password = DB_PWD
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
                    driver = DB_DRIVER
                    url = TEST_DB_URL
                    user = DB_USR
                    password = DB_PWD
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
                        packageName = "open.tresorier.generated.jooq.test"
                        directory= generatedDirTest
                    }
                }
            }
        }
    }
}

tasks.named("jooqCodegenTresorier") {mustRunAfter("migrateDatabase")}
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