import org.jooq.meta.jaxb.ForcedType

val tresorier_db_driver: String by project
val tresorier_db_url_dflt: String by project
val tresorier_db_usr_dflt: String by project
val tresorier_db_pwd_dflt: String by project
val tresorier_db_version: String by project

val test_db_url_dflt: String by project
val test_db_usr_dflt: String by project
val test_db_pwd_dflt: String by project

val auth_db_url_dflt: String by project
val auth_db_usr_dflt: String by project
val auth_db_pwd_dflt: String by project
val auth_db_version: String by project


val tresorierDB = "HEROKU_POSTGRESQL_AQUA_JDBC"
val testDB = "HEROKU_POSTGRESQL_GREEN_JDBC"
val authDB = "HEROKU_POSTGRESQL_RED_JDBC"

val tresorier_db_url = System.getenv(tresorierDB +"_URL") ?: tresorier_db_url_dflt
val tresorier_db_usr = System.getenv(tresorierDB + "_USERNAME") ?: tresorier_db_usr_dflt
val tresorier_db_pwd = System.getenv(tresorierDB + "_PASSWORD") ?: tresorier_db_pwd_dflt

val test_db_url = System.getenv(testDB +"_URL") ?: test_db_url_dflt
val test_db_usr = System.getenv(testDB + "_USERNAME") ?: test_db_usr_dflt
val test_db_pwd = System.getenv(testDB + "_PASSWORD") ?: test_db_pwd_dflt

val auth_db_url = System.getenv(authDB +"_URL") ?: auth_db_url_dflt
val auth_db_usr = System.getenv(authDB + "_USERNAME") ?: auth_db_usr_dflt
val auth_db_pwd = System.getenv(authDB + "_PASSWORD") ?: auth_db_pwd_dflt

buildscript {
    dependencies {
        classpath("org.postgresql:postgresql:42.2.12")
    }
}

plugins {
    kotlin("jvm") version "1.4.10"
    id("org.jetbrains.dokka") version "1.4.0-rc"
    id("org.flywaydb.flyway") version "6.5.5"
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
    implementation("org.slf4j:slf4j-simple:1.7.30")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.10.3")
    implementation("org.koin:koin-core:2.2.1")
    implementation("io.supertokens:javalin:1.4.+")

    implementation("de.mkammerer:argon2-jvm:2.7")

    testImplementation("org.koin:koin-test:2.2.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.1.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.1.1")

    implementation("org.jooq:jooq:3.13.4")
    implementation("org.jooq:jooq-meta:3.13.4")
    implementation("org.jooq:jooq-codegen:3.13.4")
    jooqGenerator("org.postgresql:postgresql:42.2.12")
    implementation("org.postgresql:postgresql:42.2.12")
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

tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("migrateAuthSessionDatabase") {
    url = auth_db_url
    user = auth_db_usr
    password = auth_db_pwd
    locations = arrayOf(auth_db_version)
}

tasks.register<org.flywaydb.gradle.task.FlywayMigrateTask>("migrateTestDatabase") {
    url = test_db_url
    user = test_db_usr
    password = test_db_pwd
    locations = arrayOf(tresorier_db_version)
}

tasks.register("migrate") {
    dependsOn("migrateAuthSessionDatabase")
    dependsOn("migrateTresorierDatabase")
    dependsOn("migrateTestDatabase")
}

tasks.register<org.flywaydb.gradle.task.FlywayCleanTask>("cleanTestDatabase") {
    url = test_db_url
    user = test_db_usr
    password = test_db_pwd
    locations = arrayOf(tresorier_db_version)
}

tasks.register<org.flywaydb.gradle.task.FlywayCleanTask>("cleanTresorierDatabase") {
    url = tresorier_db_url
    user = tresorier_db_usr
    password = tresorier_db_pwd
    locations = arrayOf(tresorier_db_version)
}


tasks.register<org.flywaydb.gradle.task.FlywayCleanTask>("cleanAuthSessionDatabase") {
    url = auth_db_url
    user = auth_db_usr
    password = auth_db_pwd
    locations = arrayOf(auth_db_version)
}

tasks.register("cleanAllDB") {
    dependsOn("cleanAuthSessionDatabase")
    dependsOn("cleanTresorierDatabase")
    dependsOn("cleanTestDatabase")
}


jooq {
    configurations {
        create("main") { // name of the jOOQ configuration
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
                                     packageName = "open.tresorier.generated.jooq"
                                     directory = generatedDir
                                 }
                                 strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                             }
                         }
        }
    }
}

tasks.named("test") {dependsOn("migrate")}
tasks.named("test") {finalizedBy("cleanTestDatabase")}
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
