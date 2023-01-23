/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.samples;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.http.HttpHeadersThreadLocal;
import org.odpi.openmetadata.metadatasecurity.connectors.OpenMetadataPlatformSecurityConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * CocoPharmaPlatformSecurityConnector overrides the default behavior for the security connector
 * to allow requests the Coco Pharmaceutical's server administrator APIs.  In this example,
 * only Gary Geeke is allowed to issue these requests.
 */
public class CocoPharmaPlatformSecurityConnectorTokenBased extends OpenMetadataPlatformSecurityConnector
{
    final static String  platformAdministrator = "garygeeke";

    private enum PlatformRoles{
        PLATFORM_ADMINISTRATOR,
        PLATFORM_OPERATOR,
        PLATFORM_INVESTIGATOR;

        private String getName(){
            return this.toString().toLowerCase();
        }
    }

    private final byte[] secret = Base64.getDecoder().decode("d14uaEwsGU3cXopmxaEDqhQTow81zixFWbFUuu3budQ");

    private static final Logger log = LoggerFactory.getLogger(CocoPharmaPlatformSecurityConnectorTokenBased.class);


    /**
     * Check that the calling user is authorized to create new servers.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this platform
     */
    public void  validateUserForNewServer(String   userId) throws UserNotAuthorizedException
    {
        final String methodName = "validateUserForNewServer";

        if (! getUserActions(userId).contains(PlatformRoles.PLATFORM_ADMINISTRATOR.getName()))
        {
            super.throwUnauthorizedPlatformAccess(userId, methodName);
        }
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue operator commands to this platform
     */
    public void  validateUserAsOperatorForPlatform(String   userId) throws UserNotAuthorizedException
    {
        final String methodName = "validateUserAsOperatorForPlatform";

        if (! getUserActions(userId).contains(PlatformRoles.PLATFORM_OPERATOR.getName()))
        {
            super.throwUnauthorizedPlatformAccess(userId, methodName);
        }
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue diagnostic commands to this platform
     */
    public void  validateUserAsInvestigatorForPlatform(String   userId) throws UserNotAuthorizedException
    {
        final String methodName = "validateUserAsInvestigatorForPlatform";

        if (  getUserActions(userId) != null && !getUserActions(userId).contains(PlatformRoles.PLATFORM_INVESTIGATOR.getName()))
        {
            super.throwUnauthorizedPlatformAccess(userId, methodName);
        }
    }

    private List<String> getUserActions(String userId){
        Map<String, String> headersMap = HttpHeadersThreadLocal.getHeadersThreadLocal().get();
        if (headersMap != null && !headersMap.isEmpty()) {
            Jws<Claims> jwtClaims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret))
                    .build().parseClaimsJws(headersMap.get("authorization"));

            String username = jwtClaims.getBody().getSubject();
            List<String> actions = jwtClaims.getBody().get("actions", List.class);
            if (username.equals(userId)  && !actions.isEmpty()) {
                log.info("User {} validated for issuing requests.", username);
                return actions;
            }
        }
        return null;
    }
}
