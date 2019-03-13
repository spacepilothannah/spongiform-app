package com.github.spacepilothannah.spongiform.support

import com.github.spacepilothannah.spongiform.data.DataManager
import com.github.spacepilothannah.spongiform.data.api.Credentials
import com.github.spacepilothannah.spongiform.data.api.dto.Request

class MockDataManager : DataManager {
    var allowRequestImpl: ((Request) -> Unit)? = null

    override fun allowRequest(request: Request) {
        if (allowRequestImpl == null) {
            TODO("unexpected call to unmocked function allowRequest($request)") //To change body of created functions use File | Settings | File Templates.
        }
        allowRequestImpl?.invoke(request)
    }

    var denyRequestImpl: ((Request) -> Unit)? = null

    override fun denyRequest(request: Request) {
        if (denyRequestImpl == null) {
            TODO("unexpected call to unmocked function denyRequest($request)")
        }
        denyRequestImpl?.invoke(request)

    }

    var getCredentialsImpl: (() -> Credentials)? = null
    override fun getCredentials(): Credentials {
        if (getCredentialsImpl == null) {
            TODO("unexpected call to unmocked function getCredentials")
        } else {
            return getCredentialsImpl!!.invoke()
        }
    }

    var getRequestsImpl : ((Map<String,String>) -> List<Request>)? = null
    override fun getRequests(params: Map<String, String>): List<Request> {
        if(getRequestsImpl == null) {
            TODO("unexpected call to unmocked function  getRequests($params)")
        } else {
            return getRequestsImpl!!.invoke(params)
        }
    }

    var setCredentialsImpl : ((Credentials) -> Boolean)? = null
    override fun setCredentials(credentials: Credentials): Boolean {
        if(setCredentialsImpl == null) {
            TODO("unexpected call to unmocked function setCredentials")
        } else {
            return setCredentialsImpl!!.invoke(credentials)
        }
    }

    var tryLoginImpl : (() -> Boolean)? = null
    override fun tryLogin(): Boolean {
        if (tryLoginImpl == null) {
            TODO("unexpected call to unmocked function tryLogin")
        } else {
            return tryLoginImpl!!.invoke()
        }
    }
}