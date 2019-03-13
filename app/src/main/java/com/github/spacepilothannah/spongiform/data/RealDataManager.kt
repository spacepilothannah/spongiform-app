package com.github.spacepilothannah.spongiform.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.github.spacepilothannah.spongiform.data.api.Credentials
import com.github.spacepilothannah.spongiform.data.api.SpongiformService
import com.github.spacepilothannah.spongiform.data.api.dto.Request
import com.github.spacepilothannah.spongiform.data.api.dto.RequestUpdate

class RealDataManager(
        val context: Context
) : DataManager {

    override fun getCredentials(): Credentials {
        val prefs = context.getSharedPreferences("credentials", MODE_PRIVATE)
        return Credentials(username = prefs.getString("username","") ?: "",
                           password = prefs.getString("password", "") ?: "",
                           url = prefs.getString("uri", "") ?: ""
        )
    }

    override fun setCredentials(credentials: Credentials): Boolean {
        val editor = context.getSharedPreferences("credentials", MODE_PRIVATE).edit()
        editor.putString("username", credentials.username)
        editor.putString("password", credentials.password)
        editor.putString("uri", credentials.url)
        editor.apply()
        return true
    }

    override fun tryLogin(callback: (Boolean) -> Unit) {
        return getApiClient().getRequests(pending = true, allowed = null).enqueue(object : Callback<List<Request>> {
            override fun onResponse(call: Call<List<Request>>, response: Response<List<Request>>) {
                Log.d("SQUID","tryLogin received response $response")
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<List<Request>>, t: Throwable) {
                Log.d("SQUID",  "Call failed! " + t.toString())
                callback(false)
            }
        })
    }

    override fun getRequests(pending : Boolean?, allowed: Boolean?, callback: (List<Request>) -> Unit) {
        val resp = getApiClient().getRequests(pending = pending, allowed = allowed).enqueue(object : Callback<List<Request>> {
                override fun onResponse(call: Call<List<Request>>, response: Response<List<Request>>) {
                    if(response.isSuccessful) {
                        callback(response.body() ?: listOf())
                    } else {
                        throw Error("Response was not successful :(")
                    }
                }

                override fun onFailure(call: Call<List<Request>>, t: Throwable) {
                    throw t
                }
        })
    }
    override fun allowRequest(request: Request, callback: (Boolean) -> Unit) {
        setRequestAllowed(request, true, callback)
    }

    override fun denyRequest(request: Request, callback: (Boolean) -> Unit) {
        setRequestAllowed(request, false, callback)
    }

    private fun setRequestAllowed(request: Request, allowed: Boolean, callback: (Boolean) -> Unit) {
        getApiClient().updateRequest(request.id, RequestUpdate(allow = allowed)).enqueue(object : Callback<Request> {
            override fun onResponse(call: Call<Request>, response: Response<Request>) {
                callback(response.isSuccessful)
            }

            override fun onFailure(call: Call<Request>, t: Throwable) {
                throw t
            }
        })
    }

    private fun getApiClient() : SpongiformService.Api {
        val credentials = getCredentials()
        return SpongiformService.getApiClient(credentials.url, credentials.username, credentials.password)
    }
}