package com.masabi.swagwire.core

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import com.github.tomakehurst.wiremock.stubbing.Scenario
import com.google.gson.Gson
import java.util.UUID.randomUUID

class RemoteOperation<TYPE>(
        private val mappingBuilder: MappingBuilder,
        private val gson: Gson,
        private val contentType: String,
        private val wireMock: WireMockServer
) {
    /**
     * Provides access to the built request.
     * <p>
     * This can be useful when setting up post verification checks, but you still want swagwire
     * to construct requests or for manipulating the response further, e.g. setting up scenarios.
     * If you stub with multiple responses then only the last stub will be available.
     * <p>
     */
    val request = mappingBuilder

    fun respondsWith(responseObject: TYPE) {
        wireMock.givenThat(mappingBuilder.willReturn(
                defaultResponse()
                        .withBody(responseObject.toJson())
        ))
    }

    private fun defaultResponse(): ResponseDefinitionBuilder {
        return WireMock.aResponse()
            .withStatus(200)
            .withHeader("Content-Type", contentType)
    }

    fun respondsWith(response: ResponseDefinitionBuilder) {
        wireMock.givenThat(mappingBuilder.willReturn(response))
    }

    fun respondsWith(response: RemoteOperationResponse<TYPE>) {
        wireMock.givenThat(mappingBuilder
            .willReturn(response.populateResponse(defaultResponse(), gson)))
    }

    fun respondsWith(vararg responses: RemoteOperationResponse<TYPE>) {
        val scenarioName = "Test Scenario"
        var currentScenario = Scenario.STARTED
        responses.forEachIndexed { index, response ->
            val nextScenario = "$scenarioName $index"
            wireMock.givenThat(
                mappingBuilder
                    .inScenario(scenarioName)
                    .withId(randomUUID())
                    .whenScenarioStateIs(currentScenario)
                    .willSetStateTo(nextScenario)
                    .willReturn(response.populateResponse(defaultResponse(), gson))
            )
            currentScenario = nextScenario
        }
    }


    fun succeeds() {
        wireMock.givenThat(mappingBuilder.willReturn(defaultResponse()))
    }

    /**
     * Verifies that the request represented by this operation was called.
     */
    @JvmOverloads
    fun wasCalled(noTimes: Int = 1) {
        wireMock.verify(noTimes, RequestPatternBuilder.like(request.build().request))
    }

    /**
     * Verifies that the request represented by this operation was not called.
     */
    fun wasNotCalled() {
        wireMock.verify(0, RequestPatternBuilder.like(request.build().request))
    }

    internal fun TYPE.toJson(): String = gson.toJson(this)
}
