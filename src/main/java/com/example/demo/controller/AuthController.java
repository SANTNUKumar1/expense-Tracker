package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.RefreshToken;
import com.example.demo.modal.UserInfoDto;
import com.example.demo.response.JwtResponseDTO;
import com.example.demo.service.JwtService;
import com.example.demo.service.RefreshTokenService;
import com.example.demo.service.UserDetailsServiceImpl;

@RestController
@RequestMapping("/auth/v1")
public class AuthController {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public AuthController(JwtService jwtService, RefreshTokenService refreshTokenService, 
                         UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserInfoDto userInfoDto) {
        try {
            Boolean isSignedUp = userDetailsService.signupUser(userInfoDto);
            if (Boolean.FALSE.equals(isSignedUp)) {
                return new ResponseEntity<>("User Already Exists", HttpStatus.BAD_REQUEST);
            }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUsername());
            String jwtToken = jwtService.GenerateToken(userInfoDto.getUsername());
            
            JwtResponseDTO response = new JwtResponseDTO();
            response.setAccessToken(jwtToken);
            response.setToken(refreshToken.getToken());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Exception occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
