/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.LineageRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


/**
 * LineageHandler is the handler for managing lineage relationships.
 */
public class LineageHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public LineageHandler(String             localServerName,
                          AuditLog           auditLog,
                          String             serviceName,
                          OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, OpenMetadataType.REFERENCEABLE.typeName);
    }


    /**
     * Create a lineage relationship between two elements that indicates that data is flowing from one element to another.
     *
     * @param userId calling user
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
    public String linkLineage(String                        userId,
                              String                        elementOneGUID,
                              String                        elementTwoGUID,
                              String                        relationshipTypeName,
                              MetadataSourceOptions         metadataSourceOptions,
                              LineageRelationshipProperties properties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return openMetadataClient.createRelatedElementsInStore(userId,
                                                               relationshipTypeName,
                                                               elementOneGUID,
                                                               elementTwoGUID,
                                                               metadataSourceOptions,
                                                               relationshipBuilder.getNewElementProperties(properties));
    }


    /**
     * Update the lineage relationship.
     *
     * @param userId calling user
     * @param lineageRelationshipGUID unique identifier for the relationship
     * @param updateOptions options for the request
     * @param properties properties of the relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateLineage(String                        userId,
                              String                        lineageRelationshipGUID,
                              UpdateOptions                 updateOptions,
                              LineageRelationshipProperties properties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        openMetadataClient.updateRelationshipInStore(userId,
                                                     lineageRelationshipGUID,
                                                     updateOptions,
                                                     relationshipBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove a lineage relationship between two elements.
     *
     * @param userId calling user
     * @param lineageRelationshipGUID unique identifier of the lineage relationship
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void detachLineage(String        userId,
                              String        lineageRelationshipGUID,
                              DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        openMetadataClient.deleteRelationshipInStore(userId, lineageRelationshipGUID, deleteOptions);
    }
}
