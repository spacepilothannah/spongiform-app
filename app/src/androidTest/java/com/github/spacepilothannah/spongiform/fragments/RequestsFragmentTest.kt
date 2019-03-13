package com.github.spacepilothannah.spongiform.fragments

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.spacepilothannah.spongiform.R
import com.github.spacepilothannah.spongiform.data.api.Credentials
import com.github.spacepilothannah.spongiform.ui.RequestsFragment
import com.android21buttons.fragmenttestrule.FragmentTestRule
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RequestsFragmentTest {
    @get:Rule
    var fragmentRule : FragmentTestRule<*, RequestsFragment> = FragmentTestRule.create(RequestsFragment::class.java)

    private var fragment: RequestsFragment? = null
    private var server: MockWebServer? = null

    private fun loadResource(name: String) : ByteArray {
        return this.javaClass.classLoader!!.getResource(name).readBytes()
    }

    @Before
    fun setFragment() {
        server = MockWebServer()
        server!!.start()
        fragment = RequestsFragment()
        fragment!!.arguments = Credentials(
                server!!.getUrl("/").toString(),
                "test-user",
                "test-password"
        ).toBundle()
    }

    @Test
    fun onStart_getsRequests() {
        val response = loadResource("get_requests_200.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(response.contentToString()))
        fragmentRule.launchFragment(fragment)
        Assert.assertEquals(1, server?.requestCount)
    }

    @Test
    fun onSelect_callsHandler() {
        val response = loadResource("get_requests_200.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(response.contentToString()))
        var requested = false
        fragment!!.setSelectedListener {
            Assert.assertNotNull(it)
            Assert.assertEquals("test-url", it?.url)
            Assert.assertEquals(42, it?.id)
            requested = true
        }
        fragmentRule.launchFragment(fragment)
        onView(withId(R.id.requests_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition<androidx.recyclerview.widget.RecyclerView.ViewHolder>(2, click())
        )
        Assert.assertTrue(requested)

    }
}
