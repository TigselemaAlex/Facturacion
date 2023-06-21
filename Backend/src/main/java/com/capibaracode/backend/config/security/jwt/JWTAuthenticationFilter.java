package com.capibaracode.backend.config.security.jwt;

import com.capibaracode.backend.config.security.model.UserPrincipal;
import com.capibaracode.backend.config.tenancity.TenantIdentifierResolver;
import com.capibaracode.backend.infraestructure.services.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private  JWTProvider jwtProvider;

    @Autowired
    private TenantIdentifierResolver tenantIdentifierResolver;

    @Autowired
    private UserServiceImpl userService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJWTFromRequest(request);
            System.out.println(jwt);
            tenantIdentifierResolver.getAnyConnection();
            if(Objects.nonNull(jwt) && StringUtils.hasText(jwt) && jwtProvider.validateJWT(jwt)){
                String username = jwtProvider.getEmailFromJWT(jwt);
                UserPrincipal userDetails = (UserPrincipal) userService.loadUserByUsername(username);
                System.out.println(userDetails.getTenant());

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                tenantIdentifierResolver.getConnection(userDetails.getTenant());
            }
        }catch (Exception e){
            logger.error("Could not set user authentication in security context: " +  e.getMessage());

        }
        filterChain.doFilter(request, response);
    }

    private String getJWTFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.split("\\s+")[1];
        }
        return null;
    }
}
