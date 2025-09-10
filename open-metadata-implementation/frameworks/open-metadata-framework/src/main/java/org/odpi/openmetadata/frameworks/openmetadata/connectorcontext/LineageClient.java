/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.LineageHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.LineageRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;

/**
 * Provides services for connectors to work with lineage relationships.
 */
public class LineageClient extends ConnectorContextClientBase
{
    private final LineageHandler lineageHandler;


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
    public LineageClient(ConnectorContextBase     parentContext,
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

        this.lineageHandler = new LineageHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a lineage relationship between two elements that indicates that data is flowing from one element to another.
     *
     * @param elementOneGUID unique identifier of the element at end one
     * @param elementTwoGUID unique identifier of the element at end two
     * @param relationshipTypeName lineage relationship name
     * @param properties   additional information, endorsements etc
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @return guid of lineage relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String linkLineage(String                        elementOneGUID,
                              String                        elementTwoGUID,
                              String                        relationshipTypeName,
                              MetadataSourceOptions         metadataSourceOptions,
                              LineageRelationshipProperties properties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return lineageHandler.linkLineage(connectorUserId,
                                          elementOneGUID,
                                          elementTwoGUID,
                                          relationshipTypeName,
                                          metadataSourceOptions,
                                          properties);
    }


    /**
     * Update the lineage relationship.
     *
     * @param lineageRelationshipGUID unique identifier for the relationship
     * @param updateOptions options for the request
     * @param properties properties of the relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateLineage(String                        lineageRelationshipGUID,
                              UpdateOptions                 updateOptions,
                              LineageRelationshipProperties properties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        lineageHandler.updateLineage(connectorUserId,
                                     lineageRelationshipGUID,
                                     updateOptions,
                                     properties);
    }


    /**
     * Remove a lineage relationship between two elements.
     *
     * @param lineageRelationshipGUID unique identifier of the lineage relationship
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void detachLineage(String        lineageRelationshipGUID,
                              DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        lineageHandler.detachLineage(connectorUserId, lineageRelationshipGUID, deleteOptions);
    }
}
