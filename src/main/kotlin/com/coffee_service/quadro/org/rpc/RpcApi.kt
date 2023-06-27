package com.coffee_service.quadro.org.rpc

import com.coffee_service.quadro.org.model.*
import kotlinx.serialization.json.*
import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import java.net.URL

object RpcApi {
    private val client = XmlRpcClient()
    private val common_config = XmlRpcClientConfigImpl()
    private val models_config = XmlRpcClientConfigImpl()
    private var uid = 0
    private var db = ""
    private var password = ""
    fun version(host: String, port: String): Any? {
        return runCatching {
            common_config.serverURL = URL("http://$host:$port/xmlrpc/2/common")
            models_config.serverURL = URL("http://$host:$port/xmlrpc/2/object")
            client.execute(common_config, "version", listOf<Any>())
        }.getOrNull()
    }

    private inline fun <reified T> kwQuery(
        pMethodName: String, model: String, kw: String, domain: Map<String, Any>, params: List<Any>
    ): List<T> {
        return (client.execute(
            models_config, pMethodName, listOf(
                this.db,
                this.uid,
                this.password,
                model,
                kw,
                params,
                domain,
            )
        ) as Array<*>).map {
            Json.decodeFromJsonElement<T>(
                it.toJsonElement()
            )
        }
    }

    fun login(username: String, password: String, database: String) {
        this.db = database
        this.password = password
        this.uid = client.execute(
            common_config, "authenticate", listOf(database, username, password, listOf<Any>())
        ) as Int
    }

    fun markAsDone(id: Int): List<Boolean> {
        val domain = mutableMapOf<String, Any>()
        return kwQuery<Boolean>(
            pMethodName = "execute_kw",
            model = "mrp.production",
            kw = "markAsDone",
            domain = domain.toMap(),
            params = listOf(id)
        )
    }

    private fun queryStockMove(ids: List<Int>): List<StockMove> {
        val domain = mutableMapOf<String, Any>()
        domain["fields"] = listOf(
            "id", "product_id", "product_uom_qty"
        )
        return kwQuery<StockMove>(
            pMethodName = "execute_kw",
            model = "stock.move",
            kw = "read",
            domain = domain.toMap(),
            params = listOf(ids)
        )
    }

    fun queryProduction(): List<ProductionPayload> {
        val domain = mutableMapOf<String, Any>()
        domain["fields"] = listOf(
            "id",
            "date_deadline",
            "date_finished",
            "display_name",
            "origin",
            "name",
            "priority",
            "product_qty",
            "state",
            "product_id",
            "move_raw_ids"
        )// TODO: generate field list based on serializable Model fields
        domain["limit"] = 5 // TODO: allow customize limit by user interface
        val payload = kwQuery<Production>(
            pMethodName = "execute_kw",
            model = "mrp.production",
            kw = "search_read",
            domain = domain.toMap(),
            params = listOf(listOf(listOf("state", "=", "confirmed"))),
        )
        val body = mutableListOf<ProductionPayload>()
        for (production in payload) {
            val productId = Json.decodeFromJsonElement<Int>(production.product_id[0])
            val productDisplayName = Json.decodeFromJsonElement<String>(production.product_id[1])
            val rawStockMoveIds = Json.decodeFromJsonElement<List<Int>>(production.move_raw_ids)
            val rawStockMove = queryStockMove(rawStockMoveIds)
            val components = mutableListOf<ComponentPayload>()
            for (comp in rawStockMove) {
                components.add(
                    ComponentPayload(
                        display_name = Json.decodeFromJsonElement<String>(comp.product_id[1]),
                        qty = comp.product_uom_qty
                    )
                )
            }
            body.add(
                ProductionPayload(
                    id = production.id,
                    display_name = production.display_name,
                    origin = production.origin,
                    priority = production.priority,
                    state = production.state,
                    product = ProductPayload(
                        id = productId,
                        display_name = productDisplayName,
                    ),
                    component = components,
                )
            )
        }
        return body
    }

    /**
     * https://github.com/Kotlin/kotlinx.serialization/issues/746#issuecomment-737000705
     */
    private fun Any?.toJsonElement(): JsonElement {
        return when (this) {
            is Number -> JsonPrimitive(this)
            is Boolean -> JsonPrimitive(this)
            is String -> JsonPrimitive(this)
            is Array<*> -> this.toJsonArray()
            is List<*> -> this.toJsonArray()
            is Map<*, *> -> this.toJsonObject()
            is JsonElement -> this
            else -> JsonNull
        }
    }

    private fun Array<*>.toJsonArray(): JsonArray {
        val array = mutableListOf<JsonElement>()
        this.forEach { array.add(it.toJsonElement()) }
        return JsonArray(array)
    }

    private fun List<*>.toJsonArray(): JsonArray {
        val array = mutableListOf<JsonElement>()
        this.forEach { array.add(it.toJsonElement()) }
        return JsonArray(array)
    }

    private fun Map<*, *>.toJsonObject(): JsonObject {
        val map = mutableMapOf<String, JsonElement>()
        this.forEach {
            if (it.key is String) {
                map[it.key as String] = it.value.toJsonElement()
            }
        }
        return JsonObject(map)
    }
}