/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.governanceaction.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.governanceaction.client.DuplicateManagementInterface;
import org.odpi.openmetadata.frameworks.governanceaction.client.GovernanceConfiguration;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenGovernanceClient;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextClientBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.List;
import java.util.Map;

/**
 * Provides the methods to create, query and maintain the open metadata that controls running connectors.
 */
public class DuplicateManagementClient extends ConnectorContextClientBase
{
    private final DuplicateManagementInterface duplicateManagementClient;


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
     * @param duplicateManagementClient client to access open metadata
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public DuplicateManagementClient(ConnectorContextBase         parentContext,
                                     String                       localServerName,
                                     String                       localServiceName,
                                     String                       connectorUserId,
                                     String                       connectorGUID,
                                     String                       externalSourceGUID,
                                     String                       externalSourceName,
                                     OpenGovernanceClient         duplicateManagementClient,
                                     AuditLog                     auditLog,
                                     int                          maxPageSize)
    {
        super(parentContext,
              localServerName,
              localServiceName,
              connectorUserId,
              connectorGUID,
              externalSourceGUID,
              externalSourceName,
              auditLog,
              maxPageSize);

        this.duplicateManagementClient = duplicateManagementClient;
    }

    /**
     * Link elements as peer duplicates. Create a simple relationship between two elements.
     * If the relationship already exists, the properties are updated.
     *
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param statusIdentifier what is the status of this relationship (negative means untrusted, 0 means unverified and positive means trusted)
     * @param steward identifier of the steward
     * @param stewardTypeName type of element used to identify the steward
     * @param stewardPropertyName property name used to identify steward
     * @param source source of the duplicate detection processing
     * @param notes notes for the steward
     * @param setKnownDuplicate boolean flag indicating whether the KnownDuplicate classification should be set on the linked entities.
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void linkElementsAsPeerDuplicates(String  metadataElement1GUID,
                                             String  metadataElement2GUID,
                                             int     statusIdentifier,
                                             String  steward,
                                             String  stewardTypeName,
                                             String  stewardPropertyName,
                                             String  source,
                                             String  notes,
                                             boolean setKnownDuplicate) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        duplicateManagementClient.linkElementsAsPeerDuplicates(connectorUserId,
                                                               metadataElement1GUID,
                                                               metadataElement2GUID,
                                                               statusIdentifier,
                                                               steward,
                                                               stewardTypeName,
                                                               stewardPropertyName,
                                                               source,
                                                               notes,
                                                               setKnownDuplicate);
    }


    /**
     * Identify an element that acts as a consolidated version for a set of duplicate elements.
     * (The consolidated element is created using createMetadataElement.)
     *
     * @param consolidatedElementGUID unique identifier of the metadata element
     * @param statusIdentifier what is the status of this relationship (negative means untrusted, 0 means unverified and positive means trusted)
     * @param steward identifier of the steward
     * @param stewardTypeName type of element used to identify the steward
     * @param stewardPropertyName property name used to identify steward
     * @param source source of the duplicate detection processing
     * @param notes notes for the steward
     * @param sourceElementGUIDs List of the source elements that must be linked to the consolidated element.  It is assumed that they already
     *                           have the KnownDuplicateClassification.
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void linkConsolidatedDuplicate(String       consolidatedElementGUID,
                                          int          statusIdentifier,
                                          String       steward,
                                          String       stewardTypeName,
                                          String       stewardPropertyName,
                                          String       source,
                                          String       notes,
                                          List<String> sourceElementGUIDs) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        duplicateManagementClient.linkConsolidatedDuplicate(connectorUserId,
                                                            consolidatedElementGUID,
                                                            statusIdentifier,
                                                            steward,
                                                            stewardTypeName,
                                                            stewardPropertyName,
                                                            source,
                                                            notes,
                                                            sourceElementGUIDs);
    }
}
