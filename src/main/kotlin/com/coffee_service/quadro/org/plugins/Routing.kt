package com.coffee_service.quadro.org.plugins

import com.coffee_service.quadro.org.routes.production
import com.coffee_service.quadro.org.routes.workOrders
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
       production()
       workOrders()
    }
}
