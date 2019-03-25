/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.securitysyncservices.processor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernanceClassification;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAsset;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAssetListAPIResponse;
import org.odpi.openmetadata.adminservices.configuration.properties.SecuritySyncConfig;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.RangerSecurityServiceConnector;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model.RangerServiceResource;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model.RangerTag;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model.ResourceTagMapper;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.securitysyncservices.auditlog.SecuritySyncAuditCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants.*;

public class GovernanceEventProcessor {

    private static final Logger log = LoggerFactory.getLogger(GovernanceEventProcessor.class);
    private OMRSAuditLog auditLog;
    private SecuritySyncConfig securitySyncConfig;
    private RangerSecurityServiceConnector rangerOpenConnector;

    public GovernanceEventProcessor(SecuritySyncConfig securitySyncConfig, OMRSAuditLog auditLog) {
        this.auditLog = auditLog;
        this.securitySyncConfig = securitySyncConfig;
        rangerOpenConnector = new RangerSecurityServiceConnector(securitySyncConfig.getSecurityServerConnection());
    }

    public void processExistingGovernedAssetsFromRepository() {
        logProcessing("processExistingGovernedAssetsFromRepository", SecuritySyncAuditCode.CLASSIFIED_GOVERNED_ASSET_INITIAL_LOAD);

        GovernedAssetListAPIResponse governedAssetResponse = getGovernedAssets();
        if (governedAssetResponse == null || governedAssetResponse.getRelatedHTTPCode() != 200) {
            return;
        }

        List<GovernedAsset> governedAssets = governedAssetResponse.getGovernedAssetList();
        if (governedAssets.isEmpty()) {
            return;
        }

        rangerOpenConnector.importTaggedResources(governedAssets);
    }

    public void processClassifiedGovernedAssetEvent(GovernedAsset governedAsset) {
        logProcessing("processClassifiedGovernedAssetEvent", SecuritySyncAuditCode.CLASSIFIED_GOVERNED_ASSET_EVENT_RECEIVED);

        rangerOpenConnector.createAssociationResourceToSecurityTag(governedAsset);
    }

    public void processReClassifiedGovernedAssetEvent(GovernedAsset governedAsset) {
        logProcessing("processReClassifiedGovernedAssetEvent", SecuritySyncAuditCode.RE_CLASSIFIED_GOVERNED_ASSET_EVENT_RECEIVED);

        if (governedAsset == null) {
            return;
        }
        RangerServiceResource resource = rangerOpenConnector.getResourceByGUID(governedAsset.getGuid());
        if (resource == null) {
            return;

        }
        ResourceTagMapper resourceTagMapper = rangerOpenConnector.getTagAssociatedWithTheResource(resource.getId());

        if (resourceTagMapper != null) {
            rangerOpenConnector.deleteAssociationResourceToSecurityTag(resourceTagMapper);
        }

        if (governedAsset.getAssignedGovernanceClassifications() == null || governedAsset.getAssignedGovernanceClassifications().isEmpty()) {
            return;
        }

        GovernanceClassification classification = governedAsset.getAssignedGovernanceClassifications().get(0);
        if (!classification.getAttributes().containsKey(LEVEL)) {
            return;
        }

        String newTagGuid = classification.getAttributes().get(LEVEL);

        List<RangerTag> allTags = rangerOpenConnector.getSecurityTags();
        RangerTag existingTag = existingTag(allTags, newTagGuid);

        if (existingTag == null) {
            rangerOpenConnector.createSecurityTag(classification);
        }

        rangerOpenConnector.createAssociationResourceToSecurityTag(newTagGuid, resource.getGuid());
    }

    private void logProcessing(String action, SecuritySyncAuditCode auditCode) {

        auditLog.logRecord(action,
                auditCode.getLogMessageId(),
                OMRSAuditLogRecordSeverity.INFO,
                auditCode.getFormattedLogMessage(),
                null,
                auditCode.getSystemAction(),
                auditCode.getUserAction());
    }

    private RangerTag existingTag(List<RangerTag> rangerTags, String tagGuid) {
        Optional<RangerTag> any = rangerTags.stream().filter(tag -> tag.getGuid().equals(tagGuid)).findAny();
        return any.orElse(null);
    }

    private GovernedAssetListAPIResponse getGovernedAssets() {
        String governanceEngineURL = getGovernanceEngineURL(GOVERNED_ASSETS);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {

            ResponseEntity<String> result = restTemplate.exchange(governanceEngineURL, HttpMethod.GET, entity, String.class);
            return (GovernedAssetListAPIResponse) mapToObject(result, GovernedAssetListAPIResponse.class);
        } catch (HttpStatusCodeException exception) {
            log.error(exception.getMessage());
        }
        return null;
    }

    private String getGovernanceEngineURL(String endpoint) {
        String geBaseURL = securitySyncConfig.getGovernanceEngineServerURL();
        return MessageFormat.format(endpoint, geBaseURL, CONFIDENTIALITY);
    }

    private Object mapToObject(ResponseEntity<String> result, Class className) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return mapper.readValue(result.getBody(), className);
        } catch (IOException e) {
            log.error("403", e.getMessage(), e);
        }

        return null;
    }
}
