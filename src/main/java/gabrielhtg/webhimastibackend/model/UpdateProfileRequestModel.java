package gabrielhtg.webhimastibackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequestModel {

    private String firstName;

    private String lastName;

    private String angkatan;

    private String kotaLahir;

    private Long tanggalLahir;
}
