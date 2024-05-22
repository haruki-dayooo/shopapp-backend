package com.project.shopapp.services;

import com.project.shopapp.components.JwtTokenUntil;
import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.PermissionDeniedException;
import com.project.shopapp.models.Role;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.RoleRepository;
import com.project.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUntil jwtTokenUntil;
    private final AuthenticationManager authenticationManager;

    @Override
    public User createUser(UserDTO userDTO) throws Exception {

        String phoneNumber = userDTO.getPhoneNumber();

        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists!");
        }

        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found!"));

        if (role.getName().equalsIgnoreCase("ADMIN")) {
            throw new PermissionDeniedException("You cannot register an admin account!");
        }

        User newUser = User
                .builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .active(true)
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId()).build();

        newUser.setRole(role);

        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) throws Exception {
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if (user.isEmpty()) {
            throw new DataNotFoundException("Invalid phone number or password!");
        }
        User existingUser = user.get();
        //check password
        if (existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId() == 0) {
            if (!passwordEncoder.matches(password, existingUser.getPassword())) {
                throw new BadCredentialsException("Invalid phone number or password!");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password, existingUser.getAuthorities()
        );
        //authenticate with Java Spring security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUntil.generateToken(existingUser);
    }
}
