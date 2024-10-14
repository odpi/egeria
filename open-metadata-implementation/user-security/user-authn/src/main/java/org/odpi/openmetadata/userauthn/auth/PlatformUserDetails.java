/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userauthn.auth;

import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStoreCollectionProperty;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountStatus;
import org.odpi.openmetadata.metadatasecurity.properties.OpenMetadataUserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * PlatformUserDetails is a wrapper for OpenMetadataSecurityUserDetails which is supported by the
 * open metadata security module.  The wrapper is used to avoid including Spring classes in the Open Metadata
 * security module
 */
public class PlatformUserDetails implements UserDetails
{
    @Serial
    private static final long serialVersionUID = 1L;

    private OpenMetadataUserAccount openMetadataUserAccount = null;


    /**
     * Copy constructor
     */
    public PlatformUserDetails(OpenMetadataUserAccount openMetadataUserAccount)
    {
        this.openMetadataUserAccount = openMetadataUserAccount;
    }


    /**
     * Return if the account is not disabled.
     *
     * @return boolean
     */
    @Override
    public boolean isAccountNonExpired()
    {
        return (openMetadataUserAccount != null) &&
                (openMetadataUserAccount.getUserAccountStatus() != null) &&
                (openMetadataUserAccount.getUserAccountStatus() != UserAccountStatus.DISABLED);
    }


    /**
     * Return if the account is not locked.
     *
     * @return boolean
     */
    @Override
    public boolean isAccountNonLocked()
    {
        return (openMetadataUserAccount != null) &&
                (openMetadataUserAccount.getUserAccountStatus() != null) &&
                (openMetadataUserAccount.getUserAccountStatus() != UserAccountStatus.DISABLED) &&
                (openMetadataUserAccount.getUserAccountStatus() != UserAccountStatus.LOCKED);
    }


    /**
     * Return whether the account credentials (secrets) are still valid.
     *
     * @return boolean
     */
    @Override
    public boolean isCredentialsNonExpired()
    {
        return (openMetadataUserAccount != null) &&
                (openMetadataUserAccount.getUserAccountStatus() != null) &&
                (openMetadataUserAccount.getUserAccountStatus() == UserAccountStatus.AVAILABLE);
    }


    /**
     * Return whether the account is enabled.
     *
     * @return boolean
     */
    @Override
    public boolean isEnabled()
    {
        return (openMetadataUserAccount != null) && (openMetadataUserAccount.getUserAccountStatus() != null);
    }


    /**
     * Extracts the user identifier, security roles and security groups from the user account and sets them up as
     * authorities for the token.  This is called once the password has been validated.
     *
     * @return collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        if (openMetadataUserAccount != null)
        {
            List<String> authorities = new ArrayList<>();

            authorities.add("User " + openMetadataUserAccount.getUserId());

            if (openMetadataUserAccount.getSecurityRoles() != null)
            {
                for (String securityRole : openMetadataUserAccount.getSecurityRoles())
                {
                    if (securityRole != null)
                    {
                        authorities.add("SecurityRole " + securityRole);
                    }
                }
            }

            if (openMetadataUserAccount.getSecurityGroups() != null)
            {
                for (String securityGroup : openMetadataUserAccount.getSecurityGroups())
                {
                    if (securityGroup != null)
                    {
                        authorities.add("SecurityGroup " + securityGroup);
                    }
                }
            }

            return AuthorityUtils.createAuthorityList(authorities.toArray(new String[]{}));
        }
        else
        {
            return AuthorityUtils.createAuthorityList(new String[]{});
        }
    }


    /**
     * Return an encrypted password.  It may be possible to retrieve the encrypted password from the user account.
     * If that is not available, it retrieves the clear text password and encrypts it.
     *
     * @return encrypted password
     */
    @Override
    public String getPassword()
    {
        if ((openMetadataUserAccount != null) && (openMetadataUserAccount.getSecrets() != null))
        {
            if (openMetadataUserAccount.getSecrets().get(SecretsStoreCollectionProperty.ENCRYPTED_PASSWORD.getName()) != null)
            {
                return openMetadataUserAccount.getSecrets().get(SecretsStoreCollectionProperty.ENCRYPTED_PASSWORD.getName());
            }
            else if (openMetadataUserAccount.getSecrets().get(SecretsStoreCollectionProperty.CLEAR_PASSWORD.getName()) != null)
            {
                return new BCryptPasswordEncoder().encode(openMetadataUserAccount.getSecrets().get(SecretsStoreCollectionProperty.CLEAR_PASSWORD.getName()));
            }
        }

        return null;
    }


    /**
     * Return the userId of the user's account. Notice capitalization of Username.
     *
     * @return string userId
     */
    @Override
    public String getUsername()
    {
        if (openMetadataUserAccount != null)
        {
            return openMetadataUserAccount.getUserId();
        }

        return null;
    }


    /**
     * Return the name of the user.
     *
     * @return string name
     */
    public String getDisplayName()
    {
        if (openMetadataUserAccount != null)
        {
            return openMetadataUserAccount.getUserName();
        }

        return null;
    }


    /**
     * Return the distinguished name of the user.
     *
     * @return string name
     */
    public String getDistinguishedName()
    {
        if (openMetadataUserAccount != null)
        {
            return openMetadataUserAccount.getDistinguishedName();
        }

        return null;
    }


    @Override
    public String toString()
    {
        return "PlatformUserDetails{" +
                "openMetadataUserAccount=" + openMetadataUserAccount +
                '}';
    }


    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        PlatformUserDetails that = (PlatformUserDetails) objectToCompare;
        return Objects.equals(openMetadataUserAccount, that.openMetadataUserAccount);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(openMetadataUserAccount);
    }
}
