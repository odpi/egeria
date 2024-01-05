/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userauthn.auth.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Return details of the users defined in the demo users yaml file.
 */
@Service
@ConfigurationProperties(prefix = "egeria", ignoreUnknownFields = false, ignoreInvalidFields = false)
public class DemoUsers
{
    private final Map<String, User> demoUsers  = new HashMap<>();;


    /**
     * Return the defined demo users.
     *
     * @return map
     */
    public Map<String, User> getDemoUsers()
    {
        return demoUsers;
    }

    /**
     * Return details about a specific user.
     *
     * @param username the userId
     * @return a clone of the user from the map, with password encrypted.
     */
    public User getUser (String username)
    {
        User u1 = new User();
        User u2 = demoUsers.get(username);
        if (u2 != null)
        {
            u1.setUsername(u2.getUsername());
            u1.setPassword(new BCryptPasswordEncoder().encode(u2.getPassword()));
            u1.setAvatarUrl(u2.getAvatarUrl());
            u1.setId(u2.getId());
            u1.setRoles( new ArrayList<>(u2.getRoles()));
            u1.setName(u2.getName());
        }

        return u1;
    }
}
