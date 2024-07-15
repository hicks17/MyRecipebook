package js.apps.recipesapp.utils

import okhttp3.Interceptor
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.net.SocketTimeoutException

/*
class ErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        try {
            val response = chain.proceed(request)
            val bodyString = response.body!!.string()

            return response.newBuilder()
                .body(bodyString.toResponseBody(response.body?.contentType()))
                .build()
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException -> {
                    throw SocketTimeoutException()
                }

                // Add additional errors... //

            }
        }
    }
}*/
