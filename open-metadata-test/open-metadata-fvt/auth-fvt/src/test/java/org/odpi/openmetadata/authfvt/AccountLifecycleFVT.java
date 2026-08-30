/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.authfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AccountLifecycleFVT covers creating and removing user accounts through the platform security API, and
 * - the part that matters - whether an account created that way can actually log on afterwards.
 * <br><br>
 * Account management and logon are separate pieces of the platform that meet only in the user directory,
 * so testing them together is what catches a change that leaves one writing accounts the other cannot
 * read.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class AccountLifecycleFVT
{
    private static final String ADMIN_USER = "fvtactive";
    private static final String PASSWORD   = "fvtsecret";

    private static final String ACCOUNTS_PATH = "/open-metadata/platform-services/server-platform/security/user-accounts";


    /**
     * Create an account, log on as it, then delete it and check it can no longer log on.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void createdAccountCanLogOnAndDeletedAccountCannot() throws Exception
    {
        final String newUser     = "fvtcreated";
        final String newPassword = "fvtcreatedsecret";

        String adminToken = AuthFvtTestSupport.logon(ADMIN_USER, PASSWORD);

        try
        {
            // Before it exists, it obviously cannot log on - established up front so that the assertion
            // after the delete is meaningful rather than something that was always true.
            HttpResponse<String> beforeCreate = AuthFvtTestSupport.requestToken(newUser, newPassword, null);

            assertNotEquals(200, beforeCreate.statusCode(),
                            "An account that has not been created yet should not be able to log on");

            String requestBody = """
                    {
                      "userAccount" : {
                        "userId" : "%s",
                        "userName" : "Created Casey",
                        "userAccountStatus" : "AVAILABLE",
                        "userAccountType" : "EMPLOYEE",
                        "secrets" : { "clearPassword" : "%s" }
                      }
                    }""".formatted(newUser, newPassword);

            HttpResponse<String> create = AuthFvtTestSupport.post(ACCOUNTS_PATH, adminToken, requestBody);

            assertTrue(create.statusCode() < 400,
                       "Creating a user account should succeed, but returned " + create.statusCode() +
                               " with body: " + create.body());

            assertNotNull(AuthFvtTestSupport.readStoredSecrets(newUser),
                          "The new account should have been written to the user directory");

            // The point of the test: an account created through the API can log on.
            HttpResponse<String> logon = AuthFvtTestSupport.requestToken(newUser, newPassword, null);

            assertEquals(200, logon.statusCode(),
                         "An account created through the platform security API should be able to log on.  Body: " +
                                 logon.body());

            HttpResponse<String> delete = AuthFvtTestSupport.delete(ACCOUNTS_PATH + "/" + newUser, adminToken);

            assertTrue(delete.statusCode() < 400,
                       "Deleting a user account should succeed, but returned " + delete.statusCode() +
                               " with body: " + delete.body());

            assertNull(AuthFvtTestSupport.readStoredSecrets(newUser),
                       "The deleted account should no longer be in the user directory");

            HttpResponse<String> afterDelete = AuthFvtTestSupport.requestToken(newUser, newPassword, null);

            assertNotEquals(200, afterDelete.statusCode(),
                            "A deleted account should no longer be able to log on");
        }
        finally
        {
            // Best-effort cleanup, in case an assertion above failed part way through.
            try
            {
                AuthFvtTestSupport.delete(ACCOUNTS_PATH + "/" + newUser, adminToken);
            }
            catch (Exception ignored)
            {
                // nothing further can be done
            }
        }
    }


    /**
     * An account created with an expired status should have to set a password before it can log on, the
     * same as one that started that way in the directory.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void createdAccountHonoursItsAccountStatus() throws Exception
    {
        final String newUser     = "fvtcreatedexpired";
        final String password    = "fvtcreatedsecret";
        final String newPassword = "fvtcreatedchanged";

        String adminToken = AuthFvtTestSupport.logon(ADMIN_USER, PASSWORD);

        try
        {
            String requestBody = """
                    {
                      "userAccount" : {
                        "userId" : "%s",
                        "userName" : "Created Expired Cassie",
                        "userAccountStatus" : "CREDENTIALS_EXPIRED",
                        "userAccountType" : "EMPLOYEE",
                        "secrets" : { "clearPassword" : "%s" }
                      }
                    }""".formatted(newUser, password);

            HttpResponse<String> create = AuthFvtTestSupport.post(ACCOUNTS_PATH, adminToken, requestBody);

            assertTrue(create.statusCode() < 400,
                       "Creating a user account should succeed, but returned " + create.statusCode() +
                               " with body: " + create.body());

            assertEquals("CREDENTIALS_EXPIRED", AuthFvtTestSupport.readStoredAccountStatus(newUser),
                         "The account should have been stored with the status it was created with");

            HttpResponse<String> plainLogon = AuthFvtTestSupport.requestToken(newUser, password, null);

            assertNotEquals(200, plainLogon.statusCode(),
                            "An account created with expired credentials should not log on without setting a password");

            HttpResponse<String> changingLogon = AuthFvtTestSupport.requestToken(newUser, password, newPassword);

            assertEquals(200, changingLogon.statusCode(),
                         "Setting a password should let the new account log on.  Body: " + changingLogon.body());
        }
        finally
        {
            try
            {
                AuthFvtTestSupport.delete(ACCOUNTS_PATH + "/" + newUser, adminToken);
            }
            catch (Exception ignored)
            {
                // nothing further can be done
            }
        }
    }


    /**
     * Account management must itself be protected.  Creating a user without a token would be a way to
     * grant yourself access to everything else.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void accountManagementRequiresAToken() throws Exception
    {
        String requestBody = """
                {
                  "userAccount" : {
                    "userId" : "fvtunauthorised",
                    "userName" : "Should Not Exist",
                    "userAccountStatus" : "AVAILABLE",
                    "userAccountType" : "EMPLOYEE",
                    "secrets" : { "clearPassword" : "nope" }
                  }
                }""";

        HttpResponse<String> create = AuthFvtTestSupport.post(ACCOUNTS_PATH, null, requestBody);

        assertEquals(401, create.statusCode(),
                     "Creating a user account without a token must be rejected.  Body: " + create.body());

        assertNull(AuthFvtTestSupport.readStoredSecrets("fvtunauthorised"),
                   "A rejected account creation must not write anything to the user directory");
    }
}
