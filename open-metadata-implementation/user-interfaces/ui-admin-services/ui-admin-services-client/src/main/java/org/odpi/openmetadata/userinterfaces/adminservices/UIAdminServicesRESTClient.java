/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterfaces.adminservices;

import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;


/**
 * AssetOwnerRESTClient is responsible for issuing calls to the UI admin service REST APIs.
 */
class UIAdminServicesRESTClient extends FFDCRESTClient
{

    /**
     * Constructor for no authentication.
     *
     * @param serverName name of the UI Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the UI Server is running.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    UIAdminServicesRESTClient(String serverName,
                              String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Constructor for simple userId and password authentication.
     *
     * @param serverName name of the UI Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the UI Server is running.
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    UIAdminServicesRESTClient(String serverName,
                              String serverPlatformURLRoot,
                              String userId,
                              String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }

}
