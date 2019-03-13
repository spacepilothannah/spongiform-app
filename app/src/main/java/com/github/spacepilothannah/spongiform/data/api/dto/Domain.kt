package com.github.spacepilothannah.spongiform.api.dto

import java.util.Date

data class Domain(
        var id: Int,
        var url: String,
        var created_at: Date
)