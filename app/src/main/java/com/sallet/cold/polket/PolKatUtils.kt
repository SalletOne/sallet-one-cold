package com.sallet.cold.polket

import com.sallet.cold.bean.Keypair
import com.sallet.cold.bean.Word
import com.sallet.cold.utils.Base58
import jp.co.soramitsu.fearless_utils.encrypt.Sr25519
import org.bouncycastle.jcajce.provider.digest.Blake2b
import org.spongycastle.crypto.digests.SHA512Digest
import org.spongycastle.crypto.generators.PKCS5S2ParametersGenerator
import org.spongycastle.crypto.params.KeyParameter
import java.text.Normalizer
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.math.floor

class PolKatUtils {


    private val PREFIX = "SS58PRE".toByteArray(Charsets.UTF_8)
    private val blake2bLock = Any()
    private val blake2b256 = Blake2b.Blake2b256()
    private val blake2b512 = Blake2b.Blake2b512()
    fun ByteArray.blake2b256() = withBlake2bLock { blake2b256.digest(this) }
    fun ByteArray.blake2b512() = withBlake2bLock { blake2b512.digest(this) }
    private inline fun <T> withBlake2bLock(action: () -> T) = synchronized(blake2bLock, action)
    private val base58 = Base58()




    fun encode(publicKey: ByteArray): String {
        val normalizedKey = if (publicKey.size > 32) {
            publicKey.blake2b256()
        } else {
            publicKey
        }
        val ident = 0.toShort() and 0b00111111_11111111
        val addressTypeByteArray = when (ident) {
            in 0..63 -> byteArrayOf(ident.toByte())
            in 64..127 -> {
                val first = (ident and 0b00000000_11111100).toInt() shr 2
                val second =
                    (ident.toInt() shr 8) or ((ident and 0b00000000_00000011).toInt() shl 6)
                byteArrayOf(first.toByte() or 0b01000000, second.toByte())
            }
            else -> throw IllegalArgumentException("Reserved for future address format extensions")
        }

        val hash = (PREFIX + addressTypeByteArray + normalizedKey).blake2b512()
        val checksum = hash.copyOfRange(0, PREFIX_SIZE)

        val resultByteArray = addressTypeByteArray + normalizedKey + checksum

        return base58.encode(resultByteArray)
    }

    companion object {
        private const val PREFIX_SIZE = 2
        private val DELIMITER_REGEX = "[\\s,]+".toRegex()
        private const val SEED_PREFIX = "mnemonic"
    }


    fun generateEntropy(mnemonic: String): ByteArray {
        val words = splitMnemonic(mnemonic)
        if (words.size % 3 != 0) {
            throw Exception()
        }

        val bits = words.map {
            val index=Word().words.indexOf(it)
            if (index == -1)
                throw Exception()
            lpad(index.toString(2), "0", 11)
        }.joinToString("")

        val dividerIndex = floor(bits.length.toDouble() / 33) * 32
        val entropyBits = bits.substring(0, dividerIndex.toInt())
        val checksumBits = bits.substring(dividerIndex.toInt())

        val entropyBytes = binaryStringToByteArray(entropyBits)

        if (entropyBytes.size < 16) {
            throw Exception()
        }

        if (entropyBytes.size > 32) {
            throw Exception()
        }

        if (entropyBytes.size % 4 != 0) {
            throw Exception()
        }

        val newChecksum = deriveChecksumBits(entropyBytes)

        if (newChecksum != checksumBits)
            throw Exception()

        return entropyBytes
    }

    private fun splitMnemonic(original: String): List<String> {
        val normalized = Normalizer.normalize(original, Normalizer.Form.NFKD)

        val startIndex = normalized.indexOfFirst { it.isLetter() }
        val endIndex = normalized.indexOfLast { it.isLetter() }

        return normalized.substring(startIndex, endIndex + 1)
            .split(DELIMITER_REGEX)
    }


    private fun lpad(str: String, padString: String, length: Int): String {
        var string = str

        while (string.length < length) {
            string = padString + string
        }

        return string
    }

    private fun binaryStringToByteArray(str: String): ByteArray {
        val byteArray = mutableListOf<Byte>()
        val tempStringBuilder = StringBuilder()

        str.forEach { c ->
            tempStringBuilder.append(c)

            val tempString = tempStringBuilder.toString()

            if (tempString.length == 8) {
                val temp = tempStringBuilder.toString()

                if (temp.isNotEmpty()) {
                    val tempInteger = Integer.parseInt(tempString, 2)
                    byteArray.add(tempInteger.toByte())
                }

                tempStringBuilder.clear()
            }
        }

        return byteArray.toByteArray()
    }

    private fun deriveChecksumBits(entropy: ByteArray): String {
        val ent = entropy.size * 8
        val cs = ent / 32
        val hash = Sha256.sha256(entropy)

        return bytesToBinaryString(hash).substring(0, cs)
    }

    private fun bytesToBinaryString(bytes: ByteArray): String {
        return bytes.toUByteArray().joinToString("") { x -> lpad(x.toString(2), "0", 8); }
    }

    fun generateSeed(entropy: ByteArray, passphrase: String): ByteArray {
        val generator = PKCS5S2ParametersGenerator(SHA512Digest())
        generator.init(
            entropy,
            Normalizer.normalize("$SEED_PREFIX$passphrase", Normalizer.Form.NFKD).toByteArray(),
            2048
        )
        val key = generator.generateDerivedMacParameters(512) as KeyParameter
        return key.key.copyOfRange(0, 32)
    }


    fun getPassword(path: String): String {
        if (path.contains("///")) {
            return path.substring(path.indexOf("///")).substring(3)
        }

        return ""
    }


    private fun decodeSr25519Keypair(bytes: ByteArray): Keypair {
        val privateKey = bytes.copyOfRange(0, 32)
        val nonce = bytes.copyOfRange(32, 64)
        val publicKey = bytes.copyOfRange(64, bytes.size)
        return Keypair(
            privateKey,
            publicKey,
            nonce
        )
    }

    private val junctionDecoder = JunctionDecoder()
    fun generate(seed: ByteArray, derivationPath: String = ""): Keypair {
        var previousKeypair =deriveSr25519MasterKeypair(seed)
        if (derivationPath.isNotEmpty()) {
            val junctions = junctionDecoder.decodeDerivationPath(derivationPath)
            var currentSeed = seed
            junctions.forEach {
                if (it.type == JunctionType.SOFT) {
                    deriveSr25519SoftKeypair(it.chaincode, previousKeypair)
                } else {
                    deriveSr25519HardKeypair(it.chaincode, previousKeypair)
                }
            }
            }
        return previousKeypair

    }

    private fun deriveSr25519SoftKeypair(chaincode: ByteArray, previousKeypair: Keypair): Keypair {
        val keypair = previousKeypair.privateKey + previousKeypair.nonce!! + previousKeypair.publicKey
        val newKeypairbytes = Sr25519.deriveKeypairSoft(keypair, chaincode)
        return decodeSr25519Keypair(newKeypairbytes)
    }

    private fun deriveSr25519HardKeypair(chaincode: ByteArray, previousKeypair: Keypair): Keypair {
        val keypair = previousKeypair.privateKey + previousKeypair.nonce!! + previousKeypair.publicKey
        val newKeypairbytes = Sr25519.deriveKeypairHard(keypair, chaincode)
        return decodeSr25519Keypair(newKeypairbytes)
    }
    private fun deriveSr25519MasterKeypair(seed: ByteArray): Keypair {
        val keypairBytes = Sr25519.keypairFromSeed(seed)
        return decodeSr25519Keypair(keypairBytes)
    }



}