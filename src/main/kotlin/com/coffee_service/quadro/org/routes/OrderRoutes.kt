package com.coffee_service.quadro.org.routes

import com.coffee_service.quadro.org.model.Order
import com.coffee_service.quadro.org.routes.OrderCache.addLast
import com.coffee_service.quadro.org.routes.OrderCache.getNext
import com.coffee_service.quadro.org.routes.ProductionCache.setNext
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

object OrderCache {
  private val orderQueue = mutableListOf<List<Order>>()
  fun getNext(): List<Order>? {
    return orderQueue.removeFirstOrNull()
  }

  fun addLast(order: List<Order>): Boolean {
    setNext(order[0].uid)
    return orderQueue.add(order)
  }
}

fun Route.order() {
  route("/order") {
    get {
      val order = getNext()
      if (order != null) call.respond<List<Order>>(order)
      else call.respond(HttpStatusCode.OK, "Orders empty")
    }
    post {
      val order = call.receive<List<Order>>()
      if (addLast(order)) call.respond(HttpStatusCode.OK, "PoS order stored")
      else call.respond(HttpStatusCode.InternalServerError, "PoS order not stored")
    }
  }
}
