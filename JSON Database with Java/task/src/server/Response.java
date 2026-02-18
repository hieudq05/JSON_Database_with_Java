package server;

import com.google.gson.JsonElement;

public class Response {
    private String response;
    private JsonElement value;
    private String reason;

    public Response() {}

    public Response(String response, JsonElement value, String reason) {
        this.response = response;
        this.value = value;
        this.reason = reason;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public JsonElement getValue() {
        return value;
    }

    public void setValue(JsonElement value) {
        this.value = value;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static ResponseBuilder builder() {
        return new ResponseBuilder();
    }

    public static class ResponseBuilder {
        private String response;
        private JsonElement value;
        private String reason;

        public ResponseBuilder response(String response) {
            this.response = response;
            return this;
        }

        public ResponseBuilder value(JsonElement value) {
            this.value = value;
            return this;
        }

        public ResponseBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public Response build() {
            return new Response(response, value, reason);
        }
    }
}
