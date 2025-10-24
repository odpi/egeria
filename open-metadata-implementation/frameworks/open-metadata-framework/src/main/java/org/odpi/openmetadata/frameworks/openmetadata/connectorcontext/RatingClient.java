/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.RatingHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.RatingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;

import java.util.List;

/**
 * Provides services for connectors to work with rating elements.
 */
public class RatingClient extends ConnectorContextClientBase
{
    private final RatingHandler ratingHandler;


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
    public RatingClient(ConnectorContextBase     parentContext,
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

        this.ratingHandler = new RatingHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Adds a "Rating" to the element.
     *
     * @param elementGUID   unique identifier for the element
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addRatingToElement(String                            elementGUID,
                                   UpdateOptions                     updateOptions,
                                   RatingProperties                    properties) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        String ratingGUID = ratingHandler.addRatingToElement(connectorUserId, elementGUID, updateOptions, properties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(ratingGUID);
        }
    }


    /**
     * Removes a "Rating" added to the element by this user.
     *
     * @param elementGUID unique identifier for the rating object.
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeRatingFromElement(String                elementGUID,
                                        MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        String ratingGUID = ratingHandler.removeRatingFromElement(connectorUserId, elementGUID, metadataSourceOptions);

        if ((parentContext.getIntegrationReportWriter() != null) && (ratingGUID != null))
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(ratingGUID);
        }
    }


    /**
     * Return the ratings attached to an element.
     *
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param queryOptions multiple options to control the query
     *
     * @return list of ratings
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public List<OpenMetadataRootElement>  getAttachedRatings(String       elementGUID,
                                                             QueryOptions queryOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        return ratingHandler.getAttachedRatings(connectorUserId, elementGUID, queryOptions);
    }
}
