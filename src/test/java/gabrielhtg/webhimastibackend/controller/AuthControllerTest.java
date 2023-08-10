package gabrielhtg.webhimastibackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gabrielhtg.webhimastibackend.model.RegisterUserRequestModel;
import gabrielhtg.webhimastibackend.model.UserLoginRequestModel;
import gabrielhtg.webhimastibackend.model.WebResponse;
import gabrielhtg.webhimastibackend.repository.UserRepository;
import gabrielhtg.webhimastibackend.service.AuthService;
import gabrielhtg.webhimastibackend.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        RegisterUserRequestModel requestModel = new RegisterUserRequestModel();
        requestModel.setFirstName("UserTest");
        requestModel.setUsername("usertest");
        requestModel.setPassword("ganteng");

        userService.registerUser(requestModel);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testLoginSukses() throws Exception {
        UserLoginRequestModel requestModel = new UserLoginRequestModel();
        requestModel.setPassword("ganteng");
        requestModel.setUsername("usertest");
        mockMvc.perform(
                post("/api/himasti/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestModel))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNotNull(response);
            assertEquals("ok", response.getData());
            assertFalse(response.getError());
            assertNull(response.getPesanError());
        });
    }
}