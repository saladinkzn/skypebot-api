package ru.shadam.skypebot.webhook.v3

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.JsonNode
import java.time.LocalDateTime

/**
 * @author sala
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, visible = true, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(name = "message", value = MessageV3::class),
    JsonSubTypes.Type(name = "message/text", value = MessageV3::class),
    JsonSubTypes.Type(name = "message/image", value = MessageV3::class),
    JsonSubTypes.Type(name = "activity/contactRelationUpdate", value = ContactRelationUpdateV3::class),
    JsonSubTypes.Type(name = "conversationUpdate", value = ConversationUpdateV3::class)
)
interface ActivityV3

data class MessageV3 (
    val id: String,
    val from: AddressV3,
    val conversation: AddressV3,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'")
    val timestamp: LocalDateTime,
    val recipient: AddressV3,
    val channelId: String,
    val serviceUrl: String,
    val type: String,
    val summary: String?,
    val text: String,
    val attachments: List<AttachmentV3>?,
    val entities: List<JsonNode>
) : ActivityV3

data class ContactRelationUpdateV3 (
    val from: AddressV3,
    val to: AddressV3?,
    val recipient: AddressV3,
    val channelId: String,
    val serviceUrl: String,
    val type: String,
    val action: String
) : ActivityV3

data class ConversationUpdateV3 (
    val from: AddressV3,
    val to: AddressV3?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'")
    val timestamp: LocalDateTime,
    val recipient: AddressV3,
    val channelId: String,
    val serviceUrl: String,
    val type: String,
    val membersAdded: List<String>?,
    val membersRemoved: List<String>?,
    val topicName: String?,
    val historyDisclosed: String?
) : ActivityV3

data class AddressV3 (
    val id: String,
    val name: String?,
    val isGroup: Boolean?
)

data class AttachmentV3 (
    val contentType: String,
    val contentUrl: String?,
    val content: JsonNode
) {
    val extra: MutableMap<String, Any> = hashMapOf()

    @JsonAnySetter
    fun jsonAnySetter(key: String, value: Any) {
        extra[key] = value
    }

    @JsonAnyGetter
    fun jsonAnyGetter() : Map<String, Any> = extra
}

