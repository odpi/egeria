/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the Egeria project. */

package org.odpi.openmetadata.accessservices.connectedasset.client;

import org.odpi.openmetadata.accessservices.connectedasset.ffdc.ConnectedAssetErrorCode;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.NoConnectedAssetException;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.UnrecognizedConnectionGUIDException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;


/**
 * ConnectedAssetProperties is associated with a Connector.  Connectors provide access to
 * assets.   ConnectedAssetProperties returns properties (metadata) about the connector's asset.
 *
 * It is a generic interface for all types of open metadata assets.  However, it assumes the asset's metadata model
 * inherits from <b>Asset</b> (see model 0010 in Area 0).
 *
 * The ConnectedAssetProperties returns metadata about the asset at three levels of detail:
 * <ul>
 *     <li><b>assetSummary</b> - used for displaying details of the asset in summary lists or hover text</li>
 *     <li><b>assetDetail</b> - used to display all of the information known about the asset with summaries
 *     of the relationships to other metadata entities</li>
 *     <li><b>assetUniverse</b> - used to define the broader context for the asset</li>
 * </ul>
 *
 * ConnectedAssetProperties is a base class for the connector's metadata API that returns null,
 * for the asset's properties.  Metadata repository implementations extend this class to add their
 * implementation of the refresh() method that calls to the metadata repository to populate the metadata properties.
 */
public class ConnectedAssetProperties extends org.odpi.openmetadata.frameworks.connectors.properties.ConnectedAssetProperties
{
    private String               userId = null;
    private String               omasServerURL = null;
    private String               connectorInstanceId = null;
    private ConnectionProperties connection = null;

    private static final Logger log = LoggerFactory.getLogger(ConnectedAssetProperties.class);


    /**
     * Returns the unique identifier for the asset connected to the connection.
     *
     * @param userId the userId of the requesting user.
     * @param connectionGUID  uniqueId for the connection.
     *
     * @return unique identifer for the asset.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the connected asset properties from the property server.
     * @throws UnrecognizedConnectionGUIDException the supplied GUID is not recognized by the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String getAssetForConnection(String   userId,
                                         String   connectionGUID) throws InvalidParameterException,
                                                                         UnrecognizedConnectionGUIDException,
                                                                         NoConnectedAssetException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        return null;
    }


    /**
     * Typical constructor.
     *
     * @param userId  identifier of calling user
     * @param omasServerURL  url of server
     * @param connectorInstanceId  unique identifier of connector.
     * @param connection  connection information for connector.
     */
    public ConnectedAssetProperties(String               userId,
                                    String               omasServerURL,
                                    String               connectorInstanceId,
                                    ConnectionProperties connection)
    {
        super();

        this.userId = userId;
        this.omasServerURL = omasServerURL;
        this.connectorInstanceId = connectorInstanceId;
        this.connection = connection;
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateProperties  template to copy.
     */
    public ConnectedAssetProperties(ConnectedAssetProperties   templateProperties)
    {
        super(templateProperties);

        if (templateProperties != null)
        {
            this.userId = templateProperties.userId;
            this.connection = templateProperties.connection;
            this.connectorInstanceId = templateProperties.connectorInstanceId;
            this.omasServerURL = templateProperties.omasServerURL;
        }
    }


    /**
     * Request the values in the ConnectedAssetProperties are refreshed with the current values from the
     * metadata repository.
     *
     * @throws PropertyServerException    there is a problem connecting to the server to retrieve metadata.
     * @throws UserNotAuthorizedException the userId associated with the connector is not authorized to
     *                                    access the asset properties.
     */
    public void refresh() throws PropertyServerException, UserNotAuthorizedException
    {
        final  String  methodName  = "refresh";
        final  String  serviceName = "ConnectedAssetProperties";

        log.debug("Calling method: " + methodName);

        try
        {
            assetProperties = new ConnectedAsset(omasServerURL,
                                                 userId,
                                                 this.getAssetForConnection(userId, connection.getGUID()));
        }
        catch (UserNotAuthorizedException  error)
        {
            throw error;
        }
        catch (Throwable  error)
        {
            ConnectedAssetErrorCode errorCode    = ConnectedAssetErrorCode.PROPERTY_SERVER_ERROR;
            String                  errorMessage = errorCode.getErrorMessageId()
                                                 + errorCode.getFormattedErrorMessage(methodName,
                                                                                      serviceName,
                                                                                      omasServerURL,
                                                                                      error.getMessage());

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction(),
                                              error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + assetProperties.toString());
    }
}