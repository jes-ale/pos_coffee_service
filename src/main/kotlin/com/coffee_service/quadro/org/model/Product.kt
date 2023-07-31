package com.coffee_service.quadro.org.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

@Serializable
data class ProductPayload(
    val id: Int,
    val display_name: String,
		val categ: String,
		val pos_categ: String
    )


@Serializable
data class Product(
    val id: Int,
    val display_name: String,
		val categ_id: JsonArray,
		val pos_categ_id: JsonArray,
		val pos_production: Boolean
)
