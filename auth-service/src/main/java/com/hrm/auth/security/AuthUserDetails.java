package com.hrm.auth.security;

import com.hrm.auth.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AuthUserDetails implements UserDetails {

    private final User user; // Gói đối tượng User do MBG tạo

    public AuthUserDetails(User user) {
        this.user = user;
    }

    // Các Getter tiện ích
    public String getEmployeeId() {
        return user.getEmployeeId();
    }

    public String getUserId() {
        if (user.getId() == null) {
            return null;
        }
        return user.getId().toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
