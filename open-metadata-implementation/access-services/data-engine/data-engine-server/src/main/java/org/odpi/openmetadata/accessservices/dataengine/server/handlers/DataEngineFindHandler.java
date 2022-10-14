/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.model.Referenceable;
import org.odpi.openmetadata.accessservices.dataengine.rest.FindRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
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
    private final OpenMetadataAPIGenericHandler<Referenceable> genericHandler;
    private final DataEngineCommonHandler dataEngineCommonHandler;
    private final String serviceName;
    private final String serverName;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     * @param genericHandler          generic handler that provides utilities to manipulate entities
     * @param serviceName             service name
     * @param serverName              server name
     */
    public DataEngineFindHandler(InvalidParameterHandler invalidParameterHandler, OMRSRepositoryHelper repositoryHelper,
                                 OpenMetadataAPIGenericHandler<Referenceable> genericHandler,
                                 DataEngineCommonHandler dataEngineCommonHandler, String serviceName, String serverName) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.genericHandler = genericHandler;
        this.dataEngineCommonHandler = dataEngineCommonHandler;
        this.serviceName = serviceName;
        this.serverName = serverName;
    }

    /**
     * Performs a find for a DataEngine related object. External repositories are included
     *
     * @param userId           user id
     * @param findRequestBody  contains search criteria
     * @param methodName       method name
     *
     * @return a list of guids
     *
     * @throws InvalidParameterException     if invalid parameters
     * @throws PropertyServerException       if errors in repository
     * @throws UserNotAuthorizedException    if user not authorized
     * @throws FunctionNotSupportedException if function not supported
     * @throws RepositoryErrorException      if error in repository
     * @throws PropertyErrorException        if a property does not match
     * @throws TypeErrorException            if type is unknown
     * @throws PagingErrorException          if paging is erroneously defined
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException if user not authorized
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException if invalid parameters
     */
    public GUIDListResponse find(FindRequestBody findRequestBody, String userId, String methodName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException {

        validateParameters(findRequestBody, userId, methodName);

        GUIDListResponse searchResponse = new GUIDListResponse();

        String typeGuid = getTypeGuid(userId, findRequestBody.getType());
        final String externalSourceName = findRequestBody.getExternalSourceName();

        List<EntityDetail> result = genericHandler.getEntitiesByValue(userId, findRequestBody.getIdentifiers().getQualifiedName(),
                QUALIFIED_NAME_PROPERTY_NAME, typeGuid, findRequestBody.getType(), Collections.singletonList(QUALIFIED_NAME_PROPERTY_NAME),
                true, null, null, false, false,
                0, invalidParameterHandler.getMaxPagingSize(), dataEngineCommonHandler.getNow(), methodName);

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
