package com.warehousemanagement.configuration.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class handles the authentication via JWT.
 */
public class JwtConfigurationFilter extends OncePerRequestFilter {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtConfigurationFilter.class);

    /**
     * The <code>UserDetailsService</code> to load user detail data.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * The util that internally handles tokens.
     */
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * Name of the header attribute where the access token is expected.
     */
    @Value("${security.jwt.header}")
    private String accessTokenHeader;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain chain) throws ServletException, IOException {

        String authToken = request.getHeader(accessTokenHeader);

        String username = null;

        if (authToken != null && !authToken.isEmpty()) {
            username = jwtTokenUtil.getUsernameFromToken(authToken);
        }

        JwtConfigurationFilter.LOGGER.debug("Checking authentication for user '{}'...", username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;
            try {
                // load user data from database, throws UsernameNotFoundException if no user with username found
                userDetails = userDetailsService.loadUserByUsername(username);

                // check token validity and set security context
                Boolean isTokenValid = jwtTokenUtil.validateToken(authToken, userDetails);
                if (Boolean.TRUE.equals(isTokenValid)) {
                    final UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    JwtConfigurationFilter.LOGGER.debug("Authenticated user '{}', setting security context.", username);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (final UsernameNotFoundException e) {
                JwtConfigurationFilter.LOGGER.debug("No user '{}' found in database: {}", username, e.getMessage(), e);
            }
        }

        chain.doFilter(request, response);
    }

}
