//package com.sallet.cold.luna.base
//import kotlinx.serialization.Serializable
//import com.sallet.cold.luna.Fee
//import com.sallet.cold.luna.Message
//import com.sallet.cold.luna.Transaction
//
//@kotlinx.serialization.Serializable
//data class TransactionSignData public constructor(chainId: kotlin.String, accountNumber: kotlin.Long, sequence: kotlin.Long, fee: Fee, messages: kotlin.collections.List<@kotlinx.serialization.Contextual Message>, memo: kotlin.String) {
//    public companion object {
//    }
//
//    public constructor(transaction: Transaction, chainId: kotlin.String, accountNumber: kotlin.Long, sequence: kotlin.Long) { /* compiled code */ }
//
//    @Deprecated public constructor(seen1: kotlin.Int, @kotlinx.serialization.SerialName chainId: kotlin.String?, @kotlinx.serialization.SerialName @kotlinx.serialization.Serializable accountNumber: kotlin.Long, @kotlinx.serialization.Serializable sequence: kotlin.Long, fee: Fee?, @kotlinx.serialization.SerialName messages: kotlin.collections.List<@kotlinx.serialization.Contextual Message>?, memo: kotlin.String?, serializationConstructorMarker: kotlinx.serialization.internal.SerializationConstructorMarker?) { /* compiled code */ }
//
//    @kotlinx.serialization.SerialName @kotlinx.serialization.Serializable public final val accountNumber: kotlin.Long /* compiled code */
//
//    @kotlinx.serialization.SerialName public final val chainId: kotlin.String /* compiled code */
//
//    public final val fee: Fee /* compiled code */
//
//    public final val memo: kotlin.String /* compiled code */
//
//    @kotlinx.serialization.SerialName public final val messages: kotlin.collections.List<@kotlinx.serialization.Contextual Message> /* compiled code */
//
//    @kotlinx.serialization.Serializable public final val sequence: kotlin.Long /* compiled code */
//
//    public final operator fun component1(): kotlin.String { /* compiled code */ }
//
//    public final operator fun component2(): kotlin.Long { /* compiled code */ }
//
//    public final operator fun component3(): kotlin.Long { /* compiled code */ }
//
//    public final operator fun component4(): Fee { /* compiled code */ }
//
//    public final operator fun component5(): kotlin.collections.List<@kotlinx.serialization.Contextual Message> { /* compiled code */ }
//
//    public final operator fun component6(): kotlin.String { /* compiled code */ }
//
//    @kotlin.Deprecated public object `$serializer` : kotlinx.serialization.internal.GeneratedSerializer<TransactionSignData> {
//    }
//}