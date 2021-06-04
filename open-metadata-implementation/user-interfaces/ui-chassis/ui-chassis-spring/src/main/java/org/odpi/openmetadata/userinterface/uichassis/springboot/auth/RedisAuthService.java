/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.redis.TokenRedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.UUID;


public class RedisAuthService extends TokenSettings implements AuthService{

    @Autowired
    TokenRedisClient tokenRedisClient;

    @Value("${token.absolute.timeout}")
    Integer tokenAbsoluteTimeout;

    public void addAuthentication(HttpServletRequest request,
                                  HttpServletResponse response,
                                  Authentication authentication) {
        TokenUser tokenUser = getTokenUser(authentication);
        String token = this.createTokenForUser(tokenUser, tokenSecret);
        response.addHeader(AUTH_HEADER_NAME, token);
        tokenRedisClient.set(token,
                tokenAbsoluteTimeout.longValue() * 60 ,
                LocalDateTime.now().plusMinutes(tokenTimeout).toString()
                );
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);
        if (!ObjectUtils.isEmpty(token)) {

            validateToken(token);

            tokenRedisClient.setKeepTTL(token, LocalDateTime.now().plusMinutes(tokenTimeout).toString());

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
    public String createTokenForUser(TokenUser user, String secret) {
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(toJSON(user))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

}
