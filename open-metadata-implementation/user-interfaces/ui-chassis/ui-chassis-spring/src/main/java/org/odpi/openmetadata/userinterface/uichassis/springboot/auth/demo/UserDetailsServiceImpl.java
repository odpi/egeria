/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("demoUserDetailsService")
@ConditionalOnProperty(value = "authentication.source", havingValue = "demo")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

    @Autowired
    DemoUsers demoUsers;

    @Override
    public final User loadUserByUsername(String username) throws UsernameNotFoundException {

        final User user = demoUsers.getDemoUsers().get(username);

        //encrypt the password
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        detailsChecker.check(user);

        return user;
    }

}



