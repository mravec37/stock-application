package com.tomas.stockapplication.controller;


import com.tomas.stockapplication.dto.AuthRequest;
import com.tomas.stockapplication.dto.UserLogoutRequest;
import com.tomas.stockapplication.dto.UserRegistrationRequest;
import com.tomas.stockapplication.entity.User;
//import com.tomas.stockapplication.service.jwt.JwtService;
import com.tomas.stockapplication.exception.UserAlreadyExistsException;
import com.tomas.stockapplication.service.stock.StockService;
import com.tomas.stockapplication.service.user.UserService;
import com.tomas.stockapplication.service.watchlist.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private WatchlistService watchlistService;

    @Autowired
    private StockService stockService;

    //    @Autowired
//    private JwtService jwtService;
    private final String USER_ROLES = "ROLE_USER";


    @PostMapping("/authenticateUser")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        System.out.println("Zaciatok metody");
        System.out.println("meno: " + authRequest.getUsername() + " heslo: " + authRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        System.out.println("manager authenticoval");
        if (authentication.isAuthenticated()) {
            //System.out.println("vytvaram token");
            //return jwtService.generateToken(authRequest.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body("User authenticated. Name: " + authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("user not found");
        }
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/isAuthenticated")
    public ResponseEntity<String> checkAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.OK).body("User authenticated");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/registerUser")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        String username = userRegistrationRequest.getUserName();
        if (userService.findByUserName(username).isPresent()) {
            throw new UserAlreadyExistsException("User with this name already exists");
        }
        System.out.println("Uzivatel registrovany");
        User newUser = User.builder()
                .userName(userRegistrationRequest.getUserName())
                .email(userRegistrationRequest.getEmail())
                .password(userRegistrationRequest.getPassword())
                .roles(USER_ROLES)
                .build();
        userService.addUser(newUser);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRegistrationRequest.getUserName(),
                userRegistrationRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.OK).body("User registered. Name: " + userRegistrationRequest.getUserName());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user");
        }
    }


    @PostMapping("/logoutUser")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("User logged out successfully");
    }
}

