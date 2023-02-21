/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth.ldap;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.InetOrgPersonContextMapper;

@Configuration
public class ActiveDirectoryLdapSecurityConfig {

    @Value("${ldap.domain}")
    private String ldapDomain;

    @Value("${ldap.url}")
    protected String ldapURL;

    @Value("${ldap.user.search.base}")
    protected String userSearchBase;

    @Value("${ldap.user.search.filter}")
    protected String userSearchFilter;

    @Bean
    @ConditionalOnProperty(value = "authentication.source", havingValue = "ad")
    public ActiveDirectoryLdapAuthenticationProvider getActiveDirectoryAuthenticationProvider(){
        ActiveDirectoryLdapAuthenticationProvider adProvider =
                new ActiveDirectoryLdapAuthenticationProvider(
                        ldapDomain,
                        ldapURL,
                        userSearchBase);
        adProvider.setSearchFilter(userSearchFilter);
        adProvider.setUserDetailsContextMapper(new InetOrgPersonContextMapper());
        adProvider.setConvertSubErrorCodesToExceptions(true);
        adProvider.setUseAuthenticationRequestCredentials(true);
        return adProvider;
    }
}
