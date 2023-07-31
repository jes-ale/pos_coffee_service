package com.coffee_service.quadro.org.routes

import com.coffee_service.quadro.org.rpc.RpcApi.queryBom
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.bom() {
  route("/bom") {
    get {
      val boms = queryBom()
      call.respond(boms)
    }
  }
}
