package gabrielhtg.webhimastibackend.controller;

import gabrielhtg.webhimastibackend.entity.User;
import gabrielhtg.webhimastibackend.model.RegisterUserRequestModel;
import gabrielhtg.webhimastibackend.model.WebResponse;
import gabrielhtg.webhimastibackend.service.RegisterTokenService;
import gabrielhtg.webhimastibackend.service.UserService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RegisterTokenService registerTokenService;

    @PostMapping(
            path = "/himasti/api/user/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> registerUser (@RequestBody RegisterUserRequestModel request, @RequestHeader(name = "TOKEN") String token) {

        if (registerTokenService.isTokenActive(token)) {
            userService.registerUser(request);
            return WebResponse.<String>builder().data("OK").error(false).build();
        }

        return WebResponse.<String>builder().error(true).pesanError("unauthorized").build();
    }

    /*
     * Method ini berfungsi untuk menghapus semua user yang ada di database
     */
    @DeleteMapping(
            path = "/himasti/api/user/delete-all"
    )
    public WebResponse<String> deleteAllUsers (@RequestHeader(name = "TOKEN") String token) {
        if (token.equals("agustus163")) {
            userService.deleteAllUsers();
            return WebResponse.<String>builder().data("OK").error(false).build();
        }

        return WebResponse.<String>builder().error(true).build();
    }
}
