/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userauthn;

import org.odpi.openmetadata.userauthn.auth.PlatformUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Token service generate
 */
@Service
public class TokenService
{
    private final JwtEncoder encoder;

    /**
     * Constructor
     *
     * @param encoder JWT encoder
     */
    public TokenService(JwtEncoder encoder) {
            this.encoder = encoder;
    }

    public String generateToken(Authentication authentication)
    {
        if (authentication.getPrincipal() instanceof PlatformUserDetails platformUserDetails)
        {
            Instant now = Instant.now();
            JwtClaimsSet claims;

            if (platformUserDetails.getDistinguishedName() == null)
            {
                claims = JwtClaimsSet.builder()
                        .issuer("self")
                        .issuedAt(now)
                        .expiresAt(now.plus(1, ChronoUnit.HOURS))
                        .subject(authentication.getName())
                        .claim("displayName", platformUserDetails.getDisplayName())
                        .build();
            }
            else
            {
                claims = JwtClaimsSet.builder()
                        .issuer("self")
                        .issuedAt(now)
                        .expiresAt(now.plus(1, ChronoUnit.HOURS))
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

