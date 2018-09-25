/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.ui.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.ldap.userdetails.InetOrgPersonContextMapper;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@Order(1)
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    TokenAuthService tokenAuthService;

    @Value("${ldap.user.search.base}")
    private String userSearchBase;

    @Value("${ldap.user.search.filter}")
    private String userSearchFilter;

    @Value("${ldap.group.search.base}")
    private String groupSearchBase;

    @Value("${ldap.group.search.filter}")
    private String groupSearchFilter;

    @Value("${ldap.url}")
    private String url;

    @Value("${authentication.source}")
    private String authenticationSource;


    public SecurityConfig() {
        super(true);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling().and()
                .anonymous().and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/*.html").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/h2/**").permitAll()
                .antMatchers("/img/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new TokenAuthFilter(tokenAuthService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new LoginFilter("/auth/login", authenticationManager(), tokenAuthService),
                        UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public InetOrgPersonContextMapper userContextMapper() {
        return new InetOrgPersonContextMapper();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        if (authenticationSource.equals("ldap"))
            auth
                    .ldapAuthentication()
                    .userSearchBase(userSearchBase)
                    .userSearchFilter(userSearchFilter)
                    .groupSearchBase(groupSearchBase)
                    .groupSearchFilter(groupSearchFilter)
                    .rolePrefix("")
                    .userDetailsContextMapper(userContextMapper())
                    .contextSource()
                    .url(url);
        else auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());

    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    }

}
