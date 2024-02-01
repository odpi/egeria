/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetcatalog.converters.AssetCatalogConverter;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogErrorCode;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogException;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetCatalogBean;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetCatalogItemElement;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Connection;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Element;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Elements;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Type;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.accessservices.assetcatalog.service.ClockService;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.API_ENDPOINT;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.API_ENDPOINT_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.ASSET;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.ASSET_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.ASSET_LOCATION;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.ASSET_LOCATION_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.ASSET_SCHEMA_TYPE;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.ASSET_SCHEMA_TYPE_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.ATTRIBUTE_FOR_SCHEMA;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.ATTRIBUTE_FOR_SCHEMA_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.COMPLEX_SCHEMA_TYPE;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.CONNECTION;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.CONNECTION_CONNECTOR_TYPE;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.CONNECTION_CONNECTOR_TYPE_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.CONNECTION_ENDPOINT;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.CONNECTION_ENDPOINT_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.CONNECTION_TO_ASSET;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.CONNECTION_TO_ASSET_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.DATABASE;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.DATA_CONTENT_FOR_DATA_SET;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.DATA_CONTENT_FOR_DATA_SET_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.DATA_FILE;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.DATA_SET;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.DATA_STORE;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.DEPLOYED_API;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.DEPLOYED_VIRTUAL_CONTAINER;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.DEPLOYED_VIRTUAL_CONTAINER_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.DISPLAY_NAME;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.ENDPOINT;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.FILE_FOLDER;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.FOLDER_HIERARCHY;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.FOLDER_HIERARCHY_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.GLOSSARY_TERM;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.GLOSSARY_TERM_TYPE_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.GUID_PARAMETER;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.HOST;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.HOST_CLUSTER;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.HOST_CLUSTER_MEMBER;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.HOST_CLUSTER_MEMBER_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.HOST_LOCATION;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.HOST_LOCATION_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.HOST_NETWORK;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.HOST_NETWORK_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.HOST_OPERATING_PLATFORM;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.HOST_OPERATING_PLATFORM_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.IT_INFRASTRUCTURE;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.LOCATION;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.NAME;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.NESTED_FILE;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.NESTED_FILE_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.NESTED_SCHEMA_ATTRIBUTE;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.NESTED_SCHEMA_ATTRIBUTE_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.NETWORK;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.NETWORK_GATEWAY_LINK;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.NETWORK_GATEWAY_LINK_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.PORT_IMPLEMENTATION;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.PORT_SCHEMA;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.PORT_SCHEMA_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.PROCESS;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.PROCESS_PORT;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.PROCESS_PORT_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.QUALIFIED_NAME;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.SCHEMA_ATTRIBUTE;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.SCHEMA_ATTRIBUTE_TYPE;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.SCHEMA_ATTRIBUTE_TYPE_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.SCHEMA_ELEMENT;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.SCHEMA_ELEMENT_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.SEARCH_PARAMETER;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.SEARCH_STRING_PARAMETER_NAME;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.SEMANTIC_ASSIGNMENT;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.SEMANTIC_ASSIGNMENT_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.SERVER_ENDPOINT;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.SERVER_ENDPOINT_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.SOFTWARE_SERVER;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.SOFTWARE_SERVER_DEPLOYMENT;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.SOFTWARE_SERVER_DEPLOYMENT_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.SOFTWARE_SERVER_PLATFORM;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.SOFTWARE_SERVER_PLATFORM_DEPLOYMENT;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.SOFTWARE_SERVER_PLATFORM_DEPLOYMENT_GUID;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.TYPE_SEQUENCING;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.VIRTUAL_CONTAINER;

/**
 * Asset Catalog Handler supports the lookup of the assets from the repositories.
 * It runs on the server-side of the Asset Catalog OMAS, fetches the entities using the RepositoryHandler.
 */
public class AssetCatalogHandler {

    private static final Logger log = LoggerFactory.getLogger(AssetCatalogHandler.class);
    private static final String THIS_ASSET_IF_A_DIFFERENT_ZONE = "This asset if a different zone: {}";

    private final String serverUserName;
    private final String sourceName;
    private final RepositoryHandler repositoryHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;
    private final RepositoryErrorHandler errorHandler;
    private final CommonHandler commonHandler;
    private final AssetCatalogConverter<AssetCatalogBean> assetCatalogConverter;
    private final OpenMetadataAPIGenericHandler<AssetCatalogBean> assetHandler;
    private final Map<String, String> defaultSearchTypes = new HashMap<>();
    private final ClockService clockService;
    private List<String> supportedTypesForSearch = new ArrayList<>(Arrays.asList(GLOSSARY_TERM, ASSET, SCHEMA_ELEMENT));

    private final List<String> supportedZones;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serverUserName          name of the local server
     * @param sourceName              name of the component
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler       manages calls to the repository services
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     * @param assetHandler            provides utilities for manipulating asset catalog objects using a generic handler
     * @param assetCatalogConverter   asset catalog bean converter
     * @param errorHandler            provides common validation routines for the other handler classes
     * @param supportedZones          configurable list of zones that Asset Catalog is allowed to serve Assets from
     * @param supportedTypesForSearch configurable list of supported types used for search
     * @param clockService            clock service
     */
    public AssetCatalogHandler(String serverUserName, String sourceName, InvalidParameterHandler invalidParameterHandler,
                               RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper,
                               OpenMetadataAPIGenericHandler<AssetCatalogBean> assetHandler,
                               AssetCatalogConverter<AssetCatalogBean> assetCatalogConverter,
                               RepositoryErrorHandler errorHandler, List<String> supportedZones, List<String> supportedTypesForSearch,
                               ClockService clockService) {
        this.serverUserName = serverUserName;
        this.sourceName = sourceName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.assetHandler = assetHandler;
        this.assetCatalogConverter = assetCatalogConverter;
        this.errorHandler = errorHandler;
        this.supportedZones = supportedZones;
        this.commonHandler = new CommonHandler(sourceName, repositoryHandler, repositoryHelper, assetHandler, errorHandler,
                clockService);
        if (CollectionUtils.isNotEmpty(supportedTypesForSearch)) {
            this.supportedTypesForSearch = supportedTypesForSearch;
            Collections.sort(supportedTypesForSearch);
        }
        this.clockService = clockService;
        defaultSearchTypes.put(GLOSSARY_TERM, GLOSSARY_TERM_TYPE_GUID);
        defaultSearchTypes.put(ASSET, ASSET_GUID);
        defaultSearchTypes.put(SCHEMA_ELEMENT, SCHEMA_ELEMENT_GUID);
    }

    /**
     * Return the requested entity and converting to Asset Catalog OMAS model
     *
     * @param userId        user identifier that issues the call
     * @param assetGUID     the asset identifier
     * @param assetTypeName the asset type name
     * @return AssetCatalogBean that contains the core properties of the entity and additional properties
     * @throws InvalidParameterException  is thrown by the OMAS when a parameter is null or an invalid value.
     * @throws PropertyServerException    reporting errors when connecting to a metadata repository to retrieve properties about the connection and/or connector
     * @throws UserNotAuthorizedException is thrown by the OCF when a userId passed on a request is not authorized to perform the requested action.
     */
    public AssetCatalogBean getEntityDetails(String userId, String assetGUID, String assetTypeName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        String methodName = "getEntityDetails";
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, GUID_PARAMETER, methodName);

        EntityDetail entityByGUID = commonHandler.getEntityByGUID(userId, assetGUID, assetTypeName);
        return assetCatalogConverter.getAssetCatalogBean(entityByGUID);
    }

    /**
     * Returns a list of the relationships for the given entity identifier.
     * Relationship type name can be used for filtering.
     *
     * @param userId        user identifier that issues the call
     * @param assetGUID     the asset identifier
     * @param assetTypeName the asset type name
     * @return a list of Relationships
     * @throws UserNotAuthorizedException is thrown by the OCF when a userId passed on a request is not authorized to perform the requested action.
     * @throws PropertyServerException    reporting errors when connecting to a metadata repository to retrieve properties about the connection and/or connector
     * @throws InvalidParameterException  is thrown by the OMAG Service when a parameter is null or an invalid value.
     */
    public List<org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship> getRelationshipsByEntityGUID(String userId,
                                                                                                                   String assetGUID,
                                                                                                                   String assetTypeName)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String methodName = "getRelationshipsByEntityGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, GUID_PARAMETER, methodName);

        List<Relationship> relationshipsByType = assetHandler.getAttachmentLinks(userId, assetGUID, GUID_PARAMETER,
                 assetTypeName, null, null, null,
                null, 0, false, false, 0,
                invalidParameterHandler.getMaxPagingSize(),  clockService.getNow(), methodName);

        if (CollectionUtils.isNotEmpty(relationshipsByType)) {
            return assetCatalogConverter.convertRelationships(relationshipsByType);
        }

        return Collections.emptyList();
    }

    /**
     * Returns a list of the classification assigned to the given asset.
     * The filtering based on the classification name is possible.
     *
     * @param userId             user identifier that issues the call
     * @param assetGUID          the asset identifier
     * @param assetTypeName      the asset type name
     * @param classificationName the classification type name
     * @return a list of Classifications assigned to the given asset
     * @throws InvalidParameterException  is thrown by the OMAG Service when a parameter is null or an invalid value.
     * @throws PropertyServerException    reporting errors when connecting to a metadata repository to retrieve properties about the connection and/or connector
     * @throws UserNotAuthorizedException is thrown by the OCF when a userId passed on a request is not authorized to perform the requested action.
     */
    public List<org.odpi.openmetadata.accessservices.assetcatalog.model.Classification> getEntityClassificationByName(String userId,
                                                                                                                      String assetGUID,
                                                                                                                      String assetTypeName,
                                                                                                                      String classificationName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "getEntityClassificationByName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, GUID_PARAMETER, methodName);

        List<Classification> entityClassifications = getEntityClassifications(userId, assetGUID, assetTypeName);

        if (CollectionUtils.isEmpty(entityClassifications)) {
            return Collections.emptyList();
        }

        if (classificationName != null) {
            entityClassifications = filterClassificationByName(entityClassifications, classificationName);
        }

        return assetCatalogConverter.convertClassifications(entityClassifications);
    }

    /**
     * Returns a certain kind of relationships for a specified asset.
     *
     * @param userId               user identifier that issues the call
     * @param assetGUID            the asset identifier
     * @param assetTypeName        the asset type name
     * @param relationshipTypeName the relationship type name
     * @param from                 offset
     * @param pageSize             limit the number of the assets returned
     * @return the list of relationships for the given asset
     * @throws UserNotAuthorizedException is thrown by the OCF when a userId passed on a request is not authorized to perform the requested action.
     * @throws PropertyServerException    reporting errors when connecting to a metadata repository to retrieve properties about the connection and/or connector
     * @throws InvalidParameterException  is thrown by the OMAG Service when a parameter is null or an invalid value.
     */
    public List<org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship> getRelationships(String userId,
                                                                                                       String assetGUID,
                                                                                                       String assetTypeName,
                                                                                                       String relationshipTypeName,
                                                                                                       Integer from,
                                                                                                       Integer pageSize)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {

        String methodName = "getRelationships";
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, GUID_PARAMETER, methodName);
        invalidParameterHandler.validatePaging(from, pageSize, methodName);

        String relationshipTypeGUID = commonHandler.getTypeDefGUID(userId, relationshipTypeName);

        List<Relationship> pagedRelationshipsByType = assetHandler.getAttachmentLinks(userId, assetGUID, GUID_PARAMETER,
                assetTypeName, relationshipTypeGUID, relationshipTypeName, null, null,0,
                false, false, from, pageSize, clockService.getNow(), methodName);

        if (CollectionUtils.isNotEmpty(pagedRelationshipsByType)) {
            return assetCatalogConverter.convertRelationships(pagedRelationshipsByType);
        }

        return Collections.emptyList();
    }

    /**
     * @param userId           user identifier that issues the call
     * @param assetGUID        the asset identifier
     * @param searchParameters additional parameters for searching and filtering
     * @param serverName       the name of the server
     * @return a list of entities from the neighborhood of the given entity
     * @throws AssetCatalogException      is thrown by the Asset Catalog OMAS when the asset passed on a request is not found in the repository
     * @throws InvalidParameterException  is thrown by the OMAG Service when a parameter is null or an invalid value.
     * @throws PropertyServerException    reporting errors when connecting to a metadata repository to retrieve properties about the connection and/or connector
     * @throws UserNotAuthorizedException is thrown by the OCF when a userId passed on a request is not authorized to perform the requested action.
     */
    public List<AssetCatalogBean> getEntitiesFromNeighborhood(String userId, String assetGUID, SearchParameters searchParameters, String serverName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, AssetCatalogException {

        String methodName = "getEntitiesFromNeighborhood";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, GUID_PARAMETER, methodName);
        invalidParameterHandler.validateObject(searchParameters, SEARCH_PARAMETER, methodName);
        invalidParameterHandler.validatePaging(searchParameters.getFrom(), searchParameters.getPageSize(), methodName);

        InstanceGraph entityNeighborhood = getAssetNeighborhood(userId, assetGUID, searchParameters, serverName);

        List<EntityDetail> entities = entityNeighborhood.getEntities();
        if (CollectionUtils.isEmpty(entities)) {
            throw new AssetCatalogException(AssetCatalogErrorCode.NO_ASSET_FROM_NEIGHBORHOOD_NOT_FOUND.getMessageDefinition(assetGUID, serverName),
                    this.getClass().getName(),
                    methodName);
        }

        return getAssetCatalogBeansAfterValidation(methodName, entities);
    }

    /**
     * @param userId           user identifier that issues the call
     * @param searchCriteria   search criteria string used for finding the entities
     * @param searchParameters additional parameters for searching and filtering
     * @return a list of matching criteria entities
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException - is thrown by an OMRS Connector when the supplied UserId
     *                                                                                            is not permitted to perform a specific operation on the metadata collection.
     * @throws FunctionNotSupportedException                                                      - provides a checked exception for reporting that an
     *                                                                                            OMRS repository connector does not support the method called
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException  - is thrown by an OMRS Connector when the parameters passed to a repository connector are not valid
     * @throws PropertyErrorException                                                             - is thrown by an OMRS Connector when the properties defined for a specific entity
     *                                                                                            or relationship instance do not match the TypeDefs for the metadata collection.
     * @throws TypeErrorException                                                                 - is thrown by an OMRS Connector when the requested type for an instance is not represented by a known TypeDef.
     * @throws PagingErrorException                                                               - is thrown by an OMRS Connector when the caller has passed invalid paging attributes on a search call.
     * @throws InvalidParameterException                                                          - is thrown by the OMAG Service when a parameter is null or an invalid value.
     * @throws RepositoryErrorException                                                           - there is a problem communicating with the metadata repository.
     * @throws EntityNotKnownException                                                            - is thrown when the entity is not found in the repositories
     * @throws PropertyServerException                                                            - is thrown when there is a server configuration error
     * @throws UserNotAuthorizedException                                                         - is thrown when the user is not authorized to do the search
     */
    public List<Elements> searchByType(String userId, String searchCriteria, SearchParameters searchParameters)
            throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
            FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
            PropertyErrorException, TypeErrorException, PagingErrorException,
            InvalidParameterException, RepositoryErrorException, EntityNotKnownException, PropertyServerException, UserNotAuthorizedException {

        String methodName = "searchByType";
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(userId, searchCriteria, methodName);
        invalidParameterHandler.validateObject(searchParameters, SEARCH_PARAMETER, methodName);
        invalidParameterHandler.validatePaging(searchParameters.getFrom(), searchParameters.getPageSize(), methodName);

        List<EntityDetail> result;
        Map<String, String> typesAndGUIDsFilter;
        if (CollectionUtils.isNotEmpty(searchParameters.getEntityTypes())) {
            typesAndGUIDsFilter = commonHandler.getTypesAndGUIDs(userId, searchParameters.getEntityTypes());
            for( Map.Entry<String, String> typeAndGUID : typesAndGUIDsFilter.entrySet()) {
                if (typeAndGUID.getValue() == null) {
                    ExceptionMessageDefinition messageDefinition = AssetCatalogErrorCode.TYPE_DEF_NOT_FOUND.getMessageDefinition(typeAndGUID.getKey());
                    throw new EntityNotKnownException(messageDefinition, this.getClass().getName(), messageDefinition.getUserAction());
                }
            }
             result = collectSearchedEntitiesByType(userId, searchCriteria, searchParameters, typesAndGUIDsFilter, methodName);
        } else {
            result = collectSearchedEntitiesByType(userId, searchCriteria, searchParameters, defaultSearchTypes, methodName);
        }

        SequencingOrder sequencingOrder = searchParameters.getSequencingOrder();
        String sequencingProperty = searchParameters.getSequencingProperty();
        List<Elements> results = createSearchResultList(result, methodName);
        results.sort((firstAsset, secondAsset) ->
                orderElements(firstAsset, secondAsset, sequencingProperty, sequencingOrder));
        return results;
    }

    /**
     *
     * @param userId           user identifier that issues the call
     * @param typeName         the assets type name to search for
     * @return                 list of assets by type name
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException - is thrown by an OMRS Connector when the supplied UserId
     *                                                                                            is not permitted to perform a specific operation on the metadata collection.
     * @throws FunctionNotSupportedException                                                      - provides a checked exception for reporting that an
     *                                                                                            OMRS repository connector does not support the method called
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException  - is thrown by an OMRS Connector when the parameters passed to a repository connector are not valid
     * @throws PropertyErrorException                                                             - is thrown by an OMRS Connector when the properties defined for a specific entity
     * @throws TypeErrorException                                                                 - is thrown by an OMRS Connector when the requested type for an instance is not represented by a known TypeDef.
     * @throws PagingErrorException                                                               - is thrown by an OMRS Connector when the caller has passed invalid paging attributes on a search call.
     * @throws InvalidParameterException                                                          - is thrown by the OMAG Service when a parameter is null or an invalid value.
     * @throws RepositoryErrorException                                                           - there is a problem communicating with the metadata repository.
     * @throws EntityNotKnownException                                                            - is thrown when the entity is not found in the repositories
     * @throws PropertyServerException                                                            - reporting errors when connecting to a metadata repository to retrieve properties about the connection and/or connector
     * @throws UserNotAuthorizedException                                                         - is thrown by the OCF when a userId passed on a request is not authorized to perform the requested action.
     */
    public List<Elements> searchByTypeName(String userId, String typeName)
            throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
            FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
            PropertyErrorException, TypeErrorException, PagingErrorException,
            InvalidParameterException, RepositoryErrorException, EntityNotKnownException, PropertyServerException, UserNotAuthorizedException {

        String methodName = "searchByTypeName";
        invalidParameterHandler.validateUserId(userId, methodName);

        List<EntityDetail> result;
        if (typeName != null) {
            String typeGUID = commonHandler.getTypeDefGUID(userId, typeName);
            if (typeGUID == null) {
                ExceptionMessageDefinition messageDefinition = AssetCatalogErrorCode.TYPE_DEF_NOT_FOUND.getMessageDefinition(typeName);
                throw new EntityNotKnownException(messageDefinition, this.getClass().getName(), messageDefinition.getUserAction());
            }
            result = collectSearchedEntitiesByTypeIdentifiers(userId, typeName, typeGUID, methodName);
        } else {
            return Collections.emptyList();
        }

        return createSearchResultList(result, methodName);
    }

    /**
     *
     * @param userId           user identifier that issues the call
     * @param typeGUID         the assets type GUID to search for
     * @return                 list of assets by type GUID
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException - is thrown by an OMRS Connector when the supplied UserId
     *                                                                                            is not permitted to perform a specific operation on the metadata collection.
     * @throws FunctionNotSupportedException                                                      - provides a checked exception for reporting that an
     *                                                                                            OMRS repository connector does not support the method called
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException  - is thrown by an OMRS Connector when the parameters passed to a repository connector are not valid
     * @throws PropertyErrorException                                                             - is thrown by an OMRS Connector when the properties defined for a specific entity
     * @throws TypeErrorException                                                                 - is thrown by an OMRS Connector when the requested type for an instance is not represented by a known TypeDef.
     * @throws PagingErrorException                                                               - is thrown by an OMRS Connector when the caller has passed invalid paging attributes on a search call.
     * @throws InvalidParameterException                                                          - is thrown by the OMAG Service when a parameter is null or an invalid value.
     * @throws RepositoryErrorException                                                           - there is a problem communicating with the metadata repository.
     * @throws EntityNotKnownException                                                            - is thrown when the entity is not found in the repositories
     * @throws PropertyServerException                                                            - reporting errors when connecting to a metadata repository to retrieve properties about the connection and/or connector
     * @throws UserNotAuthorizedException                                                         - is thrown by the OCF when a userId passed on a request is not authorized to perform the requested action.
     */
    public List<Elements> searchByTypeGUID(String userId, String typeGUID)
            throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
            FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
            PropertyErrorException, TypeErrorException, PagingErrorException,
            InvalidParameterException, RepositoryErrorException, EntityNotKnownException, PropertyServerException, UserNotAuthorizedException {

        String methodName = "searchByTypeGUID";
        invalidParameterHandler.validateUserId(userId, methodName);

        List<EntityDetail> result;
        if (typeGUID != null) {
            String typeName = repositoryHelper.getTypeDef(userId, "typeName", typeGUID, methodName).getName();
            if (typeName == null) {
                ExceptionMessageDefinition messageDefinition = AssetCatalogErrorCode.TYPE_DEF_NOT_FOUND.getMessageDefinition(typeName);
                throw new EntityNotKnownException(messageDefinition, this.getClass().getName(), messageDefinition.getUserAction());
            }
            result = collectSearchedEntitiesByTypeIdentifiers(userId, typeName, typeGUID, methodName);
        } else {
            return Collections.emptyList();
        }

        return createSearchResultList(result, methodName);
    }

    /**
     * Returns a list of elements that define the context based on the type of the given asset.
     * @param userId            user identifier that issues the call
     * @param entityGUID        the identifier of the entity
     * @param entityTypeDefName the type name of the entity
     * @return the context of the given entity
     * @throws UserNotAuthorizedException - is thrown by the OCF when a userId passed on a request is not
     *                                    authorized to perform the requested action.
     * @throws PropertyServerException    - provides a checked exception for reporting errors when connecting to a
     *                                    metadata repository to retrieve properties about the connection and/or connector.
     * @throws InvalidParameterException  -  is thrown by the OMAS when a parameter is null or an invalid value.
     */
    public Elements buildContextByType(String userId,
                                       String entityGUID,
                                       String entityTypeDefName)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {

        String methodName = "buildContextByType";
        invalidParameterHandler.validateUserId(userId, methodName);

        EntityDetail entityDetail = getEntity(userId, entityGUID, entityTypeDefName);

        if (entityDetail == null || entityDetail.getType() == null || entityDetail.getType().getTypeDefName() == null) {
            return null;
        }

        String typeDefName = entityDetail.getType().getTypeDefName();
        Set<String> superTypes = commonHandler.collectSuperTypes(userId, entityDetail.getType().getTypeDefName());

        AssetCatalogItemElement assetCatalogItemElement = new AssetCatalogItemElement();

        if (typeDefName.equals(GLOSSARY_TERM)) {
            return getContextForGlossaryTerm(userId, entityDetail);
        } else {
            invalidParameterHandler.validateAssetInSupportedZone(entityDetail.getGUID(),
                    GUID_PARAMETER,
                    commonHandler.getAssetZoneMembership(entityDetail.getClassifications()),
                    supportedZones,
                    serverUserName,
                    methodName);

            Elements elements = assetCatalogConverter.buildAssetElements(entityDetail);
            if (superTypes.contains(SCHEMA_ELEMENT)) {
                getContextForSchemaElement(userId, entityDetail, assetCatalogItemElement);
            } else if (superTypes.contains(DEPLOYED_API)) {
                getContextForDeployedAPI(userId, entityDetail, assetCatalogItemElement);
            } else if (superTypes.contains(IT_INFRASTRUCTURE)) {
                getContextForInfrastructure(userId, entityDetail, assetCatalogItemElement);
            } else if (superTypes.contains(PROCESS)) {
                getContextForProcess(userId, entityDetail, assetCatalogItemElement);
            } else if (superTypes.contains(DATA_STORE)) {
                getContextForDataStore(userId, entityDetail, assetCatalogItemElement);
            } else if (superTypes.contains(DATA_SET)) {
                getContextForDataSet(userId, entityDetail, assetCatalogItemElement);
            }

            elements.setAssetCatalogItemElements(Collections.singletonList(assetCatalogItemElement));
            return elements;
        }
    }

    /**
     * Returns supported types for search with all sub-types.
     * If type name is provided, it returns the type itself and the list of sub-types for it
     *
     * @param userId   user identifier that issues the call
     * @param typeName optional type name
     * @return a list of types
     */
    public List<Type> getSupportedTypes(String userId, String typeName) {
        if (typeName != null && !typeName.isEmpty()) {
            return getSupportedTypesWithDescendants(userId, typeName);
        }

        return getSupportedTypes(userId, supportedTypesForSearch.toArray(new String[0]));
    }

    private ArrayList<Elements> createSearchResultList(List<EntityDetail> result, String methodName) {
        Set<Elements> searchResults = new HashSet<>();

        for (EntityDetail entityDetail : result) {
            try {
                invalidParameterHandler.validateAssetInSupportedZone(entityDetail.getGUID(),
                        GUID_PARAMETER,
                        commonHandler.getAssetZoneMembership(entityDetail.getClassifications()),
                        supportedZones,
                        serverUserName,
                        methodName);
                Elements elements = assetCatalogConverter.buildAssetElements(entityDetail);
                searchResults.add(elements);
            } catch (InvalidParameterException e) {
                log.debug(THIS_ASSET_IF_A_DIFFERENT_ZONE, entityDetail.getGUID());
            }
        }
        return new ArrayList<>(searchResults);
    }

    private List<AssetCatalogBean> getAssetCatalogBeansAfterValidation(String methodName,
                                                                       List<EntityDetail> entities) throws InvalidParameterException {
        List<AssetCatalogBean> result = new ArrayList<>();

        for (EntityDetail asset : entities) {

            invalidParameterHandler.validateAssetInSupportedZone(asset.getGUID(),
                    GUID_PARAMETER,
                    commonHandler.getAssetZoneMembership(asset.getClassifications()),
                    supportedZones,
                    serverUserName,
                    methodName);

            result.add(assetCatalogConverter.getAssetCatalogBean(asset));
        }
        return result;
    }

    private EntityDetail getEntity(String userId,
                                   String assetGUID,
                                   String assetTypeName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "getEntity";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, GUID_PARAMETER, methodName);

        EntityDetail entityByGUID = commonHandler.getEntityByGUID(userId, assetGUID, assetTypeName);
        if (entityByGUID != null) {
            invalidParameterHandler.validateAssetInSupportedZone(entityByGUID.getGUID(),
                    GUID_PARAMETER,
                    commonHandler.getAssetZoneMembership(entityByGUID.getClassifications()),
                    supportedZones,
                    serverUserName,
                    methodName);
        }

        return entityByGUID;
    }

    private List<EntityDetail> collectSearchedEntitiesByType(String userId,
                                                             String searchCriteria,
                                                             SearchParameters searchParameters,
                                                             Map<String, String> typesAndGUIDs,
                                                             String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        List<EntityDetail> result = new ArrayList<>();

        if(!searchParameters.getExactMatch()) {
            searchCriteria = repositoryHelper.getContainsRegex(searchCriteria, searchParameters.getCaseInsensitive());
        }

        for (Map.Entry<String, String> typeAndGUID : typesAndGUIDs.entrySet()) {
            result.addAll(searchEntityByCriteria(userId, searchCriteria, typeAndGUID.getValue(), typeAndGUID.getKey(),
                    searchParameters, methodName));
        }
        return result;
    }

    private List<EntityDetail> collectSearchedEntitiesByTypeIdentifiers(String userId,
                                                             String typeName,
                                                             String typeGUID,
                                                             String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        List<EntityDetail> result = new ArrayList<>();

        result.addAll(searchEntityByType(userId, typeGUID, typeName, methodName));
        return result;
    }

    private void getContextForSchemaElement(String userId,
                                            EntityDetail entityDetail,
                                            AssetCatalogItemElement assetCatalogItemElement)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        findAsset(userId, assetCatalogItemElement, entityDetail);
    }

    private Elements getContextForGlossaryTerm(String userId,
                                               EntityDetail glossaryTerm)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String method = "getContextForGlossaryTerm";

        if (glossaryTerm == null) {
            return null;
        }
        Elements elements = assetCatalogConverter.buildAssetElements(glossaryTerm);

        List<EntityDetail> schemas = assetHandler.getAttachedFilteredEntities(userId, glossaryTerm.getGUID(),
                GUID_PARAMETER, GLOSSARY_TERM, SEMANTIC_ASSIGNMENT, SEMANTIC_ASSIGNMENT_GUID,
                0, null, null, 0, false,
                false, 0, method);

        if (CollectionUtils.isEmpty(schemas)) {
            return elements;
        }

        List<AssetCatalogItemElement> assets = new ArrayList<>(schemas.size());

        for (EntityDetail schema : schemas) {
            AssetCatalogItemElement assetCatalogItemElement = addSchemaForGlossaryTerm(userId, schema);
            assets.add(assetCatalogItemElement);
        }
        elements.setAssetCatalogItemElements(assets);
        return elements;
    }

    private AssetCatalogItemElement addSchemaForGlossaryTerm(String userId, EntityDetail schema)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        AssetCatalogItemElement assetCatalogItemElement = new AssetCatalogItemElement();
        List<Element> elements = new ArrayList<>();
        elements.add(assetCatalogConverter.buildAssetElements(schema));
        assetCatalogItemElement.setContext(elements);

        findAsset(userId, assetCatalogItemElement, schema);

        return assetCatalogItemElement;
    }

    private void getContextForDeployedAPI(String userId,
                                          EntityDetail entityDetail,
                                          AssetCatalogItemElement assetCatalogItemElement)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String method = "getContextForDeployedAPI";

        List<EntityDetail> endpoints = assetHandler.getAttachedFilteredEntities(userId, entityDetail.getGUID(),
                GUID_PARAMETER, DEPLOYED_API, API_ENDPOINT, API_ENDPOINT_GUID,
                0, null, null, 0, false,
                false, 0, method);
        if (CollectionUtils.isEmpty(endpoints)) {
            return;
        }

        for (EntityDetail endpoint : endpoints) {
            assetCatalogConverter.addContextElement(assetCatalogItemElement, endpoint);
            getConnectionContext(userId, endpoint, assetCatalogItemElement);
        }

    }

    private void getContextForInfrastructure(String userId,
                                             EntityDetail entityDetail,
                                             AssetCatalogItemElement assetCatalogItemElement)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        switch (entityDetail.getType().getTypeDefName()) {
            case HOST:
                getContextForHost(userId, entityDetail, assetCatalogItemElement);
                break;
            case NETWORK:
                getContextForNetwork(userId, entityDetail, assetCatalogItemElement);
                break;
            case SOFTWARE_SERVER_PLATFORM:
                getContextForSoftwareServerPlatform(userId, entityDetail, assetCatalogItemElement);
                break;
            case SOFTWARE_SERVER:
                getContextForSoftwareServer(userId, entityDetail, assetCatalogItemElement);
                break;
            default:
                break;
        }

    }

    private void getContextForProcess(String userId,
                                      EntityDetail entityDetail,
                                      AssetCatalogItemElement assetCatalogItemElement)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String method = "getContextForProcess";

        List<EntityDetail> ports = assetHandler.getAttachedFilteredEntities(userId, entityDetail.getGUID(),
                GUID_PARAMETER, PROCESS, PROCESS_PORT, PROCESS_PORT_GUID,
                0, null, null, 0, false,
                false, 0, method);

        if (CollectionUtils.isNotEmpty(ports)) {
            for (EntityDetail port : ports) {
                assetCatalogConverter.addContextElement(assetCatalogItemElement, port);
                if (port.getType().getTypeDefName().equals(PORT_IMPLEMENTATION)) {
                    EntityDetail schemaType = assetHandler.getAttachedEntity(userId, port.getGUID(), GUID_PARAMETER,
                            DATABASE, PORT_SCHEMA_GUID, PORT_SCHEMA, null, false,
                            false, clockService.getNow(), method);

                    if (schemaType != null) {
                        assetCatalogConverter.addElement(assetCatalogItemElement, schemaType);
                        getContextForSchemaType(userId, assetCatalogItemElement, schemaType);
                    }
                }
            }
        }

    }

    private void getContextForDataStore(String userId,
                                        EntityDetail entityDetail,
                                        AssetCatalogItemElement assetCatalogItemElement)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {

        if (entityDetail.getType().getTypeDefName().equals(DATABASE)) {
            getContextForDatabase(userId, entityDetail, assetCatalogItemElement);
        } else {
            if (entityDetail.getType().getTypeDefName().equals(DATA_FILE)) {
                getContextForDataFile(userId, entityDetail, assetCatalogItemElement);
            } else if (entityDetail.getType().getTypeDefName().equals(FILE_FOLDER)) {
                getContextForFileFolder(userId, entityDetail, assetCatalogItemElement);
            }
        }
    }

    private void getContextForDatabase(String userId,
                                       EntityDetail entityDetail,
                                       AssetCatalogItemElement assetCatalogItemElement)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String method = "getContextForDatabase";

        List<EntityDetail> dataSets = assetHandler.getAttachedFilteredEntities(userId, entityDetail.getGUID(),
                GUID_PARAMETER, DATABASE, DATA_CONTENT_FOR_DATA_SET, DATA_CONTENT_FOR_DATA_SET_GUID,
                0, null, null, 0, false,
                false, 0, method);

        if (dataSets != null && !dataSets.isEmpty()) {
            for (EntityDetail dataSet : dataSets) {
                getContextForDataSet(userId, dataSet, assetCatalogItemElement);
            }
        }
    }

    private void getContextForDataSet(String userId,
                                      EntityDetail dataSet,
                                      AssetCatalogItemElement assetCatalogItemElement) throws
            UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String method = "getContextForDataSet";

        EntityDetail schemaType = assetHandler.getAttachedEntity(userId, dataSet.getGUID(), GUID_PARAMETER,
                DATA_SET, ASSET_SCHEMA_TYPE_GUID, ASSET_SCHEMA_TYPE, null, false,
                false, clockService.getNow(), method);

        if (schemaType == null) {
            return;
        }
        assetCatalogConverter.addElement(assetCatalogItemElement, schemaType);

        if (isComplexSchemaType(schemaType.getType().getTypeDefName()).isPresent()) {
            getContextForSchemaType(userId, assetCatalogItemElement, schemaType);
        } else {
            getAsset(userId, assetCatalogItemElement, schemaType);
        }
    }

    private void getContextForFileFolder(String userId,
                                         EntityDetail entityDetail,
                                         AssetCatalogItemElement assetCatalogItemElement)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String method = "getContextForFileFolder";

        List<EntityDetail> connections = assetHandler.getAttachedFilteredEntities(userId, entityDetail.getGUID(),
                GUID_PARAMETER, FILE_FOLDER, CONNECTION_TO_ASSET, CONNECTION_TO_ASSET_GUID,
                0, null, null, 0, false,
                false, 0, method);


        if (CollectionUtils.isNotEmpty(connections)) {
            setConnections(userId, assetCatalogItemElement, entityDetail);
            return;
        }


        List<Relationship> parentFolderRelationships = assetHandler.getAttachmentLinks(userId, entityDetail.getGUID(),
                GUID_PARAMETER, entityDetail.getType().getTypeDefName(), FOLDER_HIERARCHY_GUID,
                FOLDER_HIERARCHY, null, null,0, false, false,
                0, invalidParameterHandler.getMaxPagingSize(), clockService.getNow(), method);

        if (CollectionUtils.isEmpty(parentFolderRelationships)) {
            return;
        }

        parentFolderRelationships = parentFolderRelationships.stream()
                .filter(s -> s.getEntityTwoProxy().getGUID().equals(entityDetail.getGUID()))
                .collect(Collectors.toList());
        if (parentFolderRelationships.size() != 1) {
            return;
        }

        EntityProxy parentFolderProxy = repositoryHandler.getOtherEnd(entityDetail.getGUID(), parentFolderRelationships.get(0));

        EntityDetail parentFolder = commonHandler.getEntityByGUID(userId,
                parentFolderProxy.getGUID(),
                parentFolderProxy.getType().getTypeDefName());

        assetCatalogConverter.addElement(assetCatalogItemElement, parentFolder);
        getContextForFileFolder(userId, parentFolder, assetCatalogItemElement);
    }

    private void getContextForEachParentFolder(String userId,
                                               AssetCatalogItemElement assetCatalogItemElement,
                                               List<EntityDetail> parentFolders)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        for (EntityDetail folder : parentFolders) {
            assetCatalogConverter.addElement(assetCatalogItemElement, folder);
            getContextForFileFolder(userId, folder, assetCatalogItemElement);
        }
    }

    private void getContextForDataFile(String userId,
                                       EntityDetail entityDetail,
                                       AssetCatalogItemElement assetCatalogItemElement)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String method = "getContextForDataFile";

        List<EntityDetail> fileFolders = assetHandler.getAttachedFilteredEntities(userId, entityDetail.getGUID(),
                GUID_PARAMETER, DATA_FILE, NESTED_FILE, NESTED_FILE_GUID,
                0, null, null, 0, false,
                false, 0, method);

        if (CollectionUtils.isEmpty(fileFolders)) {
            return;
        }

        getContextForEachParentFolder(userId, assetCatalogItemElement, fileFolders);
    }

    private void getContextForSoftwareServerPlatform(String userId,
                                                     EntityDetail entityDetail,
                                                     AssetCatalogItemElement assetCatalogItemElement)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String method = "getContextForSoftwareServerPlatform";

        EntityDetail host = assetHandler.getAttachedEntity(userId, entityDetail.getGUID(), GUID_PARAMETER,
                entityDetail.getType().getTypeDefName(), SOFTWARE_SERVER_PLATFORM_DEPLOYMENT_GUID,
                SOFTWARE_SERVER_PLATFORM_DEPLOYMENT, null, false, false,
                clockService.getNow(), method);
        if (host != null) {
            assetCatalogConverter.addElement(assetCatalogItemElement, host);
            getContextForHost(userId, host, assetCatalogItemElement);
        }
    }

    private void getContextForNetwork(String userId,
                                      EntityDetail entityDetail,
                                      AssetCatalogItemElement assetCatalogItemElement)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String method = "getContextForNetwork";

        List<EntityDetail> networkGateways = assetHandler.getAttachedFilteredEntities(userId, entityDetail.getGUID(),
                GUID_PARAMETER, NETWORK, NETWORK_GATEWAY_LINK, NETWORK_GATEWAY_LINK_GUID,
                0, null, null, 0, false,
                false, 0, method);

        networkGateways.forEach(networkGateway -> assetCatalogConverter.addElement(assetCatalogItemElement, networkGateway));

        List<EntityDetail> hosts = assetHandler.getAttachedFilteredEntities(userId, entityDetail.getGUID(),
                GUID_PARAMETER, NETWORK, HOST_NETWORK, HOST_NETWORK_GUID,
                0, null, null, 0, false,
                false, 0, method);

        if (CollectionUtils.isNotEmpty(hosts)) {
            for (EntityDetail host : hosts) {
                assetCatalogConverter.addElement(assetCatalogItemElement, host);
                getContextForHost(userId, host, assetCatalogItemElement);
            }
        }
    }

    private void getContextForHost(String userId,
                                   EntityDetail entityDetail,
                                   AssetCatalogItemElement assetCatalogItemElement)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String method = "getContextForHost";
        List<EntityDetail> hosts = null;
        if (entityDetail.getType().getTypeDefName().equals(VIRTUAL_CONTAINER)) {
            hosts = assetHandler.getAttachedFilteredEntities(userId, entityDetail.getGUID(),
                    GUID_PARAMETER, HOST, DEPLOYED_VIRTUAL_CONTAINER, DEPLOYED_VIRTUAL_CONTAINER_GUID,
                    0, null, null, 0, false,
                    false, 0, method);

        } else if (entityDetail.getType().getTypeDefName().equals(HOST_CLUSTER)) {
            hosts = assetHandler.getAttachedFilteredEntities(userId, entityDetail.getGUID(),
                    GUID_PARAMETER, HOST, HOST_CLUSTER_MEMBER, HOST_CLUSTER_MEMBER_GUID,
                    0, null, null, 0, false,
                    false, 0, method);

        }
        if (hosts != null) {
            hosts.forEach(host -> assetCatalogConverter.addElement(assetCatalogItemElement, host));
        }

        EntityDetail operatingPlatform = assetHandler.getAttachedEntity(userId, entityDetail.getGUID(), GUID_PARAMETER,
                entityDetail.getType().getTypeDefName(), HOST_OPERATING_PLATFORM_GUID, HOST_OPERATING_PLATFORM,
                null, false, false, clockService.getNow(), method);
        assetCatalogConverter.addElement(assetCatalogItemElement, operatingPlatform);

        processLocations(userId, entityDetail, assetCatalogItemElement, method);
    }

    private void processLocations(String userId, EntityDetail entityDetail, AssetCatalogItemElement assetCatalogItemElement,
                                  String method) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        List<EntityDetail> locations = assetHandler.getAttachedFilteredEntities(userId, entityDetail.getGUID(),
                GUID_PARAMETER, HOST, HOST_LOCATION, HOST_LOCATION_GUID, 0,
                null, null, 0, false, false,
                0, method);
        if (CollectionUtils.isNotEmpty(locations)) {
            for (EntityDetail location : locations) {
                assetCatalogConverter.addElement(assetCatalogItemElement, location);
                getContextForLocation(userId, assetCatalogItemElement, location);
            }
        }
    }

    private void getContextForLocation(String userId,
                                       AssetCatalogItemElement assetCatalogItemElement,
                                       EntityDetail location)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String method = "getContextForLocation";

        List<EntityDetail> assetLocations = assetHandler.getAttachedFilteredEntities(userId, location.getGUID(),
                GUID_PARAMETER, LOCATION, ASSET_LOCATION, ASSET_LOCATION_GUID,
                0, null, null, 0, false,
                false, 0, method);

        if (CollectionUtils.isNotEmpty(assetLocations)) {
            for (EntityDetail assetLocation : assetLocations) {
                assetCatalogConverter.addElement(assetCatalogItemElement, assetLocation);
                getAsset(userId, assetCatalogItemElement, assetLocation);
            }
        }

        processLocations(userId, location, assetCatalogItemElement, method);
    }

    private void getContextForSoftwareServer(String userId,
                                             EntityDetail entityDetail,
                                             AssetCatalogItemElement assetCatalogItemElement)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String method = "getContextForSoftwareServer";
        Element parentElement = null;

        EntityDetail softwareServerPlatform = assetHandler.getAttachedEntity(userId, entityDetail.getGUID(),
                GUID_PARAMETER, SOFTWARE_SERVER, SOFTWARE_SERVER_DEPLOYMENT_GUID, SOFTWARE_SERVER_DEPLOYMENT,
                null, false, false, clockService.getNow(), method);
        if (softwareServerPlatform != null) {
            parentElement = assetCatalogConverter.getLastNode(assetCatalogItemElement);
            assetCatalogConverter.addElement(assetCatalogItemElement, softwareServerPlatform);
            getContextForSoftwareServerPlatform(userId, softwareServerPlatform, assetCatalogItemElement);
        }

        EntityDetail endpoint = assetHandler.getAttachedEntity(userId, entityDetail.getGUID(),
                GUID_PARAMETER, SOFTWARE_SERVER, SERVER_ENDPOINT_GUID, SERVER_ENDPOINT,
                null, false, false, clockService.getNow(), method);
        if (endpoint != null) {
            if (parentElement != null) {
                assetCatalogConverter.addChildElement(parentElement, assetCatalogConverter.buildAssetElements(endpoint));
            } else {
                assetCatalogConverter.addContextElement(assetCatalogItemElement, endpoint);
            }
            getConnectionContext(userId, endpoint, assetCatalogItemElement);
        }

    }

    private void getConnectionContext(String userId,
                                      EntityDetail endpoint,
                                      AssetCatalogItemElement assetCatalogItemElement)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String methodName = "getConnectionContext";

        List<EntityDetail> connections = assetHandler.getAttachedFilteredEntities(userId, endpoint.getGUID(),
                GUID_PARAMETER, ENDPOINT, CONNECTION_ENDPOINT, CONNECTION_ENDPOINT_GUID,
                0, null, null, 0, false,
                false, 0, methodName);
        if (CollectionUtils.isEmpty(connections)) {
            return;
        }

        for (EntityDetail connection : connections) {
            assetCatalogConverter.addElement(assetCatalogItemElement, connection);

            List<EntityDetail> elements = new ArrayList<>();
            EntityDetail connectorType = assetHandler.getAttachedEntity(userId, connection.getGUID(),
                    GUID_PARAMETER, CONNECTION, CONNECTION_CONNECTOR_TYPE_GUID, CONNECTION_CONNECTOR_TYPE,
                    null, false, false, clockService.getNow(), methodName);
            if (connectorType != null) {
                elements.add(connectorType);
            }

            EntityDetail asset = assetHandler.getAttachedEntity(userId, connection.getGUID(),
                    GUID_PARAMETER, CONNECTION, CONNECTION_TO_ASSET_GUID, CONNECTION_TO_ASSET,
                    null, false, false, clockService.getNow(), methodName);
            invalidParameterHandler.validateAssetInSupportedZone(asset.getGUID(),
                    GUID_PARAMETER,
                    commonHandler.getAssetZoneMembership(asset.getClassifications()),
                    supportedZones,
                    serverUserName,
                    methodName);
            elements.add(asset);
            elements.forEach(element -> assetCatalogConverter.addElement(assetCatalogItemElement, element));
        }
    }

    private void findAsset(String userId,
                           AssetCatalogItemElement assetCatalogItemElement, EntityDetail... entitiesByType)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {

        String method = "findAsset";
        for (EntityDetail entityDetail : entitiesByType) {
            List<EntityDetail> schemaAttributes = assetHandler.getAttachedFilteredEntities(userId, entityDetail.getGUID(),
                    GUID_PARAMETER, SCHEMA_ATTRIBUTE, ATTRIBUTE_FOR_SCHEMA, ATTRIBUTE_FOR_SCHEMA_GUID,
                    0, null, null, 0, false,
                    false, 0, method);

            if (CollectionUtils.isEmpty(schemaAttributes)) {
                schemaAttributes = assetHandler.getAttachedFilteredEntities(userId, entityDetail.getGUID(),
                        GUID_PARAMETER, SCHEMA_ATTRIBUTE, NESTED_SCHEMA_ATTRIBUTE, NESTED_SCHEMA_ATTRIBUTE_GUID,
                        0, null, null, 0, false,
                        false, 0, method);
                if (CollectionUtils.isEmpty(schemaAttributes)) {
                    continue;
                }
            }

            addSchemaAttributes(assetCatalogItemElement, schemaAttributes);

            for (EntityDetail schemaAttribute : schemaAttributes) {
                if (processSchemaAttribute(userId, assetCatalogItemElement, method, schemaAttribute)) return;
            }
        }
    }

    private boolean processSchemaAttribute(String userId, AssetCatalogItemElement assetCatalogItemElement, String method, EntityDetail schemaAttribute)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        if (isComplexSchemaType(schemaAttribute.getType().getTypeDefName()).isPresent()) {
            setAssetDetails(userId, assetCatalogItemElement, schemaAttribute);
            return true;
        } else {
            processPrimitiveSchema(userId, assetCatalogItemElement, method, schemaAttribute);
        }
        return false;
    }

    private void processPrimitiveSchema(String userId, AssetCatalogItemElement assetCatalogItemElement, String method, EntityDetail schemaAttribute)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        List<EntityDetail> schemaAttributeTypeEntities = assetHandler.getAttachedFilteredEntities(userId, schemaAttribute.getGUID(),
                GUID_PARAMETER, schemaAttribute.getType().getTypeDefName(), SCHEMA_ATTRIBUTE_TYPE,
                SCHEMA_ATTRIBUTE_TYPE_GUID, 0, null, null, 0,
                false, false, 0, method);

        if (CollectionUtils.isNotEmpty(schemaAttributeTypeEntities)) {
            schemaAttributeTypeEntities.forEach(schemaAttributeTypeEntity -> assetCatalogConverter.addElement(assetCatalogItemElement, schemaAttributeTypeEntity));
            findAsset(userId, assetCatalogItemElement, schemaAttributeTypeEntities.toArray(new EntityDetail[0]));
        } else {
            findAsset(userId, assetCatalogItemElement, schemaAttribute);
        }
    }

    private void addSchemaAttributes(AssetCatalogItemElement assetCatalogItemElement, List<EntityDetail> schemaAttributes) {
        Element lastNode = assetCatalogConverter.getLastNode(assetCatalogItemElement);
        schemaAttributes.forEach(schemaAttribute -> addNode(assetCatalogItemElement, lastNode, schemaAttribute));
    }

    private void addNode(AssetCatalogItemElement assetCatalogItemElement, Element lastNode, EntityDetail schemaAttribute) {
        if (lastNode == null) {
            assetCatalogConverter.addContextElement(assetCatalogItemElement, schemaAttribute);
        }
        assetCatalogConverter.addElement(assetCatalogItemElement, schemaAttribute);
    }

    private void getContextForSchemaType(String userId,
                                         AssetCatalogItemElement assetCatalogItemElement,
                                         EntityDetail entityDetail)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        Optional<TypeDef> isComplexSchemaType = isComplexSchemaType(entityDetail.getType().getTypeDefName());
        String method = "getContextForSchemaType";

        if (isComplexSchemaType.isPresent()) {
            setAssetDetails(userId, assetCatalogItemElement, entityDetail);
        } else {
            List<EntityDetail> attributeForSchemas = assetHandler.getAttachedFilteredEntities(userId, entityDetail.getGUID(),
                    GUID_PARAMETER, entityDetail.getType().getTypeDefName(), ATTRIBUTE_FOR_SCHEMA,
                    ATTRIBUTE_FOR_SCHEMA_GUID, 0, null, null, 0,
                    false, false, 0, method);
            if (CollectionUtils.isEmpty(attributeForSchemas)) {
                return;
            }

            for (EntityDetail attributeForSchema : attributeForSchemas) {
                assetCatalogConverter.addElement(assetCatalogItemElement, attributeForSchema);

                if (isComplexSchemaType(attributeForSchema.getType().getTypeDefName()).isPresent()) {
                    setAssetDetails(userId, assetCatalogItemElement, attributeForSchema);
                    return;
                } else {
                    List<EntityDetail> schemaAttributeTypeEntities = assetHandler.getAttachedFilteredEntities(userId,
                            attributeForSchema.getGUID(), GUID_PARAMETER, attributeForSchema.getType().getTypeDefName(),
                            SCHEMA_ATTRIBUTE_TYPE, SCHEMA_ATTRIBUTE_TYPE_GUID, 0, null,
                            null, 0, false, false, 0, method);

                    for (EntityDetail schema : schemaAttributeTypeEntities) {
                        assetCatalogConverter.addElement(assetCatalogItemElement, schema);
                        getContextForSchemaType(userId, assetCatalogItemElement, schema);
                    }
                }
            }
        }
    }

    private void setAssetDetails(String userId,
                                 AssetCatalogItemElement assetCatalogItemElement,
                                 EntityDetail entity)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "setAssetDetails";

        EntityDetail dataSet = assetHandler.getAttachedEntity(userId, entity.getGUID(),
                GUID_PARAMETER, entity.getType().getTypeDefName(), ASSET_SCHEMA_TYPE_GUID, ASSET_SCHEMA_TYPE,
                null, false, false, clockService.getNow(), methodName);
        if (dataSet == null) {
            return;
        }

        try {
            invalidParameterHandler.validateAssetInSupportedZone(dataSet.getGUID(),
                    GUID_PARAMETER,
                    commonHandler.getAssetZoneMembership(dataSet.getClassifications()),
                    supportedZones,
                    serverUserName,
                    methodName);

            if (assetCatalogItemElement.getContext() != null) {
                assetCatalogConverter.addElement(assetCatalogItemElement, dataSet);
            } else {
                assetCatalogItemElement.setContext(Collections.singletonList(assetCatalogConverter.buildAssetElements(dataSet)));
            }

            getAsset(userId, assetCatalogItemElement, dataSet);
        } catch (InvalidParameterException e) {
            if (CollectionUtils.isNotEmpty(assetCatalogItemElement.getContext())) {
                assetCatalogItemElement.getContext().remove(assetCatalogItemElement.getContext().size() - 1);
            }
            log.debug("Asset is not in the supported zones {}", dataSet.getGUID());
        }
    }

    private void getAsset(String userId,
                          AssetCatalogItemElement assetCatalogItemElement,
                          EntityDetail dataSet)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        if (dataSet == null) return;

        String methodName = "getAsset";
        List<Relationship> assetToDataSetRelationships = assetHandler.getAttachmentLinks(userId, dataSet.getGUID(),
                GUID_PARAMETER, dataSet.getType().getTypeDefName(), DATA_CONTENT_FOR_DATA_SET_GUID,
                DATA_CONTENT_FOR_DATA_SET, null, null, 1, false,
                false,0, invalidParameterHandler.getMaxPagingSize(), clockService.getNow(), methodName);

        if (CollectionUtils.isEmpty(assetToDataSetRelationships)) {
            return;
        }

        for (Relationship assetToDataSetRelationship : assetToDataSetRelationships) {
            EntityProxy entityOneProxy = assetToDataSetRelationship.getEntityOneProxy();


            if (entityOneProxy.getGUID().equals(dataSet.getGUID())) {
                setConnections(userId, assetCatalogItemElement, dataSet);
            } else {
                EntityDetail asset = commonHandler.getEntityByGUID(userId,
                        entityOneProxy.getGUID(),
                        entityOneProxy.getType().getTypeDefName());

                if (asset != null) {
                    invalidParameterHandler.validateAssetInSupportedZone(asset.getGUID(),
                            GUID_PARAMETER,
                            commonHandler.getAssetZoneMembership(asset.getClassifications()),
                            supportedZones,
                            serverUserName,
                            methodName);
                    assetCatalogConverter.addElement(assetCatalogItemElement, asset);
                    setConnections(userId, assetCatalogItemElement, asset);
                }
            }
        }
    }

    private void setConnections(String userId,
                                AssetCatalogItemElement assetCatalogItemElement,
                                EntityDetail asset)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        List<Connection> connections = getConnections(userId, asset.getGUID());
        assetCatalogItemElement.setConnections(connections);
    }

    private Optional<TypeDef> isComplexSchemaType(String typeDefName) {
        if (repositoryHelper.getKnownTypeDefGallery() == null
                || CollectionUtils.isEmpty(repositoryHelper.getKnownTypeDefGallery().getTypeDefs())) {
            return Optional.empty();
        }

        List<TypeDef> allTypes = repositoryHelper.getKnownTypeDefGallery().getTypeDefs();
        return allTypes.stream().filter(t -> t.getName().equals(typeDefName) && t.getSuperType().getName().equals(COMPLEX_SCHEMA_TYPE)).findAny();
    }

    private List<Connection> getConnections(String userId, String dataSetGuid)
            throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String methodName = "getConnections";
        List<EntityDetail> connections = assetHandler.getAttachedFilteredEntities(userId,
                dataSetGuid, GUID_PARAMETER, CONNECTION,
                CONNECTION_TO_ASSET, CONNECTION_TO_ASSET_GUID, 0, null,
                null, 0, false, false, 0, methodName);
        if (CollectionUtils.isNotEmpty(connections)) {
            List<Connection> connectionList = new ArrayList<>();

            for (EntityDetail entityDetail : connections) {
                Connection connection = new Connection(
                        entityDetail.getGUID(),
                        repositoryHelper.getStringProperty(sourceName, QUALIFIED_NAME, entityDetail.getProperties(), methodName));

                connectionList.add(connection);
            }
            return connectionList;
        }

        return Collections.emptyList();
    }

    private List<Classification> getEntityClassifications(String userId, String assetId, String assetTypeName) throws
            InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "getEntityClassifications";

        EntityDetail entityDetails = commonHandler.getEntityByGUID(userId, assetId, assetTypeName);

        invalidParameterHandler.validateAssetInSupportedZone(entityDetails.getGUID(),
                GUID_PARAMETER,
                commonHandler.getAssetZoneMembership(entityDetails.getClassifications()),
                supportedZones,
                userId,
                methodName);

        return entityDetails.getClassifications();
    }

    private List<EntityDetail> searchEntityByCriteria(String userId,
                                                      String searchCriteria,
                                                      String entityTypeGUID,
                                                      String entityTypeName,
                                                      SearchParameters searchParameters,
                                                      String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        String propertyName = NAME;
        if (commonHandler.hasDisplayName(userId, entityTypeGUID)) {
            propertyName = DISPLAY_NAME;
        }

        SequencingOrder sequencingOrder = searchParameters.getSequencingOrder() == null ? SequencingOrder.ANY : searchParameters.getSequencingOrder();
        List<EntityDetail> entitiesByPropertyValue = assetHandler.getEntitiesByValue(userId, searchCriteria,
                SEARCH_STRING_PARAMETER_NAME, entityTypeGUID, entityTypeName, Collections.singletonList(propertyName),
                searchParameters.getExactMatch(), searchParameters.getCaseInsensitive(),null, null, false,
                false, supportedZones, sequencingOrder.getName(),searchParameters.getFrom(),
                searchParameters.getPageSize(), clockService.getNow(), methodName);

        if (CollectionUtils.isNotEmpty(entitiesByPropertyValue)) {
            return entitiesByPropertyValue;
        }
        return new ArrayList<>();
    }

    private List<EntityDetail> searchEntityByType(String userId,
                                                      String entityTypeGUID,
                                                      String entityTypeName,
                                                      String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        List<EntityDetail> entitiesByPropertyValue = assetHandler.getEntitiesByType(userId, entityTypeGUID,
                entityTypeName, null,false,false, 0,
                20, clockService.getNow(), methodName);

        if (CollectionUtils.isNotEmpty(entitiesByPropertyValue)) {
            return entitiesByPropertyValue;
        }
        return new ArrayList<>();
    }

    private List<Classification> filterClassificationByName(List<Classification> classifications, String classificationName) {
        return classifications.stream().filter(classification -> classification.getName().equals(classificationName)).collect(Collectors.toList());
    }

    private InstanceGraph getAssetNeighborhood(String userId, String entityGUID, SearchParameters searchParameters, String serverName)
            throws AssetCatalogException, PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        OMRSMetadataCollection metadataCollection = commonHandler.getOMRSMetadataCollection();

        InstanceGraph entityNeighborhood = null;
        String methodName = "getAssetNeighborhood";
        try {
            entityNeighborhood = metadataCollection.getEntityNeighborhood(
                    userId,
                    entityGUID,
                    commonHandler.getTypesGUID(userId, searchParameters.getEntityTypes()),
                    searchParameters.getRelationshipTypeGUIDs(),
                    Collections.singletonList(InstanceStatus.ACTIVE),
                    searchParameters.getLimitResultsByClassification(),
                    null,
                    searchParameters.getLevel());
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException
                | TypeErrorException
                | FunctionNotSupportedException
                | PropertyErrorException | RepositoryErrorException e) {
            errorHandler.handleRepositoryError(e, methodName);
        } catch (EntityNotKnownException e) {
            errorHandler.handleUnknownEntity(e, entityGUID, searchParameters.getEntityTypes().get(0), methodName, GUID_PARAMETER);
        } catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException e) {
            errorHandler.handleUnauthorizedUser(userId, methodName);
        }

        if (entityNeighborhood == null) {
            throw new AssetCatalogException(AssetCatalogErrorCode.ASSET_NEIGHBORHOOD_NOT_FOUND.getMessageDefinition(entityGUID, serverName),
                    this.getClass().getName(),
                    methodName);
        }

        return entityNeighborhood;
    }

    /**
     *
     * @param userId      user identifier that issues the call
     * @param supportedTypesForSearch the list of types
     * @return a list of types and all of sub-types recursive
     */
    private List<Type> getSupportedTypesWithDescendants(String userId, String... supportedTypesForSearch) {
        List<Type> response = new ArrayList<>();
        for (String type : supportedTypesForSearch) {
            List<Type> typeContext = commonHandler.getTypeContext(userId, type);
            response.addAll(typeContext);
        }
        return response;
    }

    /**
     *
     * @param userId      user identifier that issues the call
     * @param supportedTypesForSearch the list of types
     * @return the list of types by names
     */
    private List<Type> getSupportedTypes(String userId, String... supportedTypesForSearch) {
        List<Type> response = new ArrayList<>();
        for (String typeName : supportedTypesForSearch) {
            Type type = commonHandler.getTypeByTypeDefName(userId, typeName);
            if(type != null) {
                response.add(type);
            }
        }
        return response;
    }

    private int orderElements(Elements firstAsset, Elements secondAsset, String sequencingProperty, SequencingOrder sequencingOrder) {
        String firstField;
        String secondField;
        if (TYPE_SEQUENCING.equals(sequencingProperty)) {
            if (firstAsset.getType() == null || secondAsset.getType() == null) {
                return 0;
            }
            firstField = firstAsset.getType().getName();
            secondField = secondAsset.getType().getName();
        } else {
            if (firstAsset.getProperties() == null || secondAsset.getProperties() == null) {
                return 0;
            }
            firstField = firstAsset.getProperties().get(sequencingProperty);
            secondField = secondAsset.getProperties().get(sequencingProperty);
            if (DISPLAY_NAME.equals(sequencingProperty)) {
                if (firstField == null) {
                    firstField = firstAsset.getProperties().get(NAME);
                }
                if (secondField == null) {
                    secondField = secondAsset.getProperties().get(NAME);
                }
            }
        }

        return compareFields(firstField, secondField, sequencingOrder);


    }

    private int compareFields(String firstComparedProperty, String secondComparedProperty, SequencingOrder sequencingOrder) {
        if (firstComparedProperty != null && secondComparedProperty != null) {
            if (sequencingOrder == SequencingOrder.PROPERTY_ASCENDING) {
                return firstComparedProperty.toLowerCase().compareTo(secondComparedProperty.toLowerCase());
            } else if (sequencingOrder == SequencingOrder.PROPERTY_DESCENDING) {
                return secondComparedProperty.toLowerCase().compareTo(firstComparedProperty.toLowerCase());
            }
        }
        if (firstComparedProperty == null && secondComparedProperty != null) {
            return 1;
        }
        if (firstComparedProperty != null &&  secondComparedProperty == null) {
            return -1;
        }
        return 0;
    }
}
