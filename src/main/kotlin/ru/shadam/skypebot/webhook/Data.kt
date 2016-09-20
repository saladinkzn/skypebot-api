package ru.shadam.skypebot.webhook

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.LocalDateTime

/**
 * @author sala
 */
enum class ActivityType(private val type: String) {
    PERSONAL("8"),
    CHAT("19"),
    BOT("28");

    companion object {
        private val map = ActivityType.values().map { it.type to it }.toMap()

        fun getByType(type: String) = map[type]
    }
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "activity")
@JsonSubTypes(
        JsonSubTypes.Type(value = IncomingMessage::class, name = "message"),
        JsonSubTypes.Type(value = Attachment::class, name = "attachment"),
        JsonSubTypes.Type(value = ContactRelationUpdate::class, name = "contactRelationUpdate"),
        JsonSubTypes.Type(value = ConversationUpdate::class, name = "conversationUpdate")
)
interface Activity {}

data class IncomingMessage(
    val id: String,
    val from: String,
    val fromDisplayName: String?,
    val to: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'") val time: LocalDateTime,
    val content: String
) : Activity  {
    val activityType: ActivityType
        get() {
          val type = to.split(":")[0]
          return ActivityType.getByType(type) ?: throw IllegalStateException("Unknown type: $type")
        }
}

data class Attachment(
    val from: String,
    val to: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'")
    val time: LocalDateTime,
    val id: String,
    val views: List<AttachmentView>,
    val type: String,
    val name: String
) : Activity

data class AttachmentView(
        private val viewId: String,
        private val size: Int? = null
)

enum class ActionType {
    add,
    remove
}

data class ContactRelationUpdate (
    val from: String,
    val to: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'")
    val time: LocalDateTime,
    val action: ActionType,
    val fromDisplayName: String
) : Activity

data class ConversationUpdate (
    val membersAdded: List<String>?,
    val membersRemoved: List<String>?,
    val topic: String?,
    val historyDisclosed: Boolean?
) : Activity
