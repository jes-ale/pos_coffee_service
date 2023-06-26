package com.coffee_service.quadro.org.routes

import com.coffee_service.quadro.org.model.Production
import com.coffee_service.quadro.org.routes.ProductionCache.getNext
import com.coffee_service.quadro.org.routes.ProductionCache.updateCache
import com.coffee_service.quadro.org.rpc.RpcApi.markAsDone
import com.coffee_service.quadro.org.rpc.RpcApi.queryComponent
import com.coffee_service.quadro.org.rpc.RpcApi.queryProduction
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.*

object ProductionCache {
    private val productionQueue = mutableListOf<Production>()
    fun updateCache(production: List<Production>) {
        production.forEachIndexed { index, item ->
            productionQueue.add(item)
        }
    }

    fun getNext(): Production? {
        return productionQueue.removeFirstOrNull()
    }
}

fun Route.production() {
    route("/production") {
        get {
            val productionConfirmed = queryProduction()
            if (productionConfirmed.isEmpty())
                call.respondText("Production orders empty", status = HttpStatusCode.OK)
            updateCache(productionConfirmed)
            val body = getNext()
            if (body == null)
                call.respondText("Production orders empty", status = HttpStatusCode.OK)
            else {
                val ids = body.move_raw_ids.map {
                    Json.decodeFromJsonElement<Int>(it)
                }
                val components = queryComponent(ids)
                body.components = components
                call.respond(HttpStatusCode.OK, body)
            }
        }
        post {
            val id = call.receive<Int>()
            val done = markAsDone(id)
            if (done[0])
                call.respond(HttpStatusCode.OK, id)
            else
                call.respondText("Production not marked as done", status = HttpStatusCode.InternalServerError)
        }
    }
}