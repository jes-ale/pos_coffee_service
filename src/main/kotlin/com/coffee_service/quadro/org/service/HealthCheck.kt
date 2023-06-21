package com.coffee_service.quadro.org.service

import io.ktor.server.application.*
import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import java.net.URL
import java.util.Arrays.asList




object HealthCheck {
    val client = XmlRpcClient()
    val common_config = XmlRpcClientConfigImpl()
    var env = mapOf<String, Any?>()
    private var host = ""
    private var port = ""
    private var db = ""
    private var username = ""
    private var password = ""

    /*
    * Always check before logging in
    * */
    fun healthCheck(): Any {
        //TODO: check how to read map or change to acces by propiety
        common_config.serverURL = runCatching { URL("${this.host}/xmlrpc/2/common") }.getOrNull()
        return client.execute(common_config, "version", listOf<Any>())
    }
    fun authenticate(): Int {
        return client.execute(common_config, "authenticate", listOf(db, username, password, listOf<Any>())) as Int
    }
}