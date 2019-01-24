package com.masabi.swagwire.core

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.http.Fault
import com.google.gson.Gson

sealed class RemoteOperationResponse<in TYPE> {
    internal abstract fun populateResponse(response: ResponseDefinitionBuilder, gson: Gson): ResponseDefinitionBuilder
}
class Fault<TYPE>(private val fault: Fault): RemoteOperationResponse<TYPE>() {
    override fun populateResponse(response: ResponseDefinitionBuilder, gson: Gson) = response.withFault(fault)

    companion object {
        @JvmStatic
        fun <TYPE> of(fault: Fault) = com.masabi.swagwire.core.Fault<TYPE>(fault)
    }
}
class Data<in TYPE>(private val data: TYPE): RemoteOperationResponse<TYPE>() {
    override fun populateResponse(response: ResponseDefinitionBuilder, gson: Gson) = response.withBody(gson.toJson(data))

    companion object {
        @JvmStatic
        fun <TYPE> of(data: TYPE) = Data(data)
    }
}
class Success<TYPE> private constructor(private val status: Int): RemoteOperationResponse<TYPE>() {
    override fun populateResponse(response: ResponseDefinitionBuilder, gson: Gson) = response.withStatus(status)

    companion object {
        @JvmStatic
        fun <TYPE> noContent() = Success<TYPE>(204)
    }
}