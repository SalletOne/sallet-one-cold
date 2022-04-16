package com.sallet.cold.luna

import kotlinx.serialization.Serializable

@Serializable
data class TransactionEvent(
    val type: String,
    val attributes: List<Attribute>,
)