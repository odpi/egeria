/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.server;

import org.odpi.openmetadata.accessservices.connectedasset.admin.ConnectedAssetAdmin;
import org.odpi.openmetadata.accessservices.connectedasset.server.properties.AssetUniverseResponse;
import org.odpi.openmetadata.adminservices.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;


/**
 * The ConnectedAssetRESTServices is the server-side implementation of the Connected Asset OMAS REST interface.
 */
public class ConnectedAssetRESTServices
{
    static private String                  accessServiceName   = null;
    static private OMRSRepositoryConnector repositoryConnector = null;

    /**
     * Provide a connector to the REST Services.
     *
     * @param accessServiceName - name of this access service
     * @param repositoryConnector - OMRS Repository Connector to the property server.
     */
    static public void setRepositoryConnector(String                   accessServiceName,
                                              OMRSRepositoryConnector repositoryConnector)
    {
        ConnectedAssetRESTServices.accessServiceName = accessServiceName;
        ConnectedAssetRESTServices.repositoryConnector = repositoryConnector;
    }

    /**
     * Default constructor
     */
    public ConnectedAssetRESTServices()
    {
        AccessServiceDescription myDescription = AccessServiceDescription.CONNECTED_ASSET_OMAS;
        AccessServiceRegistration myRegistration = new AccessServiceRegistration(myDescription.getAccessServiceCode(),
                                                                                 myDescription.getAccessServiceName(),
                                                                                 myDescription.getAccessServiceDescription(),
                                                                                 myDescription.getAccessServiceWiki(),
                                                                                 AccessServiceOperationalStatus.ENABLED,
                                                                                 ConnectedAssetAdmin.class.getName());
        OMAGAccessServiceRegistration.registerAccessService(myRegistration);
    }


    /**
     * Returns a comprehensive collection of properties about the requested asset.
     *
     * @param userId - String - userId of user making request.
     * @param guid - String - unique id for asset.
     *
     * @return AssetUniverseResponse - a comprehensive collection of properties about the asset or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem retrieving the asset properties from
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public AssetUniverseResponse getAssetProperties(String   userId,
                                                    String   guid)
    {
        return null;
    }



    /**
     * Returns a comprehensive collection of properties about the asset linked to the supplied connection.
     *
     * @param userId - String - userId of user making request.
     * @param guid - String - unique id for connection.
     * @return AssetUniverse - a comprehensive collection of properties about the connected asset
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem retrieving the asset properties from
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public AssetUniverseResponse  getAssetPropertiesByConnection(String   userId,
                                                                 String   guid)
    {
        AssetUniverse extractedAssetProperties = null;

        return null;
    }
}
