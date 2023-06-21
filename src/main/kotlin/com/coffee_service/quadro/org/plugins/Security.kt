package com.coffee_service.quadro.org.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureSecurity() {

    // Please read the jwt property from the config file if you are using EngineMain
    log.info(environment.config.toMap()["ktor"].toString())
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtDomain = environment.config.property("jwt.issuer").getString()
    val jwtRealm = environment.config.property("jwt.realm").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()
    authentication {
        jwt {
            realm = jwtRealm ?: "void"
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
    authentication {
        form(name = "caffenio_select_admin") {
            userParamName = "username"
            passwordParamName = "password"
            challenge {
                /**/
            }
        }
    }
    routing {
        authenticate("caffenio_select_admin") {
            get("/protected/route/form") {
                val principal = call.principal<UserIdPrincipal>()!!
                call.respondText("Hello ${principal.name}")
            }
        }
    }
}
