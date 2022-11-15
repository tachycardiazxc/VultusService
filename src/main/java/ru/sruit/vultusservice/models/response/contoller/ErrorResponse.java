package ru.sruit.vultusservice.models.response.contoller;

public class ErrorResponse <T> extends Response<T> {

    private final String message;

    public ErrorResponse(int code, String message) {
        this.message = message;
        this.code = code;
    }

}
