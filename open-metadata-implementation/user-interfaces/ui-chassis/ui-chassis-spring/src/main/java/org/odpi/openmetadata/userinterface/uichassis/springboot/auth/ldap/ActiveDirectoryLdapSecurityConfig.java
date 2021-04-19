/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth.ldap;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

@EnableWebSecurity
@Configuration("securityConfig")
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(value = "authentication.source", havingValue = "ad")
public class ActiveDirectoryLdapSecurityConfig extends LdapSecurityConfig {

    @Value("${ldap.domain}")
    private String ldapDomain;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        ActiveDirectoryLdapAuthenticationProvider adProvider =
                new ActiveDirectoryLdapAuthenticationProvider(
                        ldapDomain,
                        ldapURL,
                        userSearchBase);
        adProvider.setSearchFilter(userSearchFilter);
        adProvider.setUserDetailsContextMapper(userContextMapper());
        adProvider.setConvertSubErrorCodesToExceptions(true);
        adProvider.setUseAuthenticationRequestCredentials(true);
        auth.authenticationProvider(adProvider);
    }
}
