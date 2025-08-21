/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opensurvey;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.client.ConnectedAssetClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.AssetHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;

import java.util.List;

/**
 * SurveyAssetStore defines the interface to a connector broker backed by an open metadata repository that returns
 * information about the Asset that a survey action service is to analyze.  It is built around the Open Connector
 * Framework (OCF) services and Open Survey Framework (OGF).
 */
public class SurveyAssetStore
{
    private final String assetGUID;
    private final String userId;
    ConnectedAssetClient connectedAssetClient;
    AssetHandler         assetHandler;
    private final AuditLog             auditLog;

    /**
     * Constructor sets up the key parameters for accessing the asset store.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the annotations should be attached to
     */
    public SurveyAssetStore(String               assetGUID,
                            String               userId,
                            ConnectedAssetClient connectedAssetClient,
                            AssetHandler         assetHandler,
                            AuditLog             auditLog)
    {
        this.assetGUID            = assetGUID;
        this.userId               = userId;
        this.connectedAssetClient = connectedAssetClient;
        this.assetHandler         = assetHandler;
        this.auditLog             = auditLog;
    }


    /**
     * Return the unique identifier for the asset.
     *
     * @return guid
     */
    public String getAssetGUID()
    {
        return assetGUID;
    }


    /**
     * Returns the connector corresponding to the supplied connection.
     *
     * @param connection   the connection object that contains the properties needed to create the connection.
     *
     * @return the connector instance
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws UserNotAuthorizedException connector disconnected
     */
    public Connector getConnectorByConnection(Connection connection) throws InvalidParameterException,
                                                                            ConnectionCheckedException,
                                                                            ConnectorCheckedException,
                                                                            UserNotAuthorizedException
    {
        return connectedAssetClient.getConnectorByConnection(userId, connection);
    }


    /**
     * Returns a comprehensive collection of properties about the requested asset.
     *
     * @return a comprehensive collection of properties about the asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property servers.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getAssetProperties() throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        return assetHandler.getAssetByGUID(userId, assetGUID, null);
    }


    /**
     * Returns a comprehensive collection of properties about the requested asset.
     *
     * @return a comprehensive collection of properties about the asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property servers.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String getQualifiedName() throws InvalidParameterException,
                                            PropertyServerException,
                                            UserNotAuthorizedException
    {
        OpenMetadataRootElement assetElement = this.getAssetProperties();

        if (assetElement.getProperties() instanceof AssetProperties assetProperties)
        {
            return assetProperties.getQualifiedName();
        }

        return null;
    }


    /**
     * Return the connector to the requested asset.
     *
     * @return Open Connector Framework (OCF) connector
     * @throws InvalidParameterException the asset guid is not recognized or the userId is null
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or connection needed to
     *                                    create the connector.
     * @throws PropertyServerException there was a problem in the store whether the asset/connection properties are kept.
     */
    public Connector  getConnectorToAsset() throws InvalidParameterException,
                                                   ConnectionCheckedException,
                                                   ConnectorCheckedException,
                                                   UserNotAuthorizedException,
                                                   PropertyServerException
    {
        return connectedAssetClient.getConnectorForAsset(userId, assetGUID);
    }


    /**
     * Add a simple asset description linked to a connection object for a CSV file.
     *
     * @param displayName display name for the file in the catalog
     * @param description description of the file in the catalog
     * @param pathName full path of the file - used to access the file through the connector
     * @param columnHeaders does the first line of the file contain the column names. If not pass the list of column headers.
     * @param delimiterCharacter what is the delimiter character - null for default of comma
     * @param quoteCharacter what is the character to group a field that contains delimiter characters
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public  String  addCSVFileToCatalog(String       displayName,
                                        String       description,
                                        String       pathName,
                                        List<String> columnHeaders,
                                        Character    delimiterCharacter,
                                        Character    quoteCharacter) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return assetHandler.addCSVFileToCatalog(userId,
                                                displayName,
                                                description,
                                                pathName,
                                                columnHeaders,
                                                delimiterCharacter,
                                                quoteCharacter);
    }
}
