package com.masabi.swagwire.core

import java.io.UnsupportedEncodingException
import java.net.URLEncoder

interface UrlValueEncoder {
    fun encode(value: String): String
}

object UrlEncoderBasedUrlValueEncoder: UrlValueEncoder {
    override fun encode(value: String): String = try {
        URLEncoder.encode(value, "utf8").replace("\\+".toRegex(), "%20")
    } catch (e: UnsupportedEncodingException) {
        value
    }
}
