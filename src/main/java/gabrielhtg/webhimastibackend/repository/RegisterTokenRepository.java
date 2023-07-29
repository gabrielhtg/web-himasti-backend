package gabrielhtg.webhimastibackend.repository;


import gabrielhtg.webhimastibackend.entity.RegisterToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterTokenRepository extends JpaRepository<RegisterToken, String> {
}
