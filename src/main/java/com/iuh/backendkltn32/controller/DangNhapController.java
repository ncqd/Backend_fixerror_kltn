package com.iuh.backendkltn32.controller;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iuh.backendkltn32.config.JwtUntils;
import com.iuh.backendkltn32.config.UserInfoUserDetails;
import com.iuh.backendkltn32.dto.JwtResponse;
import com.iuh.backendkltn32.dto.LoginRequest;

@RestController
@RequestMapping("/api/xac-thuc")
public class DangNhapController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUntils jwtService;


	@PostMapping("/dang-nhap")
	public ResponseEntity<?> dangNhap(@Validated @RequestBody LoginRequest loginRequest) {
		String tenTaiKhoan = loginRequest.getTenTaiKhoan();
		// Xác thực từ username và password.
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(tenTaiKhoan, loginRequest.getPassword()));

		System.out.println("DangNhapController-dangNhap- " + loginRequest.getTenTaiKhoan());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtService.generateJWTToken(authentication);

		UserInfoUserDetails userDetailsImpl = (UserInfoUserDetails) authentication.getPrincipal();

		List<String> roles = userDetailsImpl.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, "Bearer ", userDetailsImpl.getUsername(), roles.get(0)));

	}

	@PostMapping("/test")
	public ResponseEntity<?> test(@Validated @RequestBody LoginRequest loginRequest) {
		
		// Xác thực từ username và password.
				Authentication authentication = authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getTenTaiKhoan(), loginRequest.getPassword()));

				System.out.println("DangNhapController-dangNhap- " + loginRequest.getTenTaiKhoan());
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
				String jwt = jwtService.generateJWTToken(authentication);

				UserInfoUserDetails userDetailsImpl = (UserInfoUserDetails) authentication.getPrincipal();

				List<String> roles = userDetailsImpl.getAuthorities().stream().map(item -> item.getAuthority())
						.collect(Collectors.toList());
				

				return ResponseEntity.ok(new JwtResponse(jwt, "Bearer ", userDetailsImpl.getUsername(), roles.get(0)));

	}

}
