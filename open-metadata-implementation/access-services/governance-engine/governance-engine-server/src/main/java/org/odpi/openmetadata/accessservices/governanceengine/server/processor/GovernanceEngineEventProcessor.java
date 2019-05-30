/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.governanceengine.api.events.GovernanceEngineEvent;
import org.odpi.openmetadata.accessservices.governanceengine.api.events.GovernanceEngineEventType;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.MetadataServerException;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAsset;
import org.odpi.openmetadata.accessservices.governanceengine.server.handlers.GovernedAssetHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
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

public class GovernanceEngineEventProcessor {

    private static final Logger log = LoggerFactory.getLogger(GovernanceEngineEventProcessor.class);

    private OpenMetadataTopicConnector governanceEngineOutputTopic;
    private GovernedAssetHandler governedAssetHandler;

    public GovernanceEngineEventProcessor(OMRSRepositoryConnector enterpriseOMRSRepositoryConnector, OpenMetadataTopicConnector governanceEngineOutputTopic) {
        this.governanceEngineOutputTopic = governanceEngineOutputTopic;

        try {
            governedAssetHandler = new GovernedAssetHandler(enterpriseOMRSRepositoryConnector);
        } catch (MetadataServerException e) {
            log.error("Metadata Server is not available.");
        }
    }

    public void processClassifiedEntity(EntityDetail entity) throws RepositoryErrorException, EntityProxyOnlyException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, UserNotAuthorizedException, InvalidParameterException {
        if (!governedAssetHandler.isSchemaElement(entity) || !governedAssetHandler.containsGovernedClassification(entity)) {
            return;
        }

        GovernanceEngineEvent governanceEvent = getGovernanceEngineEvent(entity, GovernanceEngineEventType.NEW_CLASSIFIED_ASSET);
        sendEvent(governanceEvent);
    }

    public void processReclassifiedEntity(EntityDetail entity) throws EntityProxyOnlyException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, UserNotAuthorizedException, InvalidParameterException, RepositoryErrorException {
        if (!governedAssetHandler.isSchemaElement(entity)) {
            return;
        }

        GovernanceEngineEvent governanceEvent = getGovernanceEngineEvent(entity, GovernanceEngineEventType.RE_CLASSIFIED_ASSET);

        sendEvent(governanceEvent);
    }

    public void processDeletedEntityEvent(EntityDetail entity) throws EntityProxyOnlyException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, UserNotAuthorizedException, InvalidParameterException, RepositoryErrorException {
        if (!governedAssetHandler.isSchemaElement(entity) || !governedAssetHandler.containsGovernedClassification(entity)) {
            return;
        }

        GovernanceEngineEvent governanceEngineEvent = getGovernanceEngineEvent(entity, GovernanceEngineEventType.DELETED_ASSET);

        sendEvent(governanceEngineEvent);
    }

    public void processDeclassifiedEntityEvent(EntityDetail entity) throws EntityProxyOnlyException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, UserNotAuthorizedException, InvalidParameterException, RepositoryErrorException {
        if (!governedAssetHandler.isSchemaElement(entity)) {
            return;
        }

        GovernanceEngineEvent governanceEngineEvent = getGovernanceEngineEvent(entity, GovernanceEngineEventType.DE_CLASSIFIED_ASSET);

        sendEvent(governanceEngineEvent);
    }

    private GovernanceEngineEvent getGovernanceEngineEvent(EntityDetail entityDetail, GovernanceEngineEventType governanceEngineEventType) throws EntityProxyOnlyException, TypeErrorException, TypeDefNotKnownException, PropertyErrorException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, UserNotAuthorizedException, InvalidParameterException, RepositoryErrorException {
        GovernanceEngineEvent governanceEvent = new GovernanceEngineEvent();

        governanceEvent.setEventType(governanceEngineEventType);
        GovernedAsset governedAsset = governedAssetHandler.getGovernedAsset(entityDetail);
        governanceEvent.setGovernedAsset(governedAsset);

        return governanceEvent;
    }

    private void sendEvent(GovernanceEngineEvent event) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            governanceEngineOutputTopic.sendEvent(mapper.writeValueAsString(event));
            log.info("[Governance Engine]event send");
        } catch (JsonProcessingException e) {
            log.error("[Governance Engine] Unable to map the json to string");
        } catch (ConnectorCheckedException e) {
            log.error("[Governance Engine] Unable to send event {}", event);
        }
    }

}
