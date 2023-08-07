package com.coffee_service.quadro.org.plugins

import com.coffee_service.quadro.org.routes.healthCheck
import com.coffee_service.quadro.org.routes.order
import com.coffee_service.quadro.org.routes.production
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*

fun Application.configureRouting() {
	routing {
		staticResources(remotePath = "/static", "/files")
		this@configureRouting.configureSecurity()
		healthCheck()
		production()
		order()
	}
}
