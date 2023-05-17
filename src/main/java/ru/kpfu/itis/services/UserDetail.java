package ru.kpfu.itis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kpfu.itis.models.User;

import java.util.Collection;
import java.util.Collections;

public class UserDetail implements UserDetails {

    private final User user;

    @Autowired
    public UserDetail(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
    }


    @Override
    public String getPassword() {
        return this.user.getPassword();
    }


    @Override
    public String getUsername() {
        return this.user.getLogin();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }


    public User getUser() {
        return this.user;
    }
}
