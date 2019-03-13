package com.github.spacepilothannah.spongiform.data.api.dto

import com.github.spacepilothannah.spongiform.data.api.Credentials
import org.junit.Assert.assertThat
import java.util.stream.Stream

class CredentialsTest {
    @ParameterizedTest
    @MethodSource("credentials")
    fun `isValid returns correct result`(t : td) {
        assertThat(t.credentials.isValid()).isEqualTo(t.valid)
    }


    fun `credentials`() = Stream.of(
            td(Credentials("http://google.com","test","test"),true),
            td(Credentials("https://google.com","test","test"),true),
            td(Credentials("","test",""),false),
            td(Credentials("","","test"),false),
            td(Credentials("","test","test"),false),
            td(Credentials("http://google.com","",""),false),
            td(Credentials("http://google.com","test",""),false),
            td(Credentials("http://google.com","","test"),false),

            td(Credentials("ftp://google.com","test","test"),false),
            td(Credentials("wazbnajis","test","test"),false)
    )
    data class td(val credentials : Credentials,
                  val valid : Boolean)

}