package com.sallet.cold.luna

import kotlinx.datetime.DateTimePeriod
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.overwriteWith
import kotlinx.serialization.modules.plus
import kotlinx.serialization.modules.polymorphic
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalSerializationApi::class)
object AminoFormat : StringFormat {




    private var json = createJsonFormat()

    override fun <T> encodeToString(
        serializer: SerializationStrategy<T>,
        value: T,
    ): String = json.encodeToString(serializer, value)

    fun <T> encodeToJsonElement(
        serializer: SerializationStrategy<T>,
        value: T,
    ): JsonElement = json.encodeToJsonElement(serializer, value)

    override val serializersModule: SerializersModule
        get() = TypeSerializersModule + MessageSerializersModule


    override fun <T> decodeFromString(
        deserializer: DeserializationStrategy<T>,
        string: String,
    ): T = json.decodeFromString(deserializer, string)

    fun <T> decodeFromJsonElement(
        deserializer: DeserializationStrategy<T>,
        element: JsonElement,
    ): T = json.decodeFromJsonElement(deserializer, element)



    private fun createJsonFormat() = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true

        serializersModule = this@AminoFormat.serializersModule
    }
}


val MessageSerializersModule by lazy { BankSerializersModule+FeeGrantSerializersModule+MessageAuthSerializersModule }

val FeeGrantSerializersModule = SerializersModule {
    polymorphic(Message::class) {
        subclass(
            GrantAllowanceMessage::class,
            AminoSerializer(GrantAllowanceMessage.serializer(), "feegrant/MsgGrantAllowance"),
        )
        subclass(
            RevokeAllowanceMessage::class,
            AminoSerializer(RevokeAllowanceMessage.serializer(), "feegrant/MsgRevokeAllowance"),
        )
    }

    contextual(Allowance::class, PolymorphicObjectSerializer(Allowance::class))

    polymorphic(Allowance::class) {
        subclass(BasicAllowance::class, AminoSerializer(BasicAllowance.serializer(), "feegrant/BasicAllowance"))
        subclass(
            PeriodicAllowance::class,
            AminoSerializer(PeriodicAllowance.serializer(), "feegrant/PeriodicAllowance"),
        )
        subclass(
            AllowedMsgAllowance::class,
            AminoSerializer(AllowedMsgAllowance.serializer(), "feegrant/AllowedMsgAllowance"),
        )
    }
}
val MessageAuthSerializersModule = SerializersModule {
    polymorphic(Message::class) {
        subclass(
            GrantAuthorizationMessage::class,
            AminoSerializer(GrantAuthorizationMessage.serializer(), "msgauth/MsgGrantAuthorization"),
        )
        subclass(
            ExecuteAuthorizedMessage::class,
            AminoSerializer(ExecuteAuthorizedMessage.serializer(), "msgauth/MsgExecAuthorized"),
        )
        subclass(
            RevokeAuthorizationMessage::class,
            AminoSerializer(RevokeAuthorizationMessage.serializer(), "msgauth/MsgRevokeAuthorization"),
        )
    }

    contextual(Authorization::class, PolymorphicObjectSerializer(Authorization::class))

    polymorphic(Authorization::class) {
        subclass(
            GenericAuthorization::class,
            AminoSerializer(GenericAuthorization.serializer(), "msgauth/GenericAuthorization"),
        )
        subclass(SendAuthorization::class, AminoSerializer(SendAuthorization.serializer(), "msgauth/SendAuthorization"))
        subclass(
            StakeAuthorization::class,
            AminoSerializer(StakeAuthorization.serializer(), "msgauth/StakeAuthorization"),
        )
    }
}
val BankSerializersModule = SerializersModule {
    polymorphic(Message::class) {
        subclass(SendMessage::class, AminoSerializer(SendMessage.serializer(), "bank/MsgSend"))
        subclass(MultipleSendMessage::class, AminoSerializer(MultipleSendMessage.serializer(), "bank/MsgMultiSend"))
    }
}

@Serializable
data class StakeAuthorization(
    @SerialName("max_tokens") val maxTokens: List<Coin>,
    val validators: Validators,
    @SerialName("authorization_type") val authorizationType: AuthorizationType,
) : Authorization() {

    @Serializable
    data class Validators internal constructor(
        @SerialName("allow_list") val allowList: ListWrapper? = null,
        @SerialName("deny_list") val denyList: ListWrapper? = null,
    ) {

        companion object {

            @JvmStatic
            fun allowListOf(validators: List<String>) = Validators(
                allowList = ListWrapper(validators),
            )

            @JvmStatic
            fun denyListOf(validators: List<String>) = Validators(
                denyList = ListWrapper(validators)
            )
        }

        @Serializable
        data class ListWrapper(
            val address: List<String>,
        )
    }

    @Serializable(AuthorizationType.Serializer::class)
    enum class AuthorizationType {
        UNSPECIFIED,
        DELEGATE,
        UNDELEGATE,
        REDELEGATE;

        object Serializer : KSerializer<AuthorizationType> {

            override val descriptor: SerialDescriptor =
                PrimitiveSerialDescriptor("AuthorizationType", PrimitiveKind.INT)

            override fun serialize(encoder: Encoder, value: AuthorizationType) {
                encoder.encodeInt(value.ordinal)
            }

            override fun deserialize(decoder: Decoder): AuthorizationType = values()[decoder.decodeInt()]
        }
    }
}


@Serializable
data class SendAuthorization(
    @SerialName("spend_limit") val spendLimit: List<Coin>,
) : Authorization()
@Serializable
data class GenericAuthorization(
    @SerialName("msg") val message: String,
) : Authorization()

@Serializable
data class GrantAllowanceMessage(
    val granter: String,
    val grantee: String,
    @Contextual val allowance: Allowance,
) : Message()

@Serializable
data class RevokeAuthorizationMessage(
    val granter: String,
    val grantee: String,
    @SerialName("msg_type_url") val messageType: String,
) : Message()

@Serializable
class ExecuteAuthorizedMessage(
    val grantee: String,
    @SerialName("msgs") val messages: List<@Contextual Message>,
) : Message()

@Serializable
data class RevokeAllowanceMessage(
    val granter: String,
    val grantee: String,
) : Message()

@Serializable
class GrantAuthorizationMessage(
    val granter: String,
    val grantee: String,
    val grant: MessageGrant,
) : Message()

@Serializable
data class MessageGrant(
    @Contextual val authorization: Authorization,
    val expiration: LocalDateTime,
)
@Serializable
data class PeriodicAllowance(
    val basic: BasicAllowance,
    val period: DateTimePeriod,
    @SerialName("period_spend_limit") val periodSpendLimit: List<Coin>,
    @SerialName("period_can_spend") val periodCanSpend: List<Coin>,
    @SerialName("period_reset") val periodReset: LocalDateTime,
) : Allowance()

abstract class Allowance
abstract class Authorization
@Serializable
data class BasicAllowance(
    @SerialName("spend_limit") val spendLimit: List<Coin>,
    val expiration: LocalDateTime,
) : Allowance()

@Serializable
data class AllowedMsgAllowance(
    @Contextual val allowance: Allowance,
    @SerialName("allowed_messages") val allowedMessages: List<String>,
) : Allowance()
open class AminoSerializer<T>(
    private val origin: KSerializer<T>,
    serialName: String? = null,
) : KSerializer<T> by origin {

    override val descriptor: SerialDescriptor = if (serialName == null) origin.descriptor else {
        RenamedSerialDescriptor(origin.descriptor, serialName)
    }
}

@OptIn(ExperimentalSerializationApi::class)
private class RenamedSerialDescriptor(
    private val origin: SerialDescriptor,
    override val serialName: String,
) : SerialDescriptor by origin