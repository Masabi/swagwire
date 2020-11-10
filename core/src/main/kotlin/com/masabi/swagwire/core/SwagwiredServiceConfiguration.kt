package com.masabi.swagwire.core

import com.github.tomakehurst.wiremock.WireMockServer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.masabi.kotlinbuilder.annotations.JvmBuilder
import net.dongliu.gson.GsonJava8TypeAdapterFactory

@JvmBuilder
data class SwagwiredServiceConfiguration(
    val basePath: String = "",
    val gson: Gson = GsonBuilder()
        .registerTypeAdapterFactory(GsonJava8TypeAdapterFactory())
        .create(),
    val wireMock: WireMockServer = WireMockServer(),
    val pathBuilder: PathBuilder = InterpolatingPathBuilder(encoder = NoEncodingEncoder)

) {
    companion object {
        @JvmStatic
        fun builder() = SwagwiredServiceConfiguration_Builder()
    }
}