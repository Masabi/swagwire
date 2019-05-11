package com.masabi.swagwire.core

import com.google.gson.Gson
import net.dongliu.gson.GsonJava8TypeAdapterFactory
import com.google.gson.GsonBuilder
import com.masabi.kotlinbuilder.annotations.JvmBuilder

@JvmBuilder
data class SwagwiredServiceConfiguration(
    val basePath: String = "",
    val gson: Gson = GsonBuilder()
        .registerTypeAdapterFactory(GsonJava8TypeAdapterFactory())
        .create()
) {
    companion object {
        @JvmStatic
        fun builder() = SwagwiredServiceConfiguration_Builder()
    }
}