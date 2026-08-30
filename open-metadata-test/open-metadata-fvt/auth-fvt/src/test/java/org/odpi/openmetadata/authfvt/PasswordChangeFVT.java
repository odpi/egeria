/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.authfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.http.HttpResponse;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * PasswordChangeFVT covers changing a password, which the platform does as part of logging on rather
 * than through an endpoint of its own: {@code LoginRequest} carries {@code (userId, password, newPassword)}
 * and supplying the third field to {@code POST /api/token} changes the password and returns a token in
 * the same call.
 * <br><br>
 * Each test below owns a different user account, because a password change is persisted to the user
 * directory and would otherwise leak into whatever ran next.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class PasswordChangeFVT
{
    private static final String ORIGINAL_PASSWORD = "fvtsecret";


    /**
     * Changing a password at logon should return a token, make the new password work, and stop the old
     * one working.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void passwordChangeTakesEffect() throws Exception
    {
        final String user        = "fvtchanger";
        final String newPassword = "fvtchanged";

        HttpResponse<String> change = AuthFvtTestSupport.requestToken(user, ORIGINAL_PASSWORD, newPassword);

        assertEquals(200, change.statusCode(),
                     "Logging on with a new password should succeed and return a token.  Body: " + change.body());
        assertFalse(change.body() == null || change.body().isBlank(),
                    "A password change should still return a token for the session it just authenticated");

        // The new password works on its own from now on.
        HttpResponse<String> withNew = AuthFvtTestSupport.requestToken(user, newPassword, null);

        assertEquals(200, withNew.statusCode(),
                     "The new password should be accepted on a subsequent logon.  Body: " + withNew.body());

        // And the old one no longer does.
        HttpResponse<String> withOld = AuthFvtTestSupport.requestToken(user, ORIGINAL_PASSWORD, null);

        assertNotEquals(200, withOld.statusCode(),
                        "The old password should stop working once it has been changed");
    }


    /**
     * The directory ships accounts with a clear-text password.  Changing one should replace it with an
     * encrypted password rather than leaving the clear-text value in place beside it.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void clearPasswordIsReplacedByAnEncryptedOne() throws Exception
    {
        final String user        = "fvtactive";
        final String newPassword = "fvtactivechanged";

        Map<String, Object> before = AuthFvtTestSupport.readStoredSecrets(user);

        assertNotNull(before, "The test account should be present in the user directory before the change");
        assertTrue(before.containsKey("clearPassword"),
                   "This account is expected to start with a clear-text password, but held: " + before.keySet());

        HttpResponse<String> change = AuthFvtTestSupport.requestToken(user, ORIGINAL_PASSWORD, newPassword);

        assertEquals(200, change.statusCode(),
                     "Changing the password should succeed.  Body: " + change.body());

        Map<String, Object> after = AuthFvtTestSupport.readStoredSecrets(user);

        assertNotNull(after, "The account should still be present after the change");
        assertTrue(after.containsKey("encryptedPassword"),
                   "The changed password should be stored encrypted, but the stored secrets were: " + after.keySet());
        assertFalse(after.containsKey("clearPassword"),
                    "The clear-text password should be removed once an encrypted one replaces it, but the stored " +
                            "secrets were: " + after.keySet());
        assertNotEquals(newPassword, after.get("encryptedPassword"),
                        "The stored password must not be the plain text of the new password");
    }


    /**
     * An account whose credentials have expired should be refused an ordinary logon, but allowed through
     * when it supplies a new password - and its status should be cleared as a result.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void expiredCredentialsRequireAPasswordChange() throws Exception
    {
        final String user        = "fvtexpired";
        final String newPassword = "fvtexpiredchanged";

        assertEquals("CREDENTIALS_EXPIRED", AuthFvtTestSupport.readStoredAccountStatus(user),
                     "This account is expected to start with expired credentials");

        // Right password, but no new one offered: refused because the credentials have expired.
        HttpResponse<String> withoutChange = AuthFvtTestSupport.requestToken(user, ORIGINAL_PASSWORD, null);

        assertNotEquals(200, withoutChange.statusCode(),
                        "An account with expired credentials should not be able to log on without setting a new password");

        // Same credentials, but now supplying a replacement: allowed through.
        HttpResponse<String> withChange = AuthFvtTestSupport.requestToken(user, ORIGINAL_PASSWORD, newPassword);

        assertEquals(200, withChange.statusCode(),
                     "Supplying a new password should let an expired account log on.  Body: " + withChange.body());

        assertEquals("AVAILABLE", AuthFvtTestSupport.readStoredAccountStatus(user),
                     "Setting a new password should clear the CREDENTIALS_EXPIRED status");

        // And the account behaves normally from then on.
        HttpResponse<String> subsequent = AuthFvtTestSupport.requestToken(user, newPassword, null);

        assertEquals(200, subsequent.statusCode(),
                     "Once the password has been reset the account should log on normally.  Body: " + subsequent.body());
    }


    /**
     * A password change should still require the current password.  If the old password is wrong, the
     * change must not be applied - otherwise anyone could set a new password for any account.
     *
     * @throws Exception unexpected failure in the test
     */
    @Test
    void passwordChangeStillRequiresTheCurrentPassword() throws Exception
    {
        // An ordinary AVAILABLE account, so that a rejection can only be down to the wrong current
        // password - using a locked or disabled account here would pass for the wrong reason.
        final String user           = "fvtwrongpassword";
        final String attemptedValue = "should-never-be-set";

        HttpResponse<String> response = AuthFvtTestSupport.requestToken(user, "not-the-password", attemptedValue);

        assertNotEquals(200, response.statusCode(),
                        "A password change presented with the wrong current password must be rejected");

        // And nothing should have been written for that account.
        Map<String, Object> stored = AuthFvtTestSupport.readStoredSecrets(user);

        assertNotNull(stored, "The account should still exist after a rejected change");
        assertFalse(stored.containsKey("encryptedPassword"),
                    "A rejected password change must not write a new password.  Stored secrets: " + stored.keySet());

        // The original password must still work - the rejected attempt should have changed nothing.
        assertEquals(200, AuthFvtTestSupport.requestToken(user, ORIGINAL_PASSWORD, null).statusCode(),
                     "The original password should still work after a rejected password change");
    }
}
