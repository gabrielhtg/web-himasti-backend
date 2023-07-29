package gabrielhtg.webhimastibackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequestModel {
    private String username;

    private String password;

    private String firstName;

    private String lastName;

//    private Byte[] fotoProfil;
//
//    private String angkatan;
//
//    private Long tglLahir;
//
//    private String kotaLahir;
//
//    private Boolean admin;
}
