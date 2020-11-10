package com.masabi.swagwire.core

object NoEncodingEncoder: UrlValueEncoder {
    override fun encode(value: String) = value
}
