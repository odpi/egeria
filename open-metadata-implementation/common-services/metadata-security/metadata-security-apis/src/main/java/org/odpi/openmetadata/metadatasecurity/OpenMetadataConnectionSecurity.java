/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity;


import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.properties.Asset;
import org.odpi.openmetadata.metadatasecurity.properties.Connection;

import java.util.List;

/**
 * OpenMetadataConnectionSecurity defines the interface of a security connector that is validating whether a specific
 * user should be given access to a specific Connection object.  This connection information has been retrieved
 * from an open metadata repository.  It is used to create a Connector to an Asset.  It may include user
 * credentials that could enhance the access to data and function within the Asset that is far above
 * the specific user's approval.  This is why this optional check is performed by any open metadata service
 * that is returning a Connection object (or a Connector created with the Connection object) to an external party.
 */
public interface OpenMetadataConnectionSecurity
{
    /**
     * Tests for whether a specific user should have access to a connection.
     *
     * @param userId identifier of user
     * @param connection connection object
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    void  validateUserForConnection(String     userId,
                                    Connection connection) throws UserNotAuthorizedException;


    /**
     * Select a connection from the list of connections attached to an asset.
     *
     * @param userId calling user
     * @param asset asset requested by caller
     * @param connections list of attached connections
     * @return selected connection or null (pretend there are no connections attached to the asset) or
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    Connection validateUserForAssetConnectionList(String           userId,
                                                  Asset            asset,
                                                  List<Connection> connections) throws UserNotAuthorizedException;
}
