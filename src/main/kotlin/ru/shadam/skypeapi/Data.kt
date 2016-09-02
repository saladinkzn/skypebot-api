package ru.shadam.skypeapi

/**
 * @author sala
 */
data class Activity(val id: String, val from: String, val to: String)

data class Conversation(val id: String)

data class Attachment(
        val attachmentId: String,
        // TODO: enum
        val type: String,
        val originalBase64: String,
        val thumbnailBase64: String,
        val name: String? = null
)

data class OutgoingAttachment(
        val type: String,
        val originalBase64: String,
        val thumbnailBase64: String,
        val name: String? = null
)

data class OutgoingAttachmentResponse(
        val attachmentId: String,
        val activityId: String? = null
)

data class User(val id: String)

data class OutgoingMessage(val message: Message) {
    constructor(content: String) : this(Message(content))
}

data class Message(val content: String)

data class View(val viewId: String, val size: Int)

data class AttachmentDetails(val views: List<View>, val type: String, val name: String)

data class Token(val access_token : String, val expires_in: Int)

