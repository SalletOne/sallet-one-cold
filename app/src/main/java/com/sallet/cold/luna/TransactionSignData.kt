package com.sallet.cold.luna

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.LongAsStringSerializer


interface TransactionSigner {

    suspend fun sign(wallet: TerraWallet, data: TransactionSignData, transaction: Transaction): Signature
}

@Serializable
data class TransactionSignData(
    @SerialName("chain_id") val chainId: String,
    @SerialName("account_number") @Serializable(LongAsStringSerializer::class) val accountNumber: Long,
    @Serializable(LongAsStringSerializer::class) val sequence: Long,
    val fee: Fee,
    @SerialName("msgs") val messages: List<@Contextual Message>,
    val memo: String,
) {

    constructor(
        transaction: Transaction,
        chainId: String,
        accountNumber: Long,
        sequence: Long,
    ) : this(
        chainId,
        accountNumber,
        sequence,
        transaction.fee!!,
        transaction.messages,
        transaction.memo,
    )
}