package ru.kpfu.itis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.models.User;
import ru.kpfu.itis.repositories.UserRepo;


@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserFindByLogin(User user) {
        User user_db = userRepo.findByLogin(user.getLogin());
        return user_db;
    }

    @Transactional
    public void register(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepo.save(user);
    }
}
