/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.rest.properties.EntityListResponse;
import org.odpi.openmetadata.repositoryservices.rest.properties.EntityPropertyFindRequest;
import org.odpi.openmetadata.repositoryservices.rest.properties.RelationshipListResponse;
import org.odpi.openmetadata.repositoryservices.rest.properties.TypeLimitedFindRequest;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.http.ssl.SSLContexts.custom;

/**
 * This class aims to offer support for the FVT in regards to calling the platform servers that are started on the machine.
 * It contains REST calls to various endpoints that retrieve the proxies names from lineage mappings, that find relationships based on
 * GUID, that find GUIDs based on qualified names or software sever capabilities also based on qualified names.
 */
public class RepositoryService {

    private static final String QUALIFIED_NAME = "qualifiedName";
    private static final String TABULAR_COLUMN_TYPE_GUID = "d81a0425-4e9b-4f31-bc1c-e18c3566da10";
    private static final String SOFTWARE_SERVER_CAPABILITY_TYPE_GUID = "fe30a033-8f86-4d17-8986-e6166fa24177";
    private static final String URL_TEMPLATE_GET_ENTITIES_BY_PROPERTY = "%s/servers/%s/open-metadata/repository-services/users/%s/instances/entities/by-property";
    private static final String URL_TEMPLATE_GET_RELATIONSHIPS_BY_GUID = "%s/servers/%s/open-metadata/repository-services/users/%s/instances/entity/%s/relationships";
    private static final String URL_TEMPLATE_GET_SOFTWARE_SERVER_CAPABILITY_BY_SEARCH_CRITERIA = "%s/servers/%s/open-metadata/repository-services/users/%s/instances/entities/by-property-value?searchCriteria=%s";
    private static final int PAGE_SIZE = 100;

    private final String serverName;
    private final String userId;
    private final String serverPlatformRootURL;

    private RestTemplate restTemplate;

    public RepositoryService(String serverName, String userId, String serverPlatformRootURL) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        this.serverName = serverName;
        this.userId = userId;
        this.serverPlatformRootURL = serverPlatformRootURL;

        ignoreCertificateForRestTemplate();
    }


    /** Given a list of relationships for the given attribute, the method calculates a list of qualified names indicating
     * the other proxies involved only in lineage mapping relationships.
     * @param relationships the relationships correlated to the given attribute
     * @param currentAttribute the attribute for which the lineage mapping proxies are calculated
     * @return a list of qualified names indicating the other proxies in the lineage mappings
     */
    public List<String> getLineageMappingsProxiesQualifiedNames(List<Relationship> relationships, String currentAttribute) {
        return relationships
                .stream()
                .filter(relationship -> relationship.getType().getTypeDefName().equals(LineageMapping.class.getSimpleName()))
                .map(relationship ->  {
                    String entityTwo = relationship.getEntityTwoProxy().getUniqueProperties().getPropertyValue(QUALIFIED_NAME).valueAsString();
                    if (entityTwo.equals(currentAttribute)) {
                        return relationship.getEntityOneProxy().getUniqueProperties().getPropertyValue(QUALIFIED_NAME).valueAsString();
                    }
                    return entityTwo;
                })
                .collect(Collectors.toList());
    }

    /** Given the GUID of an entity the method retrieves the relationships in which the entity is involved via a REST
     * call.
     * @param entityGUID the GUID of the entity
     * @return the list of relationships in which the entity is involved
     */
    public List<Relationship> findRelationshipsByGUID(String entityGUID) {
        String urlTemplate = String.format(URL_TEMPLATE_GET_RELATIONSHIPS_BY_GUID, serverPlatformRootURL, serverName, userId, entityGUID);
        TypeLimitedFindRequest findRequestParameters = new TypeLimitedFindRequest();
        findRequestParameters.setPageSize(PAGE_SIZE);
        RelationshipListResponse response = restTemplate.postForObject(urlTemplate, findRequestParameters, RelationshipListResponse.class, userId);
        List<Relationship> relationships = new ArrayList<>();
        if (response != null) {
            relationships = response.getRelationships();
        }
        return relationships;
    }

    /** Based on a searchCriteria the method searches for an entity GUID which has the qualified name equal to the search
     * criteria. The search is done through a REST call.
     * @param entityValue the search criteria used to search for a qualified name
     * @return the GUID of the first found object
     */
    public String findEntityGUIDByQualifiedName(String entityValue) {
        InstanceProperties matchProperties = new InstanceProperties();

        PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        primitivePropertyValue.setPrimitiveValue(entityValue);

        Map<String, InstancePropertyValue> propertiesMap = new HashMap<>();
        propertiesMap.put(QUALIFIED_NAME, primitivePropertyValue);
        matchProperties.setInstanceProperties(propertiesMap);

        EntityPropertyFindRequest findRequestParameters = new EntityPropertyFindRequest();
        findRequestParameters.setTypeGUID(TABULAR_COLUMN_TYPE_GUID);
        findRequestParameters.setMatchProperties(matchProperties);
        findRequestParameters.setMatchCriteria(MatchCriteria.ANY);
        findRequestParameters.setPageSize(PAGE_SIZE);

        String urlTemplate = String.format(URL_TEMPLATE_GET_ENTITIES_BY_PROPERTY, serverPlatformRootURL, serverName, userId);

        EntityListResponse response = restTemplate.postForObject(urlTemplate, findRequestParameters,
                EntityListResponse.class, userId);

        List<EntityDetail> entityDetails = null;
        if (response != null) {
            entityDetails = response.getEntities();
        }
        if(entityDetails == null || entityDetails.isEmpty()) {
            return "";
        }
        return entityDetails.get(0).getGUID();
    }

    /** Find a software server capability by using the search criteria given as parameter in a specific REST call
     * @param searchCriteria the property value used to search and identify the software server capability
     * @return and Optional result that may contain the found software server capability as an EntityDetail
     */
    public Optional<EntityDetail> findSoftwareServerCapabilityByPropertyValue(String searchCriteria) {
        String urlTemplate = String.format(URL_TEMPLATE_GET_SOFTWARE_SERVER_CAPABILITY_BY_SEARCH_CRITERIA,
                serverPlatformRootURL, serverName, userId, searchCriteria);
        EntityPropertyFindRequest entityPropertyFindRequest = new EntityPropertyFindRequest();
        entityPropertyFindRequest.setPageSize(PAGE_SIZE);
        EntityListResponse response = restTemplate.postForObject(urlTemplate, entityPropertyFindRequest,
                EntityListResponse.class, userId);

        List<EntityDetail> entityDetails = null;
        if (response != null) {
            entityDetails = response.getEntities();
        }

        if(entityDetails == null || entityDetails.isEmpty()) {
            return Optional.empty();
        }

        return entityDetails
                .stream()
                .filter(entityDetail -> entityDetail.getType().getTypeDefGUID().equals(SOFTWARE_SERVER_CAPABILITY_TYPE_GUID))
                .findFirst();
    }

    private void ignoreCertificateForRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        SSLContext sslContext = custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        restTemplate = new RestTemplate(requestFactory);
    }
}
