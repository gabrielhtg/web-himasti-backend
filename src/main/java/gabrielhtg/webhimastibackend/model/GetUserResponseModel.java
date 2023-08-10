package gabrielhtg.webhimastibackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetUserResponseModel {
    private String firstName;

    private String lastName;

    private String angkatan;

    private Long tanggalLahir;

    private String kotaLahir;

    private boolean admin;
}
