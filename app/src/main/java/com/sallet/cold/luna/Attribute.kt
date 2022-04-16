package com.sallet.cold.luna

import kotlinx.serialization.Serializable

@Serializable
data class Attribute(
    val key: String,
    val value: String,
)