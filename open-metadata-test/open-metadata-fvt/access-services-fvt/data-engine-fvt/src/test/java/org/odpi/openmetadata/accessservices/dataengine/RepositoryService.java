/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine;

import org.odpi.openmetadata.repositoryservices.clients.LocalRepositoryServicesClient;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.rest.properties.EntityPropertyFindRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class aims to offer support for the FVT in regards to calling the LocalRepositoryServicesClient.
 * It contains calls to various LocalRepositoryServicesClient methods that retrieve the proxies names from lineage mappings,
 * that find relationships based on GUID, that find GUIDs based on qualified names or software sever capabilities
 * also based on qualified names.
 */
public class RepositoryService {

    private static final String QUALIFIED_NAME = "qualifiedName";

    private static final String TABULAR_COLUMN_TYPE_GUID = "d81a0425-4e9b-4f31-bc1c-e18c3566da10";
    private static final String SOFTWARE_SERVER_CAPABILITY_TYPE_GUID = "fe30a033-8f86-4d17-8986-e6166fa24177";
    private static final String LINEAGE_MAPPING_TYPE_GUID = "a5991bB2-660D-A3a1-2955-fAcDA2d5F4Ff";

    private static final int PAGE_SIZE = 100;

    private final String userId;
    private final LocalRepositoryServicesClient client;

    public RepositoryService(String serverName, String userId, String serverPlatformRootURL)
            throws InvalidParameterException {
        this.userId = userId;
        this.client = new LocalRepositoryServicesClient("repository", serverPlatformRootURL +
                "/servers/" + serverName);
    }

    /**
     * Given a list of relationships for the given attribute, the method calculates a list of qualified names indicating
     * the other proxies involved in lineage mapping relationships.
     *
     * @param lineageRelationships the lineage relationships correlated to the given attribute
     * @param currentAttribute     the attribute for which the lineage mapping proxies are calculated
     * @return a list of qualified names indicating the other proxies in the lineage mappings
     */
    public List<String> getLineageMappingsProxiesQualifiedNames(List<Relationship> lineageRelationships, String currentAttribute) {
        return lineageRelationships
                .stream()
                .map(lineage -> {
                    String entityTwo = lineage.getEntityTwoProxy().getUniqueProperties().getPropertyValue(QUALIFIED_NAME).valueAsString();
                    if (entityTwo.equals(currentAttribute)) {
                        return lineage.getEntityOneProxy().getUniqueProperties().getPropertyValue(QUALIFIED_NAME).valueAsString();
                    }
                    return entityTwo;
                })
                .collect(Collectors.toList());
    }

    /**
     * Given the GUID of an entity the method retrieves the relationships in which the entity is involved via a
     * LocalRepositoryServicesClient call.
     *
     * @param entityGUID the GUID of the entity
     * @return the list of relationships in which the entity is involved
     */
    public List<Relationship> findRelationshipsByGUID(String entityGUID) throws UserNotAuthorizedException, EntityNotKnownException,
            FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException,
            TypeErrorException, PagingErrorException {

        return client.getRelationshipsForEntity(userId, entityGUID, LINEAGE_MAPPING_TYPE_GUID, 0,
                null, null, null, SequencingOrder.ANY, PAGE_SIZE);
    }

    /**
     * Based on a searchCriteria the method searches for an entity GUID which has the qualified name equal to the search
     * criteria. The search is done through a LocalRepositoryServicesClient call.
     *
     * @param entityValue the search criteria used to search for a qualified name
     * @return the GUID of the first found object
     */
    public String findEntityGUIDByQualifiedName(String entityValue) throws UserNotAuthorizedException, FunctionNotSupportedException,
            InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException {
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

        List<EntityDetail> entityDetails = client.findEntitiesByProperty(userId, TABULAR_COLUMN_TYPE_GUID, matchProperties,
                MatchCriteria.ANY, 0, null, null, null, null, SequencingOrder.ANY, PAGE_SIZE);

        if (entityDetails == null || entityDetails.isEmpty()) {
            return "";
        }
        return entityDetails.get(0).getGUID();
    }

    /**
     * Find a software server capability by using the search criteria given as parameter in a call to the
     * LocalRepositoryServicesClient
     *
     * @param searchCriteria the property value used to search and identify the software server capability
     * @return a list of EntityDetails that contain the found software server capability
     */
    public List<EntityDetail> findSoftwareServerCapabilityByPropertyValue(String searchCriteria) throws UserNotAuthorizedException,
            FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException,
            TypeErrorException, PagingErrorException {

        return client.findEntitiesByPropertyValue(userId, SOFTWARE_SERVER_CAPABILITY_TYPE_GUID, searchCriteria, 0,
                null, null, null, null, SequencingOrder.ANY, PAGE_SIZE);
    }
}
