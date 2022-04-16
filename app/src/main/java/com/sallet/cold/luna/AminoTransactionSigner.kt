package com.sallet.cold.luna

import kotlinx.serialization.json.*


object AminoTransactionSigner : TransactionSigner {

     suspend fun sign(
        wallet: TerraWallet,
        data: TransactionSignData,
    ): Signature {
        val key = wallet.key ?: throw IllegalArgumentException("${wallet.address} wallet have not key")
        val signature = key.sign(serializeSignData(data)).await()

        return Signature(signature.toBinary(), com.sallet.cold.luna.key.PublicKey(key.publicKey.toBinary()), data.accountNumber, data.sequence)
    }

    override suspend fun sign(
        wallet: TerraWallet,
        data: TransactionSignData,
        transaction: Transaction,
    ): Signature = sign(wallet, data)

    fun serializeSignData(data: TransactionSignData): String {
        return AminoFormat.encodeToJsonElement(TransactionSignData.serializer(), data)
            .sorted()
            .let { AminoFormat.encodeToString(JsonElement.serializer(), it) }
    }


    fun serializeSignData(data: Transaction): String {
        return AminoFormat.encodeToJsonElement(Transaction.serializer(), data)
            .sorted()
            .let { AminoFormat.encodeToString(JsonElement.serializer(), it) }
    }

    fun serializeSignData(data: Signature): String {
        return AminoFormat.encodeToJsonElement(Signature.serializer(), data)
            .sorted()
            .let { AminoFormat.encodeToString(JsonElement.serializer(), it) }
    }

    private fun JsonElement.sorted(): JsonElement {
        return when (this) {
            is JsonPrimitive -> this
            JsonNull -> this
            is JsonObject -> {
                val sortedMap = LinkedHashMap<String, JsonElement>()

                keys.sorted()
                    .forEach { key -> sortedMap[key] = getValue(key).sorted() }

                JsonObject(sortedMap)
            }
            is JsonArray -> JsonArray(map { it.sorted() })
        }
    }


}