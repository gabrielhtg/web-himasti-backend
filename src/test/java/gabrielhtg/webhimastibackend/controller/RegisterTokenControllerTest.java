package gabrielhtg.webhimastibackend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gabrielhtg.webhimastibackend.entity.User;
import gabrielhtg.webhimastibackend.model.RegisterUserRequestModel;
import gabrielhtg.webhimastibackend.model.UserLoginRequestModel;
import gabrielhtg.webhimastibackend.model.WebResponse;
import gabrielhtg.webhimastibackend.repository.UserRepository;
import gabrielhtg.webhimastibackend.service.UserService;
import org.junit.jupiter.api.*;
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

@AutoConfigureMockMvc
@SpringBootTest
class RegisterTokenControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        RegisterUserRequestModel requestModel = new RegisterUserRequestModel();
        requestModel.setUsername("usertesttoken");
        requestModel.setPassword("usertesttoken");
        requestModel.setFirstName("User");
        requestModel.setLastName("Test Token");
        userService.registerUser(requestModel);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteById("usertesttoken");
    }

    @Test
    void testGenRegisterTokenSukses() throws Exception {
        mockMvc.perform(
                get("/api/himasti/registerToken/generate")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("username", "gabrielhtg")
                        .header("durasi", 30)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNotNull(response);
            assertNotNull(response.getData());
            assertFalse(response.getError());
            assertNull(response.getPesanError());
        });
    }

    @Test
    void testGenRegisterTokenGagalUsernameTidakDitemukan() throws Exception {
        mockMvc.perform(
                get("/api/himasti/registerToken/generate")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("username", "userSalah")
                        .header("durasi", 30)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNull(response.getData());
            assertTrue(response.getError());
            assertNotNull(response.getPesanError());
        });
    }

    @Test
    void testGenRegisterTokenGagalUsernameBukanAdmin() throws Exception {
        mockMvc.perform(
                get("/api/himasti/registerToken/generate")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("username", "userSalah")
                        .header("durasi", 30)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            assertNull(response.getData());
            assertTrue(response.getError());
            assertNotNull(response.getPesanError());
        });
    }
}