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
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.*;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants.*;

public class RangerSecurityServiceConnector extends ConnectorBase implements SecurityServiceConnector {

    private static final Logger log = LoggerFactory.getLogger(RangerSecurityServiceConnector.class);
    private Connection connection;

    public RangerSecurityServiceConnector(Connection securityServerConnection) {
        this.connection = securityServerConnection;
    }

    @Override
    public void importTaggedResources(List<GovernedAsset> governedAssets) {
        Map<String, RangerTag> rangerTagMap = new HashMap<>();
        Map<Long, RangerTag> tags = new HashMap<>();
        long resourceIndex = 0L;
        long tagIndex = 0L;

        List<RangerServiceResource> resources = new ArrayList<>();
        Map<Long, List<Long>> resourceToTagIds = new HashMap<>();

        for (GovernedAsset governedAsset : governedAssets) {

            if (governedAsset.getAssignedGovernanceClassifications().isEmpty()) {
                continue;
            }

            GovernanceClassification classification = governedAsset.getAssignedGovernanceClassifications().get(0);
            String tagName = classification.getAttributes().get(LEVEL);
            RangerTag rangerTag;
            Long tagNo = tagIndex;
            if (rangerTagMap.containsKey(tagName)) {
                rangerTag = rangerTagMap.get(tagName);
                for (Map.Entry<Long, RangerTag> rangerTagEntry : tags.entrySet()) {
                    if (rangerTagEntry.getValue().equals(rangerTag)) {
                        tagNo = rangerTagEntry.getKey();
                    }
                }
            } else {
                rangerTag = buildTag(classification);
                rangerTagMap.put(tagName, rangerTag);
                tags.put(tagIndex++, rangerTag);
            }

            List<RangerServiceResource> rangerServiceResources = new ArrayList<>(governedAsset.getContexts().size());
            for (Context context : governedAsset.getContexts()) {
                Map<String, RangerPolicyResource> resourceMap = getRangerPolicyResourceMap(context);
                RangerServiceResource serviceResource = getRangerServiceResource(resourceMap, governedAsset.getGuid(), resourceIndex++);
                rangerServiceResources.add(serviceResource);
            }

            resources.addAll(rangerServiceResources);

            for (RangerServiceResource resource : rangerServiceResources) {
                resourceToTagIds.putAll(Collections.singletonMap(resource.getId(), Collections.singletonList(tagNo)));
            }
        }

        RangerResource rangerResource = buildRangerResource(resources, tags, resourceToTagIds);
        importTaggedResources(rangerResource);
    }

    @Override
    public void createAssociationResourceToSecurityTag(GovernedAsset asset) {
        RangerResource rangerResource = processGovernedAsset(asset);

        importTaggedResources(rangerResource);
    }

    @Override
    public ResourceTagMapper createAssociationResourceToSecurityTag(String tagGUID, String resourceGUID) {
        String rangerBaseURL = connection.getEndpoint().getURL();
        String createAssociation = MessageFormat.format("{0}/service/tags/tagresourcemaps?tag-guid={1}&resource-guid={2}", rangerBaseURL, tagGUID, resourceGUID);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(getHttpHeaders());
        try {
            ResponseEntity<ResourceTagMapper> result = restTemplate.exchange(createAssociation, HttpMethod.POST, entity, ResourceTagMapper.class);
            return result.getBody();
        } catch (HttpStatusCodeException exception) {
            log.error(exception.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAssociationResourceToSecurityTag(ResourceTagMapper resourceTagMapper) {
        String rangerBaseURL = connection.getEndpoint().getURL();
        String deleteAssociationURL = MessageFormat.format("{0}/service/tags/tagresourcemap/{1}", rangerBaseURL, resourceTagMapper.getId());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            restTemplate.delete(deleteAssociationURL, HttpMethod.DELETE, entity);
        } catch (HttpStatusCodeException exception) {
            log.error(exception.getMessage());
        }
    }

    @Override
    public void createSecurityTag(GovernanceClassification classification) {
        final RangerTag rangerTag = buildTag(classification);

        String body = getBody(rangerTag);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(body, getHttpHeaders());

        try {
            String createTagURL = getRangerURL("{0}/service/tags/");
            restTemplate.exchange(createTagURL, HttpMethod.POST, entity, String.class);
        } catch (HttpStatusCodeException exception) {
            log.error(exception.getMessage());
        }
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
    public List<RangerTag> getSecurityTags() {
        String allMappedResources = getRangerURL(SERVICE_ALL_TAGS);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(getHttpHeaders());

        try {
            ResponseEntity<List<RangerTag>> response = restTemplate.exchange(allMappedResources, HttpMethod.GET, entity, new ParameterizedTypeReference<List<RangerTag>>() {
            });
            return response.getBody();
        } catch (HttpStatusCodeException exception) {
            log.error(exception.getMessage());
        }
        return Collections.emptyList();
    }

    private RangerResource processGovernedAsset(GovernedAsset governedAsset) {
        Map<Long, RangerTag> tags = buildTags(governedAsset.getAssignedGovernanceClassifications());

        List<RangerServiceResource> rangerServiceResources = getRangerServiceResources(governedAsset);
        Map<Long, List<Long>> resourceToTagIds = mapResources(tags, rangerServiceResources);

        return buildRangerResource(rangerServiceResources, tags, resourceToTagIds);
    }

    private RangerResource buildRangerResource(List<RangerServiceResource> rangerServiceResource,
                                               Map<Long, RangerTag> tags,
                                               Map<Long, List<Long>> resourceToTagIds) {
        RangerResource rangerResource = new RangerResource();

        rangerResource.setTags(tags);

        Map<Long, RangerTagDef> rangerTagDef = createRangerTagDef();
        rangerResource.setTagDefinitions(rangerTagDef);

        rangerResource.setServiceResources(rangerServiceResource);
        rangerResource.setResourceToTagIds(resourceToTagIds);
        rangerResource.setOp(ADD_OR_UPDATE);
        if (connection.getAdditionalProperties() != null && connection.getAdditionalProperties().get("tagServiceName") != null) {
            rangerResource.setServiceName((String) connection.getAdditionalProperties().get("tagServiceName"));
        }
        rangerResource.setTagVersion(1L);

        return rangerResource;
    }

    private Map<Long, RangerTagDef> createRangerTagDef() {
        Map<Long, RangerTagDef> tagDefinitions = new HashMap<>();
        RangerTagDef rangerTagDef = new RangerTagDef();
        rangerTagDef.setId(1L);
        rangerTagDef.setCreatedBy(RANGER_CONNECTOR);
        rangerTagDef.setName(CONFIDENTIALITY);

        tagDefinitions.put(1L, rangerTagDef);
        return tagDefinitions;
    }

    private List<RangerServiceResource> getRangerServiceResources(GovernedAsset governedAsset) {
        Long resourceId = 0L;
        List<RangerServiceResource> rangerServiceResources = new ArrayList<>(governedAsset.getContexts().size());

        for (Context context : governedAsset.getContexts()) {
            Map<String, RangerPolicyResource> resourceMap = getRangerPolicyResourceMap(context);
            RangerServiceResource serviceResource = getRangerServiceResource(resourceMap, governedAsset.getGuid(), resourceId++);
            rangerServiceResources.add(serviceResource);
        }
        return rangerServiceResources;
    }

    private Map<Long, List<Long>> mapResourcesToTags(Long rangerServiceResourceId, List<Long> tagList) {
        Map<Long, List<Long>> resourceToTagIds = new HashMap<>();
        resourceToTagIds.put(rangerServiceResourceId, tagList);

        return resourceToTagIds;
    }

    private Map<Long, List<Long>> mapResources(Map<Long, RangerTag> tags, List<RangerServiceResource> rangerServiceResources) {
        Map<Long, List<Long>> resourceToTagIds = new HashMap<>();
        List<Long> tagList = new ArrayList<>(tags.keySet());

        for (RangerServiceResource rangerServiceResource : rangerServiceResources) {
            resourceToTagIds.putAll(mapResourcesToTags(rangerServiceResource.getId(), tagList));
        }

        return resourceToTagIds;
    }

    public RangerServiceResource getResourceByGUID(String resourceGuid) {
        String rangerBaseURL = connection.getEndpoint().getURL();
        String resourceURL = MessageFormat.format(SERVICE_TAGS_RESOURCE_BY_GUID, rangerBaseURL, resourceGuid);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(getHttpHeaders());

        try {
            ResponseEntity<RangerServiceResource> result = restTemplate.exchange(resourceURL, HttpMethod.GET, entity, RangerServiceResource.class);
            return result.getBody();
        } catch (HttpStatusCodeException exception) {
            log.error(exception.getMessage());
        }
        return null;
    }

    private void importTaggedResources(RangerResource resource) {
        String body = getBody(resource);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(body, getHttpHeaders());

        try {
            String url = getRangerURL(SERVICE_TAGS_IMPORT_SERVICE_TAGS);
            restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
        } catch (HttpStatusCodeException exception) {
            log.error(exception.getMessage());
        }
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
            log.error(exception.getMessage());
        }
        return Collections.emptyList();
    }

    private Map<Long, RangerTag> buildTags(List<GovernanceClassification> classifications) {
        Map<Long, RangerTag> tags = new HashMap<>(classifications.size());
        long index = 0L;

        for (GovernanceClassification classification : classifications) {
            final RangerTag rangerTag = buildTag(classification);
            tags.put(index++, rangerTag);
        }

        return tags;
    }

    private RangerTag buildTag(GovernanceClassification classification) {
        RangerTag tag = new RangerTag();

        tag.setCreatedBy(RANGER_CONNECTOR);
        tag.setType(CONFIDENTIALITY);
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

    private RangerServiceResource getRangerServiceResource(Map<String, RangerPolicyResource> resourceElements, String guid, Long resourceId) {
        RangerServiceResource serviceResource = new RangerServiceResource();

        serviceResource.setId(resourceId);
        serviceResource.setGuid(guid);
        serviceResource.setServiceName(DEFAULT_SCHEMA_NAME);
        serviceResource.setCreatedBy(RANGER_CONNECTOR);
        serviceResource.setResourceElements(resourceElements);

        return serviceResource;
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
        String rangerBaseURL = connection.getEndpoint().getURL();
        return MessageFormat.format(s, rangerBaseURL);
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = getBasicHTTPHeaders();
        if (connection != null && connection.getAdditionalProperties() != null
                && connection.getAdditionalProperties().containsKey("securityServerAuthorization")) {
            headers.set("Authorization", (String) connection.getAdditionalProperties().get("securityServerAuthorization"));
        }
        return headers;
    }

    private HttpHeaders getBasicHTTPHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

}
