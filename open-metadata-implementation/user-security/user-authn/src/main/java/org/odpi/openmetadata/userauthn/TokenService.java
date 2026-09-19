/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userauthn;

import org.odpi.openmetadata.userauthn.auth.PlatformUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Token service generates JWT bearer tokens for authenticated users.
 */
@Service
public class TokenService
{
    /**
     * Number of hours that a bearer token is valid for if the bearerTokenTimeout property is not set.
     */
    public static final long DEFAULT_BEARER_TOKEN_TIMEOUT = 1L;

    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    private final JwtEncoder encoder;
    private final long       bearerTokenTimeout;

    /**
     * Constructor
     *
     * @param encoder JWT encoder
     * @param bearerTokenTimeout number of hours that an issued bearer token remains valid.  A null value means the
     *                           property is not set and the default is used.  A value that is not a positive number
     *                           of hours is reported and the default is used as if the property was not set.
     */
    public TokenService(JwtEncoder encoder,
                        @Value("${bearerTokenTimeout:" + DEFAULT_BEARER_TOKEN_TIMEOUT + "}") Long bearerTokenTimeout)
    {
        this.encoder = encoder;

        if (bearerTokenTimeout == null)
        {
            /*
             * The property is present but has no value, which is the same as not setting it at all.
             */
            this.bearerTokenTimeout = DEFAULT_BEARER_TOKEN_TIMEOUT;
        }
        else if (bearerTokenTimeout > 0)
        {
            this.bearerTokenTimeout = bearerTokenTimeout;
        }
        else
        {
            log.warn("Option bearerTokenTimeout is set to {} which is not a valid number of hours! Using the default of {} hour(s) instead.",
                     bearerTokenTimeout,
                     DEFAULT_BEARER_TOKEN_TIMEOUT);

            this.bearerTokenTimeout = DEFAULT_BEARER_TOKEN_TIMEOUT;
        }
    }

    /**
     * Generate token
     *
     * @param authentication information from the request
     * @return bearer token as a string
     */
    public String generateToken(Authentication authentication)
    {
        if (authentication.getPrincipal() instanceof PlatformUserDetails platformUserDetails)
        {
            Instant      now = Instant.now();
            JwtClaimsSet claims;

            if (platformUserDetails.getDistinguishedName() == null)
            {
                claims = JwtClaimsSet.builder()
                        .issuer("self")
                        .issuedAt(now)
                        .expiresAt(now.plus(bearerTokenTimeout, ChronoUnit.HOURS))
                        .subject(authentication.getName())
                        .claim("displayName", platformUserDetails.getDisplayName())
                        .build();
            }
            else
            {
                claims = JwtClaimsSet.builder()
                        .issuer("self")
                        .issuedAt(now)
                        .expiresAt(now.plus(bearerTokenTimeout, ChronoUnit.HOURS))
                        .subject(authentication.getName())
                        .claim("displayName", platformUserDetails.getDisplayName())
                        .claim("dn", platformUserDetails.getDistinguishedName())
                        .build();
            }

            return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        }

        return null;
    }
}
