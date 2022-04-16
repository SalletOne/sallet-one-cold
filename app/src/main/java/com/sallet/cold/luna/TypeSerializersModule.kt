package com.sallet.cold.luna

import kotlinx.serialization.modules.SerializersModule


val TypeSerializersModule = SerializersModule {
    contextual(Message::class, PolymorphicObjectSerializer(Message::class))
    contextual(EnumMessage::class, PolymorphicKeyValueSerializer(EnumMessage::class))
    contextual(Binary::class, Base64BinarySerializer)
}