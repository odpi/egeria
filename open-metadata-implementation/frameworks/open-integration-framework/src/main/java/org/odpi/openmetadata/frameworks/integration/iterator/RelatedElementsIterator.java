/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.iterator;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationManagerClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;


/**
 * RelatedElementsIterator is used to iterate through the related elements of a particular type within a metadata collection.
 */
public class RelatedElementsIterator extends IntegrationIterator
{
    private final String parentGUID;
    private final String parentRelationshipTypeName;
    private final int    parentAtEnd;

    /**
     * Create the iterator.
     *
     * @param metadataCollectionGUID unique identifier of the metadata collection
     * @param catalogTargetName name of target
     * @param connectorName name of the calling connector
     * @param parentGUID unique identifier of element that the desired results are linked to
     * @param parentRelationshipTypeName type of relationship - may be null
     * @param parentAtEnd which end of the relationship to start from: 0 (either); 1 or 2
     * @param attachmentEntityTypeName type of element to receive
     * @param integrationContext clients to access metadata
     * @param targetPermittedSynchronization the synchronization policy for this target
     * @param maxPageSize max page size for the server
     * @param auditLog logging destination
     * @throws UserNotAuthorizedException connector has disconnected
     */
    public RelatedElementsIterator(String                   metadataCollectionGUID,
                                   String                   catalogTargetName,
                                   String                   connectorName,
                                   String                   parentGUID,
                                   String                   parentRelationshipTypeName,
                                   int                      parentAtEnd,
                                   String                   attachmentEntityTypeName,
                                   IntegrationContext       integrationContext,
                                   PermittedSynchronization targetPermittedSynchronization,
                                   int                      maxPageSize,
                                   AuditLog                 auditLog) throws UserNotAuthorizedException
    {
        super(metadataCollectionGUID,
              catalogTargetName,
              connectorName,
              attachmentEntityTypeName,
              integrationContext,
              targetPermittedSynchronization,
              maxPageSize,
              auditLog);

        this.parentGUID = parentGUID;
        this.parentRelationshipTypeName = parentRelationshipTypeName;
        this.parentAtEnd = parentAtEnd;
    }


    /**
     * Retrieve information from the open metadata repositories.
     *
     * @return boolean where true means there are elements to process.
     *
     * @throws InvalidParameterException problem with a parameter value
     * @throws PropertyServerException repository not working properly
     * @throws UserNotAuthorizedException permissions problem
     */
    protected boolean fillCache() throws InvalidParameterException,
                                         PropertyServerException,
                                         UserNotAuthorizedException
    {
        if ((elementCache == null) || (elementCache.isEmpty()))
        {
            ClassificationManagerClient classificationManagerClient = integrationContext.getClassificationManagerClient(metadataTypeName);

            elementCache = classificationManagerClient.getRelatedRootElements(parentGUID,
                                                                              parentAtEnd,
                                                                              parentRelationshipTypeName,
                                                                              classificationManagerClient.getQueryOptions(startFrom, maxPageSize));

            startFrom = startFrom + maxPageSize;
        }

        return (elementCache != null);
    }
}
