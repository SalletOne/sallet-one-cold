package com.sallet.cold.luna

import org.bouncycastle.crypto.digests.SHA256Digest
import org.bouncycastle.crypto.params.ECDomainParameters
import org.bouncycastle.crypto.params.ECPublicKeyParameters
import org.bouncycastle.crypto.signers.ECDSASigner
import org.bouncycastle.crypto.signers.HMacDSAKCalculator
import org.web3j.crypto.Bip32ECKeyPair
import org.web3j.crypto.Sign
import java.math.BigInteger

 object Bip32 {

     fun keyPair(seed: ByteArray, hdPath: IntArray): Bip32KeyPair {
        val masterKey = Bip32ECKeyPair.generateKeyPair(seed)
        val terraHD = Bip32ECKeyPair.deriveKeyPair(masterKey, hdPath)
        val privateKey = terraHD.privateKeyBytes33
        val publicKey = terraHD.publicKeyPoint.getEncoded(true)

        return Bip32KeyPair(publicKey, privateKey)
    }

     fun publicKeyFor(privateKey: ByteArray): ByteArray {
        return Sign.publicPointFromPrivate(BigInteger(1, privateKey)).getEncoded(true)
    }

     fun sign(messageHash: ByteArray, privateKey: ByteArray): ByteArray {
        val keyPair = Bip32ECKeyPair.create(privateKey)

        val signature = Sign.signMessage(messageHash, keyPair, false)

        return concatRandS(signature.r, signature.s)
    }

    private val CURVE = ECDomainParameters(
        Sign.CURVE_PARAMS.curve,
        Sign.CURVE_PARAMS.g,
        Sign.CURVE_PARAMS.n,
        Sign.CURVE_PARAMS.h
    )

     fun verify(messageHash: ByteArray, publicKey: ByteArray, signature: ByteArray): Boolean {
        val (r, s) = splitRandS(signature)
        val signer = ECDSASigner(HMacDSAKCalculator(SHA256Digest()))
        val publicKeyParameters = ECPublicKeyParameters(CURVE.curve.decodePoint(publicKey), CURVE)
        signer.init(false, publicKeyParameters)

        return signer.verifySignature(messageHash, BigInteger(1, r), BigInteger(1, s))
    }

     fun recoverPublicKey(messageHash: ByteArray, signature: ByteArray): ByteArray {
        val (r, s) = splitRandS(signature)

        return Sign.signedMessageToKey(messageHash, Sign.SignatureData(byteArrayOf(), r, s)).toByteArray()
    }
     internal fun splitRandS(bytes: ByteArray): Pair<ByteArray, ByteArray> {
         return bytes.sliceArray(0 until 32) to bytes.sliceArray(32 until bytes.size)
     }
     internal fun concatRandS(r: ByteArray, s: ByteArray): ByteArray {
         var index = 0
         val start = if (r.size > 32) r.size - 32 else 0

         val result = ByteArray(r.size + s.size - start)

         for (i in start until r.size) {
             result[index++] = r[i]
         }

         for (i in s.indices) {
             result[index++] = s[i]
         }

         return result
     }
}

data class Bip32KeyPair(
    val publicKey: ByteArray,
    val privateKey: ByteArray
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Bip32KeyPair

        if (!publicKey.contentEquals(other.publicKey)) return false
        if (!privateKey.contentEquals(other.privateKey)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = publicKey.contentHashCode()
        result = 31 * result + privateKey.contentHashCode()
        return result
    }
}