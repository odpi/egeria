/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * UserSecurityFVT covers the {@code user-security} services as the <em>administration and operation</em>
 * clients meet them: which endpoints are open without credentials, which are not, and whether the clients
 * can obtain and use a bearer token for themselves.
 * <br><br>
 * It deliberately does not repeat auth-fvt.  That suite is the thorough treatment of the logon contract -
 * password changes, disabled and locked accounts, tampered tokens - and duplicating it here would be
 * coverage that has to be maintained twice.  What is left, and what has no coverage anywhere else, is the
 * boundary this suite sits on: <b>every other class in server-fvt only works because a client obtained a
 * token from a secrets store</b>, and that path - {@code SpringRESTClientConnector.refreshAuthorizationToken}
 * calling the YAML secrets store, which POSTs to {@code /api/token} - is used by every real deployment and
 * by no other test.
 * <br><br>
 * So the tests here ask two things.  First, that the token path works and that a client which has one can do
 * something a client without one cannot - which is the assertion that gives the rest of the suite its
 * meaning.  Second, that the endpoints {@code SecurityConfig} declares open really are open and that the
 * administration endpoints really are not, because an administration API reachable without credentials is a
 * more serious defect than any of the ones this suite was written to look for.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class UserSecurityFVT
{
    /**
     * The path used to show that a request without credentials is refused.
     * <br><br>
     * Deliberately an endpoint that exists and requires authentication.  An endpoint that does not exist
     * would also fail to answer, and would make this test pass while proving nothing - the same mistake
     * auth-fvt's README warns about.
     */
    private static final String PROTECTED_PATH = "/open-metadata/platform-services/server-platform/servers";


    /**
     * A client built against the secrets store should obtain a token and be able to use it.
     * <br><br>
     * This is the assertion the rest of the suite rests on: every other test in server-fvt builds its client
     * the same way, so if the token exchange were quietly not happening they would all still pass - against
     * a platform that had stopped checking. Pairing the successful call with the refused one below is what
     * makes this a test of authentication rather than of connectivity.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aClientObtainsATokenFromTheSecretsStoreAndCanUseIt() throws Exception
    {
        PlatformServicesClient client = ServerFvtTestSupport.getPlatformServicesClient();

        assertNotNull(client.getKnownServers(),
                      "A client that has obtained a bearer token should be able to call a protected endpoint");
    }


    /**
     * The same endpoint should refuse a request that carries no credentials at all.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void aProtectedEndpointRefusesAnUnauthenticatedCall() throws Exception
    {
        HttpResponse<String> response = ServerFvtTestSupport.getWithoutCredentials(PROTECTED_PATH);

        /*
         * Asserting the exact status rather than "not 200".  A 404 is also "not 200", and would mean this
         * test was passing against an endpoint that does not exist - which would prove nothing at all about
         * whether the platform checks credentials.
         */
        assertEquals(401, response.statusCode(),
                     "A protected endpoint should refuse a request with no credentials.  It answered " +
                             response.statusCode() + " with body: " + response.body());
    }


    /**
     * The administration API should refuse an unauthenticated call.
     * <br><br>
     * Checked separately from the platform services endpoint above because the two are configured by
     * different controllers, and a filter chain that has been mis-wired usually lets some paths through
     * rather than all of them.  The administration API is the one where that would matter most: it can
     * reconfigure and shut down every server on the platform.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void theAdministrationApiRefusesAnUnauthenticatedCall() throws Exception
    {
        HttpResponse<String> response = ServerFvtTestSupport.getWithoutCredentials(
                "/open-metadata/admin-services/servers/" + OMAGPlatformExtension.METADATA_STORE_NAME + "/configuration");

        assertEquals(401, response.statusCode(),
                     "The administration API should refuse a request with no credentials.  It answered " +
                             response.statusCode() + " with body: " + response.body());
    }


    /**
     * The endpoints {@code SecurityConfig} lists as {@code permitAll} should be reachable without
     * credentials.
     * <br><br>
     * These are what a user meets before they have logged on - the landing page, the platform's origin, and
     * the token endpoint itself - so a filter chain that closed them would lock everybody out of a platform
     * that was otherwise working perfectly.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void theOpenEndpointsStayReachableWithoutCredentials() throws Exception
    {
        for (String path : new String[]{"/open-metadata/platform-services/server-platform/origin",
                                        "/api/public/app/info",
                                        "/v3/api-docs"})
        {
            HttpResponse<String> response = ServerFvtTestSupport.getWithoutCredentials(path);

            assertEquals(200, response.statusCode(),
                         path + " is listed as permitAll and should answer without credentials, but answered " +
                                 response.statusCode());
        }
    }


    /**
     * The token endpoint should issue a token for a valid account and refuse an invalid one.
     * <br><br>
     * Kept deliberately short - auth-fvt covers the logon contract properly - but worth having here because
     * this is the endpoint the secrets store posts to on behalf of every client in this suite.  If it stops
     * working, this says so directly instead of the whole suite failing to start with a message about
     * whichever client happened to be built first.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void theTokenEndpointIssuesATokenAndRefusesBadCredentials() throws Exception
    {
        HttpResponse<String> accepted = ServerFvtTestSupport.postWithoutCredentials(
                "/api/token",
                "{\"userId\":\"" + OMAGPlatformExtension.USER_ID + "\",\"password\":\"" + OMAGPlatformExtension.PASSWORD + "\"}");

        assertEquals(200, accepted.statusCode(),
                     "A valid logon should be accepted.  Body: " + accepted.body());
        assertEquals(3, accepted.body().split("\\.").length,
                     "A successful logon should return a three-part JWT, but returned: " + accepted.body());

        HttpResponse<String> refused = ServerFvtTestSupport.postWithoutCredentials(
                "/api/token",
                "{\"userId\":\"" + OMAGPlatformExtension.USER_ID + "\",\"password\":\"not-the-password\"}");

        assertFalse(refused.statusCode() == 200,
                    "A logon with the wrong password should not be accepted, but answered 200 with body: " +
                            refused.body());
    }


    /**
     * The platform should describe itself through the endpoints user-security publishes, and the platform
     * services client should be able to reach both of them.
     * <br><br>
     * These two client methods are the only place {@code PlatformServicesClient} leaves its own service and
     * calls into user-security's URLs - {@code /api/about} and {@code /api/public/app/info} - which makes
     * them exactly the kind of cross-service call that goes stale unnoticed.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void thePlatformDescribesItselfThroughUserSecurity() throws Exception
    {
        PlatformServicesClient client = ServerFvtTestSupport.getPlatformServicesClient();

        assertNotNull(client.getPublicProperties(),
                      "The platform should return the public properties behind its landing page");
        assertNotNull(client.getPlatformBuildProperties(),
                      "The platform should return the build properties behind /api/about");
    }


    /**
     * A second account should be able to log on independently of the first.
     * <br><br>
     * This is what says the platform is authenticating the credentials presented rather than admitting
     * anybody once one account has logged on - a filter chain that cached too aggressively would pass every
     * other test in this class.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void asecondAccountLogsOnIndependently() throws Exception
    {
        HttpResponse<String> response = ServerFvtTestSupport.postWithoutCredentials(
                "/api/token",
                "{\"userId\":\"" + OMAGPlatformExtension.OTHER_USER_ID + "\",\"password\":\"" +
                        OMAGPlatformExtension.OTHER_PASSWORD + "\"}");

        assertEquals(200, response.statusCode(),
                     "The second account should be able to log on.  Body: " + response.body());

        HttpResponse<String> crossed = ServerFvtTestSupport.postWithoutCredentials(
                "/api/token",
                "{\"userId\":\"" + OMAGPlatformExtension.OTHER_USER_ID + "\",\"password\":\"" +
                        OMAGPlatformExtension.PASSWORD + "\"}");

        assertTrue(crossed.statusCode() != 200,
                   "The second account should not be admitted with the first account's password, but answered 200");
    }
}
