package com.freenow.security;

import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthTokenFilter extends OncePerRequestFilter
{
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException
    {
        try
        {
            String jwt = parseJwt(request);
            if (StringUtils.hasText(jwt) && !jwtUtil.isInvalid(jwt))
            {
                Claims claims = jwtUtil.getAllClaimsFromToken(jwt);
                String userName = String.valueOf(claims.get("userName"));
                String role = String.valueOf(claims.get("role"));

                UserDetails userDetails = new UserDetails();
                List<String> roleList = new ArrayList<>();
                roleList.add("ROLE_".concat(role));

                userDetails.setAuth(roleList);
                userDetails.setUserName(userName);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        catch (Exception e)
        {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }


    private String parseJwt(HttpServletRequest request)
    {
        String headerAuth = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer "))
            return headerAuth.substring(7);
        if (StringUtils.hasText(headerAuth))
            return headerAuth;
        return null;
    }

}
