package com.getchange.tsw.platform.aws

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.getchange.tsw.Response

class HelloHandler : RequestHandler<Any, Response> {
    override fun handleRequest(input: Any, context: Context?): Response {
        return Response(
            statusCode = 200,
            body = "Hello there :)"
        )
    }
}
