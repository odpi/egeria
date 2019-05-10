/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.Context;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernanceClassification;
import org.odpi.openmetadata.accessservices.governanceengine.api.objects.GovernedAsset;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model.RangerPolicyResource;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model.RangerServiceResource;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model.RangerTag;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model.RangerTagDef;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model.ResourceTagMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants.*;

public class RangerSecurityServiceConnector extends ConnectorBase implements SecurityServiceConnector {

    private static final Logger log = LoggerFactory.getLogger(RangerSecurityServiceConnector.class);
    private Connection connection;

    public RangerSecurityServiceConnector(Connection securityServerConnection) {
        this.connection = securityServerConnection;
    }

    @Override
    public void importTaggedResources(List<GovernedAsset> governedAssets) {
        Set<RangerTag> tags = new HashSet<>();
        Map<String, String> tagToResource = new HashMap<>();

        createRangerTagDef();

        for (GovernedAsset governedAsset : governedAssets) {

            if (governedAsset.getAssignedGovernanceClassification() == null) {
                continue;
            }

            RangerTag rangerTag = buildTag(governedAsset.getAssignedGovernanceClassification());
            tags.add(rangerTag);

            RangerServiceResource resource = createResource(governedAsset);
            tagToResource.put(rangerTag.getGuid(), resource.getGuid());
        }

        tags.forEach(this::createRangerTag);
        tagToResource.forEach(this::createAssociationResourceToSecurityTag);
    }

    @Override
    public ResourceTagMapper createAssociationResourceToSecurityTag(String tagGUID, String resourceGUID) {
        String rangerBaseURL = connection.getEndpoint().getAddress();
        String createAssociation = MessageFormat.format(SERVICE_TAGS_MAP_TAG_GUID_RESOURCE_GUI, rangerBaseURL, tagGUID, resourceGUID);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(getHttpHeaders());
        try {
            ResponseEntity<ResourceTagMapper> result = restTemplate.exchange(createAssociation, HttpMethod.POST, entity, ResourceTagMapper.class);
            return result.getBody();
        } catch (HttpStatusCodeException exception) {
            log.error("Unable to create the association between tag and resource");
        }
        return null;
    }

    @Override
    public void deleteAssociationResourceToSecurityTag(ResourceTagMapper resourceTagMapper) {
        String rangerBaseURL = connection.getEndpoint().getAddress();
        String deleteAssociationURL = MessageFormat.format(TAG_RESOURCE_ASSOCIATION, rangerBaseURL, resourceTagMapper.getId());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            restTemplate.delete(deleteAssociationURL, HttpMethod.DELETE, entity);
        } catch (HttpStatusCodeException exception) {
            log.error("Unable to delete the association between a tag and a resource");
        }
    }

    @Override
    public RangerTag createSecurityTag(GovernanceClassification classification) {
        final RangerTag rangerTag = buildTag(classification);
        return createRangerTag(rangerTag);
    }

    @Override
    public ResourceTagMapper getTagAssociatedWithTheResource(Long id) {
        List<ResourceTagMapper> mapper = getMappedResources();
        if (mapper == null) {
            return null;
        }

        Optional<ResourceTagMapper> mappedResource = mapper.stream().filter(resourceTagMapper -> resourceTagMapper.getResourceId().equals(id)).findFirst();
        return mappedResource.orElse(null);
    }

    @Override
    public RangerServiceResource getResourceByGUID(String resourceGuid) {
        String resourceURL = getResourceURL(resourceGuid, SERVICE_TAGS_RESOURCE_BY_GUID);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(getHttpHeaders());

        try {
            ResponseEntity<RangerServiceResource> result = restTemplate.exchange(resourceURL, HttpMethod.GET, entity, RangerServiceResource.class);
            return result.getBody();
        } catch (HttpStatusCodeException exception) {
            log.debug("Unable to fetch the resource with guid = {}", resourceGuid);
        }
        return null;
    }

    @Override
    public void deleteResourceByGUID(String resourceGuid) {
        String resourceURL = getResourceURL(resourceGuid, SERVICE_TAGS_RESOURCE_BY_GUID);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(getHttpHeaders());

        try {
            restTemplate.delete(resourceURL, HttpMethod.DELETE, entity);
            log.info("The resource with guid = {} has been deleted", resourceGuid);
        } catch (HttpStatusCodeException exception) {
            log.debug("Unable to delete the resource with guid = {}", resourceGuid);
        }
    }

    @Override
    public RangerServiceResource createResource(GovernedAsset governedAsset) {
        RangerServiceResource serviceResource = buildRangerServiceResource(governedAsset);

        String rangerBaseURL = connection.getEndpoint().getAddress();
        String createAssociation = MessageFormat.format(SERVICE_TAGS_RESOURCES, rangerBaseURL);

        String body = getBody(serviceResource);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(body, getHttpHeaders());

        try {
            ResponseEntity<RangerServiceResource> result = restTemplate.exchange(createAssociation, HttpMethod.POST, entity, RangerServiceResource.class);
            return result.getBody();
        } catch (HttpStatusCodeException exception) {
            log.debug("Unable to create the association between tag and resource");
        }
        return null;
    }

    private RangerServiceResource buildRangerServiceResource(GovernedAsset governedAsset) {
        RangerServiceResource serviceResource = new RangerServiceResource();

        serviceResource.setGuid(governedAsset.getGuid());
        serviceResource.setServiceName(DEFAULT_SCHEMA_NAME);
        serviceResource.setCreatedBy(RANGER_CONNECTOR);
        Map<String, RangerPolicyResource> resourceElements = getRangerPolicyResourceMap(governedAsset.getContext());
        serviceResource.setResourceElements(resourceElements);

        return serviceResource;
    }

    private String getResourceURL(String resourceGuid, String serviceTagsResourceByGuid) {
        String rangerBaseURL = connection.getEndpoint().getAddress();
        return MessageFormat.format(serviceTagsResourceByGuid, rangerBaseURL, resourceGuid);
    }

    private RangerTag createRangerTag(RangerTag rangerTag) {
        String createTagURL = getRangerURL(SERVICE_TAGS);
        String body = getBody(rangerTag);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(body, getHttpHeaders());

        try {
            ResponseEntity<RangerTag> result = restTemplate.exchange(createTagURL, HttpMethod.POST, entity, RangerTag.class);
            return result.getBody();
        } catch (HttpStatusCodeException exception) {
            log.debug("Unable to create a security tag");
        }
        return null;
    }

    private RangerTagDef createRangerTagDef() {
        RangerTagDef rangerTagDef = buildRangerTagDef();
        String body = getBody(rangerTagDef);

        String createRangerTagDefURL = getRangerURL(SERVICE_TAGS_TAGDEF);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(body, getHttpHeaders());

        try {
            ResponseEntity<RangerTagDef> result = restTemplate.exchange(createRangerTagDefURL, HttpMethod.POST, entity, RangerTagDef.class);
            return result.getBody();
        } catch (HttpStatusCodeException exception) {
            log.debug("Unable to create a security tag");
        }
        return null;
    }

    private RangerTagDef buildRangerTagDef() {
        RangerTagDef rangerTagDef = new RangerTagDef();
        rangerTagDef.setId(1L);
        rangerTagDef.setCreatedBy(RANGER_CONNECTOR);
        rangerTagDef.setName(CONFIDENTIALITY);

        return rangerTagDef;
    }

    private List<ResourceTagMapper> getMappedResources() {
        String allMappedResources = getRangerURL(SERVICE_TAGS_TAG_RESOURCE_MAPS);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(getHttpHeaders());

        try {
            ResponseEntity<List<ResourceTagMapper>> response = restTemplate.exchange(allMappedResources, HttpMethod.GET, entity, new ParameterizedTypeReference<List<ResourceTagMapper>>() {
            });
            return response.getBody();
        } catch (HttpStatusCodeException exception) {
            log.debug("Unable to fetch the mapped resources");
        }
        return Collections.emptyList();
    }

    private RangerTag buildTag(GovernanceClassification classification) {
        RangerTag tag = new RangerTag();

        tag.setCreatedBy(RANGER_CONNECTOR);
        tag.setType(CONFIDENTIALITY);
        tag.setOwner(OPEN_METADATA_OWNER);
        if (classification.getAttributes().containsKey(LEVEL)) {
            tag.setGuid(classification.getAttributes().get(LEVEL));
        }

        return tag;
    }

    private Map<String, RangerPolicyResource> getRangerPolicyResourceMap(Context context) {
        Map<String, RangerPolicyResource> resourceElements = new HashMap<>(3);
        final RangerPolicyResource schemaValues = getListOfPossibleValuesOfElements(DEFAULT_SCHEMA_NAME);
        resourceElements.put(SCHEMA, schemaValues);

        final RangerPolicyResource tableValue = getListOfPossibleValuesOfElements(context.getTable());
        resourceElements.put(TABLE, tableValue);

        if (context.getColumn() != null) {
            final RangerPolicyResource columnValue = getListOfPossibleValuesOfElements(context.getColumn());
            resourceElements.put(COLUMN, columnValue);
        }

        return resourceElements;
    }

    private RangerPolicyResource getListOfPossibleValuesOfElements(String value) {
        RangerPolicyResource resourceValue = new RangerPolicyResource();
        resourceValue.setValues(Collections.singletonList(value));
        return resourceValue;
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

    private String getRangerURL(String s) {
        String rangerBaseURL = connection.getEndpoint().getAddress();
        return MessageFormat.format(s, rangerBaseURL);
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = getBasicHTTPHeaders();

        if (connection != null && connection.getAdditionalProperties() != null
                && connection.getAdditionalProperties().containsKey(SECURITY_SERVER_AUTHORIZATION)
                && connection.getAdditionalProperties().get(SECURITY_SERVER_AUTHORIZATION).matches("[a-zA-Z0-9]++")) {
            headers.set("Authorization", connection.getAdditionalProperties().get(SECURITY_SERVER_AUTHORIZATION));
        }

        return headers;
    }

    private HttpHeaders getBasicHTTPHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RangerSecurityServiceConnector that = (RangerSecurityServiceConnector) o;
        return Objects.equals(connection, that.connection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), connection);
    }
}
