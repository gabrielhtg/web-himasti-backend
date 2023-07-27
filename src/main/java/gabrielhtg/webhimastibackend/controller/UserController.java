package gabrielhtg.webhimastibackend.controller;

import gabrielhtg.webhimastibackend.model.RegisterUserRequestModel;
import gabrielhtg.webhimastibackend.model.WebResponse;
import gabrielhtg.webhimastibackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(
            path = "/himasti/api/user/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> registerUser (@RequestBody RegisterUserRequestModel request) {
        userService.registerUser(request);

        WebResponse<String> webResponse = new WebResponse<String>();
        webResponse.setData("OK");
        return webResponse;
    }

    @DeleteMapping(
            path = "/himasti/api/user/delete-all"
    )
    public WebResponse<String> deleteAllUsers (@RequestHeader(name = "TOKEN") String token) {
        WebResponse<String> webResponse = new WebResponse<>();


        if (token.equals("agustus163")) {
            userService.deleteAllUsers();
            webResponse.setData("OK");
        }

        else {
            webResponse.setData("FAILED");
        }

        return webResponse;
    }
}
