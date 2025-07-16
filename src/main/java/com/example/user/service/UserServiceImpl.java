package com.example.user.service;

import com.example.exception.DomainException;
import com.example.exception.UsernameAlreadyExistsException;
import com.example.security.AuthenticationMetadata;
import com.example.user.model.User;
import com.example.user.model.UserRole;
import com.example.user.repository.UserRepository;
import com.example.wallet.model.Wallet;
import com.example.wallet.service.WalletService;
import com.example.web.dto.RegisterRequest;
import com.example.web.dto.UserEditRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WalletService walletService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, WalletService walletService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.walletService = walletService;
    }

    @Override
    @Transactional
    public void register(RegisterRequest registerRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(registerRequest.getUsername());

        if (optionalUser.isPresent()) {
            throw new UsernameAlreadyExistsException("Username [%s] already exist.".formatted(registerRequest.getUsername()));
        }

        User user = userRepository.save(initializeUser(registerRequest));

        Wallet wallet = walletService.initalizeWallet(user);

        addWalletToUser(user, wallet);

        userRepository.save(user);

        log.info("Wallet with id [%s] was added to User with username [%s].".formatted(wallet.getId(), user.getUsername()));

        log.info("Successfully create new user account for username [%s] and id [%s]".formatted(user.getUsername(), user.getId()));
    }

    private User initializeUser(RegisterRequest registerRequest) {

        return User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }

    private void addWalletToUser(User user, Wallet wallet) {
        user.setWallet(wallet);
    }

    @Override
    public void editUserDetails(Long userId, UserEditRequest userEditRequest) {
        User user = getById(userId);

        user.setFirstName(userEditRequest.getFirstName());
        user.setLastName(userEditRequest.getLastName());
        user.setProfilePictureUrl(userEditRequest.getProfilePictureUrl());

        log.info("Successfully updated details of user with id [%s].".formatted(userId));

        userRepository.save(user);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new DomainException("User with id [%s] does not exist.".formatted(id)));
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void switchStatus(Long userId) {
        User user = getById(userId);

        user.setActive(!user.isActive());
        userRepository.save(user);
    }

    @Override
    public void switchRole(Long userId, UserRole newRole) {
        User user = getById(userId);

        if (user.getRole() == newRole) {
            throw new DomainException("User with id [%s] already has this role.".formatted(userId));
        }

        user.setRole(newRole);

        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new DomainException("User with this username does not exist."));

        return new AuthenticationMetadata(user.getId(), username, user.getPassword(), user.getRole(), user.isActive());
    }
}