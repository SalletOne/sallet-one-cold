package com.sallet.cold.polket

class SigningData(
    val publicKey: ByteArray,
    val privateKey: ByteArray,
    val nonce: ByteArray? = null
)
