package com.sallet.cold.luna

class Binary(val data: ByteArray)

@Suppress("NOTHING_TO_INLINE")
inline fun ByteArray.toBinary() = Binary(this)
