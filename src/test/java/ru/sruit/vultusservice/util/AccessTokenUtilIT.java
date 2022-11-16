package ru.sruit.vultusservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.sruit.vultusservice.models.response.jwt.LoginJwtRequest;

import java.util.LinkedHashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
public class AccessTokenUtilIT {

    private final ObjectMapper objectMapper;
    private final JacksonJsonParser jsonParser;

    public AccessTokenUtilIT(ObjectMapper objectMapper, JacksonJsonParser jsonParser) {
        this.objectMapper = objectMapper;
        this.jsonParser = jsonParser;
    }

    @SuppressWarnings("unchecked")
    public String obtainNewAccessToken(final String username, final String password, MockMvc mockMvc) throws Exception {
        LoginJwtRequest user = new LoginJwtRequest(username, password);

        MvcResult response =
                mockMvc.perform(post("/api/auth/login")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        LinkedHashMap<String, String> linkedHashMap = (LinkedHashMap<String, String>)
                jsonParser.parseMap(response.getResponse().getContentAsString())
                        .get("data");

        String[] accessToken = {null};

        linkedHashMap.forEach((key, value) -> {
            if ("accessToken".equals(key)) {
                accessToken[0] = value;
            }
        });

        return "Bearer " + accessToken[0];
    }

}
