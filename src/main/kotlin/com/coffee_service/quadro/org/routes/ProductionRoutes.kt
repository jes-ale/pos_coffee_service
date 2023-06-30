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
import java.util.stream.Collectors
import kotlinx.serialization.Serializable

object ProductionCache {
  private val productionCache = mutableMapOf<String, List<ProductionPayload>>()
  private val productionQueue = mutableListOf<String>()
  fun updateCache(production: List<ProductionPayload>) {
    production.map { it.origin }.stream().distinct().collect(Collectors.toList()).forEach {
      productionCache[it] = production.filter { p -> p.origin == it }
    }
  }
  fun setNext(origin: String) = productionQueue.add(origin)
  fun getNext(): List<ProductionPayload>? =
      runCatching { productionCache[productionQueue.removeFirstOrNull()] }.getOrDefault(null)
}

fun Route.production() {
  route("/production") {
    get {
      val production = queryProduction()
      if (production.isEmpty()) call.respond(HttpStatusCode.OK, "Production orders empty")
      updateCache(production)
      val body = getNext()
      if (body == null) call.respond(HttpStatusCode.OK, "Production orders empty")
      else call.respond(HttpStatusCode.OK, body)
    }
    post {
      val id = call.receive<IdPayload>()
      val done = markAsDone(id.id)
      if (done) call.respond(HttpStatusCode.OK, id.id)
      else call.respond(HttpStatusCode.InternalServerError, "Production not marked as done")
    }
    post("/setNext") {
      val uid = call.receive<String>()
      setNext(uid)
    }
  }
}

@Serializable data class IdPayload(val id: Int)
