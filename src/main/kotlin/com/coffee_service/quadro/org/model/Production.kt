package com.coffee_service.quadro.org.model

import kotlinx.serialization.Serializable

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
    //val product_id: Int,
    //val picking_ids: List<Int>,
    //val product_tmpl_id: Int,
    //val product_variant_attributes: List<String>,
    //val workorder_ids: List<Int>,
    //val product_uom_id: Int,
    // val user_id:
)
