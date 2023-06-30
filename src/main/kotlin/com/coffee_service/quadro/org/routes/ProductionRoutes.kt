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
import java.util.stream.Collectors

object ProductionCache {
    private val productionQueue = mutableListOf<List<ProductionPayload>>()
    fun updateCache(production: List<ProductionPayload>) {
        production
            .map { it.origin }
            .stream().distinct().collect(Collectors.toList())
            .forEach { productionQueue.add(production.filter { p -> p.origin == it }) }
    }

    fun getNext(): List<ProductionPayload>? = productionQueue.removeFirstOrNull()
}

fun Route.production() {
    route("/production") {
        get {
            val production = queryProduction()
            if (production.isEmpty())
                call.respond(HttpStatusCode.OK, "Production orders empty")
            updateCache(production)
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
