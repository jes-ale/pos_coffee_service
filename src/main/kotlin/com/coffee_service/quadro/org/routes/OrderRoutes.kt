package com.coffee_service.quadro.org.routes

import com.coffee_service.quadro.org.model.Order
import io.ktor.server.routing.*

object OrderCache {
   private val orderQueue = mutableListOf<Order>()
}
fun Route.order() {
    route ("/order") {
        get {

        }
        get ("{id?}") {

        }
        post {

        }
        delete("{id?}") {

        }
    }
}
