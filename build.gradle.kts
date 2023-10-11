val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.8.22"
    id("io.ktor.plugin") version "2.3.5"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.22"
}

group = "com.coffe_service.quadro.org"
version = "0.1.1"
application {
    mainClass.set("com.coffee_service.quadro.org.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    flatDir {
        dirs("libs")
    }
}
dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-websockets-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-swagger-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-http-redirect-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-cors-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-freemarker-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.22")
    // local libs
    implementation(files("libs/commons-logging-1.1.jar"))
    implementation(files("libs/ws-commons-util-1.0.2.jar"))
    implementation(files("libs/xmlrpc-client-3.1.2.jar"))
    implementation(files("libs/xmlrpc-common-3.1.2.jar"))
    implementation(files("libs/xmlrpc-server-3.1.2.jar"))
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
