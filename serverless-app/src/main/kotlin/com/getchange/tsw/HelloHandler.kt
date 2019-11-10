package com.getchange.tsw

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler

class HelloHandler : RequestHandler<Any, Response> {
    override fun handleRequest(input: Any, context: Context?): Response {
        return Response(
            statusCode = 200,
            body = "Hello there :)"
        )
    }
}
