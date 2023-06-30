package com.coffee_service.quadro.org.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

@Serializable
data class ProductionPayload(
    val id: Int,
    val display_name: String,
    val origin: String,
    val priority: String,
    val state: String,
    val product: ProductPayload,
    val component: List<ComponentPayload>
)

@Serializable data class ProductPayload(val id: Int, val display_name: String)

@Serializable data class ComponentPayload(val display_name: String, val qty: Double)

@Serializable
data class Production(
    val id: Int,
    val date_deadline: Boolean,
    val date_finished: Boolean,
    val display_name: String,
    val origin: String?,
    val name: String,
    val priority: String,
    val product_qty: Double,
    val state: String,
    val product_id: JsonArray,
    val move_raw_ids: JsonArray
)

@Serializable
data class StockMove(val id: Int, val product_id: JsonArray, val product_uom_qty: Double)
