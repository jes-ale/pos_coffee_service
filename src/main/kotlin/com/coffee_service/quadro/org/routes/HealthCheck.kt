package com.coffee_service.quadro.org.routes

import com.coffee_service.quadro.org.rpc.RpcApi
import com.coffee_service.quadro.org.rpc.RpcApi.login
import com.coffee_service.quadro.org.rpc.RpcApi.version
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.healthCheck() {
  route("/version") {
    authenticate("quadro-jwt") {
      get {
        val version = version(
          call.application.environment.config.property("rpc.host").getString(),
          call.application.environment.config.property("rpc.port").getString()
        )

        call.respond(HttpStatusCode.OK, "$version")
      }
    }
  }
}
