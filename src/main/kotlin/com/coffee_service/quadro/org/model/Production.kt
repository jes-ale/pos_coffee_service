package com.coffee_service.quadro.org.model

import kotlinx.serialization.Serializable

@Serializable
data class Production(
    val id: Int,
    val session_id: Int,
    val date_deadline: String,
    val date_finished: String,
    val display_name: String,
    val origin: String,
    val name: String,
    val picking_ids: List<Int>,
    val priority: String,
    val product_id: Int,
    val product_qty: Int,
    val product_tmpl_id: Int,
    val product_uom_id: Int,
    val product_variant_attributes: List<String>,
    val state: String,
    val workorder_ids: List<Int>
    // val user_id:
)
