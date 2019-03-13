package com.github.spacepilothannah.spongiform.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.github.spacepilothannah.spongiform.R
import com.github.spacepilothannah.spongiform.data.DataManager
import com.github.spacepilothannah.spongiform.data.RealDataManager
import com.github.spacepilothannah.spongiform.data.api.dto.Request


class MainActivity : AppCompatActivity(), RequestsFragment.RequestSelectedAllowedDeniedHandler {

    override fun requestAllowed(request: Request) {
        dataManager.allowRequest(request) { success ->
            if(success) {
                Toast.makeText(this, "Allowed!", Toast.LENGTH_LONG).show()
                val requestsFragment = supportFragmentManager.fragments.firstOrNull { it.tag == "REQUESTS" }
                if(requestsFragment is RequestsFragment) {
                    requestsFragment.refreshRequests()
                }
            } else {
                Toast.makeText(this, "Failed!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun requestDenied(request: Request) {
        dataManager.denyRequest(request) { success ->
            if(success) {
                val requestsFragment = supportFragmentManager.fragments.firstOrNull { it.tag == "REQUESTS" }
                if(requestsFragment is RequestsFragment) {
                    requestsFragment.refreshRequests()
                }
                Toast.makeText(this, "Denied!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Failed!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun requestSelected(request: Request) {
        Log.d("SPONGI", "MainActivity Selected ${request.url}")
    }

    val dataManager : DataManager by lazy { RealDataManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(findViewById(R.id.main_activity_action_bar))
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.main_frame, RequestsFragment.newInstance(dataManager), "REQUESTS")
                    .commitNow()
            supportFragmentManager.fragments.forEach {
                Log.d("SQUID", "onCreate ${it.tag} - isVisible: ${it.isVisible}")
            }
        }
    }



    override fun onResume() {
        super.onResume()
        supportFragmentManager.fragments.forEach {
            Log.d("SQUID", "onResume ${it.tag} - isVisible: ${it.isVisible}")
        }
    }

    fun onRequestSelected(request: Request?) {
        Log.d("SQUID", "Request ${request} selected")
        // TODO: replace the fragment with the RequestInfoFragment
    }
}
