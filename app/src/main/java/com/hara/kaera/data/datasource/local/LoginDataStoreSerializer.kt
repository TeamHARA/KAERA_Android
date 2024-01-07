package com.hara.kaera.data.datasource.local

import androidx.datastore.core.Serializer
import com.hara.kaera.data.util.CryptoManager
import com.hara.kaera.domain.entity.login.LoginData
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class LoginDataStoreSerializer(
    private val cryptoManager: CryptoManager
) : Serializer<LoginData> {
    override val defaultValue: LoginData
        get() = LoginData()

    override suspend fun readFrom(input: InputStream): LoginData {
        val decryptedBytes = cryptoManager.decrypt(input)
        return try {
            Json.decodeFromString(
                deserializer = LoginData.serializer(),
                string = decryptedBytes.decodeToString()
            )
        } catch (e: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: LoginData, output: OutputStream) {
        cryptoManager.encrypt(
            bytes = Json.encodeToString(
                serializer = LoginData.serializer(),
                value = t
            ).encodeToByteArray(),
            outputStream = output
        )
    }
}