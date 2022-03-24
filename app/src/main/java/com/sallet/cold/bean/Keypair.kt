package com.sallet.cold.bean

class Keypair (
    val privateKey: ByteArray,// private key
    val publicKey: ByteArray,// public key
    val nonce: ByteArray? = null
)

