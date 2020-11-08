package com.alternate.easystack.core;

import com.alternate.easystack.common.concurrent.ThreadLocalWithDefault;
import com.alternate.easystack.common.utils.GSONCodec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;

public abstract class LambdaEndpoint implements RequestHandler<Map<String, Object>, LambdaEndpointResponse> {

    private final ThreadLocal<Application> appReference = new ThreadLocalWithDefault<>(this::getApplication);

    @Override
    @SuppressWarnings("unchecked")
    public LambdaEndpointResponse handleRequest(Map<String, Object> input, Context context) {
        String body = input.get("body").toString();
        Map<String, Object> map = GSONCodec.decode(Map.class, body);
        String serviceName = map.get("service").toString();
        String handlerName = map.get("handler").toString();
        Map<String, Object> request = (Map<String, Object>) map.get("request");

        Service service = appReference.get().getService(serviceName);
        HandlerWrapper wrapper = service.getHandler(handlerName);

        Response response = wrapper.invoke(request);

        return LambdaEndpointResponse.builder()
                .withResponse(response)
                .build();
    }

    public abstract Application getApplication();
}
