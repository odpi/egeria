/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.authenticationplugins.gaianimpersonation;

import com.ibm.gaiandb.GaianAuthenticator;
import org.apache.derby.authentication.UserAuthenticator;

import java.sql.SQLException;
import java.util.Properties;

/**
 * ProxyUserAuthenticator performs the user authentication for the user name using the proxy properties or the basic
 * authentication if proxy properties or not set up.
 */
public class ProxyUserAuthenticator implements UserAuthenticator {

    private static final com.ibm.gaiandb.Logger logger = new com.ibm.gaiandb.Logger("ProxyUserAuthenticator", 30);

    private static final String PROXY_UID_KEY = "proxy-user";
    private static final String PROXY_PWD_KEY = "proxy-pwd";
    private static final GaianAuthenticator basAuth = new GaianAuthenticator();

    /**
     * Authenticate the user using proxy properties for the default ones
     *
     * @param userName      the name of the user that should be authenticated
     * @param passwordOrSid password to authenticate the user
     * @param dbName        database name
     * @param info          properties
     * @return boolean if the authentication was successful or not
     * @throws SQLException provides information on a database access error or other errors
     */
    public boolean authenticateUser(String userName, String passwordOrSid, String dbName, Properties info) throws SQLException {

        String userId = info.getProperty(PROXY_UID_KEY);
        String password = info.getProperty(PROXY_PWD_KEY);

        boolean isAuthenticated = performProxyAuthentication(userName, dbName, info, userId, password);

        if (!isAuthenticated) {
            isAuthenticated = performRegularAuthentication(userName, passwordOrSid, dbName, info);
        }

        if (isAuthenticated) {
            logger.logDetail("authentication was successful");
        } else {
            logger.logDetail("authentication failed");
        }

        return isAuthenticated;
    }

    /**
     * In case that the proxy authentication failed or the proxy user is not used,
     * perform regular authentication for the user, no need to manipulate properties.
     *
     * @param userName      the name of the user that should be authenticated
     * @param passwordOrSid password to authenticate the user
     * @param dbName        database name
     * @param info          properties
     * @return boolean true if the authentication was successful
     * @throws SQLException provides information on a database access error or other errors
     */
    private boolean performRegularAuthentication(String userName, String passwordOrSid, String dbName, Properties info) throws SQLException {
        logger.logDetail("Performing regular authentication for user:" + userName);
        return basAuth.authenticateUser(userName, passwordOrSid, dbName, info);
    }

    /**
     * Authentication based on this proxy user and can assert identity amd force automatic creation of schema
     * There are no additional checks to control if this user has privileges to perform escalation.
     *
     * @param userName the name of the user that should be authenticated
     * @param dbName   database name
     * @param info     properties
     * @param userId user identifier
     * @param password user password
     * @return boolean true if the authentication was successful
     * @throws SQLException provides information on a database access error or other errors
     */
    private boolean performProxyAuthentication(String userName, String dbName, Properties info, String userId, String password) throws SQLException {

        if (userId == null || password == null) {
            return false;
        }

        logger.logDetail("Performing proxy authentication with user:" + userId + " on behalf of:" + userName);
        info.setProperty("create", "true");

        return basAuth.authenticateUser(userId, password, dbName, info);
    }
}