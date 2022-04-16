package com.sallet.cold.luna

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionLog(
    val events: List<TransactionEvent>,
    @SerialName("msg_index") val index: UInt? = null,
    val log: String? = null,
)