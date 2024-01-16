/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.stewardshipaction.client;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyAssetStore;

/**
 * SurveyAssetStoreClient provides a concrete implementation of the SurveyAssetStore for the
 * Survey Action Framework (SAF).  It delegates the work to the supplied connected asset client.
 */
public class SurveyAssetStoreClient extends SurveyAssetStore
{
    private final ConnectedAssetClient connectedAssetClient;

    /**
     * Constructor.
     *
     * @param assetGUID unique identifier for the asset un
     * @param userId calling user
     * @param connectedAssetClient connected asset client implements the REST API calls needed
     *                             to support the OCF calls.
     */
    public SurveyAssetStoreClient(String               assetGUID,
                                  String               userId,
                                  ConnectedAssetClient connectedAssetClient)
    {
        super(assetGUID, userId);

        this.connectedAssetClient = connectedAssetClient;
    }


    /**
     * Returns the connector corresponding to the supplied connection.
     *
     * @param connection the connection object that contains the properties needed to create the connection.
     * @return Connector   connector instance
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                    the creation of a connector.
     * @throws ConnectorCheckedException  there are errors in the initialization of the connector.
     */
    @Override
    public Connector getConnectorByConnection(Connection connection) throws InvalidParameterException,
                                                                            ConnectionCheckedException,
                                                                            ConnectorCheckedException
    {
        return connectedAssetClient.getConnectorByConnection(userId, connection);
    }


    /**
     * Returns a comprehensive collection of properties about the requested asset.
     *
     * @return a comprehensive collection of properties about the asset.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving the asset properties from the property servers).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public AssetUniverse getAssetProperties() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        return connectedAssetClient.getAssetProperties(AccessServiceDescription.STEWARDSHIP_ACTION_OMAS.getAccessServiceFullName(),
                                                       userId,
                                                       assetGUID);
    }


    /**
     * Log an audit message about this asset.
     *
     * @param surveyService name of survey service
     * @param message       message to log
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving the asset properties from the property servers).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void logAssetAuditMessage(String surveyService, String message) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        connectedAssetClient.logAssetAuditMessage(userId, assetGUID, surveyService, message);
    }


    /**
     * Return the connector to the requested asset.
     *
     * @return Open Connector Framework (OCF) connector
     * @throws InvalidParameterException  the asset guid is not recognized or the userId is null
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                    the creation of a connector.
     * @throws ConnectorCheckedException  there are errors in the initialization of the connector.
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or connection needed to
     *                                    create the connector.
     * @throws PropertyServerException    there was a problem in the store whether the asset/connection properties are kept.
     */
    @Override
    public Connector getConnectorToAsset() throws InvalidParameterException, ConnectionCheckedException, ConnectorCheckedException, UserNotAuthorizedException, PropertyServerException
    {
        return connectedAssetClient.getConnectorForAsset(userId, assetGUID);
    }
}
