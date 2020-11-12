package com.masabi.swagwire.core

import java.time.format.DateTimeFormatter
import java.util.*

object ApiUtils {
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