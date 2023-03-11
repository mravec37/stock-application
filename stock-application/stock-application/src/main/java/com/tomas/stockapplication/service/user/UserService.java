package com.tomas.stockapplication.service.user;

import com.tomas.stockapplication.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUserName(String username);

    User addUser(User userInfo);


}
