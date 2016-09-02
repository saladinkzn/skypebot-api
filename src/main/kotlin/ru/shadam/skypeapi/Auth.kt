package ru.shadam.skypeapi

import ru.shadam.skypeapi.impl.AuthImpl
import ru.shadam.skypeapi.impl.createInternalApi

/**
 * @author sala
 */
fun createAuth(clientId: String, clientSecret: String) : Auth {
    return AuthImpl(clientId, clientSecret, createInternalApi())
}

interface Auth {
    fun getToken() : Token
}

