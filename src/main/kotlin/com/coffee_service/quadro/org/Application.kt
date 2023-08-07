package com.coffee_service.quadro.org

import io.ktor.server.application.*
import com.coffee_service.quadro.org.plugins.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)
fun Application.module() {
	configureSockets()
	configureSecurity()
	configureRouting()
	configureSerialization()
	configureHTTP()
	configureTemplating()
}
