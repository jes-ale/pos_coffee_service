package com.coffee_service.quadro.org.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
  val user: String,
  val password: String
)
