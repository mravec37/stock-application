package com.tomas.stockapplication.model;

import com.tomas.stockapplication.entity.User;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Setter
public class UserDetailsImplementation implements UserDetails {

   private String userName;
   private String password;
   private List<GrantedAuthority> authorities;
   private boolean isAccountNonExpired;
   private boolean isAccountNonLocked;
   private boolean isCredentialsNonExpired;
   private boolean isEnabled;

    public UserDetailsImplementation(User user) {
        setUpUserDetailsInformation(user);
    }

    private void setUpUserDetailsInformation(User user) {
        this.userName = user.getUserName();
        this.password = user.getPassword();
        this.authorities = setUserDetailsAuthorities(user.getRoles());
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
        this.isEnabled = true;
    }

    private List<GrantedAuthority> setUserDetailsAuthorities(String roles) {
      return  Arrays.stream(roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
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
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
