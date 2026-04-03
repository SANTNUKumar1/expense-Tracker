package com.example.demo.service;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.entity.UserInfo;
import com.example.demo.entity.UserRole;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private String username;
    private String password;
    Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UserInfo byUsername) {
        // Use reflection to get the private fields if Lombok methods don't work
        try {
            this.username = (String) byUsername.getClass().getDeclaredField("username").get(byUsername);
            this.password = (String) byUsername.getClass().getDeclaredField("password").get(byUsername);
            byUsername.getClass().getDeclaredField("username").setAccessible(true);
            byUsername.getClass().getDeclaredField("password").setAccessible(true);
            this.username = (String) byUsername.getClass().getDeclaredField("username").get(byUsername);
            this.password = (String) byUsername.getClass().getDeclaredField("password").get(byUsername);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        List<GrantedAuthority> auths = new ArrayList<>();

        for(UserRole role : byUsername.getRoles()){
            auths.add(new SimpleGrantedAuthority(role.getName().toUpperCase()));
        }
        this.authorities = auths;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
}
