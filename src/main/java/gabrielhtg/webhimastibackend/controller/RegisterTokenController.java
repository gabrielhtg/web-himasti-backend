package gabrielhtg.webhimastibackend.controller;

import gabrielhtg.webhimastibackend.model.WebResponse;
import gabrielhtg.webhimastibackend.service.RegisterTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterTokenController {
    @Autowired
    private RegisterTokenService registerTokenService;

    @GetMapping(
            path = "/himasti/api/registerToken/generate",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    private WebResponse<String> generateRegisterToken (@RequestHeader(name = "username") String reqUsername, @RequestHeader(name = "durasi") Integer durasi) {
        if (registerTokenService.generateRegisterTokenService(reqUsername, durasi)) {
            return WebResponse.<String>builder().data("ok").error(false).build();
        }

        return WebResponse.<String>builder().error(true).pesanError("unauthorized").build();
    }
}
