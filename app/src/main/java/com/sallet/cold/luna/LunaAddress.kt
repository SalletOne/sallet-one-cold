package com.sallet.cold.luna


import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.sallet.cold.App

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


class LunaAddress (){


      var chainId= "columbus-5"
//      var chainId= "bombay-12"






     fun getAddress(mnemonic:String) :String{

             val wallet: TerraWallet = walletFromMnemonic(mnemonic)
             return wallet.address

    }

//    fun sign(){
////         val wallet: TerraWallet = TerraWallet(mnemonic)
//        val wallet: TerraWallet = TerraWallet.fromRawKey("fb77996acc5b18b10a90927b2cf0397d327bdbaad9b0815d7a9320a8e668d035"
//        ,"Aqb8JPZaGqOBXBIm5IBlEYGURzA2f9Vk6p5UzeQbtUHp")
////         val fee =Fee(200000, listOf(Coin(Uint128("120000"), "uluna")))
////         val message=SendMessage(wallet.address, receiveWallet, listOf(Coin(Uint128("200000"), "uluna")))
//
//        val feeCoin=Coin(Uint128("1400"), "uluna")
//        val messageCoin=Coin(Uint128("50000"), "uluna")
//        val fee=Fee(101000, listOf(feeCoin))
//        val message= SendMessage(wallet.address, "terra1uv5acl0xnckxd76s630h5erm8l8nuw9hq6gr2s", listOf(messageCoin))
//        runBlocking {
//            launch {
//                val data: String
//                val signData = TransactionSignData(
//                    chainId = chainId,
//                    accountNumber = 3725118,
//                    sequence = 1,
//                    fee = fee,
//                    messages = listOf(message),
//                    memo = "",
//                )
//
//
//
//                val signature: Signature = AminoTransactionSigner.sign(wallet, signData)
//
//                val transaction: Transaction = Transaction.builder()
//                    .fee(fee)
//                    .message(message)
//                    .build()
//
//                transaction.signatures = listOf(signature)
//
//                data = AminoFormat.encodeToString(
//                    BroadcastSyncRequest.serializer(),
//                    BroadcastSyncRequest(transaction)
//                )
//                println(data)
//
//            }
//        }
//
//
//
//
//
//
//
//
//
//    }




     fun sign(signBack: SignBack, mnemonic:String, receiveWallet:String,
              gasAmount: Long, feeAmount: String, denomination: String, transactionAmount:String
              , accountNumber:Long, sequence:Long){
         val wallet: TerraWallet = walletFromMnemonic(mnemonic)
//         val fee =Fee(200000, listOf(Coin(Uint128("120000"), "uluna")))
//         val message=SendMessage(wallet.address, receiveWallet, listOf(Coin(Uint128("200000"), "uluna")))

         val feeCoin=Coin(Uint128(feeAmount), denomination)
         val messageCoin=Coin(Uint128(transactionAmount), denomination)
         val fee=Fee(gasAmount, listOf(feeCoin))
         val message= SendMessage(wallet.address, receiveWallet, listOf(messageCoin))
         runBlocking {
             launch {
                 val data: String
                 val signData = TransactionSignData(
                     chainId = chainId,
                     accountNumber = accountNumber,
                     sequence = sequence,
                     fee = fee,
                     messages = listOf(message),
                     memo = "",
                 )



                 val signature: Signature = AminoTransactionSigner.sign(wallet, signData)

                 val transaction: Transaction = Transaction.builder()
                     .fee(fee)
                     .message(message)
                     .build()

                 transaction.signatures = listOf(signature)

                 data = AminoFormat.encodeToString(
                     Signature.serializer(),
                     signature
                 )
                 println(data)



                 signBack.onSignBack(data)
             }
         }









    }









    @Serializable
    class BroadcastSyncRequest(
        @SerialName("tx") override val transaction: Transaction,
    ) : BroadcastRequest {
        override val mode = "sync"
    }

    interface BroadcastRequest {
        val transaction: Transaction
        val mode: String
    }
    fun walletFromMnemonic(
        mnemonic: String,
        account: Int = 0,
        index: Int = 0,
        coinType: Int = 330,
    ) = TerraWallet.fromMnemonic(mnemonic, account, index, coinType)
}


