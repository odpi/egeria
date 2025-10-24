/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.iterator;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;


/**
 * MetadataCollectionIterator is used to iterate through the elements of a particular type within a metadata collection.
 */
public class MetadataCollectionIterator extends IntegrationIterator
{

    /**
     * Create the iterator.
     *
     * @param metadataCollectionGUID unique identifier of the metadata collection
     * @param metadataCollectionQualifiedName unique name of the metadata collection
     * @param externalScopeGUID unique identifier for the owning scope (typically a catalog)
     * @param externalScopeName unique name for the owning scope (typically a catalog)
     * @param catalogTargetName name of target
     * @param connectorName name of the calling connector
     * @param metadataTypeName type of element to receive
     * @param openMetadataStore client to access metadata
     * @param targetPermittedSynchronization the synchronization policy for this target
     * @param maxPageSize max page size for the server
     * @param auditLog logging destination
     */
    public MetadataCollectionIterator(String                   metadataCollectionGUID,
                                      String                   metadataCollectionQualifiedName,
                                      String                   externalScopeGUID,
                                      String                   externalScopeName,
                                      String                   catalogTargetName,
                                      String                   connectorName,
                                      String                   metadataTypeName,
                                      OpenMetadataStore        openMetadataStore,
                                      PermittedSynchronization targetPermittedSynchronization,
                                      int                      maxPageSize,
                                      AuditLog                 auditLog)
    {
        super(metadataCollectionGUID,
              metadataCollectionQualifiedName,
              externalScopeGUID,
              externalScopeName,
              catalogTargetName,
              connectorName,
              metadataTypeName,
              openMetadataStore,
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
        if ((elementCache == null) || (elementCache.isEmpty()))
        {
            elementCache = openMetadataStore.findMetadataElements(propertyHelper.getSearchPropertiesForMetadataCollectionName(metadataCollectionQualifiedName),
                                                                  null,
                                                                  openMetadataStore.getQueryOptions(metadataTypeName, null, startFrom, maxPageSize));
            startFrom = startFrom + maxPageSize;
        }

        return (elementCache != null);
    }
}
