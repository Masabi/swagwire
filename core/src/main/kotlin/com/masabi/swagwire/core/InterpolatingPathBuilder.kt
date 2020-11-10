package com.masabi.swagwire.core

import java.io.UnsupportedEncodingException
import java.net.URLEncoder

/**
 * An implementation of a PathBuilder that performs simple string replacement with a provided encoder.
 *
 * Any key in the <pre>pathParams</pre> parameter that is found in the <pre>path</pre> parameter surrounded by double curly brackets
 * will be replaced with the encoded value of the params value.
 *
 * @return the path to use for making calls
 */
class InterpolatingPathBuilder(
    private val encoder: UrlValueEncoder
): PathBuilder {
    override fun buildPath(path: String, pathParams: Map<String, String>): String =
        pathParams.entries.fold(
            path,
            { wipPath, param ->  wipPath.replace("{${param.key}}", param.value.encode()) })

    private fun String.encode() = encoder.encode(this)
}