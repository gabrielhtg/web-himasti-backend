package gabrielhtg.webhimastibackend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gabrielhtg.webhimastibackend.entity.User;
import gabrielhtg.webhimastibackend.model.RegisterUserRequestModel;
import gabrielhtg.webhimastibackend.model.WebResponse;
import gabrielhtg.webhimastibackend.repository.UserRepository;
import gabrielhtg.webhimastibackend.security.BCrypt;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        User user = userRepository.findById("usertest").orElse(null);

        if (user != null) {
            userRepository.delete(user);
        }
    }

    @Test
    void testRegisterUserSukses () throws Exception {
        RegisterUserRequestModel requestModel = new RegisterUserRequestModel();
        requestModel.setUsername("usertest");
        requestModel.setPassword("usertest");
        requestModel.setFirstName("User");
        requestModel.setLastName("Test");

        mockMvc.perform(
                post("/api/himasti/user/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestModel))
                        .header("TOKEN", "027fbe2e-2229-4e15-94c5-034e79ac9e96")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });

            Assertions.assertNotNull(response);
            Assertions.assertFalse(response.getError());
            Assertions.assertNull(response.getPesanError());
            Assertions.assertEquals("OK", response.getData());
        });
    }

    @Test
    void testRegisterUserGagal () throws Exception {
        RegisterUserRequestModel requestModel = new RegisterUserRequestModel();
        requestModel.setUsername("usertest");
        requestModel.setPassword("usertest");
        requestModel.setFirstName("User");
        requestModel.setLastName("Test");

        mockMvc.perform(
                post("/api/himasti/user/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestModel))
                        .header("TOKEN", "027fbe2e-2229-4e15-94c5-034e79ac9e97")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });
            Assertions.assertTrue(response.getError());
            Assertions.assertEquals("unauthorized", response.getPesanError());
            Assertions.assertNull(response.getData());
        });
    }

    @Test
    void testRegisterUserAdmin () throws Exception {
        RegisterUserRequestModel requestModel = new RegisterUserRequestModel();
        requestModel.setUsername("gabriel");
        requestModel.setPassword("agustus163");
        requestModel.setFirstName("Gabriel");
        requestModel.setLastName("Hutagalung");

        mockMvc.perform(
                post("/api/himasti/user/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestModel))
                        .header("TOKEN", "efe06ff0-b458-432c-88a9-5c268ce6b9cc")
        ).andExpectAll(
                status().isOk()
        );

        User user = userRepository.findById("gabriel").orElse(null);

        Assertions.assertNotNull(user);
        user.setAdmin(true);
        userRepository.save(user);
    }

    @Test
    public void testRegisterUserLastNameNull () throws Exception {
       RegisterUserRequestModel requestModel = new RegisterUserRequestModel();
       requestModel.setUsername("wilo");
       requestModel.setPassword("wilo");
       requestModel.setFirstName("Wilona");

       mockMvc.perform(
               post("/api/himasti/user/register")
                       .content(objectMapper.writeValueAsString(requestModel))
                       .accept(MediaType.APPLICATION_JSON)
                       .contentType(MediaType.APPLICATION_JSON)
                       .header("TOKEN", "e82e41ce-92e7-4e54-9767-e84c9b83fa1d")
       ).andExpectAll(
               status().isOk()
       ).andDo(result -> {
           WebResponse<String> webResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
           });

           Assertions.assertEquals("OK", webResponse.getData());
           Assertions.assertFalse(webResponse.getError());
           Assertions.assertNull(webResponse.getPesanError());
       });
    }
}