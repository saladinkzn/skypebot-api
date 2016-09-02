package ru.shadam.skypeapi.impl

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import ru.shadam.skypeapi.Auth
import ru.shadam.skypeapi.Token

internal class AuthImpl(private val clientId: String, private val clientSecret: String, private val auth: AuthApi) : Auth {
    override fun getToken(): Token {
        return auth.getToken(clientId, clientSecret, "client_credentials", "https://graph.microsoft.com/.default").execute().body()
    }
}

internal interface AuthApi {
    @POST("/common/oauth2/v2.0/token")
    @FormUrlEncoded
    fun getToken(
            @Field("client_id") clientId: String,
            @Field("client_secret") clientSecret: String,
            @Field("grant_type") grantType : String,
            @Field("scope") scope : String
    ) : Call<Token>
}

internal fun createInternalApi(): AuthApi {
    val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    val authApi = Retrofit.Builder()
            .baseUrl("https://login.microsoftonline.com")
            .addConverterFactory(JacksonConverterFactory.create(ObjectMapper().registerKotlinModule().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)))
            .client(OkHttpClient.Builder().addInterceptor(logging).build())
            .build()
            .create(AuthApi::class.java)
    return authApi
}