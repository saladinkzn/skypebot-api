package ru.shadam.skypeapi

import ru.shadam.skypeapi.impl.DefaultSkypeApi
import ru.shadam.skypeapi.impl.createInternalSkypeApi

interface SkypeApi {
    fun sendMessage(conversationId: String, message: String)

    fun sendAttachment(conversationId: String, attachment: OutgoingAttachment) : OutgoingAttachmentResponse

    fun getAttachment(attachmentId: String) : Attachment?

    fun getAttachmentView(attachmentId: String, viewId: String) : Array<Byte>
}

fun createSkypeApi(token: String, baseUrl: String = "https://apis.skype.com") : SkypeApi = DefaultSkypeApi(createInternalSkypeApi(token, baseUrl))
