package com.masabi.swagwire.core

interface PathBuilder {
    fun buildPath(path: String, pathParams: Map<String, String>): String
}