package com.kyawhtut.pos.utils.webserver.component

import com.kyawhtut.pos.utils.webserver.model.errorResponse
import com.yanzhenjie.andserver.annotation.Resolver
import com.yanzhenjie.andserver.error.BasicException
import com.yanzhenjie.andserver.framework.ExceptionResolver
import com.yanzhenjie.andserver.framework.body.JsonBody
import com.yanzhenjie.andserver.http.HttpRequest
import com.yanzhenjie.andserver.http.HttpResponse
import com.yanzhenjie.andserver.util.StatusCode
import timber.log.Timber

/**
 * Created by Zhenjie Yan on 2018/9/11.
 */
@Resolver
class AppExceptionResolver : ExceptionResolver {
    override fun onResolve(
        req: HttpRequest,
        res: HttpResponse,
        e: Throwable
    ) {
        e.printStackTrace()
        if (e is BasicException) {
            res.status = e.statusCode
        } else {
            res.status = StatusCode.SC_INTERNAL_SERVER_ERROR
        }
        val body: String = errorResponse {
            statusCode = res.status
            message = e.message ?: ""
        }.toString()
        Timber.d("AppExceptionResolver => %s", body)
        res.setBody(JsonBody(body))
    }
}