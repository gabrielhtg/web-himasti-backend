package gabrielhtg.webhimastibackend.service;

import gabrielhtg.webhimastibackend.entity.User;
import gabrielhtg.webhimastibackend.model.UserLoginRequestModel;
import gabrielhtg.webhimastibackend.repository.UserRepository;
import gabrielhtg.webhimastibackend.security.BCrypt;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public boolean Login (UserLoginRequestModel request) {
        User user = userRepository.findById(request.getUsername()).orElse(null);

        if (user == null || !BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            return false; // jika password tidak sama
        }

        user.setToken(UUID.randomUUID().toString());

        // dengan ini token akan aktif terhitung 7 hari dari saat ini
        user.setTokenExpiredAt(System.currentTimeMillis() + (1000L * 60 * 60 * 24* 7)); // 1000 * 60 detik * 60 menit * 24jam * 7 hari
        userRepository.save(user);

        return true;
    }
}
