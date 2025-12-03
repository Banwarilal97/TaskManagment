
package com.example.taskmanager.service;

import com.example.taskmanager.dto.*;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.repository.UserRepository;

import jakarta.transaction.Transactional;

import com.example.taskmanager.config.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private PasswordEncoder encoder;

	@Transactional
	public String signup(SignupRequest req) {
		User u = new User();
		u.setName(req.getName());
		u.setEmail(req.getEmail());
		u.setPassword(encoder.encode(req.getPassword()));
		userRepo.save(u);
		return "Signup successful";
	}

	public LoginResponse login(LoginRequest req) {
		User u = userRepo.findByEmail(req.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
		if (!encoder.matches(req.getPassword(), u.getPassword()))
			throw new RuntimeException("Invalid credentials");

		return new LoginResponse(jwtUtil.generateToken(u.getEmail()));
	}
}
