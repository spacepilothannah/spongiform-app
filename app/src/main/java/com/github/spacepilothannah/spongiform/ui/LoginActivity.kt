package com.github.spacepilothannah.spongiform.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.github.spacepilothannah.spongiform.R
import com.github.spacepilothannah.spongiform.data.DataManager
import com.github.spacepilothannah.spongiform.data.DataManagerFactory
import com.github.spacepilothannah.spongiform.data.api.Credentials

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {
    var dataManagerFactory: DataManagerFactory = DataManagerFactory.Companion

    val dataManager: DataManager
        get() {
            return dataManagerFactory.createDataManager(this)
        }

    // UI references.
    private val mUriView: EditText by lazy { findViewById<View>(R.id.uri) as EditText }
    private val mUserView: EditText by lazy { findViewById<View>(R.id.user) as EditText }
    private val mPassView: EditText by lazy { findViewById<View>(R.id.password) as EditText }
    private val mProgressView: View by lazy { findViewById<View>(R.id.login_progress) }
    private val mLoginFormView: View by lazy { findViewById<View>(R.id.login_form) }

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SQUID", "LoginActivity onCreate")
        // if I weren't using HTTP Basic auth like a prune I'd have:
        // val token = dataManager.getToken()
        // if(token != "") {
        //    attemptLoginWithToken()
        // }
        setSupportActionBar(findViewById(R.id.login_activity_action_bar))
        val credentials = dataManager.getCredentials()
        if(credentials.isValid()) {
            attemptLogin()
        }


        setContentView(R.layout.login_activity)
        mPassView.text.replace(0, mPassView.text.length, credentials.password)
        mUserView.text.replace(0, mUserView.text.length, credentials.username)
        mUriView.text.replace(0, mUriView.text.length, credentials.url)

        mPassView.setOnEditorActionListener { _: TextView, id: Int, keyEvent: KeyEvent ->
            var done = false
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                if(setCredentials()) {
                    done = true
                    attemptLogin()
                }
            }
            done
        }

        val signInButton = findViewById<View>(R.id.sign_in_button) as Button
        signInButton.setOnClickListener {
            setCredentials()
            attemptLogin()
        }
    }

    private fun launchMainActivity() {
        Log.d("SQUID", "launching MainActivity")
        val intent = Intent(this, MainActivity::class.java).apply {
            // extra data goes here (there is none?)
        }
        startActivity(intent)
    }

    private fun attemptLogin() {
        showProgress(true)
        Log.d("SQUID", "attempting login with credentials " + dataManager.getCredentials().toString())
        dataManager.tryLogin() { success ->
            Log.d("SQUID","logged in? $success")
            if(success) {
                launchMainActivity()
            } else {
                Toast.makeText(this, "Couldn't log in for some reason :-C", Toast.LENGTH_LONG).show()
            }
            showProgress(false)
        }
    }

    private fun setCredentials() : Boolean {
        // Reset errors.
        mUriView.error = null
        mPassView.error = null

        var cancel = false
        var focusView: View? = null

        for(view in arrayListOf(mPassView, mUserView, mUriView)) {
            if(view.text.isEmpty()) {
                view.error = "This field is required"
                focusView = view
                cancel = true
            }
        }

        focusView?.requestFocus()
        if (cancel) {
            return false
        }

        val uri = mUriView.text.toString()
        val user = mUserView.text.toString()
        val password = mPassView.text.toString()
        val creds = Credentials(
            username = user,
            password = password,
            url = uri
        )

        Log.d("SQUID", "setting credentials to " + creds.toString())
        dataManager.setCredentials(creds)
        return true
    }

    private fun isUserValid(email: String): Boolean {
        return !email.contains(" ")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)

            mLoginFormView?.visibility = if (show) View.GONE else View.VISIBLE
            mLoginFormView?.animate()?.setDuration(shortAnimTime.toLong())?.alpha(
                    (if (show) 0 else 1).toFloat())?.setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mLoginFormView?.visibility = if (show) View.GONE else View.VISIBLE
                }
            })

            mProgressView?.visibility = if (show) View.VISIBLE else View.GONE
            mProgressView?.animate()?.setDuration(shortAnimTime.toLong())?.alpha(
                    (if (show) 1 else 0).toFloat())?.setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    mProgressView?.visibility = if (show) View.VISIBLE else View.GONE
                }
            })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView?.visibility = if (show) View.VISIBLE else View.GONE
            mLoginFormView?.visibility = if (show) View.GONE else View.VISIBLE
        }
    }
}

