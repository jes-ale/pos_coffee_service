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
import kotlinx.serialization.Serializable

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
                call.respond(HttpStatusCode.OK, "Production orders empty")
            val sortedProduction = listOf(productionConfirmed)
            updateCache(sortedProduction)
            val body = getNext()
            if (body == null)
                call.respond(HttpStatusCode.OK, "Production orders empty")
            else
                call.respond(HttpStatusCode.OK, body)
        }
        post {
            val id = call.receive<IdPayload>()
            val done = markAsDone(id.id)
            if (done)
                call.respond(HttpStatusCode.OK, id.id)
            else
                call.respond(HttpStatusCode.InternalServerError, "Production not marked as done")
        }
    }
}

@Serializable
data class IdPayload(
    val id: Int
)
