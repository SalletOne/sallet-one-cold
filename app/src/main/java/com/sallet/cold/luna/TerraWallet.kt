package com.sallet.cold.luna

import com.sallet.cold.luna.MnemonicKey.Companion.COIN_TYPE
import com.sallet.cold.luna.bitcoinj.Bech32
import com.sallet.cold.luna.bitcoinj.Ripemd160
import kr.jadekim.common.encoder.HEX
import kr.jadekim.common.encoder.decodeHex
import kr.jadekim.common.hash.SHA_256

import org.web3j.crypto.Hash
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmOverloads

interface TerraWallet {

    val address: String
    val key: Key?

    companion object {

        @JvmStatic
        @JvmOverloads
        fun create(account: Int = 0, index: Int = 0): Pair<TerraWallet, MnemonicKey> {
            val key = MnemonicKey.create(account, index)

            return TerraWallet(key) to key
        }

        @JvmStatic
        @JvmOverloads
        fun fromMnemonic(
            mnemonic: String,
            account: Int = 0,
            index: Int = 0,
            coinType: Int = COIN_TYPE,
        ) = TerraWallet(MnemonicKey(mnemonic, account, index, coinType))

        @JvmStatic
        @JvmOverloads
        fun fromRawKey(privateKey: ByteArray, publicKey: ByteArray? = null) = TerraWallet(RawKey(privateKey, publicKey))

        @JvmStatic
        @JvmOverloads
        fun fromRawKey(
            privateKey: String,
            publicKey: String? = null,
        ) = fromRawKey(privateKey.decodeHex(), publicKey?.decodeHex())

        @JvmStatic
        fun isValidAddress(address: String): Boolean = try {
            val (hrp, _) = decode(address)
            hrp == Bech32Hrp.ACCOUNT
        } catch (e: Exception) {
            false
        }
    }
}

fun TerraWallet(address: String): TerraWallet = TerraWalletImpl(address)

fun TerraWallet(publicKey: ByteArray): TerraWallet = TerraWalletImpl(
    PublicKey(
        publicKey
    )
)

fun TerraWallet(key: Key): TerraWallet = TerraWalletImpl(key)

class TerraWalletImpl private constructor(override val address: String, override val key: Key?) : TerraWallet {

    constructor(address: String) : this(address, null)

    constructor(key: Key) : this(key.accountAddress, key)

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is TerraWallet) {
            return false
        }

        return address == other.address
    }

    override fun hashCode(): Int {
        return address.hashCode()
    }
}

val Key.accountAddress: String
    get() {
        val hashed = hash(SHA_256.hash(this.publicKey))

        return encode(Bech32Hrp.ACCOUNT, toWords(hashed))
    }

val Key.accountPublicKey: String
    get() {
        val data = BECH32_PUBLIC_KEY_DATA_PREFIX + this.publicKey

        return encode(Bech32Hrp.ACCOUNT_PUBLIC_KEY, toWords(data))
    }

internal val BECH32_PUBLIC_KEY_DATA_PREFIX = HEX.decode("eb5ae98721")


 fun encode(hrp: Bech32Hrp, data: ByteArray): String = Bech32.encode(hrp.value, data)

 fun toWords(data: ByteArray): ByteArray = Bech32.toWords(data)

 fun decode(str: String): Pair<Bech32Hrp, ByteArray> {
    val result = Bech32.decode(str)
    val hrp = Bech32Hrp.fromHrp(result.hrp) ?: throw IllegalArgumentException("Unknown hrp ${result.hrp}")

    return hrp to result.data
}

fun hash(data: ByteArray): ByteArray = Ripemd160.getHash(data)

enum class Bech32Hrp(val value: String) {
    ACCOUNT("terra"),
    ACCOUNT_PUBLIC_KEY("terrapub"),
    VALIDATOR_OPERATOR("terravaloper"),
    VALIDATOR_OPERATOR_PUBLIC_KEY("terravaloperpub"),
    CONSENSUS_NODE("terravalcons"),
    CONSENSUS_NODE_PUBLIC_KEY("terravalconspub");

    companion object {

        @JvmStatic
        fun fromHrp(hrp: String): Bech32Hrp? = values().firstOrNull { it.value.equals(hrp, true) }
    }
}