/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.LikeHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.LikeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with like elements.
 */
public class LikeClient extends ConnectorContextClientBase
{
    private final LikeHandler likeHandler;


    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param connectorGUID the unique identifier that represents this connector in open metadata
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openMetadataClient client to access the open metadata store
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public LikeClient(ConnectorContextBase     parentContext,
                      String                   localServerName,
                      String                   localServiceName,
                      String                   connectorUserId,
                      String                   connectorGUID,
                      String                   externalSourceGUID,
                      String                   externalSourceName,
                      OpenMetadataClient       openMetadataClient,
                      AuditLog                 auditLog,
                      int                      maxPageSize)
    {
        super(parentContext, localServerName, localServiceName, connectorUserId, connectorGUID, externalSourceGUID, externalSourceName, auditLog, maxPageSize);

        this.likeHandler = new LikeHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Adds a "Like" to the element.
     *
     * @param elementGUID   unique identifier for the element
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addLikeToElement(String                            elementGUID,
                                 UpdateOptions                     updateOptions,
                                 LikeProperties                    properties) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        String likeGUID = likeHandler.addLikeToElement(connectorUserId, elementGUID, updateOptions, properties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(likeGUID);
        }
    }


    /**
     * Removes a "Like" added to the element by this user.
     *
     * @param elementGUID unique identifier for the like object.
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeLikeFromElement(String                elementGUID,
                                      MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        String likeGUID = likeHandler.removeLikeFromElement(connectorUserId, elementGUID, metadataSourceOptions);

        if ((parentContext.getIntegrationReportWriter() != null) && (likeGUID != null))
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(likeGUID);
        }
    }


    /**
     * Return the likes attached to an element.
     *
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param queryOptions multiple options to control the query
     *
     * @return list of likes
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public List<OpenMetadataRootElement>  getAttachedLikes(String       elementGUID,
                                                           QueryOptions queryOptions) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        return likeHandler.getAttachedLikes(connectorUserId, elementGUID, queryOptions);
    }
}
