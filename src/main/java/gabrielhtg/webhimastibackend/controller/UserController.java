package gabrielhtg.webhimastibackend.controller;

import gabrielhtg.webhimastibackend.model.*;
import gabrielhtg.webhimastibackend.service.RegisterTokenService;
import gabrielhtg.webhimastibackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RegisterTokenService registerTokenService;

    @PostMapping(
            path = "/api/himasti/user/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    private WebResponse<String> registerUser (@RequestBody RegisterUserRequestModel request, @RequestHeader(name = "TOKEN") String token) {

        log.info("Fungsi registerUser dieksekusi!");

        System.out.println(request.toString());
        if (registerTokenService.isRegisterTokenActive(token)) {
            boolean temp = userService.registerUser(request);

            if(temp) {
                return WebResponse.<String>builder().data("OK").error(false).build();
            }

            return WebResponse.<String>builder().error(true).pesanError("Username already used!").build();

        }

        return WebResponse.<String>builder().error(true).pesanError("unauthorized").build();
    }

    /*
     * Method ini berfungsi untuk menghapus semua user yang ada di database
     */
    @DeleteMapping(
            path = "/api/himasti/user/delete-all"
    )
    private WebResponse<String> deleteAllUsers (@RequestHeader(name = "TOKEN") String token) {
        if (token.equals("agustus163")) {
            userService.deleteAllUsers();
            return WebResponse.<String>builder().data("OK").error(false).build();
        }

        return WebResponse.<String>builder().error(true).build();
    }

    @GetMapping(
            path = "/api/himasti/user/is-data-lengkap"
    )
    private WebResponse<String> isDataLengkap (@RequestHeader(name = "username") String username) {
        if (userService.isDataLengkap(username)) {
            return WebResponse.<String>builder().data("ok").error(false).build();
        }

        return WebResponse.<String>builder().error(true).pesanError("user belum lengkap datanya").build();
    }

    @GetMapping(
            path = "/api/himasti/user/get-user"
    )
    private WebResponse<GetUserResponseModel> getUser (@RequestHeader(name = "username") String username) {
        return WebResponse.<GetUserResponseModel>builder().data(userService.getUser(username)).build();
    }

    @PostMapping(
            path = "/api/himasti/user/upload-foto-profil",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    private WebResponse<String> uploadFotoProfil (@RequestHeader(name = "username") String username, @RequestHeader(name ="token") String token, @RequestPart(name = "fotoProfil") MultipartFile  fotoProfil) {
        int result = userService.uploadFotoProfil(username, token, fotoProfil);

        if (result == 1) {
            return WebResponse.<String>builder().data("ok").error(false).build();
        }

        else if (result == 2) {
            return WebResponse.<String>builder().error(true).pesanError("token tidak aktif").build();
        }

        else {
            return WebResponse.<String>builder().error(true).pesanError("user tidak ditemukan").build();
        }
    }

    @GetMapping(
            path = "/api/himasti/user/foto/{username}/{userToken}"
    )
    private ResponseEntity<?> getFotoProfil(@PathVariable(name = "username") String username, @PathVariable("userToken") String userToken) throws IOException {
        log.info("Fungsi getFotoProfilDieksekusi!");

        byte[] fileData = userService.getFotoProfil(username, userToken);

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_PNG).body(fileData);
    }

    @PostMapping(
            path = "/api/himasti/user/update-profile",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    private WebResponse<String> updateProfil (@RequestHeader(name = "username") String username, @RequestHeader(name = "token") String token, @RequestBody UpdateProfileRequestModel requestModel) {
        int response = userService.updateProfile(username, token, requestModel);

        if (response == 1) {
            return  WebResponse.<String>builder().data("ok").error(false).build();
        }

        else if (response == 2) {
            return WebResponse.<String>builder().error(true).pesanError("token user tidak aktif").build();
        }

        else {
            return WebResponse.<String>builder().error(true).pesanError("user tidak ditemukan").build();
        }
    }


}
