package com.tjsse.jikespace.auth_user;

import com.tjsse.jikespace.entity.Admin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @program: JiKeSpace_backend
 * @description: admin class for auth
 * @package_name: com.tjsse.jikespace.auth_user
 * @author: peng peng
 * @date: 2022/12/3
 **/

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AppAdmin implements UserDetails {
    private Admin admin;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    @Override
    public String getUsername() {
        return admin.getUsername();
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