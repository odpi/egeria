/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.Context;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.GovernanceClassification;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.GovernedAsset;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model.RangerPolicyResource;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.model.RangerSecurityServicePolicies;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants.COLUMN;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants.DEFAULT_SCHEMA_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants.NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants.OPEN_METADATA_OWNER;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants.RANGER_CONNECTOR;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants.SCHEMA;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants.SECURITY_SERVER_AUTHORIZATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants.SECURITY_TAGS;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants.SERVICE_POLICIES;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants.SERVICE_TAGS;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants.SERVICE_TAGS_MAP_TAG_GUID_RESOURCE_GUI;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants.SERVICE_TAGS_RESOURCES;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants.SERVICE_TAGS_RESOURCE_BY_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants.SERVICE_TAGS_TAGDEF;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants.SERVICE_TAGS_TAG_RESOURCE_MAPS;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants.TABLE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.securitysync.rangerconnector.util.Constants.TAG_RESOURCE_ASSOCIATION;

public class RangerSecurityServiceConnector extends ConnectorBase implements SecurityServiceConnector {

    private static final Logger log = LoggerFactory.getLogger(RangerSecurityServiceConnector.class);
    private Connection connection;

    public RangerSecurityServiceConnector(Connection securityServerConnection) {
        this.connection = securityServerConnection;
    }

    @Override
    public void importTaggedResources(List<GovernedAsset> governedAssets) {
        Set<RangerTag> tags = new HashSet<>();
        List<RangerServiceResource> resources = new ArrayList<>();
        Map<String, Set<String>> tagToResource = buildResourceToTagsAssociationMap(governedAssets, tags, resources);

        createRangerTagDef();
        List<ResourceTagMapper> exitingAssociationResourceTags = getExistingAssociationResourceTags();
        if (exitingAssociationResourceTags.isEmpty()) {
            resources.forEach(this::createRangerServiceResource);
            tags.forEach(this::createRangerTag);
            tagToResource.forEach((key, value) -> value.forEach(x -> createAssociationResourceToSecurityTag(key, x)));
            return;
        }

        List<RangerServiceResource> existingResources = getExistingResources();
        Map<Long, RangerServiceResource> existingResourcesMap = mapResourceIds(existingResources);
        Set<RangerTag> rangerExistingTags = getExistingTags();
        Map<Long, RangerTag> existingTagsMap = mapTagIds(rangerExistingTags);

        Map<String, Set<String>> existingAssoc = mapResourceTagsById(exitingAssociationResourceTags, existingResourcesMap, existingTagsMap);

        if (tagToResource.isEmpty()) {
            existingAssoc.forEach((key, value) -> value.forEach(x -> deleteAssociationResourceToSecurityTagBasedOnIds(key, x)));
            return;
        }

        syncTags(tags, rangerExistingTags);
        syncResources(resources, existingResources);
        syncAssociations(tagToResource, existingAssoc);
    }

    @Override
    public RangerSecurityServicePolicies getSecurityServicePolicies(String serviceName, Long lastKnownVersion) {
        if (serviceName == null) {
            return null;
        }
        String servicePoliciesURL = MessageFormat.format(SERVICE_POLICIES, connection.getEndpoint().getAddress(), serviceName, lastKnownVersion);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(getHttpHeaders());

        try {
            ResponseEntity<RangerSecurityServicePolicies> result = restTemplate.exchange(servicePoliciesURL, HttpMethod.GET, entity, RangerSecurityServicePolicies.class);
            if (result.getStatusCode().value() == HttpStatus.OK.value()) {
                return result.getBody();
            } else if (result.getStatusCode().value() == HttpStatus.NOT_MODIFIED.value()) {
                log.debug("Policies list not modified since last known version {}", lastKnownVersion);
                return null;
            }
            return result.getBody();
        } catch (HttpStatusCodeException exception) {
            log.debug("Unable to fetch the security service policies for service = {} with last known version {}", serviceName, lastKnownVersion);
        }
        return null;
    }

    @Override
    public RangerServiceResource createResource(GovernedAsset governedAsset) {
        RangerServiceResource serviceResource = buildRangerResource(governedAsset);
        return createRangerServiceResource(serviceResource);
    }

    private RangerServiceResource createRangerServiceResource(RangerServiceResource resource) {
        String createAssociation = getRangerURL(SERVICE_TAGS_RESOURCES);

        String body = getBody(resource);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(body, getHttpHeaders());

        try {
            ResponseEntity<RangerServiceResource> result = restTemplate.exchange(createAssociation, HttpMethod.POST, entity, RangerServiceResource.class);
            return result.getBody();
        } catch (HttpStatusCodeException exception) {
            log.debug("Unable to create the resource {}", resource);
        }
        return null;
    }

    @Override
    public RangerServiceResource getResourceByGUID(String resourceGuid) {
        String resourceURL = getRangerURL(SERVICE_TAGS_RESOURCE_BY_GUID, resourceGuid);

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
    public void deleteResource(String resourceGuid) {
        String resourceURL = getRangerURL(SERVICE_TAGS_RESOURCE_BY_GUID, resourceGuid);
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
    public List<RangerTag> createSecurityTags(GovernanceClassification classification) {

        if (classification.getSecurityLabels() == null || classification.getSecurityLabels().isEmpty()) {
            return Collections.emptyList();
        }

        List<RangerTag> rangerTags = new ArrayList<>(classification.getSecurityLabels().size());
        for (String securityTag : classification.getSecurityLabels()) {
            RangerTag rangerTag = buildRangerTag(securityTag, classification.getSecurityProperties());
            rangerTags.add(createRangerTag(rangerTag));
        }

        return rangerTags;
    }

    @Override
    public List<ResourceTagMapper> getTagsAssociatedWithTheResource(Long id) {
        List<ResourceTagMapper> mapper = getExistingAssociationResourceTags();
        if (mapper == null) {
            return Collections.emptyList();
        }

        return mapper.stream().filter(resourceTagMapper -> resourceTagMapper.getResourceId().equals(id)).collect(Collectors.toList());
    }


    @Override
    public ResourceTagMapper createAssociationResourceToSecurityTag(String resourceGUID, String tagGUID) {
        String rangerBaseURL = connection.getEndpoint().getAddress();
        String createAssociation = MessageFormat.format(SERVICE_TAGS_MAP_TAG_GUID_RESOURCE_GUI, rangerBaseURL, tagGUID, resourceGUID);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(getHttpHeaders());
        try {
            ResponseEntity<ResourceTagMapper> result = restTemplate.exchange(createAssociation, HttpMethod.POST, entity, ResourceTagMapper.class);
            return result.getBody();
        } catch (HttpStatusCodeException exception) {
            log.debug("Unable to create the association between tag {} and resource {}", tagGUID, resourceGUID);
        }
        return null;
    }

    @Override
    public void deleteAssociationResourceToSecurityTag(ResourceTagMapper resourceTagMapper) {
        String rangerBaseURL = connection.getEndpoint().getAddress();
        String deleteAssociationURL = MessageFormat.format(TAG_RESOURCE_ASSOCIATION, rangerBaseURL, resourceTagMapper.getId());

        Boolean isDeleted = doDelete(deleteAssociationURL);
        if (isDeleted) {
            log.info("The association with id {} between tag {} and resource {} has been removed", resourceTagMapper.getId(), resourceTagMapper.getTagId(), resourceTagMapper.getResourceId());
        } else {
            log.info("Unable to delete the association with id = {} between tag {} and resource {}", resourceTagMapper.getId(), resourceTagMapper.getTagId(), resourceTagMapper.getResourceId());
        }
    }

    private void deleteAssociationResourceToSecurityTagBasedOnIds(String resourceGUID, String tagGUID) {
        String rangerBaseURL = connection.getEndpoint().getAddress();
        String deleteURLByGUIDs = MessageFormat.format(SERVICE_TAGS_MAP_TAG_GUID_RESOURCE_GUI, rangerBaseURL, tagGUID, resourceGUID);

        Boolean isDeleted = doDelete(deleteURLByGUIDs);
        if (isDeleted) {
            log.debug("The association between tag {} and resoure {} has been deleted", tagGUID, resourceGUID);
        } else {
            log.debug("Unable to delete the association between tag {} and resource {}", tagGUID, resourceGUID);
        }
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

    private RangerTag createRangerTag(RangerTag rangerTag) {
        String createTagURL = getRangerURL(SERVICE_TAGS);
        String body = getBody(rangerTag);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(body, getHttpHeaders());

        try {
            restTemplate.exchange(createTagURL, HttpMethod.POST, entity, RangerTag.class);
            return rangerTag;
        } catch (HttpStatusCodeException exception) {
            log.debug("Unable to create a security tag {}", rangerTag);
        }
        return rangerTag;
    }

    private RangerTagDef buildRangerTagDef() {
        RangerTagDef rangerTagDef = new RangerTagDef();
        rangerTagDef.setId(1L);
        rangerTagDef.setCreatedBy(RANGER_CONNECTOR);
        rangerTagDef.setName(SECURITY_TAGS);

        return rangerTagDef;
    }

    private RangerServiceResource buildRangerResource(GovernedAsset governedAsset) {
        RangerServiceResource serviceResource = new RangerServiceResource();

        serviceResource.setGuid(governedAsset.getGuid());
        serviceResource.setServiceName(DEFAULT_SCHEMA_NAME);
        serviceResource.setCreatedBy(RANGER_CONNECTOR);
        Map<String, RangerPolicyResource> resourceElements = getRangerPolicyResourceMap(governedAsset.getContext());
        serviceResource.setResourceElements(resourceElements);

        return serviceResource;
    }

    private RangerTag buildRangerTag(String tagGUID, Map<String, String> tagAttributes) {
        RangerTag tag = new RangerTag();

        tag.setCreatedBy(RANGER_CONNECTOR);
        tag.setType(SECURITY_TAGS);
        tag.setOwner(OPEN_METADATA_OWNER);
        tag.setGuid(tagGUID);

        if (tagAttributes == null) {
            tagAttributes = new HashMap<>();
        }

        tagAttributes.put(NAME, tagGUID);
        tag.setAttributes(tagAttributes);

        return tag;
    }


    private List<ResourceTagMapper> getExistingAssociationResourceTags() {
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


    private Map<String, RangerPolicyResource> getRangerPolicyResourceMap(Context context) {
        Map<String, RangerPolicyResource> resourceElements = new HashMap<>(3);
        final RangerPolicyResource schemaValues = getListOfPossibleValuesOfElements(DEFAULT_SCHEMA_NAME);
        resourceElements.put(SCHEMA, schemaValues);

        if (context == null) {
            return resourceElements;
        }

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

    public List<RangerServiceResource> getExistingResources() {
        String createAssociation = getRangerURL(SERVICE_TAGS_RESOURCES);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(getHttpHeaders());
        try {
            ResponseEntity<List<RangerServiceResource>> response =
                    restTemplate.exchange(createAssociation, HttpMethod.GET, entity, new ParameterizedTypeReference<List<RangerServiceResource>>() {
                    });

            if (response.getBody() != null) {
                return response.getBody();
            }
        } catch (HttpStatusCodeException exception) {
            log.debug("Unable to fetch the resources");
        }

        return Collections.emptyList();
    }

    private Set<RangerTag> getExistingTags() {
        String createTagURL = getRangerURL(SERVICE_TAGS);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(getHttpHeaders());

        try {
            ResponseEntity<List<RangerTag>> response =
                    restTemplate.exchange(createTagURL, HttpMethod.GET, entity, new ParameterizedTypeReference<List<RangerTag>>() {
                    });
            if (response.getBody() != null) {
                return new HashSet<>(response.getBody());
            }
        } catch (HttpStatusCodeException exception) {
            log.debug("Unable to get the security tags");
        }
        return Collections.emptySet();
    }


    private Map<String, Set<String>> buildResourceToTagsAssociationMap(List<GovernedAsset> governedAssets, Set<RangerTag> tags, List<RangerServiceResource> resources) {
        Map<String, Set<String>> tagToResource = new HashMap<>();

        for (GovernedAsset governedAsset : governedAssets) {

            if (governedAsset.getAssignedGovernanceClassification() == null
                    || governedAsset.getAssignedGovernanceClassification().getSecurityLabels() == null
                    || governedAsset.getAssignedGovernanceClassification().getSecurityLabels().isEmpty()) {
                continue;
            }

            RangerServiceResource resource = buildRangerResource(governedAsset);
            resources.add(resource);

            GovernanceClassification governanceClassification = governedAsset.getAssignedGovernanceClassification();
            for (String securityLabel : governanceClassification.getSecurityLabels()) {
                RangerTag rangerTag = buildRangerTag(securityLabel, governanceClassification.getSecurityProperties());
                tags.add(rangerTag);

                addTag(tagToResource, resource, rangerTag);
            }
        }
        return tagToResource;
    }

    private void addTag(Map<String, Set<String>> tagToResource, RangerServiceResource resource, RangerTag rangerTag) {
        if (tagToResource.containsKey(resource.getGuid())) {
            tagToResource.get(resource.getGuid()).add(rangerTag.getGuid());
        } else {
            Set<String> securityTags = new HashSet<>();
            securityTags.add(rangerTag.getGuid());
            tagToResource.put(resource.getGuid(), securityTags);
        }
    }

    private void syncResources(List<RangerServiceResource> resources, List<RangerServiceResource> existingResources) {
        Collection<RangerServiceResource> newResources = CollectionUtils.subtract(resources, existingResources);
        newResources.forEach(this::createRangerServiceResource);
    }

    private void syncTags(Set<RangerTag> tags, Set<RangerTag> rangerExistingTags) {
        Collection<RangerTag> newTags = CollectionUtils.subtract(tags, rangerExistingTags);
        newTags.forEach(this::createRangerTag);
    }

    private void syncAssociations(Map<String, Set<String>> tagToResource, Map<String, Set<String>> existingMapping) {
        Map<String, List<String>> newMappings = new HashMap<>();
        Map<String, List<String>> outdatedMapping = new HashMap<>();

        for (Map.Entry<String, Set<String>> tags : tagToResource.entrySet()) {
            Set<String> existingTags = existingMapping.get(tags.getKey());
            if (existingTags == null) {
                newMappings.put(tags.getKey(), new ArrayList<>(tags.getValue()));
                continue;
            }

            Collection<String> newlyAdded = CollectionUtils.subtract(tags.getValue(), existingTags);
            if (!newlyAdded.isEmpty()) {
                newMappings.put(tags.getKey(), (List<String>) newlyAdded);
            }

            Collection<String> outdatedTags = CollectionUtils.subtract(existingTags, tags.getValue());
            if (!outdatedTags.isEmpty()) {
                outdatedMapping.put(tags.getKey(), (List<String>) outdatedTags);
            }
        }

        newMappings.forEach((resourceId, tags) -> tags.forEach(tagId -> createAssociationResourceToSecurityTag(resourceId, tagId)));
        outdatedMapping.forEach((resourceId, tags) -> tags.forEach(tag -> deleteAssociationResourceToSecurityTagBasedOnIds(resourceId, tag)));
    }

    private Map<Long, RangerTag> mapTagIds(Set<RangerTag> tags) {
        return tags.stream().collect(Collectors.toMap(RangerTag::getId, Function.identity()));
    }

    private Map<Long, RangerServiceResource> mapResourceIds(List<RangerServiceResource> resources) {
        return resources.stream().collect(Collectors.toMap(RangerServiceResource::getId, Function.identity()));
    }

    private Map<String, Set<String>> mapResourceTagsById(List<ResourceTagMapper> exitingAssociationResourceTags,
                                                         Map<Long, RangerServiceResource> existingResourcesMap, Map<Long, RangerTag> existingTagsMap) {

        Map<String, Set<String>> existingAssoc = new HashMap<>();

        for (ResourceTagMapper mapper : exitingAssociationResourceTags) {
            RangerServiceResource resource = existingResourcesMap.get(mapper.getResourceId());
            RangerTag rangerTag = existingTagsMap.get(mapper.getTagId());
            addTag(existingAssoc, resource, rangerTag);
        }

        return existingAssoc;
    }

    private String getRangerURL(String s, Object... params) {
        String rangerBaseURL = connection.getEndpoint().getAddress();
        return MessageFormat.format(s, rangerBaseURL, params);
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

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = getBasicHTTPHeaders();

        if (connection != null && connection.getConfigurationProperties() != null
                && connection.getConfigurationProperties().containsKey(SECURITY_SERVER_AUTHORIZATION)) {
            headers.set("Authorization", (String) connection.getConfigurationProperties().get(SECURITY_SERVER_AUTHORIZATION));
        }

        return headers;
    }

    private HttpHeaders getBasicHTTPHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    private Boolean doDelete(String deleteAssociationURL) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHttpHeaders();
        headers.add("X-HTTP-Method-Override", "DELETE");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(deleteAssociationURL, HttpMethod.DELETE, entity, Void.class);
        } catch (HttpStatusCodeException exception) {
            log.debug("Unable to doDelete the association between tag and resource");
            return false;
        }
        return true;
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