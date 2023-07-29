package gabrielhtg.webhimastibackend.controller;

import gabrielhtg.webhimastibackend.model.UserLoginRequestModel;
import gabrielhtg.webhimastibackend.model.WebResponse;
import gabrielhtg.webhimastibackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping (
            path = "/api/himasti/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    private WebResponse<String> login(@RequestBody UserLoginRequestModel requestModel) {
        if (authService.Login(requestModel)) {
            return WebResponse.<String>builder().build();
        }

        return WebResponse.<String>builder().error(true).build();
    }
}
