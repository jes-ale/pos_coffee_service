package com.coffee_service.quadro.org.routes

import com.coffee_service.quadro.org.rpc.RpcApi
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.healthCheck() {
    route("/version") {
        get {
            val version = RpcApi.version(
                call.application.environment.config.property("rpc.host").getString(),
                call.application.environment.config.property("rpc.port").getString()
            )
            RpcApi.login(
                call.application.environment.config.property("rpc.username").getString(),
                call.application.environment.config.property("rpc.password").getString(),
                call.application.environment.config.property("rpc.database").getString(),
            )
            call.respond("$version")
        }
    }
}