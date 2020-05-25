package com.kyawhtut.pos.utils.webserver.component

import com.google.gson.Gson
import com.yanzhenjie.andserver.annotation.Interceptor
import com.yanzhenjie.andserver.framework.HandlerInterceptor
import com.yanzhenjie.andserver.framework.handler.RequestHandler
import com.yanzhenjie.andserver.http.HttpRequest
import com.yanzhenjie.andserver.http.HttpResponse
import timber.log.Timber

/**
 * Created by Zhenjie Yan on 2018/9/11.
 */
@Interceptor
class LoggerInterceptor : HandlerInterceptor {
    override fun onIntercept(
        request: HttpRequest, response: HttpResponse,
        handler: RequestHandler
    ): Boolean {
        val path = request.path
        val method = request.method
        val valueMap = request.parameter
        Timber.d("LoggerInterceptor Path => %s", path)
        Timber.d("LoggerInterceptor Method => %s", method.value())
        Timber.d("LoggerInterceptor Param => %s", Gson().toJson(valueMap))
        return false
    }
}