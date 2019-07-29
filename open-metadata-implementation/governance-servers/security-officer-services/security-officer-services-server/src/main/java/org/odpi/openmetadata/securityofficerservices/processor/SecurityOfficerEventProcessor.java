/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */

package org.odpi.openmetadata.securityofficerservices.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SchemaElementEntity;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SecurityClassification;
import org.odpi.openmetadata.adminservices.configuration.properties.SecurityOfficerConfig;
import org.odpi.openmetadata.openconnector.governancedarmonconnectors.securityofficerconnectors.SecurityOfficerConnector;
import org.odpi.openmetadata.openconnector.governancedarmonconnectors.securityofficerconnectors.securitytagconnector.SecurityTagConnector;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.Collections;

public class SecurityOfficerEventProcessor {

    private static final Logger log = LoggerFactory.getLogger(SecurityOfficerEventProcessor.class);
    private static final String SECURITY_OFFICER_SERVER = "SecurityOfficerServer";
    private static final String SECURITY_OFFICER_OMAS_URL = "{0}/servers/{1}/open-metadata/access-services/security-officer/users/{2}/security-tag/element/{3}";

    private OMRSAuditLog auditLog;
    private SecurityOfficerConfig securitySyncConfig;
    private SecurityOfficerConnector connector;

    public SecurityOfficerEventProcessor(SecurityOfficerConfig securityOfficerConfig, OMRSAuditLog auditLog) {
        this.auditLog = auditLog;
        this.securitySyncConfig = securityOfficerConfig;
        connector = new SecurityTagConnector();
    }

    public void processNewAssignment(SchemaElementEntity schemaElementEntity) {
        if (schemaElementEntity == null) {
            return;
        }

        SecurityClassification securityClassification = connector.resolveSecurityClassification(schemaElementEntity);
        setSecurityTag(schemaElementEntity.getGuid(), securityClassification);
    }

    private void setSecurityTag(String guid, SecurityClassification securityClassification) {
        String securityOMASURL = getSecurityOMASURL(guid);
        String securityClassificationBody = getBody(securityClassification);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(securityClassificationBody, getBasicHTTPHeaders());

        try {
            ResponseEntity<SchemaElementEntity> elementEntity = restTemplate.exchange(securityOMASURL, HttpMethod.POST, entity, SchemaElementEntity.class);
            if (elementEntity.getBody() != null) {
                log.debug(String.valueOf(elementEntity));
            }
        } catch (HttpStatusCodeException exception) {
            log.debug("Unable to set/update the security tag for schema element {}", guid);
        }
    }

    private String getBody(Object resource) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(resource);
        } catch (JsonProcessingException e) {
            log.error("error write json ");
        }
        return null;
    }

    private String getSecurityOMASURL(String guid) {

        return MessageFormat.format(
                SECURITY_OFFICER_OMAS_URL,
                securitySyncConfig.getAccessServiceRootURL(),
                securitySyncConfig.getAccessServiceServerName(),
                SECURITY_OFFICER_SERVER,
                guid);
    }

    private HttpHeaders getBasicHTTPHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

}