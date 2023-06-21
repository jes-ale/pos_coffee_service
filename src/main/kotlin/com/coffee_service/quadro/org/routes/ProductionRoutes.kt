package com.coffee_service.quadro.org.routes

import com.coffee_service.quadro.org.model.Production
import com.coffee_service.quadro.org.service.ProductionService.queryProduction
import com.coffee_service.quadro.org.service.ProductionService.storeProduction
import com.coffee_service.quadro.org.service.HealthCheck.healthCheck
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.production() {
    route("/version") {
        get {
            val version = healthCheck()
            call.application.environment.log.info("$version")
            call.respond(version)
        }
    }
    route("/production") {
        get {
            call.respond(queryProduction())
        }
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            // Currently do not support single id read
            call.respondText("No production found", status = HttpStatusCode.OK)
        }
        post {
            val customer = call.receive<Production>()
            storeProduction(customer)
            call.respondText("Production order stored correctly", status = HttpStatusCode.Created)
        }
        delete("{id?}") {

        }
    }
}