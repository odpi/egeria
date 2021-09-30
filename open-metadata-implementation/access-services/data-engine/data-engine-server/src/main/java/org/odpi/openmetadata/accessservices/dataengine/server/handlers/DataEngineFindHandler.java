/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.rest.FindRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.REFERENCEABLE_TYPE_GUID;


/**
 * Manages find operations for DataEngine related objects
 */
public class DataEngineFindHandler {

    private final InvalidParameterHandler invalidParameterHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final RepositoryHandler repositoryHandler;
    private final String serviceName;
    private final String serverName;
    private final DataEngineCommonHandler dataEngineCommonHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     * @param serviceName             service name
     * @param serverName              server name
     * @param dataEngineCommonHandler provides common Data Engine Omas utilities
     */
    public DataEngineFindHandler(InvalidParameterHandler invalidParameterHandler, OMRSRepositoryHelper repositoryHelper,
                                 RepositoryHandler repositoryHandler, String serviceName, String serverName,
                                 DataEngineCommonHandler dataEngineCommonHandler) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.dataEngineCommonHandler = dataEngineCommonHandler;
    }

    /**
     * Performs a find for a DataEngine related object
     *
     * @param userId             user id
     * @param findRequestBody  contains search criteria
     * @param methodName         method name
     *
     * @throws InvalidParameterException  if invalid parameters
     * @throws PropertyServerException    if errors in repository
     * @throws UserNotAuthorizedException if user not authorized
     */
    public GUIDListResponse find(FindRequestBody findRequestBody, String userId, String methodName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {

        validateParameters(findRequestBody, userId, methodName);

        GUIDListResponse searchResponse = new GUIDListResponse();

        String matchRegex = repositoryHelper.getExactMatchRegex(findRequestBody.getIdentifiers().getQualifiedName(), false);
        String typeGuid = getTypeGuid(userId, findRequestBody.getType());
        SearchProperties searchProperties = buildSearchProperties(userId, matchRegex);
        final String externalSourceName = findRequestBody.getIdentifiers().getExternalSourceName();

        List<EntityDetail> result = repositoryHandler.findEntities(userId, typeGuid, null, searchProperties,
                Collections.singletonList(InstanceStatus.ACTIVE), null, null, null,
                SequencingOrder.ANY, 0, 50, methodName);

        searchResponse.setGUIDs(result == null ? null : result.stream()
                .filter(ed -> externalSourceName == null || externalSourceName.contentEquals(ed.getMetadataCollectionName()))
                .map(EntityDetail::getGUID).collect(Collectors.toList()));

        return searchResponse;
    }

    private void validateParameters(FindRequestBody findRequestBody, String userId, String methodName)
            throws InvalidParameterException {

        invalidParameterHandler.validateName(findRequestBody.getIdentifiers().getQualifiedName(),
                "findRequestBody.identifiers.qualifiedName", methodName);
        invalidParameterHandler.validateUserId(userId, methodName);
    }

    private SearchProperties buildSearchProperties(String userId, String matchRegex){
        InstanceProperties instanceProperties = new InstanceProperties();

        PrimitivePropertyValue qualifiedNamePrimitivePropertyValue = new PrimitivePropertyValue();
        qualifiedNamePrimitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        qualifiedNamePrimitivePropertyValue.setPrimitiveValue(matchRegex);
        qualifiedNamePrimitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());
        qualifiedNamePrimitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());
        instanceProperties.setProperty(QUALIFIED_NAME_PROPERTY_NAME, qualifiedNamePrimitivePropertyValue );

        return repositoryHelper.getSearchPropertiesFromInstanceProperties(userId, instanceProperties, MatchCriteria.ALL);
    }

    private String getTypeGuid(String userId, String typeName){
        if(typeName == null){
            return REFERENCEABLE_TYPE_GUID;
        }
        TypeDef typeDef = repositoryHelper.getTypeDefByName(userId, typeName);
        return typeDef == null ? REFERENCEABLE_TYPE_GUID : typeDef.getGUID();
    }

}
