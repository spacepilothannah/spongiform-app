package com.github.spacepilothannah.spongiform.data

import android.content.Context
import com.github.spacepilothannah.spongiform.data.api.Credentials
import com.github.spacepilothannah.spongiform.data.api.dto.Request

interface DataManager {
    fun getRequests(pending: Boolean?, allowed: Boolean?, callback: (List<Request>) -> Unit)
    fun allowRequest(request: Request, callback: (Boolean) -> Unit)
    fun denyRequest(request: Request, callback: (Boolean) -> Unit)

    fun tryLogin(callback: (Boolean) -> Unit)
    fun setCredentials(credentials: Credentials) : Boolean
    fun getCredentials() : Credentials
}
interface DataManagerFactory {
    fun createDataManager(context : Context) : DataManager

    companion object : DataManagerFactory {
        override fun createDataManager(context : Context) :DataManager {
            return RealDataManager(context)
        }
    }
}