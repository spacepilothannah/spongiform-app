package com.github.spacepilothannah.spongiform.data.api.dto

import java.util.*

data class Request(
        val id: Int,
        val url: String,
        val created_at: Date? = null,
        val allowed_at: Date? = null,
        val denied_at: Date? = null,
        val requested_at: Date? = null,
        val allowed: Boolean? = null
) {
    fun isRequested(): Boolean {
        return this.requested_at != null
    }
    fun isAllowed(): Boolean {
        return this.allowed_at != null
    }
    fun isDenied(): Boolean {
        return this.denied_at != null
    }
    fun isPending(): Boolean {
        return isRequested() && !isAllowed() && !isDenied()
    }
}