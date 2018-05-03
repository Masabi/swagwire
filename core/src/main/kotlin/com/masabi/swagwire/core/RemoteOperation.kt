package com.masabi.swagwire.core

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.google.gson.Gson

class RemoteOperation<in TYPE>(
        private val mappingBuilder: MappingBuilder,
        private val gson: Gson,
        private val contentType: String
) {

    fun respondsWith(responseObject: TYPE) {
        WireMock.stubFor(mappingBuilder.willReturn(
                WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", contentType)
                        .withBody(responseObject.toJson())
        ))
    }

    fun respondsWith(response: ResponseDefinitionBuilder) {
        WireMock.stubFor(mappingBuilder.willReturn(response))
    }

    // TODO is this always a 200? Could we get it from the input spec?
    fun succeeds() {
        WireMock.stubFor(mappingBuilder.willReturn(
                WireMock.aResponse()
                        .withStatus(200)
        ))
    }

    private fun TYPE.toJson(): String = gson.toJson(this)
}
