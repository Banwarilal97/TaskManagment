
package com.example.taskmanager.controller;

import com.example.taskmanager.dto.*;
import com.example.taskmanager.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService auth;

	@PostMapping("/signup")
	public String signup(@RequestBody @Valid SignupRequest req) {
		return auth.signup(req);
	}

	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginRequest req) {
		return auth.login(req);
	}
}
