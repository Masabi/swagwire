package com.masabi.swagwire.core

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.time.format.DateTimeFormatter
import java.util.*

object ApiUtils {
    @JvmStatic
    fun escapeString(str: String): String {
        return try {
            URLEncoder.encode(str, "utf8").replace("\\+".toRegex(), "%20")
        } catch (e: UnsupportedEncodingException) {
            str
        }
    }

    @JvmStatic
    fun selectHeaderAccept(accepts: Array<String>): String {
        if (accepts.isEmpty()) { // Nothing was specified to presume it's json we want
            return "application/json"
        }
        for (accept in accepts) {
            if (isJsonMime(accept)) {
                return accept
            }
        }
        return join(accepts, ",")
    }

    private fun isJsonMime(mime: String?): Boolean {
        val jsonMime = Regex("(?i)^(application/json|[^;/ \t]+/[^;/ \t]+[+]json)[ \t]*(;.*)?$")
        return mime != null && (mime.matches(jsonMime) || mime.equals("application/json-patch+json", ignoreCase = true))
    }

    private fun join(array: Array<String>, separator: String): String = array.joinToString(separator = separator)

    @JvmStatic
    fun parameterToString(param: Any?): String =
        if (param == null) {
            ""
        } else if (param is Date) {
            DateTimeFormatter.ISO_INSTANT.format(param.toInstant())
        } else if (param is Collection<*>) {
            param.joinToString(separator = ",")
        } else {
            param.toString()
        }
}