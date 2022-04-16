package com.sallet.cold.luna.key

import com.sallet.cold.luna.Binary
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class PublicKey(
    @Contextual val value: Binary,
    val type: String = "tendermint/PubKeySecp256k1",
)