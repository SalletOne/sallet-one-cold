package com.sallet.cold.polket

import com.sallet.cold.bean.Keypair

fun mapSigningDataToKeypair(singingData: SigningData): Keypair {
    return with(singingData) {
        Keypair(
            publicKey = publicKey,
            privateKey = privateKey,
            nonce = nonce
        )
    }
}

fun mapKeyPairToSigningData(keyPair: Keypair): SigningData {
    return with(keyPair) {
        SigningData(
            publicKey = publicKey,
            privateKey = privateKey,
            nonce = nonce
        )
    }
}
