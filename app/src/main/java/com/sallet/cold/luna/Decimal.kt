package com.sallet.cold.luna

import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.math.RoundingMode

@Serializable(DecimalSerializer::class)
 class Decimal constructor(internal val origin: BigDecimal) : Number(), Comparable<Decimal> {

     companion object {
         val ZERO: Decimal = Decimal(BigDecimal.ZERO)
         val ONE: Decimal = Decimal(BigDecimal.ONE)
    }

     constructor(value: String) : this(BigDecimal(value))

    override fun compareTo(other: Decimal): Int = origin.compareTo(other.origin)

    override fun toByte(): Byte = origin.toByte()

    override fun toChar(): Char = origin.toChar()

    override fun toDouble(): Double = origin.toDouble()

    override fun toFloat(): Float = origin.toFloat()

    override fun toInt(): Int = origin.toInt()

    override fun toLong(): Long = origin.toLong()

    override fun toShort(): Short = origin.toShort()

    override fun toString(): String = origin.toPlainString()

    override fun equals(other: Any?): Boolean = origin.equals(other)

    override fun hashCode(): Int = origin.hashCode()

     operator fun plus(other: Decimal): Decimal = Decimal(origin + other.origin)

     operator fun minus(other: Decimal): Decimal = Decimal(origin - other.origin)

     operator fun times(other: Decimal): Decimal = Decimal(origin * other.origin)

     operator fun div(other: Decimal): Decimal = Decimal(origin / other.origin)

     operator fun rem(other: Decimal): Decimal = Decimal(origin % other.origin)
}

fun BigDecimal.asDecimal() = Decimal(this)

fun Decimal.asBigDecimal() = origin

 operator fun Decimal.times(other: Uint128): Decimal = origin.multiply(other.origin.toBigDecimal()).asDecimal()

 fun Decimal.ceil(): Decimal = origin.setScale(0, RoundingMode.CEILING).asDecimal()

 fun Decimal.toUint128(): Uint128 = origin.toBigInteger().asUint128()

 fun Decimal.unscaled(scale: Int): Uint128 = origin.setScale(scale).unscaledValue().asUint128()