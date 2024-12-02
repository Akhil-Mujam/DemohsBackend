package com.example.Demohs.config;

import com.example.Demohs.util.JavaUtilToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@Order(1)
public class AuthorizationFilter extends OncePerRequestFilter {
    @Autowired
    JavaUtilToken javaUtilToken;

    @Autowired
    UserDetailsService userDetailService;

//    @Override
//    protected  void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException
//    {
//        String authorization=request.getHeader("Authorization");
//        String token=null;
//        String uname=null;
//        if(authorization!=null&&authorization.startsWith("Bearer "))
//        {
//            token=authorization.substring(7);
//            uname=javaUtilToken.extractUsername(token);
//        }
//        if(uname!=null && SecurityContextHolder.getContext().getAuthentication()==null)
//        {
//            UserDetails userDetails=userDetailService.loadUserByUsername(uname);
//            if(javaUtilToken.validateToken(token))
//            {
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
//                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//            }
//        }
//        filterChain.doFilter(request,response);
//    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Look for the token in the cookies
        String token = getTokenFromCookies(request);
        String uname = null;

        if (token != null) {
            // Extract username from token
            uname = javaUtilToken.extractUsername(token);
        }

        // If the username exists and the security context doesn't already have authentication
        if (uname != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load the user details by username
            UserDetails userDetails = userDetailService.loadUserByUsername(uname);

            // Validate the token
            if (javaUtilToken.validateToken(token)) {
                // Create a new authentication object
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Continue with the request
        filterChain.doFilter(request, response);
    }

    // Utility method to extract the token from the cookies
    private String getTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // Check for the name of the cookie where the token is stored (e.g., "jwtToken")
                if ("jwtToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null; // Return null if no token found
    }


}
