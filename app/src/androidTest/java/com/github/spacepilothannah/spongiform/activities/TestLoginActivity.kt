package com.github.spacepilothannah.spongiform.activities

import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.*
import com.github.spacepilothannah.spongiform.data.RealDataManager
import com.github.spacepilothannah.spongiform.data.api.Credentials
import com.github.spacepilothannah.spongiform.support.MockDataManager
import com.github.spacepilothannah.spongiform.ui.LoginActivity
import org.junit.runner.RunWith
import com.github.spacepilothannah.spongiform.support.MockDataManagerFactory

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {
    var mDataManager : MockDataManager = MockDataManager()
    @get:Rule
    var mActivityRule = object : ActivityTestRule<LoginActivity>(LoginActivity::class.java, true, false) {
        override fun beforeActivityLaunched() {
            this.activity.dataManagerFactory = MockDataManagerFactory(mDataManager)
        }
    }

    @After
    fun resetMockDataManager() {
        mDataManager = MockDataManager()
    }

    @Test
    fun onLaunch_createsOwnDataManager() {
        mActivityRule.launchActivity(null)
        Assert.assertEquals(RealDataManager::class, mActivityRule.activity.dataManager::class)
    }

    @Test
    @Ignore
    fun whenURIIsBlank_noLoginAttempt() {
        TODO("implement meee")
    }

    @Test
    fun onLaunch_attemptsLogin() {
        var loginAttempts = 0
        mDataManager.getCredentialsImpl = {
            Credentials("https://localhost:9000/", "hello", "world")
        }
        mDataManager.tryLoginImpl = {
            loginAttempts++
            true
        }


        mActivityRule.launchActivity(null)
        Assert.assertEquals(1,loginAttempts)
    }

}