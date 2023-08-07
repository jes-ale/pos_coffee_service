package com.coffee_service.quadro.org.routes

import com.coffee_service.quadro.org.model.ProductionPayload
import com.coffee_service.quadro.org.routes.ProductionCache.getCache
import com.coffee_service.quadro.org.routes.ProductionCache.getNext
import com.coffee_service.quadro.org.routes.ProductionCache.getQueue
import com.coffee_service.quadro.org.routes.ProductionCache.setNext
import com.coffee_service.quadro.org.routes.ProductionCache.updateCache
import com.coffee_service.quadro.org.rpc.RpcApi.markAsDone
import com.coffee_service.quadro.org.rpc.RpcApi.queryProduction
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.stream.Collectors
import kotlinx.serialization.Serializable

object ProductionCache {
  private val productionCache = mutableMapOf<String, List<ProductionPayload>>()
  private val productionQueue = mutableListOf<String>()
  fun updateCache(production: List<ProductionPayload>) {
    productionCache.clear()
    production.map { it.origin }.stream().distinct().collect(Collectors.toList()).forEach {
      productionCache[it] = production.filter { p -> p.origin == it }
    }
  }

  fun setNext(origin: String): List<String> {
    productionQueue.add(origin)
    return productionQueue
  }

  fun getNext(): List<ProductionPayload>? =
    runCatching { productionCache[productionQueue.removeFirstOrNull()] }.getOrDefault(null)

  fun getQueue(): List<String> {
    return productionQueue
  }

  fun getCache(): Map<String, List<ProductionPayload>> {
    return productionCache
  }
}

fun Route.production() {
  route("/production") {
    authenticate("auth-jwt") {
      get {
        val production = queryProduction()
        if (production.isEmpty()) call.respond(HttpStatusCode.OK, "Production orders empty")
        updateCache(production)
        val body = getNext()
        call.application.environment.log.info(body.toString())
        if (body == null) call.respond(HttpStatusCode.OK, "Production orders empty")
        else call.respond(HttpStatusCode.OK, body)
      }
    }
    authenticate("auth-jwt") {
      post {
        val id = call.receive<IdPayload>()
        val done = markAsDone(id.id)
        if (done) call.respond(HttpStatusCode.OK, id.id)
        else call.respond(HttpStatusCode.InternalServerError, "Production not marked as done")
      }
    }
  }
  route("/setNextProduction") {
    authenticate("auth-jwt") {
      post {
        val uid = call.receive<UidPayload>()
        call.application.environment.log.info(uid.uid)
        // UID reaches here as POS-Orden {uid} so we can just push it unlike /order endpoint in
        // which we need to transform the UID into Origin string
        setNext(uid.uid)
        call.respond(HttpStatusCode.OK, uid.uid)
      }
    }
  }
  route("/getProductionQueue") {
    authenticate("auth-jwt") {
      get { call.respond(HttpStatusCode.OK, getQueue()) }
    }
  }
  route("/getProductionCache") {
    authenticate("auth-jwt") {
      get { call.respond(HttpStatusCode.OK, getCache()) }
    }
  }
}

@Serializable
data class IdPayload(val id: Int)

@Serializable
data class UidPayload(val uid: String)
