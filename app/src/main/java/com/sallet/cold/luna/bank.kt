package com.sallet.cold.luna

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendMessage(
    @SerialName("from_address") val fromAddress: String,
    @SerialName("to_address") val toAddress: String,
    val amount: List<Coin>,
) : Message()

@Serializable
data class MultipleSendMessage(
    val inputs: List<Amount>,
    val outputs: List<Amount>,
) : Message(){

    @Serializable
    data class Amount(
        val address: String,
        val coins: List<Coin>,
    )
}