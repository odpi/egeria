/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client;

import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectedAssetDetails;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;

import java.io.Serial;


/**
 * EgeriaConnectedAssetDetails is associated with a Connector.  Connectors provide access to
 * assets.   EgeriaConnectedAssetDetails returns properties (metadata) about the connector's asset.
 * It is a generic interface for all types of open metadata assets.  However, it assumes the asset's metadata model
 * inherits from <b>Asset</b> (see model 0010 in Area 0).
 * The EgeriaConnectedAssetDetails returns metadata about the asset at three levels of detail:
 * <ul>
 *     <li><b>assetSummary</b> - used for displaying details of the asset in summary lists or hover text</li>
 *     <li><b>assetDetail</b> - used to display all the information known about the asset with summaries
 *     of the relationships to other metadata entities</li>
 *     <li><b>assetUniverse</b> - used to define the broader context for the asset</li>
 * </ul>
 *
 * EgeriaConnectedAssetDetails is a base class for the connector's metadata API that returns null,
 * for the asset's properties.  Metadata repository implementations extend this class to add their
 * implementation of the refresh() method that calls to the metadata repository to populate the metadata properties.
 */
public class EgeriaConnectedAssetDetails extends ConnectedAssetDetails
{
    private String               serviceName;
    private String               remoteServerName;
    private String               userId              = null;
    private String               localServerUserId   = null;
    private String               localServerPassword = null;
    private String               platformURLRoot       = null;
    private String            connectorInstanceId = null;
    private ConnectionDetails connection          = null;
    private String            assetGUID           = null;


    @Serial
    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(EgeriaConnectedAssetDetails.class);


    /**
     * Constructor with no security used on the HTTP request.
     *
     * @param serviceName calling service
     * @param remoteServerName  name of the server.
     * @param userId  identifier of calling user
     * @param platformURLRoot  url of server
     * @param connectorInstanceId  unique identifier of connector.
     * @param connection  connection information for connector.
     * @param assetGUID  String   unique id for connected asset.
     */
    public EgeriaConnectedAssetDetails(String               serviceName,
                                       String               remoteServerName,
                                       String               userId,
                                       String               platformURLRoot,
                                       String               connectorInstanceId,
                                       ConnectionDetails connection,
                                       String               assetGUID)
    {
        super();

        this.serviceName         = serviceName;
        this.remoteServerName    = remoteServerName;
        this.userId              = userId;
        this.platformURLRoot     = platformURLRoot;
        this.connectorInstanceId = connectorInstanceId;
        this.connection          = connection;
        this.assetGUID           = assetGUID;
    }


    /**
     * Constructor with userId and password embedded in the HTTP request.
     *
     * @param serviceName calling service
     * @param remoteServerName  name of the server to call.
     * @param localServerUserId userId to use on the rest call.
     * @param localServerPassword password to use on the rest call.
     * @param userId  identifier of calling user
     * @param platformURLRoot  url of server
     * @param connectorInstanceId  unique identifier of connector.
     * @param connection  connection information for connector.
     * @param assetGUID  String   unique id for connected asset.
     */
    public EgeriaConnectedAssetDetails(String               serviceName,
                                       String               remoteServerName,
                                       String               localServerUserId,
                                       String               localServerPassword,
                                       String               userId,
                                       String               platformURLRoot,
                                       String               connectorInstanceId,
                                       ConnectionDetails connection,
                                       String               assetGUID)
    {
        super();

        this.serviceName         = serviceName;
        this.remoteServerName    = remoteServerName;
        this.localServerUserId   = localServerUserId;
        this.localServerPassword = localServerPassword;
        this.userId              = userId;
        this.platformURLRoot     = platformURLRoot;
        this.connectorInstanceId = connectorInstanceId;
        this.connection          = connection;
        this.assetGUID           = assetGUID;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template  template to copy.
     */
    public EgeriaConnectedAssetDetails(EgeriaConnectedAssetDetails template)
    {
        super(template);

        if (template != null)
        {
            this.serviceName         = template.serviceName;
            this.remoteServerName    = template.remoteServerName;
            this.localServerUserId   = template.localServerUserId;
            this.localServerPassword = template.localServerPassword;
            this.userId              = template.userId;
            this.connection          = template.connection;
            this.connectorInstanceId = template.connectorInstanceId;
            this.platformURLRoot     = template.platformURLRoot;
            this.assetGUID           = template.assetGUID;
        }
    }


    /**
     * Request the values in the EgeriaConnectedAssetDetails are refreshed with the current values from the
     * metadata repository.
     *
     * @throws PropertyServerException    there is a problem connecting to the server to retrieve metadata.
     * @throws UserNotAuthorizedException the userId associated with the connector is not authorized to
     *                                    access the asset properties.
     */
    @Override
    public void refresh() throws PropertyServerException, UserNotAuthorizedException
    {
        final  String  methodName  = "refresh";

        log.debug("Calling method: " + methodName);

        RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

        try
        {
            if ((localServerUserId != null) && (localServerPassword != null))
            {
                assetProperties = ConnectedAssetUniverse.create(serviceName,
                                                                remoteServerName,
                                                                localServerUserId,
                                                                localServerPassword,
                                                                platformURLRoot,
                                                                userId,
                                                                assetGUID,
                                                                connection.getGUID());
            }
            else
            {
                assetProperties = ConnectedAssetUniverse.create(serviceName,
                                                                remoteServerName,
                                                                platformURLRoot,
                                                                userId,
                                                                assetGUID,
                                                                connection.getGUID());
            }
        }
        catch (UserNotAuthorizedException  error)
        {
            throw error;
        }
        catch (Exception  error)
        {
            restExceptionHandler.handleUnexpectedException(error, methodName, remoteServerName, platformURLRoot);
        }

        log.debug("Returning from method: " + methodName + " having retrieved: " + assetProperties.toString());
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "EgeriaConnectedAssetDetails{" +
                "serviceName='" + serviceName + '\'' +
                ", remoteServerName='" + remoteServerName + '\'' +
                ", userId='" + userId + '\'' +
                ", localServerUserId='" + localServerUserId + '\'' +
                ", platformURLRoot='" + platformURLRoot + '\'' +
                ", connectorInstanceId='" + connectorInstanceId + '\'' +
                ", connection=" + connection +
                ", assetGUID='" + assetGUID + '\'' +
                ", extendedProperties=" + assetProperties +
                '}';
    }
}