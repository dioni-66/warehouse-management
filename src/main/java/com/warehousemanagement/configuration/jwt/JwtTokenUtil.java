package com.warehousemanagement.configuration.jwt;

import com.warehousemanagement.pojo.ApplicationUserDetails;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Util class for JWT operations.
 */
@Component
public final class JwtTokenUtil {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

    /**
     * the name of the username attribute inside the tokens.
     */
    private static final String CLAIM_KEY_USERNAME = "sub";

    /**
     * the name of the creation date attribute inside the tokens.
     */
    private static final String CLAIM_KEY_CREATED = "created";


    /**
     * The algorithm to be used for creating signatures.
     */
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    /**
     * the secret used for token encryption as given in the properties.
     */
    @Value("${security.jwt.secret}")
    private String secret;

    /**
     * the access token expiration as given in the properties.
     */
    @Value("${security.jwt.expiration}")
    private Long accessTokenExpiration;

    @Value("${security.jwt.header:Authorization}")
    private String accessTokenHeader;

    /**
     * Extracts the username from the given token.
     *
     * @param token the token.
     * @return the extracted username.
     */
    public String getUsernameFromToken(final String token) {
        final Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            return claims.getSubject();
        }
        return null;
    }

    /**
     * Extracts the creation date from the given token.
     *
     * @param token the token.
     * @return the extracted creation date.
     */
    public Date getCreatedDateFromToken(final String token) {
        final Claims claims = getClaimsFromToken(token);
        if (claims != null && claims.get(JwtTokenUtil.CLAIM_KEY_CREATED) != null) {
            return new Date((Long) claims.get(JwtTokenUtil.CLAIM_KEY_CREATED));
        }
        return null;
    }

    /**
     * Internally extracts the information of the given token and returns a <code>Claims</code> value objects containing
     * the information.
     *
     * @param token the token.
     * @return the extracted information.
     */
    private Claims getClaimsFromToken(final String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            //claims are needed because additional checks are done with the expiration date claim during validation
            JwtTokenUtil.LOGGER.debug("Error extracting claims information from token: {}", e.getMessage(), e);
            return e.getClaims();
        } catch (final IllegalArgumentException | JwtException e) {
            JwtTokenUtil.LOGGER.debug("Error extracting claims information from token: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Generates an expiration date for a newly created token.
     *
     * @return the expiration date.
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + accessTokenExpiration * 1000);
    }

    /**
     * Checks if the given token is already expired.
     * Additionally check if the token expiry date is equal to today's date
     *
     * @param token               the token.
     * @return true, if the token is expired or does not contain a expiration information.
     */
    private Boolean isTokenExpired(final String token) {
        final Claims claims = getClaimsFromToken(token);
        if (claims != null && claims.getExpiration() != null) {
            Date tokenExpirationDate = claims.getExpiration();
            return tokenExpirationDate.before(new Date());
        }
        return true;
    }

    /**
     * Creates a new token based on the given <code>UserDetails</code> information.
     *
     * @param currentUsername      the username used for token generation.
     * @return the new token.
     */
    public String generateToken(final String currentUsername) {
        final Map<String, Object> claims = new HashMap<>();
        claims.put(JwtTokenUtil.CLAIM_KEY_USERNAME, currentUsername);
        claims.put(JwtTokenUtil.CLAIM_KEY_CREATED, new Date());

        return generateToken(claims);
    }

    /**
     * Internally creates a new token based on the given information.
     *
     * @param claims a Map containing the information needed.
     * @return the new token.
     */
    private String generateToken(final Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate())
                .signWith(JwtTokenUtil.SIGNATURE_ALGORITHM, secret).compact();
    }

    /**
     * Validates the given token against a given <code>UserDetails</code> object, thus checking username, token
     * expiration and meanwhile password changes.
     *
     * @param token                    the token.
     * @param userDetails              userDetails the <code>UserDetails</code> used for validation.
     * @return true, if the given token is valid.
     */
    public Boolean validateToken(final String token, final UserDetails userDetails) {
        final ApplicationUserDetails user = (ApplicationUserDetails) userDetails;
        final String username = getUsernameFromToken(token);

        if (username != null) {
            final Date created = getCreatedDateFromToken(token);
            if (user == null || created == null) {
                return false;
            }
        }

        return username != null && username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Method that generates new access and refresh tokens and adds them to response headers
     *
     * @param response             the http servlet response
     * @param currentUsername      the cuurent username connected to the sub claim of JWT
     */
    public void generateTokens(HttpServletResponse response, String currentUsername) {
        final String accessToken = generateToken(currentUsername);
        // add created token to response header
        response.addHeader(accessTokenHeader, accessToken);
    }
}
