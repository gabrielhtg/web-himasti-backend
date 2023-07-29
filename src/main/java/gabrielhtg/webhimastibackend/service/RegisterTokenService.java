package gabrielhtg.webhimastibackend.service;

import gabrielhtg.webhimastibackend.entity.RegisterToken;
import gabrielhtg.webhimastibackend.repository.RegisterTokenRepository;
import gabrielhtg.webhimastibackend.repository.UserRepository;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RegisterTokenService {
    @Autowired
    private RegisterTokenRepository registerTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    // * User yang bisa melakukan generate register token service adalah user yang
    // * berstatus sebagai admin
    @Transactional
    public boolean generateRegisterTokenService (String reqUsername, Integer hari) {
        if (userService.isAdmin(reqUsername)) {
            RegisterToken registerToken = new RegisterToken();
            registerToken.setRegisterToken(UUID.randomUUID().toString());
            registerToken.setActiveUntil(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * hari));
            registerTokenRepository.save(registerToken);

            return true;
        }

        return false;
    }

    /*
      * Method ini berfungsi untuk memeriksa apakah token yang di request oleh user masih aktif atau tidak
     */
    @Transactional
    public boolean isTokenActive (String token) {
        RegisterToken registerToken = registerTokenRepository.findById(token).orElse(null);

        return (registerToken != null) && (registerToken.getActiveUntil() > System.currentTimeMillis());
    }
}
