/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userauthn.auth.ldap;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.InetOrgPersonContextMapper;


/**
 * ActiveDirectoryLDAPSecurityConfig provides the properties for
 */
@Configuration
public class ActiveDirectoryLDAPSecurityConfig
{

    @Value("${ldap.domain}")
    private String ldapDomain;

    @Value("${ldap.url}")
    protected String ldapURL;

    @Value("${ldap.user.search.base}")
    protected String userSearchBase;

    @Value("${ldap.user.search.filter}")
    protected String userSearchFilter;


    /**
     * Provide information about the active directory authentication service (user directory).
     *
     * @return the authentication provider component
     */
    @Bean
    @ConditionalOnProperty(value = "authentication.source", havingValue = "ad")
    public ActiveDirectoryLdapAuthenticationProvider getActiveDirectoryAuthenticationProvider()
    {
        ActiveDirectoryLdapAuthenticationProvider adProvider =
                new ActiveDirectoryLdapAuthenticationProvider(ldapDomain, ldapURL, userSearchBase);

        adProvider.setSearchFilter(userSearchFilter);
        adProvider.setUserDetailsContextMapper(new InetOrgPersonContextMapper());
        adProvider.setConvertSubErrorCodesToExceptions(true);
        adProvider.setUseAuthenticationRequestCredentials(true);

        return adProvider;
    }
}
