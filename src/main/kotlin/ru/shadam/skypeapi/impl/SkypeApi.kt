package ru.shadam.skypeapi.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.shadam.skypeapi.*

internal interface InternalSkypeApi {

    @POST("/v2/conversations/{conversationId}/activities/")
    fun sendMessage(
            @Path("conversationId") conversationId: String,
            @Body message: OutgoingMessage
    ) : Call<Void>

    @POST("/v2/conversations/{conversationId}/attachments/")
    fun sendAttachement(
            @Path("conversationId") conversationId: String,
            @Body attachment: OutgoingAttachment
    ) : Call<OutgoingAttachmentResponse>

    @GET("/v2/attachments/{attachmentId}/")
    fun getAttachment (
            @Path("attachmentId") attachementId : String
    ) : Call<Attachment>

    @GET("/v2/attachments/{attachmentId}/views/{viewId}/")
    fun getAttachmentView(
            @Path("attachmentId") attachmentId: String,
            @Path("viewId") viewId: String
    ) : Call<ResponseBody>

}

internal class DefaultSkypeApi(private val delegate: InternalSkypeApi) : SkypeApi {
    override fun sendMessage(conversationId: String, message: String) {
        if(message.isEmpty()) {
            throw IllegalArgumentException("message cannot be empty")
        }
        delegate.sendMessage(conversationId, OutgoingMessage(message)).execute()
    }

    override fun sendAttachment(conversationId: String, attachment: OutgoingAttachment): OutgoingAttachmentResponse {
        return delegate.sendAttachement(conversationId, attachment).execute().body()
    }

    override fun getAttachment(attachmentId: String): Attachment? {
        return delegate.getAttachment(attachmentId).execute().body()
    }

    override fun getAttachmentView(attachmentId: String, viewId: String): Array<Byte> {
        return delegate.getAttachmentView(attachmentId, viewId).execute().body().bytes().toTypedArray()
    }
}

internal fun createInternalSkypeApi(token: String, baseUrl: String): InternalSkypeApi {
    val loggingInterceptor = HttpLoggingInterceptor({ message -> logger.info(message) })
            .apply { level = HttpLoggingInterceptor.Level.BODY }
    //
    val authorizationInterceptor = AuthorizationInterceptor(token)
    val client = OkHttpClient.Builder()
            .addInterceptor(authorizationInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    return Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(ObjectMapper().registerKotlinModule()))
            .client(client)
            .baseUrl(baseUrl)
            .build()
            .create(InternalSkypeApi::class.java)
}

internal class AuthorizationInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}

private val logger : Logger = LoggerFactory.getLogger("ru.shadam.skypeapi.skypeapi")
