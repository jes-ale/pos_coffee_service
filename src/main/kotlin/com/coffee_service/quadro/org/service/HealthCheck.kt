package com.coffee_service.quadro.org.service

import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import java.net.URL

object HealthCheck {
    val client = XmlRpcClient()
    val start_config = XmlRpcClientConfigImpl()
    var env = mapOf<String, Any?>()
    fun healthCheck(): Any {
        start_config.serverURL = runCatching { URL(env["ktor.rpc.host"].toString()) }.getOrNull()
        return client.execute(start_config, "version", listOf<Any>())
    }
}