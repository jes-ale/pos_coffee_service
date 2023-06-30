package com.coffee_service.quadro.org.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.httpsredirect.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

fun Application.configureHTTP() {
  routing { swaggerUI(path = "openapi") }
  /* install(HttpsRedirect) {
      // The port to redirect to. By default, 443, the default HTTPS port.
      sslPort = 443
      // 301 Moved Permanently, or 302 Found redirect.
      permanentRedirect = true
  }*/
  install(CORS) {
    allowMethod(HttpMethod.Options)
    allowMethod(HttpMethod.Put)
    allowMethod(HttpMethod.Delete)
    allowMethod(HttpMethod.Patch)
    allowHeader(HttpHeaders.Authorization)
    allowHeader(HttpHeaders.ContentType)
    allowHeader(HttpHeaders.Accept)
    allowHost("158.69.63.47")
    allowHost("158.69.63.47:8079")
    allowHost("158.69.63.47:3000")
    allowHost("158.69.63.47", schemes = listOf("http", "https"))
    allowSameOrigin = true
  }
}
