package com.coffee_service.quadro.org.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.coffee_service.quadro.org.model.User
import com.coffee_service.quadro.org.rpc.RpcApi
import com.coffee_service.quadro.org.rpc.RpcApi.login
import com.coffee_service.quadro.org.rpc.RpcApi.version
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
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
				JWT.require(Algorithm.HMAC256(jwtSecret))
					.withAudience(jwtAudience)
					.withIssuer(jwtDomain)
					.build()
			)
			validate { credential ->
				if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload)
				else null
			}
			challenge { _, _ ->
				call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
			}
		}
	}
	routing {
		post("/login") {
			val user = call.receive<User>()
			val version = version(
				call.application.environment.config.property("rpc.host").getString(),
				call.application.environment.config.property("rpc.port").getString(),
				call.application.environment.config.property("rpc.api_key").getString(),
				call.application.environment.config.property("rpc.database").getString(),
			)
			if (version == null) {
				call.respond(HttpStatusCode.InternalServerError, "Login error.")
				return@post
			}
			val uid = login(
				user.user,
				user.password
			)
			if (uid == null) {
				call.respond(HttpStatusCode.InternalServerError, "Login error.")
				return@post
			}
			val token = JWT.create()
				.withAudience(jwtAudience)
				.withIssuer(jwtDomain)
				.withClaim("username", user.user)
				.withExpiresAt(Date(System.currentTimeMillis() + 100000000000))
				.sign(Algorithm.HMAC256(jwtSecret))
			call.respond(hashMapOf("token" to token))
		}
	}
}
