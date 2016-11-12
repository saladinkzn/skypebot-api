package ru.shadam.skypeapi

import ru.shadam.skypeapi.impl.AuthImpl
import ru.shadam.skypeapi.impl.createInternalApi

/**
 * @author sala
 */
fun createAuth(clientId: String, clientSecret: String, baseUrl: String = "https://login.microsoftonline.com") : Auth {
    return AuthImpl(clientId, clientSecret, createInternalApi(baseUrl))
}

interface Auth {
    fun getToken() : Token
}

