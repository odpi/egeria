/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.publisher;

import org.odpi.openmetadata.accessservices.governanceengine.server.processor.GovernanceEngineEventProcessor;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventProcessor;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.GLOSSARY_TERM;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.RELATIONAL_COLUMN;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.RELATIONAL_TABLE;
import static org.odpi.openmetadata.accessservices.governanceengine.server.util.Constants.SEMANTIC_ASSIGNMENT;

/**
 * GovernanceEnginePublisher is responsible for publishing events about governed asset components .  It is called
 * when an interesting OMRS Event is added to the Enterprise OMRS Topic.
 */
public class GovernanceEnginePublisher extends OMRSInstanceEventProcessor {

    private static final Logger log = LoggerFactory.getLogger(GovernanceEnginePublisher.class);
    private GovernanceEngineEventProcessor governanceEngineEventProcessor;

    public GovernanceEnginePublisher(GovernanceEngineEventProcessor governanceEngineEventProcessor) {
        this.governanceEngineEventProcessor = governanceEngineEventProcessor;
    }

    @Override
    public void sendInstanceEvent(String sourceName, OMRSInstanceEvent instanceEvent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processNewEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processUpdatedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail oldEntity, EntityDetail newEntity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processUndoneEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processClassifiedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail entity) {
        log.info("GE Process Classified Entity");

        if (validateEntityType(entity)) return;

        if (validateClassifications(entity)) return;

        try {
            governanceEngineEventProcessor.processClassifiedEntity(entity);
        } catch (RepositoryErrorException | EntityProxyOnlyException | InvalidParameterException | UserNotAuthorizedException | PagingErrorException | TypeDefNotKnownException | EntityNotKnownException | PropertyErrorException | FunctionNotSupportedException | TypeErrorException e) {
            log.debug("Governance Engine OMAS is unable to process a Classified Entity event");
        }
    }

    @Override
    public void processDeclassifiedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processReclassifiedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail entity) {
        log.info("GE Process Re-Classified Entity");

        if (validateEntityType(entity)) return;

        if (validateClassifications(entity)) return;

        try {
            governanceEngineEventProcessor.processReclassifiedEntity(entity);
        } catch (EntityProxyOnlyException | RepositoryErrorException | InvalidParameterException | UserNotAuthorizedException | PagingErrorException | TypeDefNotKnownException | EntityNotKnownException | PropertyErrorException | FunctionNotSupportedException | TypeErrorException e) {
            log.debug("Governance Engine OMAS is unable to process a Re-Classified Entity event");
        }
    }

    @Override
    public void processDeletedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processPurgedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String typeDefGUID, String typeDefName, String instanceGUID) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processRestoredEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processReTypedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, TypeDefSummary originalTypeDef, EntityDetail entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processReHomedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String originalHomeMetadataCollectionId, EntityDetail entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processReIdentifiedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String originalEntityGUID, EntityDetail entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processRefreshEntityRequested(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String typeDefGUID, String typeDefName, String instanceGUID, String homeMetadataCollectionId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processRefreshEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processNewRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName,
                                            String originatorServerType, String originatorOrganizationName,
                                            Relationship relationship) {
        log.info("GE Process New Relationship");

        if (!(relationship.getType().getTypeDefName().equals(SEMANTIC_ASSIGNMENT)
                && relationship.getEntityTwoProxy().getType().getTypeDefName().equals(GLOSSARY_TERM))) {
            return;
        }

        try {
            governanceEngineEventProcessor.processSemanticAssignmentRelationshipCreation(relationship);
        } catch (RepositoryErrorException | UserNotAuthorizedException | EntityProxyOnlyException | PropertyErrorException | FunctionNotSupportedException | PagingErrorException | TypeDefNotKnownException | TypeErrorException | EntityNotKnownException | InvalidParameterException e) {
            log.debug("Governance Engine OMAS is unable to process a New Relationship Event");
        }
    }

    @Override
    public void processUpdatedRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, Relationship oldRelationship, Relationship newRelationship) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processUndoneRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, Relationship relationship) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processDeletedRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, Relationship relationship) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processPurgedRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String typeDefGUID, String typeDefName, String instanceGUID) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processRestoredRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, Relationship relationship) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processReTypedRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, TypeDefSummary originalTypeDef, Relationship relationship) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processReHomedRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String originalHomeMetadataCollectionId, Relationship relationship) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processReIdentifiedRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String originalRelationshipGUID, Relationship relationship) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processRefreshRelationshipRequest(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String typeDefGUID, String typeDefName, String instanceGUID, String homeMetadataCollectionId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processRefreshRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, Relationship relationship) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processInstanceBatchEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, InstanceGraph instances) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processConflictingInstancesEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String targetMetadataCollectionId, TypeDefSummary targetTypeDef, String targetInstanceGUID, String otherMetadataCollectionId, InstanceProvenanceType otherOrigin, TypeDefSummary otherTypeDef, String otherInstanceGUID, String errorMessage) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processConflictingTypeEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String targetMetadataCollectionId, TypeDefSummary targetTypeDef, String targetInstanceGUID, TypeDefSummary otherTypeDef, String errorMessage) {
        throw new UnsupportedOperationException();
    }

    private boolean validateEntityType(EntityDetail entity) {
        final String typeDefName = entity.getType().getTypeDefName();
        if (!(typeDefName.equals(RELATIONAL_COLUMN) || typeDefName.equals(RELATIONAL_TABLE) || typeDefName.equals(GLOSSARY_TERM))) {
            log.info("GE OMAS processes only Relational Column, Relational Table and Glossary Terms");
            return true;
        }
        return false;
    }

    private boolean validateClassifications(EntityDetail entity) {
        if (entity.getClassifications() == null || entity.getClassifications().isEmpty()) {
            log.info("GE OMAS does not processes entities without classifications, this is a wrong event!");
            return true;
        }
        return false;
    }
}
