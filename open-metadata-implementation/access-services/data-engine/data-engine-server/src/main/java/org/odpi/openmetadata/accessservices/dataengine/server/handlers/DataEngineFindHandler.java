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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
     * Performs a find for a DataEngine related object. External repositories are included
     *
     * @param userId           user id
     * @param findRequestBody  contains search criteria
     * @param methodName       method name
     *
     * @throws InvalidParameterException     if invalid parameters
     * @throws PropertyServerException       if errors in repository
     * @throws UserNotAuthorizedException    if user not authorized
     * @throws FunctionNotSupportedException if function not supported
     * @throws RepositoryErrorException      if error in repository
     * @throws PropertyErrorException        if a property does not match
     * @throws TypeErrorException            if type is unknown
     * @throws PagingErrorException          if paging is erroneously defined
     */
    public GUIDListResponse find(FindRequestBody findRequestBody, String userId, String methodName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException {

        validateParameters(findRequestBody, userId, methodName);

        GUIDListResponse searchResponse = new GUIDListResponse();

        String matchRegex = repositoryHelper.getExactMatchRegex(findRequestBody.getIdentifiers().getQualifiedName(), false);
        String typeGuid = getTypeGuid(userId, findRequestBody.getType());
        InstanceProperties instanceProperties = buildInstanceProperties(userId, matchRegex);
        final String externalSourceName = findRequestBody.getExternalSourceName();

        List<EntityDetail> result = repositoryHandler.getMetadataCollection().findEntitiesByProperty(userId, typeGuid,
                instanceProperties, MatchCriteria.ANY, 0, Collections.singletonList(InstanceStatus.ACTIVE),
                null, null, null, SequencingOrder.ANY, 50);

        if(!Objects.isNull(result)){
            List<String> guids = result.stream()
                    .filter(ed -> Objects.isNull(externalSourceName) || externalSourceName.contentEquals(ed.getMetadataCollectionName()))
                    .map(EntityDetail::getGUID).collect(Collectors.toList());
            searchResponse.setGUIDs(guids);
        }

        return searchResponse;
    }

    private void validateParameters(FindRequestBody findRequestBody, String userId, String methodName)
            throws InvalidParameterException {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(findRequestBody, "findRequestBody", methodName);
        invalidParameterHandler.validateObject(findRequestBody.getIdentifiers(), "findRequestBody.identifiers", methodName);
        invalidParameterHandler.validateName(findRequestBody.getIdentifiers().getQualifiedName(),
                "findRequestBody.identifiers.qualifiedName", methodName);
    }

    private InstanceProperties buildInstanceProperties(String userId, String matchRegex){
        InstanceProperties instanceProperties = new InstanceProperties();

        PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        primitivePropertyValue.setPrimitiveValue(matchRegex);
        primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());
        primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());

        instanceProperties.setProperty(QUALIFIED_NAME_PROPERTY_NAME, primitivePropertyValue);

        return instanceProperties;
    }

    private String getTypeGuid(String userId, String typeName){
        if(typeName == null){
            return REFERENCEABLE_TYPE_GUID;
        }
        TypeDef typeDef = repositoryHelper.getTypeDefByName(userId, typeName);
        return typeDef == null ? REFERENCEABLE_TYPE_GUID : typeDef.getGUID();
    }

}
