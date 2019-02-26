/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.auth;

import org.odpi.openmetadata.userinterface.accessservices.domain.User;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.ldap.userdetails.InetOrgPerson;

import java.util.List;
import java.util.stream.Collectors;


public class TokenUser extends org.springframework.security.core.userdetails.User {
    private User user;

    public TokenUser(User user) {
        super(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRoles().toArray(new String[]{})));
        this.user = user;
    }

    public TokenUser(InetOrgPerson inetOrgPerson) {
        super(inetOrgPerson.getUsername(), "" , inetOrgPerson.getAuthorities());
        this.user = new User();
        this.user.setUsername(inetOrgPerson.getUsername());
        this.user.setRoles(inetOrgPerson.getAuthorities().stream().map((e -> e.getAuthority())).collect(Collectors.toList()));
        this.user.setName(inetOrgPerson.getSn());
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    public List<String> getRole() {
        return user.getRoles();
    }
}
