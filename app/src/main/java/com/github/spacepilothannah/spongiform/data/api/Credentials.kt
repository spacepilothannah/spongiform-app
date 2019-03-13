package com.github.spacepilothannah.spongiform.data.api

import android.os.Bundle
import java.net.URI
import java.net.URISyntaxException

class Credentials(
        var url: String,
        var username: String,
        var password: String
) {
    fun isValid() : Boolean {
        try {
            val uri = URI(url)
            if(uri.scheme != "http" && uri.scheme != "https") {
                return false
            }
        } catch(e: URISyntaxException) {
            return false
        }

        return url.isNotBlank() && username.isNotBlank() && password.isNotBlank()
    }
    fun toBundle() : Bundle {
        var bdl = Bundle()
        bdl.putString("url", url)
        bdl.putString("username", username)
        bdl.putString("password", password)
        return bdl
    }

    override fun toString(): String {
        return "Credentials(url: \"${url}\", username: \"$username\", password: \"$password\")"
    }

    companion object {
        fun fromBundle(bdl: Bundle) : Credentials {
            return Credentials(bdl.getString("url") ?: "",
                               bdl.getString("username") ?: "",
                               bdl.getString("password") ?: "")
        }
    }
}