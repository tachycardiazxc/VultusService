package ru.sruit.vultusservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sruit.vultusservice.models.response.Response;

@RestController
@RequestMapping("/test")
public class TestRestController {

    @GetMapping
    public Response<String> response() {
        return Response.ok("Hello!");
    }

}
