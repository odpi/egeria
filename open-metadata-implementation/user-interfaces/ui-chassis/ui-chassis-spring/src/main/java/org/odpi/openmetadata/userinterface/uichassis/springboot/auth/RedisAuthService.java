/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.redis.TokenRedisClient;
import org.odpi.openmetadata.userinterface.uichassis.springboot.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;


public class RedisAuthService extends  TokenSettings implements AuthService{

    @Autowired
    TokenRedisClient tokenRedisClient;

    @Value("${token.absolute.timeout}")
    Integer absoluteTimeout;

    public User addAuthentication(HttpServletRequest request,
                                  HttpServletResponse response,
                                  Authentication authentication) {
        TokenUser tokenUser = getTokenUser(authentication);
        String token = this.createTokenForUser(tokenUser.getUser(), tokenSecret);
        response.addHeader(AUTH_HEADER_NAME, token);
        tokenRedisClient.set(token,
                LocalDateTime.now().plusMinutes(tokenTimeout).toString()
                );
        tokenRedisClient.expire(token,absoluteTimeout * 60);
        return tokenUser.getUser();
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);
        if (!ObjectUtils.isEmpty(token)) {

            validateToken(token);

            tokenRedisClient.set(token, LocalDateTime.now().plusMinutes(tokenTimeout).toString());

            final TokenUser user = parseUserFromToken(token, tokenSecret);
            if (user != null) {
                return new UserAuthentication(user);
            }
        }
        return null;
    }

    private void validateToken(String token){
        String tokenValue = tokenRedisClient.get(token);
        if(tokenValue == null){
            throw new JwtException("redis token is expired ");
        }
        LocalDateTime tokenExpire = LocalDateTime.parse( tokenValue );
        if ( tokenExpire.isBefore(LocalDateTime.now()) ) {
            tokenRedisClient.del(token);
            throw new JwtException("redis token is expired");
        }

    }

    /**
     * {@inheritDoc}
     *
     * creates a token without expiration
     * expiration is to be validated against redis
     */
    public String createTokenForUser(User user, String secret) {
        return Jwts.builder()
                .setSubject(toJSON(user))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

}
