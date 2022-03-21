//package com.sallet.cold.utils
////import wallet.core.jni.*
//import android.os.Bundle
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import com.google.protobuf.ByteString
//import com.sallet.cold.R
//import wallet.core.jni.CoinType
//import wallet.core.jni.HDWallet
//import wallet.core.java.AnySigner
//import wallet.core.jni.proto.Ethereum
//import wallet.core.jni.BitcoinScript
//import wallet.core.jni.BitcoinSigHashType
//import wallet.core.jni.proto.Bitcoin
//import wallet.core.jni.proto.Common
//import wallet.core.jni.proto.Ripple
//import java.math.BigInteger
//import kotlin.experimental.and
//
//class JniWallet {
//
//    init {
//        System.loadLibrary("TrustWalletCore")
//    }
//
//    private val seedPhrase = "width fortune hammer use tortoise phrase laugh atom jump retreat assault violin"
//    private val passphrase = ""
//
//    fun xrpAddress(mem:String ) :String  {
//
//
//        // 'Import' a wallet
//        val wallet = HDWallet(seedPhrase, passphrase)
//
//        val coin: CoinType = CoinType.SOLANA
//        // Get the default address
//        val address = wallet.getAddressForCoin(coin)
//
//
//        return  address
//    }
//
//
//    fun xrpSign(mem:String,amount:Long,fee:Long,sequence:Int,lastLedgerSequence:Int,
//                account:String,destination:String ,destinationTag:Long,flags:Long ) :String  {
//
//
//        // 'Import' a wallet
//        val wallet = HDWallet(mem, passphrase)
//
//        val coin: CoinType = CoinType.XRP
//        // Get the default address
//        val address = wallet.getAddressForCoin(coin)
//
//        val secretPrivateKey = wallet.getKeyForCoin(coin)
//
//        val signerInput = Ripple.SigningInput.newBuilder().apply {
//            this.amount = amount
//            this.fee = fee
//            this.sequence = sequence
//            this.lastLedgerSequence = lastLedgerSequence
//            this.account = account    // decimal 21000
//            this.destination = destination     // decimal 21000
//            this.privateKey = ByteString.copyFrom(secretPrivateKey.data())    // decimal 21000
//        }.build()
//        val signerOutput = AnySigner.sign(signerInput, CoinType.XRP, Ripple.SigningOutput.parser())
//
//        return  signerOutput.encoded.toByteArray().toHexString(false)
//    }
//
//
//    private fun ByteArray.toHexString(withPrefix: Boolean = true): String {
//        val stringBuilder = StringBuilder()
//        if(withPrefix) {
//            stringBuilder.append("0x")
//        }
//        for (element in this) {
//            stringBuilder.append(String.format("%02x", element and 0xFF.toByte()))
//        }
//        return stringBuilder.toString()
//    }
//
//    private fun BigInteger.toByteString(): ByteString {
//        return ByteString.copyFrom(this.toByteArray())
//    }
//
//    private fun String.hexStringToByteArray() : ByteArray {
//        val HEX_CHARS = "0123456789ABCDEF"
//        val result = ByteArray(length / 2)
//        for (i in 0 until length step 2) {
//            val firstIndex = HEX_CHARS.indexOf(this[i].toUpperCase());
//            val secondIndex = HEX_CHARS.indexOf(this[i + 1].toUpperCase());
//            val octet = firstIndex.shl(4).or(secondIndex)
//            result.set(i.shr(1), octet.toByte())
//        }
//        return result
//    }
//
//
//    fun test(){
//        // 'Import' a wallet
//        val wallet = HDWallet(seedPhrase, passphrase)
//        showLog("Mnemonic: \n${wallet.mnemonic()}")
//
//        // Ethereum example
//        val coinEth: CoinType = CoinType.SOLANA
//        // Get the default address
//        val addressEth = wallet.getAddressForCoin(coinEth)
//        showLog("Default ETH address: \n$addressEth")
//
//        // Signing a transaction (using EthereumSigner)
//        val secretPrivateKey = wallet.getKeyForCoin(coinEth)
//        val dummyReceiverAddress = "0xC37054b3b48C3317082E7ba872d7753D13da4986"
//
//        val signerInput = Ethereum.SigningInput.newBuilder().apply {
//            this.chainId = ByteString.copyFrom(BigInteger("01").toByteArray())
//            this.gasPrice = BigInteger("d693a400", 16).toByteString() // decimal 3600000000
//            this.gasLimit = BigInteger("5208", 16).toByteString()     // decimal 21000
//            this.toAddress = dummyReceiverAddress
//            this.transaction = Ethereum.Transaction.newBuilder().apply {
//                this.transfer = Ethereum.Transaction.Transfer.newBuilder().apply {
//                    this.amount = BigInteger("0348bca5a16000", 16).toByteString()
//                }.build()
//            }.build()
//            this.privateKey = ByteString.copyFrom(secretPrivateKey.data())
//        }.build()
//        val signerOutput = AnySigner.sign(signerInput, CoinType.ETHEREUM, Ethereum.SigningOutput.parser())
//        showLog("Signed transaction: \n${signerOutput.encoded.toByteArray().toHexString(false)}")
//
//        // Bitcoin example
//        val coinBtc: CoinType = CoinType.BITCOIN
//        // Get the default address
//        val addressBtc = wallet.getAddressForCoin(coinBtc)
//        showLog("Default BTC address: \n$addressBtc")
//
//        // Build a transaction
//        val utxoTxId = "050d00e2e18ef13969606f1ceee290d3f49bd940684ce39898159352952b8ce2".hexStringToByteArray();
//        val secretPrivateKeyBtc = wallet.getKeyForCoin(coinBtc)
//        val toAddress = "1Bp9U1ogV3A14FMvKbRJms7ctyso4Z4Tcx"
//        val changeAddress = "1FQc5LdgGHMHEN9nwkjmz6tWkxhPpxBvBU"
//        val script = BitcoinScript.lockScriptForAddress(addressBtc, coinBtc).data()
//
//        val outPoint = Bitcoin.OutPoint.newBuilder().apply {
//            this.hash = ByteString.copyFrom(utxoTxId)
//            this.index = 2
//        }.build()
//        val utxo = Bitcoin.UnspentTransaction.newBuilder().apply {
//            this.amount = 5151
//            this.outPoint = outPoint
//            this.script = ByteString.copyFrom(script)
//        }.build()
//        val input = Bitcoin.SigningInput.newBuilder().apply {
//            this.amount = 600
//            this.hashType = BitcoinSigHashType.ALL.value()
//            this.toAddress = toAddress
//            this.changeAddress = changeAddress
//            this.byteFee = 2
//            this.coinType = coinBtc.value()
//            this.addUtxo(utxo)
//            this.addPrivateKey(ByteString.copyFrom(secretPrivateKeyBtc.data()))
//        }
//
//        // Calculate fee (plan a tranaction)
//        val plan = AnySigner.plan(input.build(), coinBtc, Bitcoin.TransactionPlan.parser())
//        showLog("Planned fee:  ${plan.fee}  amount: ${plan.amount}  avail_amount: ${plan.availableAmount}  change: ${plan.change}")
//
//        // Set the precomputed plan
//        input.plan = plan
//        input.amount = plan.amount
//
//        val output = AnySigner.sign(input.build(), coinBtc, Bitcoin.SigningOutput.parser())
//
//        assert(output.error == Common.SigningError.OK)
//        val signedTransaction = output.encoded?.toByteArray()
//        showLog("Signed BTC transaction: \n${signedTransaction?.toHexString()}")
//    }
//
//    private fun showLog(log: String) {
//    }
//
//
//
//}