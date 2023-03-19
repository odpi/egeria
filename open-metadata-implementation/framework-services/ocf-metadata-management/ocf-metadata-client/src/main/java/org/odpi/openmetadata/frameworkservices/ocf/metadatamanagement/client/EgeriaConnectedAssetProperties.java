/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client;

import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectedAssetProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;


/**
 * EgeriaConnectedAssetProperties is associated with a Connector.  Connectors provide access to
 * assets.   EgeriaConnectedAssetProperties returns properties (metadata) about the connector's asset.
 *
 * It is a generic interface for all types of open metadata assets.  However, it assumes the asset's metadata model
 * inherits from <b>Asset</b> (see model 0010 in Area 0).
 *
 * The EgeriaConnectedAssetProperties returns metadata about the asset at three levels of detail:
 * <ul>
 *     <li><b>assetSummary</b> - used for displaying details of the asset in summary lists or hover text</li>
 *     <li><b>assetDetail</b> - used to display all the information known about the asset with summaries
 *     of the relationships to other metadata entities</li>
 *     <li><b>assetUniverse</b> - used to define the broader context for the asset</li>
 * </ul>
 *
 * EgeriaConnectedAssetProperties is a base class for the connector's metadata API that returns null,
 * for the asset's properties.  Metadata repository implementations extend this class to add their
 * implementation of the refresh() method that calls to the metadata repository to populate the metadata properties.
 */
public class EgeriaConnectedAssetProperties extends ConnectedAssetProperties
{
    private static final long    serialVersionUID = 1L;

    private String               serviceName;
    private String               remoteServerName;
    private String               userId              = null;
    private String               localServerUserId   = null;
    private String               localServerPassword = null;
    private String               omasServerURL       = null;
    private String               connectorInstanceId = null;
    private ConnectionProperties connection          = null;
    private String               assetGUID           = null;


    private static final Logger log = LoggerFactory.getLogger(EgeriaConnectedAssetProperties.class);


    /**
     * Constructor with no security used on the HTTP request.
     *
     * @param serviceName calling service
     * @param remoteServerName  name of the server.
     * @param userId  identifier of calling user
     * @param omasServerURL  url of server
     * @param connectorInstanceId  unique identifier of connector.
     * @param connection  connection information for connector.
     * @param assetGUID  String   unique id for connected asset.
     */
    public EgeriaConnectedAssetProperties(String               serviceName,
                                          String               remoteServerName,
                                          String               userId,
                                          String               omasServerURL,
                                          String               connectorInstanceId,
                                          ConnectionProperties connection,
                                          String               assetGUID)
    {
        super();

        this.serviceName         = serviceName;
        this.remoteServerName    = remoteServerName;
        this.userId              = userId;
        this.omasServerURL       = omasServerURL;
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
     * @param omasServerURL  url of server
     * @param connectorInstanceId  unique identifier of connector.
     * @param connection  connection information for connector.
     * @param assetGUID  String   unique id for connected asset.
     */
    public EgeriaConnectedAssetProperties(String               serviceName,
                                          String               remoteServerName,
                                          String               localServerUserId,
                                          String               localServerPassword,
                                          String               userId,
                                          String               omasServerURL,
                                          String               connectorInstanceId,
                                          ConnectionProperties connection,
                                          String               assetGUID)
    {
        super();

        this.serviceName         = serviceName;
        this.remoteServerName    = remoteServerName;
        this.localServerUserId   = localServerUserId;
        this.localServerPassword = localServerPassword;
        this.userId              = userId;
        this.omasServerURL       = omasServerURL;
        this.connectorInstanceId = connectorInstanceId;
        this.connection          = connection;
        this.assetGUID           = assetGUID;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template  template to copy.
     */
    public EgeriaConnectedAssetProperties(EgeriaConnectedAssetProperties template)
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
            this.omasServerURL       = template.omasServerURL;
            this.assetGUID           = template.assetGUID;
        }
    }


    /**
     * Request the values in the EgeriaConnectedAssetProperties are refreshed with the current values from the
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
                assetProperties = new ConnectedAssetUniverse(serviceName,
                                                             remoteServerName,
                                                             localServerUserId,
                                                             localServerPassword,
                                                             omasServerURL,
                                                             userId,
                                                             assetGUID,
                                                             connection.getGUID());
            }
            else
            {
                assetProperties = new ConnectedAssetUniverse(serviceName,
                                                             remoteServerName,
                                                             omasServerURL,
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
            restExceptionHandler.handleUnexpectedException(error, methodName, remoteServerName, omasServerURL);
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
        return "EgeriaConnectedAssetProperties{" +
                "serviceName='" + serviceName + '\'' +
                ", remoteServerName='" + remoteServerName + '\'' +
                ", userId='" + userId + '\'' +
                ", localServerUserId='" + localServerUserId + '\'' +
                ", omasServerURL='" + omasServerURL + '\'' +
                ", connectorInstanceId='" + connectorInstanceId + '\'' +
                ", connection=" + connection +
                ", assetGUID='" + assetGUID + '\'' +
                ", extendedProperties=" + assetProperties +
                '}';
    }
}