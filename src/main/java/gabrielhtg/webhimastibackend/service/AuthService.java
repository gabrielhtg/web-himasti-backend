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
    public String login (UserLoginRequestModel request) {
        User user = userRepository.findById(request.getUsername()).orElse(null);

//        ! Kalau user null atau password yang dimasukkan user tidak tepat maka akan return false
        if (user == null || !BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            return null; // jika password tidak sama
        }

//        ! Session token yang disimpan adalah data browser digabung dengan uuid kemudian dienkripsi
        String token = UUID.randomUUID().toString();
        user.setToken(token);

        // dengan ini token akan aktif terhitung 1 hari dari saat ini
        user.setTokenExpiredAt(System.currentTimeMillis() + (1000L * 60 * 60 * 24)); // 1000 * 60 detik * 60 menit * 24jam * x hari
        userRepository.save(user);

        return token;
    }

    @Transactional
    public boolean logout (String username) {
        User user = userRepository.findById(username).orElse(null);

        if (user != null) {
            user.setTokenExpiredAt(null);
            user.setToken(null);
            userRepository.save(user);
            return true;
        }

        return false;
    }

    @Transactional
    public boolean isLogin (String username, String sessionToken) {
        User user = userRepository.findById(username).orElse(null);

        if (user != null && user.getToken() != null && user.getToken().equals(sessionToken)) {
            return (user.getTokenExpiredAt() > System.currentTimeMillis());
        }

        return false;
    }
}
