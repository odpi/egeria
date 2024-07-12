/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.iterator;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.integration.context.OpenMetadataAccess;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;

import java.util.ArrayList;
import java.util.List;


/**
 * MetadataCollectionIterator is ued to iterate through the elements of a particular type within a metadata collection.
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
     * @param metadataCollectionQualifiedName unique name of the metadata collection
     * @param catalogTargetName name of target
     * @param connectorName name of the calling connector
     * @param parentGUID unique identifier of element that the desired results are linked to
     * @param parentRelationshipTypeName type of relationship - may be null
     * @param parentAtEnd which end of the relationship to start from: 0 (either); 1 or 2
     * @param metadataTypeName type of element to receive
     * @param openMetadataAccess client to access metadata
     * @param targetPermittedSynchronization the synchronization policy for this target
     * @param maxPageSize max page size for the server
     * @param auditLog logging destination
     */
    public RelatedElementsIterator(String                   metadataCollectionGUID,
                                   String                   metadataCollectionQualifiedName,
                                   String                   catalogTargetName,
                                   String                   connectorName,
                                   String                   parentGUID,
                                   String                   parentRelationshipTypeName,
                                   int                      parentAtEnd,
                                   String                   metadataTypeName,
                                   OpenMetadataAccess       openMetadataAccess,
                                   PermittedSynchronization targetPermittedSynchronization,
                                   int                      maxPageSize,
                                   AuditLog                 auditLog)
    {
        super(metadataCollectionGUID,
              metadataCollectionQualifiedName,
              catalogTargetName,
              connectorName,
              metadataTypeName,
              openMetadataAccess,
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
            List<RelatedMetadataElement> relatedMetadataElementList = openMetadataAccess.getRelatedMetadataElements(parentGUID,
                                                                                                                    parentAtEnd,
                                                                                                                    parentRelationshipTypeName,
                                                                                                                    startFrom,
                                                                                                                    maxPageSize);
            elementCache = this.getElementCache(relatedMetadataElementList);

            startFrom = startFrom + maxPageSize;
        }

        return (elementCache != null);
    }


    /**
     * Convert a list of OpenMetadataRelationship into a list of OpenMetadataElements.
     *
     * @param relatedMetadataElementList related elements retrieved from the open metadata
     * @return results
     */
    private List<OpenMetadataElement> getElementCache(List<RelatedMetadataElement> relatedMetadataElementList)
    {
        if (relatedMetadataElementList != null)
        {
            List<OpenMetadataElement> results = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementList)
            {
                if (relatedMetadataElement != null)
                {
                    results.add(relatedMetadataElement.getElement());
                }
            }

            return results;
        }

        return null;
    }
}
