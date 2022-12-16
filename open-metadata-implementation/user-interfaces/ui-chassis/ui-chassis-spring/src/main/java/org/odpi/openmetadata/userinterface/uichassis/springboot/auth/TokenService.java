/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Date;

public class TokenService extends RoleService{

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ObjectReader OBJECT_READER = OBJECT_MAPPER.reader();
    private static final ObjectWriter OBJECT_WRITER = OBJECT_MAPPER.writer();

    @Value("${token.secret}")
    protected String tokenSecret;

    /**
     * token timout in minutes
     */
    @Value("${token.timeout:30}")
    protected Long tokenTimeout;

    /**
     *
     * @return token timeout in milliseconds
     */
    public long getTokenTimeout(){
        return tokenTimeout * 60 * 1000;
    }


    /**
     *
     * @param obj the object to be serialized
     * @return the json string representing TokenUser
     */
    public String toJSON(Object obj) {
        try {
            return OBJECT_WRITER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     *
     * @param user the user to create token for
     * @param secret the secret for signature
     * @return jwt token
     */
    public String createTokenForUser(TokenUser user, String secret) {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + getTokenTimeout()))
                .setSubject(toJSON(user))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     *
     * @param token the encoded token
     * @param secret secret phrase to decode
     * @return parsed TokenUser
     */
    public TokenUser parseUserFromToken(String token, String secret) {
        String userJSON = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return fromJSON(userJSON);
    }

    /**
     *
     * @param userJSON representation of the TokenUser
     * @return the TokenUser
     */
    public TokenUser fromJSON(final String userJSON) {
        try {
            return OBJECT_READER.readValue(userJSON, TokenUser.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
