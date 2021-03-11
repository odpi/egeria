/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.securitysyncservices.processor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.GovernanceClassification;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.GovernedAsset;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.rest.GovernedAssetListResponse;
import org.odpi.openmetadata.adminservices.configuration.properties.SecuritySyncConfig;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.RangerSecurityServiceConnector;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model.RangerSecurityServicePolicies;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model.RangerServiceResource;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model.RangerTag;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model.ResourceTagMapper;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.securitysyncservices.auditlog.SecuritySyncAuditCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants.GOVERNANCE_ENGINE_OMAS_URL;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants.SECURITY_SYNC_SERVER;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants.SECURITY_TAGS;

import static com.google.json.JsonSanitizer.sanitize;


public class SecuritySyncEventProcessor {

    private static final Logger log = LoggerFactory.getLogger(SecuritySyncEventProcessor.class);
    private static final Long DEFAULT_POLLING_INTERVAL = 360L;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private OMRSAuditLog auditLog;
    private SecuritySyncConfig securitySyncConfig;
    private RangerSecurityServiceConnector rangerOpenConnector;
    private Long tagPoliciesLastKnownVersion = 0L;
    private Long accessPoliciesLastKnownVersion = 0L;

    public SecuritySyncEventProcessor(SecuritySyncConfig securitySyncConfig, OMRSAuditLog auditLog) {
        this.auditLog = auditLog;
        this.securitySyncConfig = securitySyncConfig;
        rangerOpenConnector = new RangerSecurityServiceConnector(securitySyncConfig.getSecuritySyncServerConnection());
    }

    public void processExistingGovernedAssetsFromRepository() {
        logProcessing("processExistingGovernedAssetsFromRepository", SecuritySyncAuditCode.CLASSIFIED_GOVERNED_ASSET_INITIAL_LOAD);

        GovernedAssetListResponse governedAssetResponse = getGovernedAssets();
        if (governedAssetResponse == null || governedAssetResponse.getRelatedHTTPCode() != 200) {
            return;
        }

        List<GovernedAsset> governedAssets = governedAssetResponse.getGovernedAssetList();

        rangerOpenConnector.importTaggedResources(governedAssets);
    }

    public void syncSecurityPolicies() {
        final Runnable syncSecurityServicePolicies = () ->
        {
            log.debug("Re-sync policies from external tool");
            processSecurityServicePolicies();
        };
        scheduler.scheduleAtFixedRate(syncSecurityServicePolicies,
                securitySyncConfig.getPollingInterval() != null ? securitySyncConfig.getPollingInterval() : DEFAULT_POLLING_INTERVAL,
                securitySyncConfig.getPollingInterval() != null ? securitySyncConfig.getPollingInterval() : DEFAULT_POLLING_INTERVAL,
                SECONDS);
    }

    public void processSecurityServicePolicies() {
        String methodName = "processSecurityServicePolicies";
        logProcessing(methodName, SecuritySyncAuditCode.SYNCHRONIZE_POLICIES_STARTED);

        syncSecurityServicePolicies(securitySyncConfig.getSecuritySyncTagServiceName(), tagPoliciesLastKnownVersion);
        syncSecurityServicePolicies(securitySyncConfig.getSecuritySyncAccessResourceServiceName(), accessPoliciesLastKnownVersion);

        logProcessing(methodName, SecuritySyncAuditCode.SYNCHRONIZE_POLICIES_FINISHED);
    }

    public void processClassifiedGovernedAssetEvent(GovernedAsset governedAsset) {
        logProcessing("processClassifiedGovernedAssetEvent", SecuritySyncAuditCode.CLASSIFIED_GOVERNED_ASSET_EVENT_RECEIVED);

        List<RangerTag> securityTags = rangerOpenConnector.createSecurityTags(governedAsset.getAssignedGovernanceClassification());
        if (securityTags == null || securityTags.isEmpty()) {
            return;
        }
        RangerServiceResource resource = rangerOpenConnector.createResource(governedAsset);

        for (RangerTag securityTag : securityTags) {
            rangerOpenConnector.createAssociationResourceToSecurityTag(resource.getGuid(), securityTag.getGuid());
        }
    }

    public void processReClassifiedGovernedAssetEvent(GovernedAsset governedAsset) {
        logProcessing("processReClassifiedGovernedAssetEvent", SecuritySyncAuditCode.RE_CLASSIFIED_GOVERNED_ASSET_EVENT_RECEIVED);

        RangerServiceResource resource = declassifiedGovernedAsset(governedAsset);

        if (governedAsset.getAssignedGovernanceClassification() == null) {
            return;
        }

        GovernanceClassification classification = governedAsset.getAssignedGovernanceClassification();
        if (classification.getSecurityLabels() == null || classification.getSecurityLabels().isEmpty()) {
            return;
        }

        List<RangerTag> securityTags = rangerOpenConnector.createSecurityTags(classification);

        if (resource != null && resource.getGuid() != null) {
            for (RangerTag securityTag : securityTags) {
                rangerOpenConnector.createAssociationResourceToSecurityTag(resource.getGuid(), securityTag.getGuid());
            }
        }
    }

    public void processDeClassifiedGovernedAssetEvent(GovernedAsset governedAsset) {
        logProcessing("processDeclassifiedGovernedAssetEvent", SecuritySyncAuditCode.DE_CLASSIFIED_GOVERNED_ASSET_EVENT_RECEIVED);

        declassifiedGovernedAsset(governedAsset);
    }

    public void processDeletedGovernedAssetEvent(GovernedAsset governedAsset) {
        logProcessing("processDeclassifiedGovernedAssetEvent", SecuritySyncAuditCode.DELETED_GOVERNED_ASSET_EVENT_RECEIVED);

        RangerServiceResource resource = declassifiedGovernedAsset(governedAsset);

        if (resource != null && resource.getGuid() != null) {
            rangerOpenConnector.deleteResource(resource.getGuid());
        }
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

    private GovernedAssetListResponse getGovernedAssets() {
        String governanceEngineURL = getGovernanceEngineURL();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {

            ResponseEntity<String> result = restTemplate.exchange(governanceEngineURL, HttpMethod.GET, entity, String.class);
            return (GovernedAssetListResponse) mapToObject(result, GovernedAssetListResponse.class);
        } catch (HttpStatusCodeException exception) {
            log.error("Unable to get the governed assets: ", exception);
        }
        return null;
    }

    private String getGovernanceEngineURL() {

        return MessageFormat.format(GOVERNANCE_ENGINE_OMAS_URL,
                securitySyncConfig.getAccessServiceRootURL(),
                securitySyncConfig.getAccessServiceServerName(),
                SECURITY_SYNC_SERVER,
                SECURITY_TAGS);
    }

    private Object mapToObject(ResponseEntity<String> result, Class className) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            String resultString = sanitize(result.getBody());
            return mapper.readValue(resultString, className);
        } catch (IOException e) {
            log.error("403 {} - {}", e.getMessage(), e);
        }

        return null;
    }

    private RangerServiceResource declassifiedGovernedAsset(GovernedAsset governedAsset) {
        if (governedAsset == null) {
            return null;
        }

        log.debug("de-classified entity: {}", governedAsset.getGuid());

        RangerServiceResource resource = rangerOpenConnector.getResourceByGUID(governedAsset.getGuid());
        if (resource == null) {
            return null;
        }

        List<ResourceTagMapper> resourceTagMapper = rangerOpenConnector.getTagsAssociatedWithTheResource(resource.getId());

        if (resourceTagMapper != null) {
            resourceTagMapper.forEach(mapping -> rangerOpenConnector.deleteAssociationResourceToSecurityTag(mapping));
        }

        return resource;
    }

    private void syncSecurityServicePolicies(String securitySyncServiceName, Long tagPoliciesLastKnownVersion) {
        if (securitySyncServiceName == null) {
            return;
        }

        RangerSecurityServicePolicies securityServicePolicies = rangerOpenConnector.getSecurityServicePolicies(securitySyncServiceName, tagPoliciesLastKnownVersion);
        if (securityServicePolicies != null) {
            setLastKnownVersion(securitySyncServiceName, securityServicePolicies);
            //TODO: process the policies and sync MR
        }
    }

    private void setLastKnownVersion(String securitySyncTagServiceName, RangerSecurityServicePolicies securityServicePolicies) {
        if (securitySyncTagServiceName.startsWith("tag")) {
            tagPoliciesLastKnownVersion = securityServicePolicies.getPolicyVersion();
        } else {
            accessPoliciesLastKnownVersion = securityServicePolicies.getPolicyVersion();
        }
    }
}