/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */

/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.securityofficer.server.admin.publisher;

import org.odpi.openmetadata.accessservices.securityofficer.server.admin.processor.SecurityOfficerEventProcessor;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityOfficerPublisher extends OMRSInstanceEventProcessor {

    private static final Logger log = LoggerFactory.getLogger(SecurityOfficerPublisher.class);
    private SecurityOfficerEventProcessor securityOfficerEventProcessor;

    public SecurityOfficerPublisher(SecurityOfficerEventProcessor securityOfficerEventProcessor) {
        this.securityOfficerEventProcessor = securityOfficerEventProcessor;
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
    public void processClassifiedEntityEvent(String sourceName, String originatorMetadataCollectionId,
                                             String originatorServerName, String originatorServerType,
                                             String originatorOrganizationName, EntityDetail entity) {
        log.info("[Process Classified Entity Event] GUID = {}, type = {} has been classified.", entity.getGUID(), entity.getType().getTypeDefName());
        securityOfficerEventProcessor.processClassifiedEntity(entity);
    }

    @Override
    public void processDeclassifiedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processReclassifiedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processDeletedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processRestoredEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, EntityDetail entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processPurgedEntityEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String typeDefGUID, String typeDefName, String instanceGUID) {
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
    public void processNewRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, Relationship relationship) {
        throw new UnsupportedOperationException();
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
    public void processRestoredRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, Relationship relationship) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void processPurgedRelationshipEvent(String sourceName, String originatorMetadataCollectionId, String originatorServerName, String originatorServerType, String originatorOrganizationName, String typeDefGUID, String typeDefName, String instanceGUID) {
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
}