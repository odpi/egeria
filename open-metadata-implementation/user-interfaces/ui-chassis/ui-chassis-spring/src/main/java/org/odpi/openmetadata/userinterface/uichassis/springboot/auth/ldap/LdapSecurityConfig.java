/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth.ldap;

import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.AuthenticationExceptionHandler;
import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.SecurityConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.ldap.InvalidSearchFilterException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;


@EnableWebSecurity
@Configuration("securityConfig")
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(value = "authentication.source", havingValue = "ldap", matchIfMissing = true)
class LdapSecurityConfig extends SecurityConfig {

    @Value("${ldap.user.search.base}")
    private String userSearchBase;

    @Value("${ldap.user.search.filter}")
    private String userSearchFilter;

    @Value("${ldap.group.search.base}")
    private String groupSearchBase;

    @Value("${ldap.group.search.filter}")
    private String groupSearchFilter;

    @Value("${ldap.url}")
    private String ldapURL;

    @Value("#{'${ldap.user.dn.patterns}'.split(';')}")
    private String[] userDnPatterns;

    @Value("${ldap.npa.dn}")
    private String npaDn;

    @Value("${ldap.npa.password}")
    private String npaPassword;

    @Value("${ldap.group.role.attribute}")
    private String roleAttribute;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth
                    .ldapAuthentication()
                    .userDetailsContextMapper(userContextMapper())
                    .groupSearchBase(groupSearchBase)
                    .groupSearchFilter(groupSearchFilter)
                    .userSearchBase(userSearchBase)
                    .userSearchFilter(userSearchFilter)
                    .groupRoleAttribute(roleAttribute)
                    .rolePrefix("")
                    .contextSource()
                    .url(ldapURL )
                    .managerDn(npaDn)
                    .managerPassword(npaPassword)
                    .and()
                    .userDnPatterns(userDnPatterns);

    }

    @Override
    protected AuthenticationExceptionHandler getAuthenticationExceptionHandler() {
        return new AuthenticationExceptionHandler() {
            @Override
            public boolean isBadCredentials(AuthenticationException e) {
                return e instanceof BadCredentialsException || e.getCause() instanceof InvalidSearchFilterException;
            }
        };
    }
}
