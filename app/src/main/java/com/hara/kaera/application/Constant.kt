package com.hara.kaera.application

import androidx.datastore.preferences.core.stringPreferencesKey

object Constant {
    const val APPLICATION_JSON = "application/json"
    // dataStore
    const val TOKEN_DATASTORE_NAME = "token_datastore.json"
    const val EMPTY_TOKEN = "token not founded"
    const val EMPTY_NAME = "김캐라"
    const val SHARED_PREFERENCE_NAME = "fcm_pref"
    const val SHARED_PREFERENCE_KEY = "fcm_device_token"
}