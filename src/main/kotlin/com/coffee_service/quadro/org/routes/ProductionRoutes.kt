package com.coffee_service.quadro.org.routes

import com.coffee_service.quadro.org.model.ProductionPayload
import com.coffee_service.quadro.org.routes.ProductionCache.getNext
import com.coffee_service.quadro.org.routes.ProductionCache.updateCache
import com.coffee_service.quadro.org.rpc.RpcApi.markAsDone
import com.coffee_service.quadro.org.rpc.RpcApi.queryProduction
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

object ProductionCache {
    private val productionQueue = mutableListOf<List<ProductionPayload>>()
    fun updateCache(production: List<List<ProductionPayload>>) {
        // TODO: agrupar por order_id
    }

    fun getNext(): List<ProductionPayload>? {
        //TODO: get next by orderid
        throw Exception("not yet implemented")
    }
}

fun Route.production() {
    route("/production") {
        get {
            val productionConfirmed = queryProduction()
            if (productionConfirmed.isEmpty())
                call.respondText("Production orders empty", status = HttpStatusCode.OK)
            val sortedProduction = listOf(productionConfirmed)
            updateCache(sortedProduction)
            val body = getNext()
            if (body == null)
                call.respondText("Production orders empty", status = HttpStatusCode.OK)
            else {
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