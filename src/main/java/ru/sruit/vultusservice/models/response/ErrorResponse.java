package ru.sruit.vultusservice.models.response;

public class ErrorResponse <T> extends Response<T> {

    private String message;

    public ErrorResponse(int code, String message) {
        this.message = message;
        this.code = code;
    }

}
