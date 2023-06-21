package com.coffee_service.quadro.org.plugins

import com.coffee_service.quadro.org.service.HealthCheck
import io.ktor.server.application.*

fun Application.configureServices() {
    HealthCheck.env = environment.config.toMap()
    //ProductionService.env = environment.config.toMap()
    //WorkOrderService.env = environment.config.toMap()
}