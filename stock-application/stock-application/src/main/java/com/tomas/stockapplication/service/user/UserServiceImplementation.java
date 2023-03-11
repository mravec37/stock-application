package com.tomas.stockapplication.service.user;

import com.tomas.stockapplication.entity.User;
import com.tomas.stockapplication.entity.Watchlist;
import com.tomas.stockapplication.repository.UserRepository;
import com.tomas.stockapplication.repository.WatchlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImplementation implements UserService{

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findByUserName(String username) {
      return  userRepository.findByUserName(username);
    }

    @Override
    public User addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Watchlist watchlist = new Watchlist();
        watchlist.setUser(user);
        user.setWatchlist(watchlist);
        watchlistRepository.save(watchlist);
        return userRepository.save(user);
    }
}
