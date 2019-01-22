/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.securitysyncservices.processor;

import com.google.gson.Gson;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAsset;
import org.odpi.openmetadata.adminservices.configuration.properties.SecuritySyncConfig;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.securitysyncservices.model.Ranger.RangerPolicyResource;
import org.odpi.openmetadata.securitysyncservices.model.Ranger.RangerResource;
import org.odpi.openmetadata.securitysyncservices.model.Ranger.RangerServiceResource;
import org.odpi.openmetadata.securitysyncservices.model.Ranger.RangerTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.odpi.openmetadata.securitysyncservices.util.Constants.ADD_OR_UPDATE;
import static org.odpi.openmetadata.securitysyncservices.util.Constants.DEFAULT_SCHEMA_NAME;
import static org.odpi.openmetadata.securitysyncservices.util.Constants.SECURITY_SYNC;
import static org.odpi.openmetadata.securitysyncservices.util.Constants.TAG_SVC;

public class SecuritySyncProcessor {

    private static final Logger log = LoggerFactory.getLogger(SecuritySyncProcessor.class);
    private SecuritySyncConfig securitySyncConfig;
    private OMRSAuditLog auditLog;

    public SecuritySyncProcessor(SecuritySyncConfig securitySyncConfig, OMRSAuditLog auditLog) {
        this.securitySyncConfig = securitySyncConfig;
        this.auditLog = auditLog;
    }

    public void processGovernedEvent(GovernedAsset governedAsset) {
        //TODO: processing will be added here
    }

    private RangerPolicyResource getListOfPossibleValuesOfElements(String value) {
        RangerPolicyResource resourceValue = new RangerPolicyResource();

        List<String> possibleValues = new ArrayList<>(1);
        possibleValues.add(value);
        resourceValue.setValues(possibleValues);

        return resourceValue;
    }

    private Map<Long, List<Long>> mapResourcesToTags(RangerServiceResource rangerServiceResource, Map<Long, RangerTag> tags) {
        Map<Long, List<Long>> resourceToTagIds = new HashMap<>();
        final Long resourceId = rangerServiceResource.getId();
        List<Long> tagList = new ArrayList<>(tags.keySet());

        resourceToTagIds.put(resourceId, tagList);
        return resourceToTagIds;
    }

    private RangerResource buildRangerResource(List<RangerServiceResource> rangerServiceResource, Map<Long, RangerTag> tags, Map<Long, List<Long>> resourceToTagIds) {
        RangerResource rangerResource = new RangerResource();

        rangerResource.setTags(tags);
        rangerResource.setServiceResources(rangerServiceResource);
        rangerResource.setResourceToTagIds(resourceToTagIds);
        rangerResource.setOp(ADD_OR_UPDATE);
        rangerResource.setServiceName(TAG_SVC);
        rangerResource.setTagVersion(1L);

        return rangerResource;
    }

    private List<RangerServiceResource> buildServiceResources(Map<String, RangerPolicyResource> resourceElements, String guid) {
        RangerServiceResource serviceResource = getRangerServiceResource(resourceElements, guid);

        List<RangerServiceResource> resources = new ArrayList<>(1);
        resources.add(serviceResource);
        return resources;
    }

    private RangerServiceResource getRangerServiceResource(Map<String, RangerPolicyResource> resourceElements, String guid) {
        RangerServiceResource serviceResource = new RangerServiceResource();

        serviceResource.setId(0L);
        serviceResource.setGuid(guid);
        serviceResource.setServiceName(DEFAULT_SCHEMA_NAME);
        serviceResource.setCreatedBy(SECURITY_SYNC);
        serviceResource.setResourceElements(resourceElements);

        return serviceResource;
    }

    private void mapResourcesToTagsInRangerServer(RangerResource resource) {

        String body = getBody(resource);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(body, getHttpHeaders());

        try {
            String url = getRangerURL();
            HttpEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
            log.info(response.getBody());
        } catch (HttpStatusCodeException exception) {
            log.error(exception.getMessage());
        }
    }

    private String getBody(RangerResource resource) {
        Gson gson = new Gson();
        return gson.toJson(resource);
    }

    private String getRangerURL() {
        String rangerBaseURL = securitySyncConfig.getSecurityServerURL();
        return MessageFormat.format("{0}/service/tags/importservicetags", rangerBaseURL);
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        List<MediaType> accept = new ArrayList<>(1);
        accept.add(MediaType.APPLICATION_JSON);
        headers.setAccept(accept);
        headers.set("Authorization", securitySyncConfig.getSecurityServerAuthorization());

        return headers;
    }
}
