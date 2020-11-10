package com.masabi.swagwire.core

import java.io.UnsupportedEncodingException
import java.net.URLEncoder

/**
 * An implementation of a PathBuilder that performs simple string replacement with URLEncoder.
 *
 * Any key in the <pre>pathParams</pre> parameter that is found in the <pre>path</pre> parameter surrounded by double curly brackets
 * will be replaced with the params value.
 *
 * @return the path to use for making calls
 */
class UrlEncoderBasedPathBuilder: PathBuilder {
    override fun buildPath(basePath: String, path: String, pathParams: Map<String, String>): String =
        buildUninterpolatedPath(basePath, path)
            .interpolate(pathParams)

    private fun buildUninterpolatedPath(basePath: String, path: String): String {
        val pathDelim = if (basePath.isNotEmpty() && path.isNotEmpty()) "/" else ""
        return "${basePath}${pathDelim}${path}"
    }

    private fun escapeString(str: String): String {
        return try {
            URLEncoder.encode(str, "utf8").replace("\\+".toRegex(), "%20")
        } catch (e: UnsupportedEncodingException) {
            str
        }
    }

    private fun String.interpolate(pathParams: Map<String, String>): String {
        return pathParams.entries.fold(
            this,
            { path, param ->  path.replace("{${param.key}}", escapeString(param.value)) })
    }
}