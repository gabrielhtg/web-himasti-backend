package gabrielhtg.webhimastibackend.service;

import gabrielhtg.webhimastibackend.entity.User;
import gabrielhtg.webhimastibackend.model.RegisterUserRequestModel;
import gabrielhtg.webhimastibackend.repository.UserRepository;
import gabrielhtg.webhimastibackend.security.BCrypt;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/*
  * Ini adalah service yang digunakan untuk melakukan perubahan
  * terhadap data ataupun objek yang berkaitan dengan Entity User
 */

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /*
      * Method registerUser ini digunakan untuk menambahkan user baru ke dalam database.
      * method ini membutuhkan RegisterUserRequestModel yang digunakan sebagai template
      * data user yang melakukan request untuk dimasukkan ke Entity user actual.
     */
    @Transactional
    public void registerUser (RegisterUserRequestModel request) {
        if (userRepository.existsById(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already used");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt())); // yang disimpan adalah password terenkripsi
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
//        user.setFotoProfil(request.getFotoProfil());
//        user.setAngkatan(request.getAngkatan());
//        user.setTglLahir(request.getTglLahir());
//        user.setKotaLahir(request.getKotaLahir());
        user.setTglDaftar(System.currentTimeMillis());
//        user.setAdmin(request.getAdmin());

        userRepository.save(user);
    }

    @Transactional
    public void deleteAllUsers () {
        userRepository.deleteAll();
    }

    @Transactional
    public boolean isAdmin (String request) {
        User user = userRepository.findById(request).orElse(null);

//        ketika user tidak null dan user adalah admin, maka kita akan return true
        return (user != null) && (user.getAdmin());
    }

}
