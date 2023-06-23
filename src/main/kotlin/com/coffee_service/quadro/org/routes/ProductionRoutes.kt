package com.coffee_service.quadro.org.routes

import com.coffee_service.quadro.org.model.Production
import com.coffee_service.quadro.org.rpc.RpcApi
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.reflect.*
import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import kotlin.reflect.full.declaredMembers

fun Route.production() {

    route("/production") {
        get {
            val productionConfirmed = RpcApi.queryProduction()
            call.application.environment.log.info(productionConfirmed.toString())
            if (productionConfirmed.isEmpty())
                call.respondText("Production orders empty", status = HttpStatusCode.OK)
            call.respond(HttpStatusCode.OK, productionConfirmed)
        }
    }
}
