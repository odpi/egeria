/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


package org.odpi.openmetadata.adapters.authenticationplugins.gaianimpersonation;

import com.ibm.gaiandb.GaianAuthenticator;
import com.ibm.gaiandb.Logger;
import org.apache.derby.authentication.UserAuthenticator;

import java.sql.SQLException;
import java.util.Properties;

//TODO
public class ProxyUserAuthenticator implements UserAuthenticator {

    private static final Logger logger = new Logger("ProxyUserAuthenticator", 30);


    private static final String PROXYUID_KEY = "proxy-user";
    private static final String PROXYPWD_KEY = "proxy-pwd";

    private static final GaianAuthenticator basAuth = new GaianAuthenticator();

    public boolean authenticateUser(String userName, String passwordOrSid, String dbName, Properties info) throws SQLException {

        boolean res = false;

        // see if we need to go with proxy auth
        String proxyUID = info.getProperty(PROXYUID_KEY);
        String proxyPwd = info.getProperty(PROXYPWD_KEY);

        if (null != proxyUID && null != proxyPwd) {
            // we authenticate based on this proxy user & can assert identity
            // there are no additional checks to control if this user has privileges to perform escalation
            logger.logInfo("Performing proxy authentication with user:" + proxyUID + " on behalf of:" + userName);

            // force automatic creation of schema
            info.setProperty("create", "true");

            res = basAuth.authenticateUser(proxyUID, proxyPwd, dbName, info);

        }


        // If either that didn't work - or we are not using proxy then we'll auth normally
        // straight pass through - no need to manipulate properties
        if (!res) {
            logger.logInfo("Performing regular authentication for user:" + userName);
            res = basAuth.authenticateUser(userName, passwordOrSid, dbName, info);  // drop back to Basic if no asserted id
        }

        if (res)
            logger.logInfo("authentication was successful");
        else
            logger.logInfo("authentication failed");

        return res;
    }
}
