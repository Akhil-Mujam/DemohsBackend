package com.example.Demohs.Controller;

import com.example.Demohs.Dto.UserAuthDataDto;
import com.example.Demohs.Entity.UserAuthData;
import com.example.Demohs.Service.UserAuthDataService;
import com.example.Demohs.util.AuthRequest;
import com.example.Demohs.util.JavaUtilToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/userauthdata")
public class UserAuthDataController {

    @Autowired
    private UserAuthDataService userAuthDataService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JavaUtilToken javaUtilToken;

    @PutMapping("/version/authenticate")
    public List<String> authenticateUser(@RequestBody AuthRequest authRequest, HttpServletResponse response) throws IllegalArgumentException {
        authenticateUserCredentials(authRequest);
        String accessToken = javaUtilToken.generateToken(authRequest.getUsername());
        String refreshToken = javaUtilToken.generateRefreshToken(authRequest.getUsername());
        addTokenCookieToResponse(response, "jwtToken", accessToken, 15*60);
        addTokenCookieToResponse(response, "refreshToken", refreshToken, 100 * 60);
        UserAuthData userAuthData= userAuthDataService.findByUserName(authRequest.getUsername());
        // Prioritize the roles: Admin > ClassTeacher > Teacher > Student
        List<String> roles = userAuthData.getUserRole();
        String prioritizedRole = prioritizeRole(roles);

        return List.of(prioritizedRole); // Return the most prioritized role
    }

    @GetMapping("/getUsername")
    public String getUsernameFromToken(@CookieValue(value = "jwtToken", defaultValue = "") String jwtToken) {
        if (jwtToken.isEmpty()) {
            throw new RuntimeException("JWT token is missing from cookies.");
        }

        // Validate the token and extract the username
        if (javaUtilToken.validateToken(jwtToken)) {
            String username = javaUtilToken.extractUsername(jwtToken);

            // Optionally, fetch user data from the database, if needed
            UserAuthData userAuthData = userAuthDataService.findByUserName(username);
            if (userAuthData != null) {
                return username; // Return username if found
            } else {
                throw new RuntimeException("User not found.");
            }
        } else {
            throw new RuntimeException("Invalid JWT token.");
        }
    }

    @GetMapping("/getUserRole")
    public List<String> getUserRolesFromToken(@CookieValue(value = "jwtToken", defaultValue = "") String jwtToken) {
        if (jwtToken.isEmpty()) {
            throw new RuntimeException("JWT token is missing from cookies.");
        }

        // Validate the token and extract the username
        if (javaUtilToken.validateToken(jwtToken)) {
            String username = javaUtilToken.extractUsername(jwtToken);

            // Fetch user roles from the database based on the username
            UserAuthData userAuthData = userAuthDataService.findByUserName(username);
            if (userAuthData != null) {
                // Return the list of roles associated with the user
                List<String> roles = userAuthData.getUserRole();
                String prioritizedRole = prioritizeRole(roles);

                return List.of(prioritizedRole);
            } else {
                throw new RuntimeException("User not found.");
            }
        } else {
            throw new RuntimeException("Invalid JWT token.");
        }
    }
    @PutMapping("/refresh")
    public List<String> refreshAccessToken(
            @CookieValue(name = "refreshToken", defaultValue = "") String refreshToken,
            HttpServletResponse response) {

        if (refreshToken.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is missing");
        }

        if (!javaUtilToken.validateToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired refresh token");
        }

        String username = javaUtilToken.extractUsername(refreshToken);
        String newAccessToken = javaUtilToken.generateToken(username);

        // Add the new access token to the response cookie
        addTokenCookieToResponse(response, "jwtToken", newAccessToken, 5 * 60 * 60);

        // Fetch and return user roles
        UserAuthData userAuthData = userAuthDataService.findByUserName(username);
        return userAuthData.getUserRole();
    }

    @PostMapping("/validate-token")
    public ResponseEntity<String> validateToken(@RequestParam String token) {
        // Validate the provided token
        boolean isValid = javaUtilToken.validateToken(token);
        if (isValid) {
            return ResponseEntity.ok("Token is valid");
        } else {
            return ResponseEntity.status(400).body("Invalid token");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        // Clear the jwtToken cookie
        clearCookie(response, "jwtToken");

        // Clear the refreshToken cookie
        clearCookie(response, "refreshToken");

        return ResponseEntity.ok("User logged out successfully");
    }

    private void authenticateUserCredentials(AuthRequest authRequest) throws IllegalArgumentException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("User not found for the given credentials in request");
        }
    }

    private String prioritizeRole(List<String> roles) {
        if (roles.contains("Admin")) return "Admin";
        if (roles.contains("ClassTeacher")) return "ClassTeacher";
        if (roles.contains("Teacher")) return "Teacher";
        if (roles.contains("Student")) return "Student";
        return "guest"; // Default if no roles are found
    }

    private void addTokenCookieToResponse(HttpServletResponse response, String cookieName, String token, int maxAgeInSeconds) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true); // Set as HTTP-only
        cookie.setSecure(true);  // Ensure it is sent over HTTPS
        cookie.setPath("/");     // Accessible throughout the app
        cookie.setMaxAge(maxAgeInSeconds); // Set expiration time in seconds
        response.addCookie(cookie);
//        cookie.setDomain("demohsbackend-production.up.railway.app"); // Set your domain here

        // Manually append the SameSite attribute and other cookie attributes
//        String cookieHeader = String.format("%s=%s; Max-Age=%d; Path=%s; HttpOnly; Secure; SameSite=None; Domain=%s",
//                cookieName,
//                token,
//                maxAgeInSeconds,
//                "/",
//                "your-domain.com"); // Replace with your actual domain
//
//        response.addHeader("Set-Cookie", cookieHeader);
    }


    private void clearCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null); // Set value to null
        cookie.setHttpOnly(true); // HTTP-only for security
        cookie.setSecure(true);  // Set true if using HTTPS
        cookie.setPath("/");      // Ensure the path matches where the cookie is set
        cookie.setMaxAge(0);      // Expire the cookie immediately
        response.addCookie(cookie);
    }

}

