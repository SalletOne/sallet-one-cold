package com.sallet.cold.luna

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


@Serializable
object DecimalSerializer : KSerializer<Decimal> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Decimal", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Decimal) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Decimal = Decimal(decoder.decodeString())
}
