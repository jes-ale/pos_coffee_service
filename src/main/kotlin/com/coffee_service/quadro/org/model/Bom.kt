package com.coffee_service.quadro.org.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

@Serializable
data class BomPayload(val id: Int, val bom_lines: List<BomLinePayload>, val product_id: Int)

@Serializable
data class BomLinePayload(
    val id: Int,
    val product_id: Int,
    val product_qty: Int,
    val uom: String,
    val bom_product_template_attribute_value_ids: List<Int>
)

@Serializable
data class Bom(
    val id: Int,
    val display_name: String,
    val bom_line_ids: List<Int>,
    val product_id: Int,
    val product_qty: Int,
    val product_tmpl_id: Int,
    val product_uom_id: JsonArray
)

@Serializable
data class BomLine(
    val id: Int,
    val display_name: String,
    val bom_id: JsonArray,
    val product_id: JsonArray,
    val product_qty: Int,
    val product_uom_id: JsonArray,
    val product_tmpl_id: Int,
    val bom_product_template_attribute_value_ids: List<Int>
)
