/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.authfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * LogonFVT covers the basic contract of the platform's logon endpoint and the bearer token it issues:
 * who is allowed in, who is turned away, and whether the resulting token actually opens anything.
 * <br><br>
 * The negative cases matter as much as the positive one here.  A filter chain that has been mis-wired
 * during an upgrade usually fails by letting too much through rather than too little, and a suite that
 * only checked "a valid user can log on" would pass against a platform that had stopped checking
 * anything at all.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class LogonFVT
{
    private static final String ACTIVE_USER = "fvtactive";
    private static final String PASSWORD    = "fvtsecret";


    /**
     * A valid userId and password should return a bearer token.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void validCredentialsReturnAToken() throws Exception
    {
        HttpResponse<String> response = AuthFvtTestSupport.requestToken(ACTIVE_USER, PASSWORD, null);

        assertEquals(200, response.statusCode(), "A valid logon should be accepted.  Body: " + response.body());

        String token = response.body();

        assertFalse(token == null || token.isBlank(), "A successful logon should return a token");

        // A JWT is three dot-separated base64url sections.  This is not validating the signature - the
        // platform does that on the next request - just checking that what came back is a token at all
        // rather than, say, an error page or an empty string.
        assertEquals(3, token.split("\\.").length,
                     "The token should be a three-part JWT, but was: " + token);
    }


    /**
     * The token should open an endpoint that requires authentication.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void tokenOpensAProtectedEndpoint() throws Exception
    {
        String token = AuthFvtTestSupport.logon(ACTIVE_USER, PASSWORD);

        HttpResponse<String> withToken = AuthFvtTestSupport.get(AuthFvtTestSupport.PROTECTED_PATH, token);

        // Deliberately asserting 200 rather than "not 401": a 404 would also be "not 401", and would mean
        // this test was passing against an endpoint that does not exist and proves nothing.
        assertEquals(200, withToken.statusCode(),
                     "A protected endpoint should accept a valid bearer token and answer.  Status was " +
                             withToken.statusCode() + " with body: " + withToken.body());
    }


    /**
     * The same endpoint should refuse a request that carries no token at all.  If this fails, the chain
     * is letting unauthenticated callers through.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void protectedEndpointRefusesAnUnauthenticatedCall() throws Exception
    {
        HttpResponse<String> noToken = AuthFvtTestSupport.get(AuthFvtTestSupport.PROTECTED_PATH, null);

        assertEquals(401, noToken.statusCode(),
                     "A protected endpoint should reject a request with no token.  Body: " + noToken.body());
    }


    /**
     * A token that has been tampered with should be refused.  This is what proves the platform is
     * verifying the signature rather than merely reading the claims.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void tamperedTokenIsRefused() throws Exception
    {
        String   token = AuthFvtTestSupport.logon(ACTIVE_USER, PASSWORD);
        String[] parts = token.split("\\.");

        // Flip a character in the middle of the signature, leaving a structurally valid but unverifiable
        // token.  Deliberately not the last character: base64url's final character can carry padding bits
        // that decode to the same signature bytes, so flipping it may not change the signature at all.
        int      flipPosition  = parts[2].length() / 2;
        char     original      = parts[2].charAt(flipPosition);
        char     replacement   = (original == 'A') ? 'B' : 'A';
        String   tamperedToken = parts[0] + "." + parts[1] + "."
                                       + parts[2].substring(0, flipPosition) + replacement
                                       + parts[2].substring(flipPosition + 1);

        HttpResponse<String> response = AuthFvtTestSupport.get(AuthFvtTestSupport.PROTECTED_PATH, tamperedToken);

        assertEquals(401, response.statusCode(),
                     "A token with a broken signature should be rejected.  Body: " + response.body());
    }


    /**
     * The wrong password should be refused, and the response must not leak the fact that the account
     * exists by behaving differently from an unknown user.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void wrongPasswordIsRefused() throws Exception
    {
        HttpResponse<String> response = AuthFvtTestSupport.requestToken(ACTIVE_USER, "not-the-password", null);

        assertNotEquals(200, response.statusCode(),
                        "A logon with the wrong password should not be accepted");
    }


    /**
     * An unknown userId should be refused.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void unknownUserIsRefused() throws Exception
    {
        HttpResponse<String> response = AuthFvtTestSupport.requestToken("no-such-user-in-the-directory", PASSWORD, null);

        assertNotEquals(200, response.statusCode(),
                        "A logon for a user that is not in the directory should not be accepted");
    }


    /**
     * Accounts that are disabled or locked should not be able to log on even with the right password.
     * The account status check is separate from the password check, and an upgrade could plausibly
     * disturb one without the other.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void disabledAndLockedAccountsCannotLogOn() throws Exception
    {
        HttpResponse<String> disabled = AuthFvtTestSupport.requestToken("fvtdisabled", PASSWORD, null);

        assertNotEquals(200, disabled.statusCode(),
                        "A DISABLED account should not be able to log on even with the correct password");

        HttpResponse<String> locked = AuthFvtTestSupport.requestToken("fvtlocked", PASSWORD, null);

        assertNotEquals(200, locked.statusCode(),
                        "A LOCKED account should not be able to log on even with the correct password");
    }


    /**
     * The endpoints SecurityConfig lists as permitAll should be reachable without a token.  If these
     * start requiring authentication, clients can no longer discover the platform or log on to it at all.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void publicEndpointsStayOpen() throws Exception
    {
        HttpResponse<String> origin = AuthFvtTestSupport.get("/open-metadata/platform-services/server-platform/origin", null);

        assertTrue(origin.statusCode() < 400,
                   "The platform origin endpoint should be reachable without a token, but returned " + origin.statusCode());

        HttpResponse<String> about = AuthFvtTestSupport.get("/api/about", null);

        assertTrue(about.statusCode() < 400,
                   "The about endpoint should be reachable without a token, but returned " + about.statusCode());

        // The API docs are permitAll too.  This also happens to be the only place in the whole test suite
        // where springdoc is actually loaded and asked to produce anything - every other platform switches
        // it off - so it doubles as the check that springdoc is in step with the Spring Boot version.
        HttpResponse<String> apiDocs = AuthFvtTestSupport.get("/v3/api-docs", null);

        assertEquals(200, apiDocs.statusCode(),
                     "The OpenAPI document should be served without a token, but returned " +
                             apiDocs.statusCode() + " with body: " + apiDocs.body());
        assertTrue(apiDocs.body().contains("\"openapi\""),
                   "The API docs endpoint should return an OpenAPI document");
    }
}
