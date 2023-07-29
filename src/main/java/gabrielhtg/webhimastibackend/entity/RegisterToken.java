package gabrielhtg.webhimastibackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "register_token")
public class RegisterToken {
    @Id
    @Column(name = "register_token")
    private String registerToken;

    @Column(name = "active_until")
    private Long activeUntil;
}
