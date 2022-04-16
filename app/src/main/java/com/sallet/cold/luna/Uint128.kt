package com.sallet.cold.luna

import kotlinx.serialization.Serializable
import java.math.BigInteger

@Serializable(Uint128Serializer::class)
 class Uint128 constructor(internal val origin: BigInteger) : Number(), Comparable<Uint128> {

     companion object {
         val ZERO: Uint128 = Uint128(BigInteger.ZERO)
         val ONE: Uint128 = Uint128(BigInteger.ONE)
    }

     constructor(value: String) : this(BigInteger(value))

    init {
        if (origin.signum() == -1) {
            throw IllegalArgumentException("Can't be negative")
        }
    }

    override fun compareTo(other: Uint128): Int = origin.compareTo(other.origin)

    override fun toByte(): Byte = origin.toByte()

    override fun toChar(): Char = origin.toChar()

    override fun toDouble(): Double = origin.toDouble()

    override fun toFloat(): Float = origin.toFloat()

    override fun toInt(): Int = origin.toInt()

    override fun toLong(): Long = origin.toLong()

    override fun toShort(): Short = origin.toShort()

    override fun toString(): String = origin.toString()

    override fun equals(other: Any?): Boolean = origin.equals(other)

    override fun hashCode(): Int = origin.hashCode()

     operator fun plus(other: Uint128): Uint128 = Uint128(origin + other.origin)

     operator fun minus(other: Uint128): Uint128 = Uint128(origin - other.origin)

     operator fun times(other: Uint128): Uint128 = Uint128(origin * other.origin)

     operator fun div(other: Uint128): Uint128 = Uint128(origin / other.origin)

     operator fun rem(other: Uint128): Uint128 = Uint128(origin % other.origin)
}

fun BigInteger.asUint128() = Uint128(this)

fun Uint128.asBigInteger() = origin

 fun Uint128.toDecimal(): Decimal = origin.toBigDecimal().asDecimal()

 fun Uint128.scaled(scale: Int): Decimal = origin.toBigDecimal(scale).asDecimal()