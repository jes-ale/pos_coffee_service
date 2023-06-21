package com.coffee_service.quadro.org.plugins

import com.coffee_service.quadro.org.routes.healthCheck
import com.coffee_service.quadro.org.routes.order
import com.coffee_service.quadro.org.routes.production
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        healthCheck()
        production()
        order()
    }
}
