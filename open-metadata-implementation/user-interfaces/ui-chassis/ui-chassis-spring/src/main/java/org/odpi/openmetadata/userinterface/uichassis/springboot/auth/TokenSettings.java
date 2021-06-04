/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.auth;

import org.springframework.beans.factory.annotation.Value;

public class TokenSettings extends RoleService{

    @Value("${token.secret}")
    protected String tokenSecret;

    /**
     * token timout in minutes
     */
    @Value("${token.timeout:30}")
    protected Long tokenTimeout;

    /**
     *
     * @return token timeout in milliseconds
     */
    public long getTokenTimeout(){
        return tokenTimeout * 60 * 1000;
    }
}
