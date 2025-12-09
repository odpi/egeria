/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userauthn.auth;

import org.odpi.openmetadata.metadatasecurity.properties.OpenMetadataUserAccount;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataPlatformSecurityVerifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * PlatformUserDetails details service for the platform user repository.  This is called during the logon process.
 * It is responsible for retrieving details of the user's account from the user directory.
 */
@Service("userService")
@ConditionalOnProperty(value = "authentication.source", havingValue = "platform")
public class PlatformUserDetailsService implements UserDetailsService
{
    /**
     * Used to validate the user account is active.
     */
    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

    /**
     * Retrieve the user account from the secrets store.  This is called when a user requests a token.
     *
     * @param userId calling user
     * @return user details to check
     * @throws UsernameNotFoundException unrecognized user id
     */
    @Override
    public final PlatformUserDetails loadUserByUsername(String userId) throws UsernameNotFoundException
    {
        OpenMetadataUserAccount userAccount = OpenMetadataPlatformSecurityVerifier.getUser(userId);

        if (userAccount != null)
        {
            PlatformUserDetails user = new PlatformUserDetails(userAccount);

            detailsChecker.check(user);

            return user;
        }
        else
        {
            throw new UsernameNotFoundException("No user called: " + userId);
        }
    }
}



