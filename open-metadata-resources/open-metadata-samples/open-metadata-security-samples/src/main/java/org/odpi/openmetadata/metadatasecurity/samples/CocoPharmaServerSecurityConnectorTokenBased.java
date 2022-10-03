/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.metadatasecurity.samples;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.http.HttpHeadersThreadLocal;
import org.odpi.openmetadata.metadatasecurity.OpenMetadataServerSecurity;
import org.odpi.openmetadata.metadatasecurity.connectors.OpenMetadataServerSecurityConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is a mock. The purpose of this class is to highlight the usage of HTTP headers passed through ThreadLocal.
 */
public class CocoPharmaServerSecurityConnectorTokenBased extends OpenMetadataServerSecurityConnector implements OpenMetadataServerSecurity {
    /*
     * These variables represent users and user tokens.  Typically these would be
     * implemented as a look up to a user directory such as LDAP rather than in memory lists.
     * The lists are used here to make the demo easier to set up.
     */
    private List<String> allUsers = new ArrayList<>();

    private List<String> serverAdmins = new ArrayList<>();
    private List<String> serverOperators = new ArrayList<>();
    private List<String> serverInvestigators = new ArrayList<>();
    private List<String> defaultZoneMembership = new ArrayList<>();

    private Map<String, String> usersJWT = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(CocoPharmaServerSecurityConnectorTokenBased.class);


    /*
     * Zones requiring special processing
     */
    private final String quarantineZoneName = "quarantine";


    private final byte[] secret = Base64.getDecoder().decode("d14uaEwsGU3cXopmxaEDqhQTow81zixFWbFUuu3budQ");

    public CocoPharmaServerSecurityConnectorTokenBased() {
        final String garyGeekeUserId = "garygeeke";

        allUsers.add(garyGeekeUserId);

        serverAdmins.add(garyGeekeUserId);
        serverOperators.add(garyGeekeUserId);
        serverInvestigators.add(garyGeekeUserId);

        /*
         * Set up default zone membership
         */
        defaultZoneMembership.add(quarantineZoneName);

        usersJWT.put(garyGeekeUserId, this.getJWT(garyGeekeUserId));
    }

    /**
     * This method is just an example. It returns a JWT with 5 minutes expiration date.
     *
     * @return a JSON web token for the specified user
     */
    private String getJWT(String userId) {
        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(userId)
                .claim("allowedToIssueRequests", Boolean.TRUE)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(30, ChronoUnit.DAYS)))
                .signWith(Keys.hmacShaKeyFor(secret))
                .compact();
    }


    @Override
    public void validateUserForServer(String userId) throws UserNotAuthorizedException {
        Map<String, String> headersMap = HttpHeadersThreadLocal.getHeadersThreadLocal().get();
        if (headersMap != null) {
            Jws<Claims> jwtClaims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret))
                    .build().parseClaimsJws(headersMap.get("authorisation"));

            String username = jwtClaims.getBody().getSubject();
            Boolean allowedToIssueRequests = jwtClaims.getBody().get("allowedToIssueRequests", Boolean.class);
            if (username.equals(userId) && allUsers.contains(userId) && allowedToIssueRequests) {
                log.info("User {} validated for issuing requests.", username);
                return;
            }
        }
        super.validateUserForServer(userId);
    }

    /**
     * The following methods are from OpenMetadataServerSecurity interface and must be implemented in a real use-case.
     * For this example, we have shown the usage of JWT in validateUserForServer method.
     */
    @Override
    public void validateUserAsServerAdmin(String userId) throws UserNotAuthorizedException {

    }

    @Override
    public void validateUserAsServerOperator(String userId) throws UserNotAuthorizedException {

    }

    @Override
    public void validateUserAsServerInvestigator(String userId) throws UserNotAuthorizedException {

    }
}
