package com.coffee_service.quadro.org.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.coffee_service.quadro.org.model.User
import com.coffee_service.quadro.org.rpc.RpcApi.login
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureSecurity() {
	val jwtAudience = environment.config.property("jwt.audience").getString()
	val jwtDomain = environment.config.property("jwt.issuer").getString()
	val jwtRealm = environment.config.property("jwt.realm").getString()
	val jwtSecret = environment.config.property("jwt.secret").getString()
	authentication {
		jwt("auth-jwt") {
			realm = "void"
			verifier(
				JWT
					.require(Algorithm.HMAC256(jwtSecret))
					.withAudience(jwtAudience)
					.withIssuer(jwtDomain)
					.build()
			)
		}
		jwt("auth-jwt") {
			validate { credential ->
				if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
			}
		}
		jwt("auth-jwt") {
			challenge { _, _ ->
				call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
			}
		}
	}
	routing {
		post("/login") {
			val user = call.receive<User>()
			try {
				val uid = login(
					user.user,
					user.password,
					call.application.environment.config.property("rpc.database").getString(),
				)
				val token = JWT.create()
					.withAudience(jwtAudience)
					.withIssuer(jwtDomain)
					.withClaim("username", user.user)
					.withClaim("uid", uid)
					.withExpiresAt(Date(System.currentTimeMillis() + 100000000000))
					.sign(Algorithm.HMAC256(jwtSecret))
				call.respond(hashMapOf("token" to token))
			} catch (ex: Exception) {
				call.respond(HttpStatusCode.InternalServerError, ex)
			}
		}
	}
}
