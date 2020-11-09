package io.github.juztcode.easystack.core;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.github.juztcode.easystack.common.concurrent.ThreadLocalWithDefault;
import io.github.juztcode.easystack.common.utils.GSONCodec;

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
