package com.hara.kaera.feature.util

sealed class TokenState<out T> {
    object Init : TokenState<Nothing>()
    object Empty : TokenState<Nothing>()
    object Exist : TokenState<Nothing>()
    object Valid : TokenState<Nothing>()
    object Expired : TokenState<Nothing>()
}