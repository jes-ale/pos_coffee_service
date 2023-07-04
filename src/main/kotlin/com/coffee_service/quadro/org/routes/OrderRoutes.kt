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
  private val orderQueue = mutableListOf<Order>()
  fun getNext(): Order? {
    return orderQueue.removeFirstOrNull()
  }

  fun addLast(order: Order)Boolean {
    setNext("POS-Orden ${order.uid}")
    return orderQueue.add(order)
  }
}

fun Route.order() {
  route("/order") {
    get {
      val order = getNext()
      if (order != null) call.respond(order)
      else call.respond(HttpStatusCode.InternalServerError, "Orders empty")
    }
    post {
      val order = call.receive<List<Order>>()
      if (addLast(order)) call.respond(HttpStatusCode.OK, "PoS order stored")
      else call.respond(HttpStatusCode.InternalServerError, "PoS order not stored")
    }
  }
}
