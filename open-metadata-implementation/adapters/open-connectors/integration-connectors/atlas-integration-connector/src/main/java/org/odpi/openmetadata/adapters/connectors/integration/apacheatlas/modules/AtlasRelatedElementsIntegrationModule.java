/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.modules;


import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ValidValueElement;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.ApacheAtlasIntegrationConnector;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.ApacheAtlasIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.ffdc.ApacheAtlasAuditCode;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.ApacheAtlasRESTConnector;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasClassification;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasEntityHeader;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasEntityWithExtInfo;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasInstanceStatus;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * AtlasRelatedElementsIntegrationModule synchronizes related entities and classifications attached to Apache Atlas originating entities within
 * the open metadata ecosystem.  It is called after the registered integration modules have established the key assets into the open metadata ecosystem.
 */
public class AtlasRelatedElementsIntegrationModule extends AtlasIntegrationModuleBase
{
    /**
     * Unique name of this module for messages.
     */
    private static final String relatedElementsModuleName = "Apache Atlas Related Elements Integration Module";
    private static final String referenceValueAssignmentRelationshipName = "ReferenceValueAssignment";
    private static final String displayNamePropertyName    = "displayName";
    private static final String attributeNamePropertyName  = "attributeName";
    private static final String attributeNamePropertyValue = "apacheAtlasClassification";

    private final String         informalTagsMappingPolicy;
    private final String         relatedClassificationPolicy;
    private final List<String>   ignoreClassificationList;
    private final String         relatedEntityPolicy;
    private final List<String>   ignoreRelationshipList;
    private final PropertyHelper propertyHelper = new PropertyHelper();

    /**
     * Constructor for the module is supplied with the runtime context in order to operate.
     *
     * @param connectorName name of the connector (for messages)
     * @param connectionProperties connection properties used to start the connector
     * @param auditLog logging destination
     * @param myContext integration context
     * @param targetRootURL URL to connect to Apache Atlas
     * @param atlasClient client to connect to Apache Atlas
     * @param embeddedConnectors list of any embedded connectors (such as secrets connector and topic connector
     * @param informalTagsMappingPolicy determines what type of mapping to perform for informal tags
     * @param relatedClassificationPolicy determines how classifications should be processed.  Can be "ALL", "SELECTED" or "NONE".
     * @param ignoreClassificationList list of classifications to ignore if relatedClassificationPolicy==SELECTED
     * @param relatedEntityPolicy determines how related entities should be processed.  Can be "ALL", "SELECTED" or "NONE".
     * @param ignoreRelationshipList ist of relationships to ignore if relatedEntityPolicy==SELECTED
     * @throws UserNotAuthorizedException security problem
     */
    public AtlasRelatedElementsIntegrationModule(String                   connectorName,
                                                 ConnectionProperties     connectionProperties,
                                                 AuditLog                 auditLog,
                                                 CatalogIntegratorContext myContext,
                                                 String                   targetRootURL,
                                                 ApacheAtlasRESTConnector atlasClient,
                                                 List<Connector>          embeddedConnectors,
                                                 String                   informalTagsMappingPolicy,
                                                 String                   relatedClassificationPolicy,
                                                 List<String>             ignoreClassificationList,
                                                 String                   relatedEntityPolicy,
                                                 List<String>             ignoreRelationshipList) throws UserNotAuthorizedException
    {
        super(connectorName,
              relatedElementsModuleName,
              connectionProperties,
              auditLog,
              myContext,
              targetRootURL,
              atlasClient,
              embeddedConnectors);

        this.informalTagsMappingPolicy = informalTagsMappingPolicy;
        this.relatedClassificationPolicy = relatedClassificationPolicy;

        if (ignoreClassificationList != null)
        {
            this.ignoreClassificationList = ignoreClassificationList;
        }
        else
        {
            this.ignoreClassificationList = new ArrayList<>();
        }

        this.relatedEntityPolicy = relatedEntityPolicy;

        if (ignoreRelationshipList != null)
        {
            this.ignoreRelationshipList = ignoreRelationshipList;
        }
        else
        {
            this.ignoreRelationshipList = new ArrayList<>();
        }
    }


    /**
     * Handle additional related classifications and relationships/entities.
     *
     * @param referenceClassifications details of the reference classification.  This map is empty is the reference classification set is not defined.
     */
    public void synchronizeRelatedElements(Map<String, ValidValueElement> referenceClassifications)
    {
        final String methodName = "synchronizeRelatedElements";

        /*
         * Loop through all the OpenMetadataCorrelation entities in Apache Atlas and for each
         * atlas owned entity
         * - Retrieve the associated Atlas entity and open metadata entity.
         * - Based on the setting of the "related classifications" policy - ie not NONE.
         *      - For each classification on the Atlas entity (and not on ignore list)
         *           - Look up classification in classification reference set and if found, ensure
         *             there is a ReferenceValueAssignment relationship from open metadata entity to appropriate member in reference classification set.
         * - Based on the setting of the related entity policy or informalTagsMapping - ie not NONE.
         *      - Retrieve the related elements for the open metadata entity.
         *      - For each related entity for the open metadata entity (and not on ignore list)
         *           - Check that the Atlas entity has the same relationships
         *               - special processing for informal tags (if policy is LABELS, CLASSIFICATIONS or NONE)
         *               - processing for other relationships
         *                    - if the relationship is open metadata owned
         *                        - if the entity or relationship type not defined in Apache Atlas then add
         *                        - if the entity is not defined in Apache Atlas then add
         *                        - Add relationship
         *                            - Note: special processing for semantic assignments as using native relationship
         *                    - if the relationship is Atlas owned
         *                        - Check that the relationship is on Atlas entity
         *                            - Note: special check for semantic assignments as using native relationship
         *                            - If not, delete it
         *     - Check the Atlas entity does not have too many relationships (ie deleted from open metadata)
         *          - add semantic assignments. They need to be deleted on both sides.
         */
        try
        {
            final int              maxPageSize = 100;
            int                    startFrom = 0;

            List<AtlasEntityHeader> correlationEntities = atlasClient.getEntitiesForType(ApacheAtlasIntegrationConnector.OPEN_METADATA_CORRELATION_TYPE_NAME, startFrom, maxPageSize);

            while (correlationEntities != null)
            {
                for (AtlasEntityHeader correlationEntity : correlationEntities)
                {
                    /*
                     * Atlas returns entities in deleted status and they should be ignored.
                     */
                    if ((correlationEntity != null) && (correlationEntity.getStatus() == AtlasInstanceStatus.ACTIVE))
                    {
                        boolean egeriaOwned = super.getAtlasBooleanProperty(correlationEntity.getAttributes(), egeriaOwnedPropertyName);

                        /*
                         * Only entities that originated in Apache Atlas are processed.
                         */
                        if (! egeriaOwned)
                        {
                            String egeriaGUID = super.getAtlasStringProperty(correlationEntity.getAttributes(), egeriaGUIDPropertyName);

                            AtlasEntityWithExtInfo atlasEntity = atlasClient.getRelatedEntity(correlationEntity,
                                                                                              openMetadataAssociatedElementPropertyName);

                            if ((atlasEntity != null) &&
                                        (atlasEntity.getEntity() != null) &&
                                        (atlasEntity.getEntity().getStatus() == AtlasInstanceStatus.ACTIVE))
                            {
                                /*
                                 * The correlation entity is related to a valid atlas entity.  Retrieve the equivalent entity from the open metadata
                                 * ecosystem.
                                 */
                                OpenMetadataElement egeriaEntity = openMetadataAccess.getMetadataElementByGUID(egeriaGUID,
                                                                                                               false,
                                                                                                               false,
                                                                                                               null);

                                if (! ApacheAtlasIntegrationProvider.NONE_RELATED_ELEMENT_CONFIGURATION_PROPERTY_VALUE.equals(relatedClassificationPolicy))
                                {
                                    /*
                                     * The policy for classification allows that some/all the classification are linked to the
                                     * classification reference set (if defined).
                                     */
                                    processRelatedClassifications(atlasEntity,
                                                                  egeriaEntity,
                                                                  referenceClassifications);
                                }

                                if ((! ApacheAtlasIntegrationProvider.NONE_RELATED_ELEMENT_CONFIGURATION_PROPERTY_VALUE.equals(relatedEntityPolicy)) ||
                                    (! ApacheAtlasIntegrationProvider.INFORMAL_TAGS_NO_MAPPING_CONFIGURATION_PROPERTY_VALUE.equals(informalTagsMappingPolicy)))
                                {
                                    /*
                                     * The policy for related entities allows that some/all the relationship from the
                                     * open metadata entity can be copied to Apache Atlas.
                                     */
                                    List<RelatedMetadataElement> relatedMetadataElements = this.getAllRelatedMetadataElements(egeriaGUID);

                                    processRelatedEntities(atlasEntity,
                                                           egeriaEntity,
                                                           relatedMetadataElements);
                                }
                            }
                        }
                    }
                }

                startFrom = startFrom + maxPageSize;
                correlationEntities = atlasClient.getEntitiesForType(ApacheAtlasIntegrationConnector.OPEN_METADATA_CORRELATION_TYPE_NAME,
                                                                     startFrom,
                                                                     maxPageSize);
            }
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      ApacheAtlasAuditCode.UNABLE_TO_PROCESS_RELATED_ELEMENTS.getMessageDefinition(connectorName,
                                                                                                                   error.getClass().getName(),
                                                                                                                   error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Map the classifications from Atlas to ReferenceValueAssignment relationships in the open metadata ecosystem.
     *
     * @param atlasEntity entity from Apache Atlas
     * @param egeriaEntity entity from the open metadata ecosystem
     * @param referenceClassifications list of known reference classifications from the reference classification set
     */
    private void processRelatedClassifications(AtlasEntityWithExtInfo         atlasEntity,
                                               OpenMetadataElement            egeriaEntity,
                                               Map<String, ValidValueElement> referenceClassifications)
    {
        final String methodName = "processRelatedClassifications";

        if ((atlasEntity != null) && (atlasEntity.getEntity() != null) && (egeriaEntity != null) && (atlasEntity.getEntity().getClassifications() != null))
        {
            try
            {
                /*
                 * Create a map of the existing ReferenceValueAssignment relationships for this entity (map is classification->guid).
                 */
                Map<String, RelatedMetadataElement> existingReferenceValueAssignments = this.getReferenceValueAssignments(egeriaEntity.getElementGUID());

                /*
                 * This set is built up in the first loop and is used to detect existing ReferenceValueAssignment relationships that no longer
                 * match an existing Atlas classification
                 */
                Set<String> atlasClassificationNames = new HashSet<>();

                /*
                 * Step through the current list of Apache Atlas classifications and add ReferenceValueAssignments for each missing one.
                 */
                for (AtlasClassification atlasClassification : atlasEntity.getEntity().getClassifications())
                {
                    if (atlasClassification != null)
                    {
                        atlasClassificationNames.add(atlasClassification.getTypeName());

                        if (! ignoreClassificationList.contains(atlasClassification.getTypeName()))
                        {
                            /*
                             * The classification is not on the ignore list.
                             */
                            if (existingReferenceValueAssignments.get(atlasClassification.getTypeName()) == null)
                            {
                                /*
                                 * There is no reference value assignment relations fort this classification.
                                 */
                                ValidValueElement referenceValue = referenceClassifications.get(atlasClassification.getTypeName());

                                if (referenceValue != null)
                                {
                                    openMetadataAccess.createRelatedElementsInStore(referenceValueAssignmentRelationshipName,
                                                                                    egeriaEntity.getElementGUID(),
                                                                                    referenceValue.getElementHeader().getGUID(),
                                                                                    false,
                                                                                    false,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    new Date());

                                    if (auditLog != null)
                                    {
                                        auditLog.logMessage(methodName,
                                                            ApacheAtlasAuditCode.ASSIGNING_REFERENCE_VALUE.getMessageDefinition(connectorName,
                                                                                                                                egeriaEntity.getType().getTypeName(),
                                                                                                                                egeriaEntity.getElementGUID(),
                                                                                                                                referenceValue.getElementHeader().getGUID(),
                                                                                                                                atlasClassification.getTypeName(),
                                                                                                                                atlasEntity.getEntity().getGuid()));
                                    }
                                }
                            }
                        }
                    }
                }

                for (String classificationName : existingReferenceValueAssignments.keySet())
                {
                    if (! atlasClassificationNames.contains(classificationName))
                    {
                        RelatedMetadataElement relatedMetadataElement = existingReferenceValueAssignments.get(classificationName);

                        openMetadataAccess.deleteRelatedElementsInStore(relatedMetadataElement.getRelationshipGUID(), false, false, new Date());
                    }
                }
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          ApacheAtlasAuditCode.UNABLE_TO_SET_REFERENCE_VALUE_ASSIGNMENTS.getMessageDefinition(connectorName,
                                                                                                                              error.getClass().getName(),
                                                                                                                              egeriaEntity.getElementGUID(),
                                                                                                                              atlasEntity.getEntity().getGuid(),
                                                                                                                              error.getMessage()),
                                          error);
                }
            }
        }
    }


    /**
     * Return all the linked valid value definitions connected to the supplied entity via the ReferenceValueAssignment relationship.
     *
     * @param egeriaGUID unique identifier of the open metadata entity that is the starting point of the query
     * @return map of classification name to assigned related element
     */
    private Map<String, RelatedMetadataElement> getReferenceValueAssignments(String egeriaGUID)
    {
        final String methodName = "getReferenceValueAssignments";

        if (egeriaGUID != null)
        {
            Map<String, RelatedMetadataElement> results = new HashMap<>();

            try
            {
                int startFrom = 0;
                List<RelatedMetadataElement> relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(egeriaGUID,
                                                                                                                     1,
                                                                                                                     referenceValueAssignmentRelationshipName,
                                                                                                                     false,
                                                                                                                     false,
                                                                                                                     new Date(),
                                                                                                                     startFrom,
                                                                                                                     myContext.getMaxPageSize());
                while (relatedMetadataElements != null)
                {
                    for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements)
                    {
                        String attributeName = propertyHelper.getStringProperty(connectorName,
                                                                                attributeNamePropertyName,
                                                                                relatedMetadataElement.getRelationshipProperties(),
                                                                                methodName);

                        if (attributeNamePropertyValue.equals(attributeName))
                        {
                            String classificationName = propertyHelper.getStringProperty(connectorName,
                                                                                         displayNamePropertyName,
                                                                                         relatedMetadataElement.getElement().getElementProperties(),
                                                                                         methodName);

                            if (classificationName != null)
                            {
                                results.put(classificationName, relatedMetadataElement);
                            }
                        }
                    }

                    startFrom = startFrom + myContext.getMaxPageSize();
                    relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(egeriaGUID,
                                                                                            1,
                                                                                            referenceValueAssignmentRelationshipName,
                                                                                            false,
                                                                                            false,
                                                                                            new Date(),
                                                                                            startFrom,
                                                                                            myContext.getMaxPageSize());
                }
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          ApacheAtlasAuditCode.UNABLE_TO_GET_REFERENCE_VALUE_ASSIGNMENTS.getMessageDefinition(connectorName,
                                                                                                                              error.getClass().getName(),
                                                                                                                              egeriaGUID,
                                                                                                                              error.getMessage()),
                                          error);
                }
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Return all the elements relating to the starting entity.
     *
     * @param egeriaGUID starting entity
     * @return list of all elements that are connected to the starting entity
     */
    private List<RelatedMetadataElement> getAllRelatedMetadataElements(String egeriaGUID)
    {
        final String methodName = "getAllRelatedMetadataElements";

        if (egeriaGUID != null)
        {
            List<RelatedMetadataElement> results = new ArrayList<>();

            try
            {
                int startFrom = 0;
                List<RelatedMetadataElement> relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(egeriaGUID,
                                                                                                                     1,
                                                                                                                     null,
                                                                                                                     false,
                                                                                                                     false,
                                                                                                                     new Date(),
                                                                                                                     startFrom,
                                                                                                                     myContext.getMaxPageSize());
                while (relatedMetadataElements != null)
                {
                    results.addAll(relatedMetadataElements);

                    startFrom = startFrom + myContext.getMaxPageSize();
                    relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(egeriaGUID,
                                                                                            1,
                                                                                            referenceValueAssignmentRelationshipName,
                                                                                            false,
                                                                                            false,
                                                                                            new Date(),
                                                                                            startFrom,
                                                                                            myContext.getMaxPageSize());
                }
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          ApacheAtlasAuditCode.UNABLE_TO_GET_RELATED_ELEMENTS.getMessageDefinition(connectorName,
                                                                                                                   error.getClass().getName(),
                                                                                                                   egeriaGUID,
                                                                                                                   error.getMessage()),
                                          error);
                }
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
    }


    /**
     * Add representations of the relationships from an Apache Atlas originated entity to other entities in the open metadata ecosystem.
     *
     * @param atlasEntity the original Atlas entity with details of its relationships
     * @param egeriaEntity the equivalent entity in the open metadata ecosystem
     * @param relatedMetadataElements the list of entities connected to the open metadata
     */
    private void processRelatedEntities(AtlasEntityWithExtInfo       atlasEntity,
                                        OpenMetadataElement          egeriaEntity,
                                        List<RelatedMetadataElement> relatedMetadataElements)
    {
        if ((relatedMetadataElements != null) || (! relatedMetadataElements.isEmpty()))
        {
            // todo
        }
    }
}