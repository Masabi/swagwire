package com.masabi.swagwire.core

interface PathEncoder {
    fun buildPath(path: String, pathParams: Map<String, String>): String
}