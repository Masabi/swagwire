package com.masabi.swagwire.core

interface PathBuilder {
    fun buildPath(basePath: String, path: String, pathParams: Map<String, String>): String
}