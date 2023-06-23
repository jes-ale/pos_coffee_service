package com.coffee_service.quadro.org.routes

import com.coffee_service.quadro.org.model.Order
import com.coffee_service.quadro.org.model.Production
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
        return orderQueue.add(order)
    }
}

fun Route.order() {
    route("/order") {
        get {
            val order = OrderCache.getNext()
            if (order != null) call.respond<List<Order>>(order)
            else call.respondText("Orders empty", status = HttpStatusCode.OK)
        }
        post {
            val order = call.receive<List<Order>>()
            if (OrderCache.addLast(order)) call.respondText(
                "PoS order stored correctly",
                status = HttpStatusCode.Created
            )
            else call.respondText("PoS order not stored")
        }
    }
}
