package com.subscriptions.subscriptions.userdetails;

import com.subscriptions.subscriptions.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {

    private String userName;
  private  String password;

  private  final List<? extends GrantedAuthority> grantedAuthorities;


    public MyUserDetails(  User user){

        this.userName = user.getEmail();
        this.password = user.getPassword();
        this.grantedAuthorities = Arrays.stream(user.getRoles().split(",")).map(SimpleGrantedAuthority::new ).collect(Collectors.toList());
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
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
