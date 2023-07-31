package com.coffee_service.quadro.org.model

import kotlinx.serialization.Serializable

@Serializable data class Bom(id: Int, bom_lines: List<BomLine>, product_id: Int)

@Serializable
data class BomLine(
    id: Int,
    product_id: Int,
    product_qty: Int,
    product_uom_id: Int,
    bom_product_template_attribute_value_ids: List<Int>
)
