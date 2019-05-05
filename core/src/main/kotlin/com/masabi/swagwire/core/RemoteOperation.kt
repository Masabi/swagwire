package com.masabi.swagwire.core

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import com.github.tomakehurst.wiremock.stubbing.Scenario
import com.google.gson.Gson

class RemoteOperation<TYPE>(
        private val mappingBuilder: MappingBuilder,
        private val gson: Gson,
        private val contentType: String
) {
    /**
     * Provides access to the built request.
     * <p>
     * This can be useful when setting up post verification checks but you still want swagwire to construct requests or
     * for manipulating the response further, e.g. setting up scenarios.
     * <p>
     */
    val request = mappingBuilder

    fun respondsWith(responseObject: TYPE) {
        WireMock.stubFor(mappingBuilder.willReturn(
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
        WireMock.stubFor(mappingBuilder.willReturn(response))
    }

    fun respondsWith(response: RemoteOperationResponse<TYPE>) {
        WireMock.stubFor(mappingBuilder
            .willReturn(response.populateResponse(defaultResponse(), gson)))
    }

    fun respondsWith(vararg responses: RemoteOperationResponse<TYPE>) {
        val scenarioName = "Test Scenario"
        var currentScenario = Scenario.STARTED
        responses.forEachIndexed { index, response ->
            val nextScenario = "$scenarioName $index"
            WireMock.stubFor(mappingBuilder
                .inScenario(scenarioName)
                .whenScenarioStateIs(currentScenario)
                .willSetStateTo(nextScenario)
                .willReturn(response.populateResponse(defaultResponse(), gson)))
            currentScenario = nextScenario
        }
    }


    fun succeeds() {
        WireMock.stubFor(mappingBuilder.willReturn(defaultResponse()))
    }

    /**
     * Verifies that the request represented by this operation was called.
     */
    @JvmOverloads
    fun wasCalled(noTimes: Int = 1) {
        WireMock.verify(noTimes, RequestPatternBuilder.like(request.build().request))
    }

    /**
     * Verifies that the request represented by this operation was not called.
     */
    fun wasNotCalled() {
        WireMock.verify(0, RequestPatternBuilder.like(request.build().request))
    }

    internal fun TYPE.toJson(): String = gson.toJson(this)
}
