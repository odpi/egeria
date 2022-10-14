/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("demoUserDetailsService")
@ConditionalOnProperty(value = "authentication.source", havingValue = "demo")
@EnableConfigurationProperties( DemoUsers.class )
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

    @Autowired
    DemoUsers demoUsers;

    @Override
    public final User loadUserByUsername(String username) throws UsernameNotFoundException {

        final User user = demoUsers.getUser(username);

        detailsChecker.check(user);

        return user;
    }

}



