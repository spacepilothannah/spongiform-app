package com.github.spacepilothannah.spongiform.util

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat

class RFC8601JsonAdapter {
    @FromJson
    fun fromJson(jsonReader: JsonReader) : java.util.Date? {
        if(jsonReader.peek() == JsonReader.Token.NULL) return jsonReader.nextNull()

        val dateString = jsonReader.nextString()
        if (dateString.isEmpty()) return null

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z")
        return sdf.parse(dateString)
    }

    @ToJson
    fun toJson(date : java.util.Date) : String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").format(date)
    }

}