package com.coffee_service.quadro.org.service

import com.coffee_service.quadro.org.model.Production
import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import java.net.URL
import io.ktor.server.application.ApplicationEnvironment.*

/*
* Define and implement singleton with high level use cases
* Full business logic allowed at this level
* */
object ProductionService {
    fun queryProduction(): List<Production> {
        // TODO: call network intensive task and update mutable state
        return listOf()
    }

    fun queryProduction(id: Int) {

    }

    fun storeProduction(data: Production) {

    }

    fun clearProduction() {

    }
}