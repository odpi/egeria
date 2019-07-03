/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */
package org.odpi.openmetadata.accessservices.securityofficer.server.admin.processor;

import org.odpi.openmetadata.accessservices.securityofficer.api.events.SecurityOfficerEventType;
import org.odpi.openmetadata.accessservices.securityofficer.api.events.SecurityOfficerNewTagEvent;
import org.odpi.openmetadata.accessservices.securityofficer.api.ffdc.exceptions.MetadataServerException;
import org.odpi.openmetadata.accessservices.securityofficer.server.admin.handler.SecurityOfficerHandler;
import org.odpi.openmetadata.accessservices.securityofficer.server.admin.utils.Builder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityOfficerEventProcessor {

    private static final String SECURITY_OFFICER_OMAS = "SecurityOfficerOMAS";
    private static final Logger log = LoggerFactory.getLogger(SecurityOfficerEventProcessor.class);
    private SecurityOfficerHandler securityOfficerHandler;
    private OMRSRepositoryConnector omrsRepositoryConnector;
    private Builder builder = new Builder();

    public SecurityOfficerEventProcessor(OMRSRepositoryConnector enterpriseOMRSRepositoryConnector) {
        omrsRepositoryConnector = enterpriseOMRSRepositoryConnector;

        try {
            securityOfficerHandler = new SecurityOfficerHandler(enterpriseOMRSRepositoryConnector);
        } catch (MetadataServerException e) {
            log.error(e.getErrorMessage());
        }
    }

    public SecurityOfficerNewTagEvent processSemanticAssignmentForSchemaElement(Relationship relationship) {
        SecurityOfficerNewTagEvent securityOfficerEvent = new SecurityOfficerNewTagEvent();

        securityOfficerEvent.setEventType(SecurityOfficerEventType.NEW_SECURITY_ASSIGNMENT);

        EntityDetail glossaryTermDetails = securityOfficerHandler.getEntityDetailById(SECURITY_OFFICER_OMAS, relationship.getEntityTwoProxy().getGUID());
        EntityDetail schemaElement = securityOfficerHandler.getEntityDetailById(SECURITY_OFFICER_OMAS, relationship.getEntityOneProxy().getGUID());
        securityOfficerEvent.setSchemaElementEntity(builder.buildSchemaElementContext(schemaElement, glossaryTermDetails));

        return securityOfficerEvent;
    }
}
