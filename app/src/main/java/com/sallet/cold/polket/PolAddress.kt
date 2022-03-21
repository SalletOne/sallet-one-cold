package com.sallet.cold.polket

import android.database.sqlite.SQLiteConstraintException
import com.sallet.cold.bean.Keypair
import jp.co.soramitsu.fearless_utils.encrypt.Sr25519
import org.bouncycastle.util.encoders.Hex

class PolAddress (private val bip39: PolKatUtils){

      fun saveFromMnemonic(
        mnemonic: String,
        derivationPath: String
    ): String {
            val entropy = bip39.generateEntropy(mnemonic)
            val password = bip39.getPassword(derivationPath)
            val seed = bip39.generateSeed(entropy, password)
            val keys = bip39.generate(seed, derivationPath)
            val address = bip39.encode(keys.publicKey)
            return address
    }


    private fun sign(statement: String, mnemonic: String,
                     derivationPath: String){
        val entropy = bip39.generateEntropy(mnemonic)
        val password = bip39.getPassword(derivationPath)
        val seed = bip39.generateSeed(entropy, password)
        val keys = bip39.generate(seed, derivationPath)
        signSr25519(statement,keys)
    }



    private fun signSr25519(statement: String, keypair: Keypair): SignatureWrapper {

        require(keypair.nonce != null)

        val sign = Sr25519.sign(keypair.publicKey, keypair.privateKey + keypair.nonce, statement.toByteArray())

        return SignatureWrapper.Other(signature = sign)
    }



}