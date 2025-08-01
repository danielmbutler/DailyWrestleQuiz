package com.dbtechprojects.dailywrestlequiz.data.data.persistence.file

import android.util.Base64
import com.dbtechprojects.dailywrestlequiz.appContext
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


    actual fun decryptFileToJson(fileName: String, key: String): String? {
        return try {
            // 1. Get resource ID from name
            val resId = appContext.resources.getIdentifier(fileName, "raw", appContext.packageName)
            if (resId == 0) throw IllegalArgumentException("Resource not found: $fileName")

            // 2. Read the file from res/raw
            val encryptedBase64 = appContext.resources.openRawResource(resId)
                .bufferedReader()
                .use { it.readText().trim() }

            // 3. Prepare AES key
            val keyBytes = key.toByteArray(Charsets.UTF_8)
            require(keyBytes.size == 32) { "Key must be 32 bytes" }
            val secretKey = SecretKeySpec(keyBytes, "AES")

            // 4. Zero IV for CBC
            val ivBytes = ByteArray(16)
            val ivSpec = IvParameterSpec(ivBytes)

            // 5. Decode Base64 and decrypt
            val cipherBytes = android.util.Base64.decode(encryptedBase64, android.util.Base64.DEFAULT)
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
            val plainBytes = cipher.doFinal(cipherBytes)

            // 6. Return decrypted JSON string
            String(plainBytes, Charsets.UTF_8)

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }