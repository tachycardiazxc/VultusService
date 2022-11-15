package ru.sruit.vultusservice.models.response.contoller;

public class SuccessResponse <T> extends Response<T> {

    public SuccessResponse() {
        this.code = 200;
    }

    public SuccessResponse(T data) {
        this.code = 200;
        this.data = data;
    }
}
