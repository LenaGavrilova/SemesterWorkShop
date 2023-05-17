package ru.kpfu.itis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.models.User;
import ru.kpfu.itis.repositories.UserRepo;


@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepo userRepo;

    @Autowired
    public UserDetailService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByLogin(username);

        if (user.equals(null)) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return new UserDetail(user);
    }
}
