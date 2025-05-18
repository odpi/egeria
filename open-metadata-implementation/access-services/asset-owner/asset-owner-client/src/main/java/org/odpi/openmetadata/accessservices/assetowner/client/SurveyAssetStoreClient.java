/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.client;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.surveyaction.SurveyAssetStore;

import java.util.List;


/**
 * SurveyAssetStoreClient provides a concrete implementation of the SurveyAssetStore for the
 * Survey Action Framework (SAF).  It delegates the work to the supplied connected asset client.
 */
public class SurveyAssetStoreClient extends SurveyAssetStore
{
    private final ConnectedAssetClient connectedAssetClient;
    private final FileSystemAssetOwner fileSystemAssetOwner;
    private final CSVFileAssetOwner    csvFileAssetOwner;
    private final AuditLog             auditLog;

    /**
     * Constructor.
     *
     * @param assetGUID unique identifier for the asset un
     * @param userId calling user
     * @param connectedAssetClient connected asset client implements the REST API calls needed
     *                             to support the OCF calls.
     * @param fileSystemAssetOwner client to work with files and folders
     * @param csvFileAssetOwner client to work with CSV files
     */
    public SurveyAssetStoreClient(String               assetGUID,
                                  String               userId,
                                  ConnectedAssetClient connectedAssetClient,
                                  FileSystemAssetOwner fileSystemAssetOwner,
                                  CSVFileAssetOwner    csvFileAssetOwner,
                                  AuditLog             auditLog)
    {
        super(assetGUID, userId);

        this.connectedAssetClient = connectedAssetClient;
        this.fileSystemAssetOwner = fileSystemAssetOwner;
        this.csvFileAssetOwner    = csvFileAssetOwner;
        this.auditLog             = auditLog;
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
     * @throws PropertyServerException    there is a problem retrieving the asset properties from the property servers.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public AssetUniverse getAssetProperties() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        return connectedAssetClient.getAssetProperties(userId, assetGUID);
    }


    /**
     * Log an audit message about this asset.
     *
     * @param surveyService name of survey service
     * @param message       message to log
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving the asset properties from the property servers.
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
        return connectedAssetClient.getConnectorForAsset(userId, assetGUID, auditLog);
    }


    /**
     * Creates a new data file asset and links it to the folder structure implied in the path name.  If the folder
     * structure is not catalogued already, this is created automatically using the createFolderStructureInCatalog() method.
     * For example, a pathName of "one/two/three/MyFile.txt" potentially creates 3 new folder assets, one called "one",
     * the next called "one/two" and the last one called "one/two/three" plus a file asset called
     * "one/two/three/MyFile.txt".
     *
     * @param displayName display name for the file in the catalog
     * @param description description of the file in the catalog
     * @param pathName pathname of the data file
     *
     * @return list of GUIDs from the top level to the root of the pathname
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public String addDataFileAssetToCatalog(String   displayName,
                                            String   description,
                                            String   pathName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        List<String> fileAssetGUIDs = fileSystemAssetOwner.addDataFileAssetToCatalog(userId, displayName, description, pathName);

        if ((fileAssetGUIDs != null) && (! fileAssetGUIDs.isEmpty()))
        {
            return fileAssetGUIDs.get(fileAssetGUIDs.size() - 1);
        }

        return null;
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
    @Override
    public String  addCSVFileToCatalog(String       displayName,
                                       String       description,
                                       String       pathName,
                                       List<String> columnHeaders,
                                       Character    delimiterCharacter,
                                       Character    quoteCharacter) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        List<String> fileAssetGUIDs = csvFileAssetOwner.addCSVFileToCatalog(userId, displayName, description, pathName, columnHeaders, delimiterCharacter, quoteCharacter);

        if ((fileAssetGUIDs != null) && (! fileAssetGUIDs.isEmpty()))
        {
            return fileAssetGUIDs.get(fileAssetGUIDs.size() - 1);
        }

        return null;
    }
}
