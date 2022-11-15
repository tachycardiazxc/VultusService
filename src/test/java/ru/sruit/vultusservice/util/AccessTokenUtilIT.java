package ru.sruit.vultusservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.sruit.vultusservice.models.response.jwt.LoginJwtRequest;

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

    public String obtainNewAccessToken(final String username, final String password, MockMvc mockMvc) throws Exception {
        LoginJwtRequest user = new LoginJwtRequest(username, password);

        MvcResult response =
                mockMvc
                        .perform(
                                post("/api/auth/login")
                                        .content(objectMapper.writeValueAsString(user))
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        String resultString = response.getResponse().getContentAsString();

        return jsonParser.parseMap(resultString).get("accessToken").toString();
    }

}
