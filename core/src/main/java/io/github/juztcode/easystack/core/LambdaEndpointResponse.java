package io.github.juztcode.easystack.core;

import io.github.juztcode.easystack.common.utils.GSONCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LambdaEndpointResponse {
    private final int statusCode;
    private final String body;
    private final Map<String, String> headers;
    private final boolean isBase64Encoded;

    public LambdaEndpointResponse(int statusCode, String body, Map<String, String> headers, boolean isBase64Encoded) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers;
        this.isBase64Encoded = isBase64Encoded;
    }

    public static Builder builder() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Credentials", "true");
        return new Builder().withHeaders(headers);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    // API Gateway expects the property to be called "isBase64Encoded" => isIs
    public boolean isIsBase64Encoded() {
        return isBase64Encoded;
    }

    public static class Builder {

        private static final Logger LOG = LoggerFactory.getLogger(Builder.class);

        private int statusCode = 200;
        private Map<String, String> headers = Collections.emptyMap();
        private String json;
        private Response response;

        public Builder withStatusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder withHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder withResponse(Response response) {
            this.response = response;
            return this;
        }

        public Builder withJson(String json) {
            this.json = json;
            return this;
        }

        public LambdaEndpointResponse build() {
            String body = null;
            if (json != null) {
                body = json;
            } else if (response != null) {
                body = GSONCodec.encode(response);
            }

            return new LambdaEndpointResponse(statusCode, body, headers, false);
        }
    }
}
