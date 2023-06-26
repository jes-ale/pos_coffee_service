package com.coffee_service.quadro.org.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

@Serializable
data class Production(
    val id: Int,
    val date_deadline: Boolean,
    val date_finished: Boolean,
    val display_name: String,
    val origin: String,
    val name: String,
    val priority: String,
    val product_qty: Double,
    val state: String,
    val product_id: JsonArray,
    val move_raw_ids: JsonArray,
    //val workorder_ids: JsonArray
    //val picking_ids: List<Int>,
    //val product_tmpl_id: Int,
    //val product_uom_id: Int,
    // val user_id:
)
@Serializable
data class Product(
    val id: Int,
    val name: String,
)