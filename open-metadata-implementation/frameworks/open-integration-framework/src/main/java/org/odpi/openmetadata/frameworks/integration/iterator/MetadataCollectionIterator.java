/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.iterator;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationManagerClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.Collections;


/**
 * MetadataCollectionIterator is used to iterate through the elements of a particular type within a metadata collection.
 */
public class MetadataCollectionIterator extends IntegrationIterator
{
    /**
     * Create the iterator.
     *
     * @param metadataCollectionGUID unique identifier of the metadata collection
     * @param catalogTargetName name of target
     * @param connectorName name of the calling connector
     * @param metadataTypeName type of element to receive
     * @param integrationContext clients to access metadata
     * @param targetPermittedSynchronization the synchronization policy for this target
     * @param maxPageSize max page size for the server
     * @param auditLog logging destination
     * @throws UserNotAuthorizedException connector has disconnected
     */
    public MetadataCollectionIterator(String                   metadataCollectionGUID,
                                      String                   catalogTargetName,
                                      String                   connectorName,
                                      String                   metadataTypeName,
                                      IntegrationContext       integrationContext,
                                      PermittedSynchronization targetPermittedSynchronization,
                                      int                      maxPageSize,
                                      AuditLog                 auditLog) throws UserNotAuthorizedException
    {
        super(metadataCollectionGUID,
              catalogTargetName,
              connectorName,
              metadataTypeName,
              integrationContext,
              targetPermittedSynchronization,
              maxPageSize,
              auditLog);
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
        final String methodName = "fillCache";

        if ((elementCache == null) || (elementCache.isEmpty()))
        {
            ClassificationManagerClient classificationManagerClient = integrationContext.getClassificationManagerClient(metadataTypeName);

            elementCache = classificationManagerClient.getRootElementsByName(metadataCollectionGUID,
                                                                             Collections.singletonList(OpenMetadataProperty.METADATA_COLLECTION_ID.name),
                                                                             classificationManagerClient.getQueryOptions(startFrom, maxPageSize),
                                                                             methodName);
            startFrom = startFrom + maxPageSize;
        }

        return (elementCache != null);
    }
}
