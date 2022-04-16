package com.sallet.cold.luna

import org.web3j.crypto.MnemonicUtils
import java.security.SecureRandom
import java.util.*

 object Mnemonic {

    private var random: Random = SecureRandom()

     fun generate(): String = ByteArray(32)
        .apply { random.nextBytes(this) }
        .let { MnemonicUtils.generateMnemonic(it) }

     fun seedFrom(mnemonic: String): ByteArray {
        return MnemonicUtils.generateSeed(mnemonic,null)
    }

    fun random(random: Random) {
        Mnemonic.random = random
    }
}