package com.github.spacepilothannah.spongiform.data.api.dto

import org.junit.Test
import org.junit.Assert.*
import java.util.*

class RequestTest {
    @Test
    fun request_isPendingWorks() {
        data class pendingTest(val request: Request, val shouldBePending: Boolean)

        val _tests = listOf(
                pendingTest(
                        Request(
                                id = 0, url = ""
                        ), false
                ),
                pendingTest(
                        Request(
                                id = 0, url = "",
                                requested_at = Calendar.getInstance().time
                        ), true
                ),
                pendingTest(
                        Request(
                                id = 0, url = "",
                                requested_at = Calendar.getInstance().time,
                                allowed_at = Calendar.getInstance().time
                        ), false
                ),
                pendingTest(
                        Request(id = 0, url = "",
                                requested_at = Calendar.getInstance().time,
                                allowed_at = Calendar.getInstance().time
                        ), false
                )
        )
        for (t in _tests) {
            assertEquals(t.shouldBePending, t.request.isPending())
        }
    }
}