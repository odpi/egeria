/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth;

import org.apache.commons.collections4.set.UnmodifiableSet;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.InetOrgPerson;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class TokenUser implements Serializable {

    private String username;

    private String displayName;

    private String firstName;

    private String lastName;

    private String email;

    private String avatarUrl;

    private Set<String> roles;

    public TokenUser() {}

    public TokenUser(String username, Collection<String> roles) {
        this.username = username;
        setRoles(roles.stream().collect(Collectors.toSet()));
    }

    public TokenUser(InetOrgPerson inetOrgPerson , Collection<String> roles) {
        this.username = inetOrgPerson.getUsername();
        this.setDisplayName(inetOrgPerson.getDisplayName());
        this.setFirstName(inetOrgPerson.getGivenName());
        this.setLastName(inetOrgPerson.getSn());
        this.setEmail(inetOrgPerson.getMail());
        setRoles(roles.stream().collect(Collectors.toSet()));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
       this.roles = UnmodifiableSet.unmodifiableSet(roles);
    }
}
