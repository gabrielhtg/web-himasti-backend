package gabrielhtg.webhimastibackend.service;

import gabrielhtg.webhimastibackend.entity.User;
import gabrielhtg.webhimastibackend.model.GetUserResponseModel;
import gabrielhtg.webhimastibackend.model.LengkapiDataRequestModel;
import gabrielhtg.webhimastibackend.model.RegisterUserRequestModel;
import gabrielhtg.webhimastibackend.model.UpdateProfileRequestModel;
import gabrielhtg.webhimastibackend.repository.UserRepository;
import gabrielhtg.webhimastibackend.security.BCrypt;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Base64;

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
    public boolean registerUser (RegisterUserRequestModel request) {
        if (userRepository.existsById(request.getUsername())) {
            return false;
        }

        System.out.println(request.getPassword());

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

        return true;
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

    @Transactional
    public boolean isDataLengkap (String username) {
        User user = userRepository.findById(username).orElse(null);

        return user != null &&
                user.getFotoProfil() != null &&
                user.getAngkatan() != null &&
                user.getKotaLahir() != null &&
                user.getTglLahir() != null;
    }

    @Transactional
    public GetUserResponseModel getUser (String username) {
        User user = userRepository.findById(username).orElse(null);

        if (user != null) {
//            return GetUserResponseModel.builder().firstName(user.getFirstName()).lastName(user.getLastName()).angkatan(user.getAngkatan()).fotoProfil(Base64.getEncoder().encodeToString(user.getFotoProfil())).kotaLahir(user.getKotaLahir()).tanggalLahir(user.getTglLahir()).build();
            return GetUserResponseModel.builder().firstName(user.getFirstName()).lastName(user.getLastName()).angkatan(user.getAngkatan()).kotaLahir(user.getKotaLahir()).tanggalLahir(user.getTglLahir()).admin(user.getAdmin()).build();
        }

        return null;
    }

    /*
        fungsi uploadFotoProfil ini adalah fungsi yang dapat digunakan untuk menambahkan atau update foto profil user.

        Return dari fungsi ini adalah integer dengan ketentuan sebagai berikut :
        1 -> berhasil melakukan update atau set foto profil
        2 -> user ditemukan tetapi tokennya sudah tidak aktif. sehingga tidak diizinkan untuk melakukan aksi ini
        3 -> user tidak ditemukan
     */
    @Transactional
    public int uploadFotoProfil (String username, String token, MultipartFile file) {
        User user = userRepository.findById(username).orElse(null);

        if (user != null) {
            if (isTokenActive(user, token)) {
                try {
                    user.setFotoProfil(file.getBytes());

                    return 1;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return 2;
        }
        return 3;
    }

    @Transactional
    public byte[] getFotoProfil (String username, String token) {
        User user = userRepository.findById(username).orElse(null);

        if (user !=  null && isTokenActive(user, token)) {
            return user.getFotoProfil();
        }

        return null;
    }

    /*
        Fungsi updateProfile ini digunakan untuk melakukan update profile user. Tetapi disini tidak ada opsi untuk
        update password user. Akan ada fungsi tersendiri untuk update user.

        return dari updateProfile ini adalah integer. Dimana return masing-masing nilai integer ini memiliki
        pesan tersendiri antara lain:
         1 -> tidak ada error. Berhasil melakukan updateProfil
         2 -> error. user ditemukan tapi token tidak ditemukan atau tidak aktif lagi
         3 -> error. user tidak ditemukan
     */
    @Transactional
    public int updateProfile (String username, String token, UpdateProfileRequestModel requestModel) {
        User user = userRepository.findById(username).orElse(null);

        System.out.println(requestModel.toString());

        // ketika user ada
        if (user != null) {

            // ketika token user masih aktif
            if (isTokenActive(user, token)) {

                if (requestModel.getFirstName() != null) {
                    user.setFirstName(requestModel.getFirstName());
                }

                if (requestModel.getLastName() != null) {
                    user.setLastName(requestModel.getLastName());
                }

                user.setAngkatan(requestModel.getAngkatan());
                user.setKotaLahir(requestModel.getKotaLahir());
                user.setTglLahir(requestModel.getTanggalLahir());

                userRepository.save(user);
                return 1;
            }

            return 3;
        }

        return  2;
    }

    public boolean isTokenActive (User user , String token) {
        return (user != null) && user.getToken().equals(token) && (user.getTokenExpiredAt() > System.currentTimeMillis());
    }
}
