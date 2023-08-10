package gabrielhtg.webhimastibackend.controller;

import gabrielhtg.webhimastibackend.entity.RegisterToken;
import gabrielhtg.webhimastibackend.model.WebResponse;
import gabrielhtg.webhimastibackend.service.RegisterTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class RegisterTokenController {
    @Autowired
    private RegisterTokenService registerTokenService;

    @GetMapping(
            path = "/api/himasti/registerToken/generate",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    private WebResponse<String> generateRegisterToken (@RequestHeader(name = "username") String reqUsername, @RequestHeader(name = "durasi") Double durasi) {
        if (registerTokenService.generateRegisterTokenService(reqUsername, durasi)) {
            return WebResponse.<String>builder().data("ok").error(false).build();
        }

        return WebResponse.<String>builder().error(true).pesanError("unauthorized").build();
    }

    @GetMapping (
            path = "/api/himasti/registerToken/get"
    )
    private WebResponse<RegisterToken> getRegisterToken (@RequestHeader(name = "username") String username, @RequestHeader(name = "token") String token) {
        RegisterToken registerToken = registerTokenService.getRegisterToken(username, token);

        if (registerToken != null) {
            return  WebResponse.<RegisterToken>builder().data(registerToken).error(false).build();
        }

        return WebResponse.<RegisterToken>builder().error(true).pesanError("unauthorized").build();
    }
}
