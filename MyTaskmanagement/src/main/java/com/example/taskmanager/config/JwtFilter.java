
package com.example.taskmanager.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.entity.User;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserRepository userRepo;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException {

		String header = req.getHeader("Authorization");
		String token = null;
		String email = null;

		if (header != null && header.startsWith("Bearer ")) {
			token = header.substring(7);
			email = jwtUtil.extractEmail(token);
		}

		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			User user = userRepo.findByEmail(email).orElse(null);

			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, List.of());

			SecurityContextHolder.getContext().setAuthentication(auth);
		}

		chain.doFilter(req, res);
	}
}
