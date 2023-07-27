package gabrielhtg.webhimastibackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    private String username;

    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Lob
    @Column(name = "foto_profil")
    private Byte[] fotoProfil;

    private String angkatan;

    @Column(name = "tgl_lahir")
    private Long tglLahir;

    @Column(name = "kota_lahir")
    private String kotaLahir;

    @Column(name = "tgl_daftar")
    private Long tglDaftar;

    private String token;

    @Column(name = "token_expired_at")
    private Long tokenExpiredAt;

    private Boolean admin;
}
