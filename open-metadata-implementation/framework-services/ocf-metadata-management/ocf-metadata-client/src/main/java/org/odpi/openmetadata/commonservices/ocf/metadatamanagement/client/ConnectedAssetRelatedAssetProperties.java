/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client;

import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.RelatedAsset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * RelatedAssetProperties is associated a related asset for a connector's EgeriaConnectedAssetProperties.  Connectors provide access to
 * assets.   EgeriaConnectedAssetProperties returns properties (metadata) about the connector's asset.  RelatedAssetProperties
 * returns similar information for an asset related to the connected asset
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
 * RelatedAssetProperties is a base class for the connector's metadata API that returns null,
 * for the asset's properties.  Metadata repository implementations extend this class to add their
 * implementation of the refresh() method that calls to the metadata repository to populate the metadata properties.
 */
public class ConnectedAssetRelatedAssetProperties extends org.odpi.openmetadata.frameworks.connectors.properties.RelatedAssetProperties
{
    private static final long    serialVersionUID = 1L;

    private String        serviceName;
    private String        serverName;
    private String        userId = null;
    private String        omasServerURL = null;
    private String        assetGUID = null;
    private OCFRESTClient restClient;

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final Logger log = LoggerFactory.getLogger(ConnectedAssetRelatedAssetProperties.class);


    /**
     * Typical constructor.
     *
     * @param bean content for the superclass
     * @param serviceName calling service
     * @param serverName  name of the server.
     * @param userId  identifier of calling user
     * @param omasServerURL  url of server
     * @param assetGUID  String   unique id for connected asset.
     * @param restClient client to call REST API
     */
    public ConnectedAssetRelatedAssetProperties(RelatedAsset         bean,
                                                String               serviceName,
                                                String               serverName,
                                                String               userId,
                                                String               omasServerURL,
                                                String               assetGUID,
                                                OCFRESTClient        restClient)
    {
        super(bean);

        this.serviceName   = serviceName;
        this.serverName    = serverName;
        this.userId        = userId;
        this.omasServerURL = omasServerURL;
        this.assetGUID     = assetGUID;
        this.restClient    = restClient;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template  template to copy.
     */
    public ConnectedAssetRelatedAssetProperties(ConnectedAssetRelatedAssetProperties template)
    {
        super(template);

        if (template != null)
        {
            this.serviceName   = template.serviceName;
            this.serverName    = template.serverName;
            this.userId        = template.userId;
            this.omasServerURL = template.omasServerURL;
            this.assetGUID     = template.assetGUID;
            this.restClient    = template.restClient;
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
        final  String  serviceName = "ConnectedAssetRelatedAssetProperties";

        log.debug("Calling method: " + methodName);

        try
        {
            assetProperties = ConnectedAssetUniverse.create(serviceName,
                                                            serverName,
                                                            omasServerURL,
                                                            userId,
                                                            assetGUID,
                                                            restClient);
        }
        catch (UserNotAuthorizedException  error)
        {
            throw error;
        }
        catch (Exception  error)
        {
            restExceptionHandler.handleUnexpectedException(error, methodName, serverName, omasServerURL);
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
        return "ConnectedAssetRelatedAssetProperties{" +
                "userId='" + userId + '\'' +
                ", omasServerURL='" + omasServerURL + '\'' +
                ", assetGUID='" + assetGUID + '\'' +
                ", extendedProperties=" + assetProperties +
                '}';
    }
}