/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth.db;

import org.odpi.openmetadata.userinterface.uichassis.springboot.auth.TokenUser;
import org.odpi.openmetadata.userinterface.uichassis.springboot.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service("dbUserDetailsService")
@ConditionalOnProperty(value = "authentication.source", havingValue = "db")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

    @Override
    public final TokenUser loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepo.findOneByUsername(username).orElseThrow(() -> new UsernameNotFoundException(""));

        TokenUser currentUser = new TokenUser(user);
        detailsChecker.check(currentUser);

        return currentUser;
    }
}