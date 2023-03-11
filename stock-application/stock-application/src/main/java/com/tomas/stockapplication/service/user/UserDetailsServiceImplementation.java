package com.tomas.stockapplication.service.user;

import com.tomas.stockapplication.entity.User;
import com.tomas.stockapplication.model.UserDetailsImplementation;
import com.tomas.stockapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Component
public class UserDetailsServiceImplementation implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUserName(username);
       UserDetailsImplementation user = new UserDetailsImplementation(userOptional.get());
        return user;
    }
}
