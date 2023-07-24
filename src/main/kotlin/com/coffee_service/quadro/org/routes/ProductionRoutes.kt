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
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.stream.Collectors
import kotlinx.serialization.Serializable

object ProductionCache {
  private val productionCache = mutableMapOf<String, List<ProductionPayload>>()
  private val productionQueue = mutableListOf<String>()
  fun updateCache(production: List<ProductionPayload>) {
    // TODO: only clear when size == limit
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
    get {
      val production = queryProduction()
      call.application.environment.log.info("=======")
      call.application.environment.log.info("production: $production")
      call.application.environment.log.info("=======")
      if (production.isEmpty()) call.respond(HttpStatusCode.OK, "Production orders empty")
      else {
        updateCache(production)
        val body = getNext()
        call.application.environment.log.info("=======")
        call.application.environment.log.info("body: $body")
        call.application.environment.log.info("=======")
        if (body == null) call.respond(HttpStatusCode.OK, "Production orders empty")
        else call.respond(HttpStatusCode.OK, body)
      }
    }
    post {
      val id = call.receive<IdPayload>()
      val done = markAsDone(id.id)
      if (done) call.respond(HttpStatusCode.OK, id.id)
      else call.respond(HttpStatusCode.InternalServerError, "Production not marked as done")
    }
  }
  route("/setNextProduction") {
    post {
      val uid = call.receive<UidPayload>()
      setNext(uid.uid)
      call.respond(HttpStatusCode.OK, uid.uid)
    }
  }
  route("/getProductionQueue") {
    get {
      call.application.environment.log.info("+++++")
      val queue = getQueue()
      call.application.environment.log.info("queue: $queue")
      call.respond(HttpStatusCode.OK, queue)
      call.application.environment.log.info("+++++")
    }
  }
  route("/getProductionCache") {
    get {
      call.application.environment.log.info("+++++")
      val cache = getCache()
      call.application.environment.log.info("cache: $cache")
      call.respond(HttpStatusCode.OK, cache)
      call.application.environment.log.info("+++++")
    }
  }
}

@Serializable data class IdPayload(val id: Int)

@Serializable data class UidPayload(val uid: String)
