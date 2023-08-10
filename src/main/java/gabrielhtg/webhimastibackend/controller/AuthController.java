package gabrielhtg.webhimastibackend.controller;

import gabrielhtg.webhimastibackend.model.UserLoginRequestModel;
import gabrielhtg.webhimastibackend.model.WebResponse;
import gabrielhtg.webhimastibackend.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
@Slf4j
//@CrossOrigin(origins = {"http://localhost:3000", "http://192.168.43.201:3000"}, allowCredentials = "true", exposedHeaders = {"Set-Cookie"})
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
        String result = authService.login(requestModel);

        if (result != null) {
//            Cookie cookie = new Cookie("username", requestModel.getUsername());
//            cookie.setPath("/");
//            cookie.setMaxAge(30 * 24 * 60 * 60);
//            cookie.setSecure(true);
//            response.addCookie(cookie);

//            ResponseCookie responseCookie = ResponseCookie.from("username", requestModel.getUsername())
//                    .path("/")
//                    .maxAge(Duration.ofDays(30))
//                    .sameSite("lax").build();

//            response.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
            return WebResponse.<String>builder().data(result).error(false).build();
        }

        return WebResponse.<String>builder().error(true).build();
    }

    @DeleteMapping(
            path = "/api/himasti/auth/logout"
    )
    private ResponseEntity<Void> logout(@RequestHeader(name = "username") String logoutUsername) {
        if (authService.logout(logoutUsername)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping(
            path = "/api/himasti/auth/islogin"
    )
    private WebResponse<String> isLogin(@RequestHeader(name = "username") String username, @RequestHeader(name = "session-token") String sessionToken) {
        if (authService.isLogin(username, sessionToken)) {
            return WebResponse.<String>builder().error(false).data("ok").build();
        }

        return WebResponse.<String>builder().error(true).pesanError("not login yet").build();
    }
}
