package com.coffee_service.quadro.org.model

import kotlinx.serialization.Serializable

@Serializable
data class Order (
    val id: Int,
    val product_id: Int,
    val options: ProductOptions,
    val components: List<Components>,
    val bom_id: Int?,
)

@Serializable
data class Components (
    val product_id: Int,
    val qty: Int
)
@Serializable
data class ProductOptions(
    val draftPackLotLines: Nothing? = null,
    val quantity: Int,
    val price_extra: Float,
    val description: String
)