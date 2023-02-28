/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth.ldap;

import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.AuthenticationExceptionHandler;
import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.SecurityConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.ldap.InvalidSearchFilterException;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.InetOrgPersonContextMapper;


@EnableWebSecurity
@Configuration("securityConfig")
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(value = "authentication.source", havingValue = "ldap", matchIfMissing = true)
class LdapSecurityConfig {

    @Value("${ldap.user.search.base}")
    protected String userSearchBase;

    @Value("${ldap.user.search.filter}")
    protected String userSearchFilter;

    @Value("${ldap.group.search.base}")
    protected String groupSearchBase;

    @Value("${ldap.group.search.filter}")
    protected String groupSearchFilter;

    @Value("${ldap.url}")
    protected String ldapURL;

    @Value("#{'${ldap.user.dn.patterns}'.split(';')}")
    protected String[] userDnPatterns;

    @Value("${ldap.npa.dn}")
    protected String npaDn;

    @Value("${ldap.npa.password}")
    protected String npaPassword;

    @Value("${ldap.group.role.attribute}")
    protected String roleAttribute;


    @Bean
    @ConditionalOnProperty(value = "authentication.source", havingValue = "ad")
    public LdapAuthenticationProvider getActiveDirectoryAuthenticationProvider(){

        LdapContextSource ldapContextSource = new DefaultSpringSecurityContextSource(ldapURL);
        ldapContextSource.setUserDn(npaDn);
        ldapContextSource.setPassword(npaPassword);
        ldapContextSource.setCacheEnvironmentProperties(true);
        ldapContextSource.setAnonymousReadOnly(false);
        ldapContextSource.setPooled(true);
        ldapContextSource.afterPropertiesSet();

        BindAuthenticator authenticator = new BindAuthenticator(ldapContextSource);
        authenticator.setUserDnPatterns(userDnPatterns);

        LdapAuthenticationProvider ldapAuthenticationProvider = new LdapAuthenticationProvider(authenticator);
        ldapAuthenticationProvider.setUserDetailsContextMapper(new InetOrgPersonContextMapper());

        return ldapAuthenticationProvider;
    }

//    @Override
//    protected AuthenticationExceptionHandler getAuthenticationExceptionHandler() {
//        return e -> e instanceof BadCredentialsException || e.getCause() instanceof InvalidSearchFilterException;
//    }
}
