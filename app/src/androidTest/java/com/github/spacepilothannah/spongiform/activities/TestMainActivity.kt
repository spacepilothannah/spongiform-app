package com.github.spacepilothannah.spongiform.activities

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.spacepilothannah.spongiform.ui.MainActivity
import com.github.spacepilothannah.spongiform.R
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {
    @get:Rule
    var mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java, true, true)

    @Test
    fun onCreate_loadsRequests() {
        val requestsFragment = mActivityRule.activity.supportFragmentManager.fragments
                .find { f -> f.tag == "REQUESTS" }

        Assert.assertNotNull("Requests fragment was not loaded at launch", requestsFragment)
        Assert.assertTrue("Requests fragment was not resumed at launch", requestsFragment!!.isResumed)
    }

    @Test
    fun requests_areLoaded() {

        onView(withId(R.id.requests_list)).check(null)
    }

}
