package com.sallet.cold.luna

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(Uint128Serializer::class)

object Uint128Serializer : KSerializer<Uint128> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Uint128", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Uint128) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Uint128 = Uint128(decoder.decodeString())
}