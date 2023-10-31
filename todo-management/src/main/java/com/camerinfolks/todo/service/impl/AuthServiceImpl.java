package com.camerinfolks.todo.service.impl;

import com.camerinfolks.todo.dto.RegisterDto;
import com.camerinfolks.todo.entity.Role;
import com.camerinfolks.todo.entity.User;
import com.camerinfolks.todo.exception.TodoApiException;
import com.camerinfolks.todo.repository.RoleRepository;
import com.camerinfolks.todo.repository.UserRepository;
import com.camerinfolks.todo.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public String register(RegisterDto registerDto) {

    if (userRepository.existsByUsername(registerDto.getUsername())){
        throw new TodoApiException(HttpStatus.BAD_REQUEST,"Username already exists");
    }

    if (userRepository.existsByEmail(registerDto.getEmail())){
        throw new TodoApiException(HttpStatus.BAD_REQUEST,"Email already exists");
    }

        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER");
        roles.add(userRole);

        user.setRoles(roles);

        userRepository.save(user);

        return "User registered successfully!!!";
    }
}
