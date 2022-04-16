package com.sallet.cold.luna

import kotlinx.serialization.Serializable

@Serializable
data class TypeWrapper<T>(
    val type: String,
    val value: T,
)