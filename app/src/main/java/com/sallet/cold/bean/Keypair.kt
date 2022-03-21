package com.sallet.cold.bean

class Keypair (
    val privateKey: ByteArray,//私钥 private key
    val publicKey: ByteArray,//公钥 public key
    val nonce: ByteArray? = null
)

