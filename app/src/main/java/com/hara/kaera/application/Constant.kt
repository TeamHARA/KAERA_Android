package com.hara.kaera.application

import androidx.datastore.preferences.core.stringPreferencesKey

object Constant {
    const val APPLICATION_JSON = "application/json"
    // dataStore
    const val TOKEN_DATASTORE_NAME = "token_datastore"
    val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
}