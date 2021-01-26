/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */
package org.odpi.openmetadata.accessservices.securityofficer.server.processors;

import org.odpi.openmetadata.accessservices.securityofficer.api.events.SecurityOfficerEventType;
import org.odpi.openmetadata.accessservices.securityofficer.api.events.SecurityOfficerTagEvent;
import org.odpi.openmetadata.accessservices.securityofficer.server.handler.SecurityOfficerHandler;
import org.odpi.openmetadata.accessservices.securityofficer.server.utils.Builder;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecurityOfficerEventProcessor {

    private static final String SECURITY_OFFICER_OMAS = AccessServiceDescription.SECURITY_OFFICER_OMAS.getAccessServiceFullName();
    private static final Logger log = LoggerFactory.getLogger(SecurityOfficerEventProcessor.class);
    private SecurityOfficerHandler securityOfficerHandler;
    private Builder builder = new Builder();

    public SecurityOfficerEventProcessor(OMRSRepositoryConnector enterpriseOMRSRepositoryConnector) {
        try {
            securityOfficerHandler = new SecurityOfficerHandler(enterpriseOMRSRepositoryConnector);
        } catch (PropertyServerException e) {
            log.error(e.getMessage());
        }
    }

    public SecurityOfficerTagEvent processSemanticAssignmentForSchemaElement(Relationship relationship) {
        EntityDetail glossaryTerm = securityOfficerHandler.getEntityDetailById(SECURITY_OFFICER_OMAS, relationship.getEntityTwoProxy().getGUID());
        EntityDetail schemaElement = securityOfficerHandler.getEntityDetailById(SECURITY_OFFICER_OMAS, relationship.getEntityOneProxy().getGUID());

        return buildSecurityOfficerNewTagEvent(schemaElement, glossaryTerm);
    }

    public List<SecurityOfficerTagEvent> processClassifiedGlossaryTerm(EntityDetail glossaryTerm) {
        List<EntityDetail> schemaElementsAssignedToBusinessTerms = securityOfficerHandler.getSchemaElementsAssignedToBusinessTerms(SECURITY_OFFICER_OMAS, glossaryTerm.getGUID());
        if (schemaElementsAssignedToBusinessTerms.isEmpty()) {
            return Collections.emptyList();
        }

        List<SecurityOfficerTagEvent> securityOfficerEvents = new ArrayList<>();
        for (EntityDetail schemaElement : schemaElementsAssignedToBusinessTerms) {
            SecurityOfficerTagEvent securityOfficerTagEvent = buildSecurityOfficerNewTagEvent(schemaElement, glossaryTerm);
            securityOfficerEvents.add(securityOfficerTagEvent);
        }

        return securityOfficerEvents;
    }

    public List<SecurityOfficerTagEvent> processReClassifiedGlossaryTerm(EntityDetail glossaryTerm) {
        List<EntityDetail> schemaElementsAssignedToBusinessTerms = securityOfficerHandler.getSchemaElementsAssignedToBusinessTerms(SECURITY_OFFICER_OMAS, glossaryTerm.getGUID());
        if (schemaElementsAssignedToBusinessTerms.isEmpty()) {
            return Collections.emptyList();
        }

        List<SecurityOfficerTagEvent> securityOfficerEvents = new ArrayList<>();
        for (EntityDetail schemaElement : schemaElementsAssignedToBusinessTerms) {
            SecurityOfficerTagEvent securityOfficerTagEvent = buildSecurityOfficerUpdatedTagEvent(schemaElement, glossaryTerm);
            securityOfficerEvents.add(securityOfficerTagEvent);
        }

        return securityOfficerEvents;
    }

    public List<SecurityOfficerTagEvent> processDeClassifiedGlossaryTerm(EntityDetail glossaryTerm) {
        List<EntityDetail> schemaElementsAssignedToBusinessTerms = securityOfficerHandler.getSchemaElementsAssignedToBusinessTerms(SECURITY_OFFICER_OMAS, glossaryTerm.getGUID());
        if (schemaElementsAssignedToBusinessTerms.isEmpty()) {
            return Collections.emptyList();
        }

        List<SecurityOfficerTagEvent> securityOfficerEvents = new ArrayList<>();
        for (EntityDetail schemaElement : schemaElementsAssignedToBusinessTerms) {
            SecurityOfficerTagEvent securityOfficerTagEvent = buildSecurityOfficerDeletedTagEvent(schemaElement, glossaryTerm);
            securityOfficerEvents.add(securityOfficerTagEvent);
        }

        return securityOfficerEvents;
    }

    private SecurityOfficerTagEvent buildSecurityOfficerNewTagEvent(EntityDetail schemaElement, EntityDetail glossaryTerm) {
        SecurityOfficerTagEvent securityOfficerEvent = new SecurityOfficerTagEvent();

        securityOfficerEvent.setEventType(SecurityOfficerEventType.NEW_SECURITY_ASSIGNMENT);
        securityOfficerEvent.setSchemaElementEntity(builder.buildSchemaElementContext(schemaElement, glossaryTerm));

        return securityOfficerEvent;
    }

    private SecurityOfficerTagEvent buildSecurityOfficerUpdatedTagEvent(EntityDetail schemaElement, EntityDetail glossaryTerm) {
        SecurityOfficerTagEvent securityOfficerEvent = new SecurityOfficerTagEvent();

        securityOfficerEvent.setEventType(SecurityOfficerEventType.UPDATED_SECURITY_ASSIGNMENT);
        securityOfficerEvent.setSchemaElementEntity(builder.buildSchemaElementContext(schemaElement, glossaryTerm));

        return securityOfficerEvent;
    }

    private SecurityOfficerTagEvent buildSecurityOfficerDeletedTagEvent(EntityDetail schemaElement, EntityDetail glossaryTerm) {
        SecurityOfficerTagEvent securityOfficerEvent = new SecurityOfficerTagEvent();

        securityOfficerEvent.setEventType(SecurityOfficerEventType.DELETED_SECURITY_ASSIGNMENT);
        securityOfficerEvent.setSchemaElementEntity(builder.buildSchemaElementContext(schemaElement, glossaryTerm));

        return securityOfficerEvent;
    }
}
