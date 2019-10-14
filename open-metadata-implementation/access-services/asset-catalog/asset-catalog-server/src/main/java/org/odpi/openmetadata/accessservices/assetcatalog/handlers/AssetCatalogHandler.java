/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.handlers;

import org.odpi.openmetadata.accessservices.assetcatalog.builders.AssetConverter;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogErrorCode;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetNotFoundException;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetElement;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Connection;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Element;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Term;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.*;

public class AssetCatalogHandler {

    private final String serviceName;
    private final String serverName;
    private final RepositoryHandler repositoryHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName             name of this service
     * @param serverName              name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler       manages calls to the repository services
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     */
    public AssetCatalogHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                               RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
    }

    public EntityDetail getEntity(String userId, String assetId, String assetType) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "getEntityDetails";
        final String guidParameter = "guid";

        return repositoryHandler.getEntityByGUID(userId, assetId, guidParameter, assetType, methodName);
    }

    public AssetDescription getEntityDetails(String userId, String assetId, String assetType) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "getEntityDetails";
        final String guidParameter = "guid";

        EntityDetail entityByGUID = repositoryHandler.getEntityByGUID(userId, assetId, guidParameter, assetType, methodName);
        AssetConverter converter = new AssetConverter(repositoryHelper);
        return converter.getAssetDescription(entityByGUID);
    }

    public List<org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship> getRelationshipsByEntityGUID(String userId, String assetId, String assetType, String relationshipType) throws UserNotAuthorizedException, PropertyServerException {
        String methodName = "getRelationshipsByEntityGUID";
        List<Relationship> relationshipsByType = repositoryHandler.getRelationshipsByType(userId, assetId, assetType, null, relationshipType, methodName);
        AssetConverter converter = new AssetConverter(repositoryHelper);
        return converter.convertRelationships(relationshipsByType);
    }

    public List<org.odpi.openmetadata.accessservices.assetcatalog.model.Classification> getEntityClassificationByName(String userId, String assetId, String assetType, String classificationName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        List<Classification> entityClassifications = getEntityClassifications(userId, assetId, assetType);
        AssetConverter converter = new AssetConverter(repositoryHelper);

        if (classificationName != null) {
            if (entityClassifications != null && !entityClassifications.isEmpty()) {
                entityClassifications = filterClassificationByName(entityClassifications, classificationName);
            }
        }

        return converter.convertClassifications(entityClassifications);
    }

    private List<Classification> getEntityClassifications(String userId, String assetId, String assetType) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "getEntityClassifications";

        EntityDetail entityDetails = repositoryHandler.getEntityByGUID(userId, assetId, "guidParameter", assetType, methodName);
        return entityDetails.getClassifications();
    }

    public List<org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship> getLinkingRelationshipsBetweenAssets(String serverName, String userId, String startAssetId, String endAssetId) throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, AssetNotFoundException {
        OMRSMetadataCollection metadataCollection = repositoryHandler.getMetadataCollection();

        InstanceGraph linkingEntities = metadataCollection.getLinkingEntities(userId,
                startAssetId,
                endAssetId,
                Collections.singletonList(InstanceStatus.ACTIVE),
                null);
        if (linkingEntities == null || linkingEntities.getRelationships() == null) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.LINKING_RELATIONSHIPS_NOT_FOUND;
            String errorMessage = errorCode.getErrorMessageId() +
                    errorCode.getFormattedErrorMessage(startAssetId, endAssetId, serverName);

            throw new AssetNotFoundException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    "getLinkingRelationshipsBetweenAssets",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
        AssetConverter converter = new AssetConverter(repositoryHelper);

        return converter.convertRelationships(linkingEntities.getRelationships());
    }


    public List<org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship> getRelationships(String userId, String assetGUID, String assetType,
                                                                                                       String relationshipTypeGUID, String relationshipTypeName,
                                                                                                       Integer startingFrom, Integer maximumResults) throws UserNotAuthorizedException, PropertyServerException {
        String methodName = "getRelationships";

        List<Relationship> pagedRelationshipsByType = repositoryHandler.getPagedRelationshipsByType(userId,
                assetGUID,
                assetType,
                relationshipTypeGUID,
                relationshipTypeName,
                startingFrom,
                maximumResults,
                methodName);

        if (pagedRelationshipsByType != null && !pagedRelationshipsByType.isEmpty()) {
            AssetConverter converter = new AssetConverter(repositoryHelper);
            return converter.convertRelationships(pagedRelationshipsByType);
        }

        return Collections.emptyList();
    }

    public String getTypeDefGUID(String userId, String typeDefName) {
        TypeDef typeDefByName = repositoryHelper.getTypeDefByName(userId, typeDefName);
        return Optional.ofNullable(typeDefByName).map(TypeDefLink::getName).orElse(null);
    }

    public List<AssetDescription> getIntermediateAssets(String userId, String startAssetId, String endAssetId) throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, AssetNotFoundException {

        OMRSMetadataCollection metadataCollection = repositoryHandler.getMetadataCollection();

        InstanceGraph linkingEntities = metadataCollection.getLinkingEntities(userId,
                startAssetId,
                endAssetId,
                Collections.singletonList(InstanceStatus.ACTIVE),
                null);
        if (linkingEntities == null || linkingEntities.getEntities() == null || linkingEntities.getEntities().isEmpty()) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.LINKING_ASSETS_NOT_FOUND;
            String errorMessage = errorCode.getErrorMessageId() +
                    errorCode.getFormattedErrorMessage(startAssetId, endAssetId, serverName);

            throw new AssetNotFoundException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    "getIntermediateAssets",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        AssetConverter converter = new AssetConverter(repositoryHelper);
        return converter.getAssetsDetails(linkingEntities.getEntities());
    }


    public List<AssetDescription> getRelatedAsset(String serverName, String userId, String startAssetId,
                                                  List<String> instanceTypes,
                                                  Integer limit, Integer offset,
                                                  String orderProperty) throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, AssetNotFoundException {
        OMRSMetadataCollection metadataCollection = repositoryHandler.getMetadataCollection();

        List<EntityDetail> relatedEntities = metadataCollection.getRelatedEntities(
                userId,
                startAssetId,
                instanceTypes,
                offset,
                Collections.singletonList(InstanceStatus.ACTIVE),
                null,
                null,
                orderProperty,
                SequencingOrder.ANY,
                limit);

        if (relatedEntities == null || relatedEntities.isEmpty()) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.NO_RELATED_ASSETS;
            String errorMessage = errorCode.getErrorMessageId() +
                    errorCode.getFormattedErrorMessage(startAssetId, serverName);

            throw new AssetNotFoundException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    "getRelatedAsset",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        AssetConverter converter = new AssetConverter(repositoryHelper);
        return converter.getAssetsDetails(relatedEntities);
    }


    public List<AssetDescription> getEntitiesFromNeighborhood(String serverName, String userId, String entityGUID, List<String> entityTypesGuid,
                                                              List<String> relationshipTypes, Integer level) throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, AssetNotFoundException {

        InstanceGraph entityNeighborhood = getAssetNeighborhood(serverName, userId, entityGUID, entityTypesGuid, relationshipTypes, level);

        List<EntityDetail> entities = entityNeighborhood.getEntities();
        if (entities == null || entities.isEmpty()) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.NO_ASSET_FROM_NEIGHBORHOOD_NOT_FOUND;
            String errorMessage = errorCode.getErrorMessageId() +
                    errorCode.getFormattedErrorMessage(entityGUID, serverName);

            throw new AssetNotFoundException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    "getEntitiesFromNeighborhood",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        AssetConverter converter = new AssetConverter(repositoryHelper);
        return converter.getAssetsDetails(entities);
    }

    public List<Term> searchAssetsAndGlossaryTerms(String userId, String searchCriteria, SearchParameters searchParameters) throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException {

        List<EntityDetail> entities = searchEntityByCriteria(userId, searchCriteria, GLOSSARY_TERM_GUID, searchParameters);
        List<EntityDetail> assets = searchEntityByCriteria(userId, searchCriteria, ASSET_GUID, searchParameters);
        entities.addAll(assets);

        return entities.stream().map(this::buildTerm).collect(Collectors.toList());
    }

    private List<EntityDetail> searchEntityByCriteria(String userId, String searchCriteria, String entityTypeGUID, SearchParameters searchParameters)
            throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException {

        OMRSMetadataCollection metadataCollection = repositoryHandler.getMetadataCollection();
        List<EntityDetail> entitiesByPropertyValue = metadataCollection.findEntitiesByPropertyValue(userId,
                entityTypeGUID,
                searchCriteria,
                searchParameters.getOffset() != null ? searchParameters.getOffset() : 0,
                Collections.singletonList(InstanceStatus.ACTIVE),
                null,
                null,
                searchParameters.getOrderProperty(),
                SequencingOrder.ANY,
                searchParameters.getLimit() != null ? searchParameters.getLimit() : 0);

        if (entitiesByPropertyValue != null) {
            return entitiesByPropertyValue;
        }

        return new ArrayList<>();
    }

    private List<Classification> filterClassificationByName(List<Classification> classifications, String classificationName) {
        return classifications.stream().filter(classification -> classification.getName().equals(classificationName)).collect(Collectors.toList());
    }

    private InstanceGraph getAssetNeighborhood(String serverName, String userId, String entityGUID, List<String> entityTypesGuid,
                                               List<String> relationshipTypes, Integer level)
            throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException,
            EntityNotKnownException,
            FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException,
            RepositoryErrorException,
            PropertyErrorException,
            TypeErrorException, AssetNotFoundException {
        OMRSMetadataCollection metadataCollection = repositoryHandler.getMetadataCollection();

        InstanceGraph entityNeighborhood = metadataCollection.getEntityNeighborhood(
                userId,
                entityGUID,
                entityTypesGuid,
                relationshipTypes,
                Collections.singletonList(InstanceStatus.ACTIVE),
                null,
                null,
                level);

        if (entityNeighborhood == null) {
            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.ASSET_NEIGHBORHOOD_NOT_FOUND;
            String errorMessage = errorCode.getErrorMessageId() +
                    errorCode.getFormattedErrorMessage(entityGUID, serverName);

            throw new AssetNotFoundException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    "getAssetNeighborhood",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return entityNeighborhood;
    }

    public Term buildContextByAssetType(String userId, AssetCatalogHandler assetCatalogHandler, EntityDetail entityDetail, String typeDefName, List<String> superTypes) throws org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, FunctionNotSupportedException, PropertyErrorException, EntityProxyOnlyException, EntityNotKnownException, PagingErrorException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, TypeErrorException, RepositoryErrorException {
        AssetElement assetElement = new AssetElement();
        Map<String, List<Connection>> knownAssetConnection = new HashMap<>();

        if (typeDefName.equals(GLOSSARY_TERM)) {
            return assetCatalogHandler.getStructureForGlossaryTerm(userId, knownAssetConnection, entityDetail);
        } else if (typeDefName.equals(DEPLOYED_API)) {
            return assetCatalogHandler.getContextForDeployedAPI(userId, entityDetail, assetElement);
        } else if (typeDefName.equals(IT_INFRASTRUCTURE) || superTypes.contains(IT_INFRASTRUCTURE)) {
            return assetCatalogHandler.getContextForInfrastructure(userId, knownAssetConnection, entityDetail, assetElement);
        } else if (typeDefName.equals(PROCESS) || superTypes.contains(PROCESS)) {
            return assetCatalogHandler.getContextForProcess(userId, knownAssetConnection, entityDetail, assetElement);
        } else if (typeDefName.equals(DATA_STORE) || superTypes.contains(DATA_STORE)) {
            return assetCatalogHandler.getContextForDataStore(userId, knownAssetConnection, entityDetail, assetElement);
        } else {
            Term term = buildTerm(entityDetail);
            assetCatalogHandler.getContextForDataSet(userId, knownAssetConnection, entityDetail, assetElement);
            term.setElements(Collections.singletonList(assetElement));
            return term;
        }
    }

    private Term getStructureForGlossaryTerm(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail glossaryTerm) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String method = "getStructureForGlossaryTerm";
        Term term = buildTerm(glossaryTerm);

        List<EntityDetail> schemas = repositoryHandler.getEntitiesForRelationshipType(userId,
                glossaryTerm.getGUID(),
                GLOSSARY_TERM,
                SEMANTIC_ASSIGNMENT_GUID,
                SEMANTIC_ASSIGNMENT,
                0,
                0,
                method);

        if (schemas == null || schemas.isEmpty()) {
            return term;
        }

        List<AssetElement> assets = new ArrayList<>(schemas.size());

        for (EntityDetail schema : schemas) {
            AssetElement assetElement = new AssetElement();
            List<Element> elements = new ArrayList<>();
            elements.add(buildElement(schema));
            assetElement.setContext(elements);

            findAsset(userId, Collections.singletonList(schema), assetElement, knownAssetConnection);
            assets.add(assetElement);
        }

        term.setElements(assets);

        return term;
    }

    private Term getContextForDeployedAPI(String userId, EntityDetail entityDetail, AssetElement assetElement) throws UserNotAuthorizedException, PropertyServerException {
        String method = "getContextForDeployedAPI";
        Term term = buildTerm(entityDetail);

        List<EntityDetail> endpoints = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                entityDetail.getGUID(),
                DEPLOYED_API,
                API_ENDPOINT_GUID,
                API_ENDPOINT,
                0,
                0,
                method);
        if (endpoints == null || endpoints.isEmpty()) {
            return term;
        }

        for (EntityDetail endpoint : endpoints) {
            addContextElement(assetElement, endpoint);
            getConnectionContext(userId, endpoint, assetElement);
        }

        return term;
    }

    private Term getContextForInfrastructure(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail, AssetElement assetElement) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        Term term = buildTerm(entityDetail);

        switch (entityDetail.getType().getTypeDefName()) {
            case HOST:
                getContextForHost(userId, knownAssetConnection, entityDetail, assetElement);
                break;
            case NETWORK:
                getContextForNetwork(userId, knownAssetConnection, entityDetail, assetElement);
                break;
            case SOFTWARE_SERVER_PLATFORM:
                getContextForSoftwareServerPlatform(userId, knownAssetConnection, entityDetail, assetElement);
                break;
            case SOFTWARE_SERVER:
                getContextForSoftwareServer(userId, knownAssetConnection, entityDetail, assetElement);
                break;
            default:
                break;
        }

        term.setElements(Collections.singletonList(assetElement));
        return term;
    }

    private Term getContextForProcess(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail, AssetElement assetElement) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String method = "getContextForProcess";
        Term term = buildTerm(entityDetail);

        List<EntityDetail> ports = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                entityDetail.getGUID(),
                PROCESS,
                PROCESS_PORT_GUID,
                PROCESS_PORT,
                0,
                0,
                method);

        if (ports != null && !ports.isEmpty()) {
            for (EntityDetail port : ports) {
                addContextElement(assetElement, port);
                if (port.getType().getTypeDefName().equals(PORT_IMPLEMENTATION)) {
                    EntityDetail schemaType = repositoryHandler.getEntityForRelationshipType(userId,
                            port.getGUID(),
                            DATABASE,
                            PORT_SCHEMA_GUID,
                            PORT_SCHEMA,
                            method);

                    if (schemaType != null) {
                        addElement(assetElement, schemaType);
                        getContextForSchemaType(userId, assetElement, knownAssetConnection, schemaType);
                    }
                }
            }
        }

        return term;
    }

    private Term getContextForDataStore(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail, AssetElement assetElement) throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, FunctionNotSupportedException, PropertyErrorException, EntityProxyOnlyException, EntityNotKnownException, PagingErrorException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, TypeErrorException, RepositoryErrorException, UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        Term term = buildTerm(entityDetail);

        if (entityDetail.getType().getTypeDefName().equals(DATABASE)) {
            getContextForDatabase(userId, knownAssetConnection, entityDetail, assetElement);
        } else {
            if (entityDetail.getType().getTypeDefName().equals(DATA_FILE)) {
                getContextForDataFile(userId, knownAssetConnection, entityDetail, assetElement);
            } else if (entityDetail.getType().getTypeDefName().equals(FILE_FOLDER)) {
                getContextForFileFolder(userId, knownAssetConnection, entityDetail, assetElement);
            }
        }
        term.setElements(Collections.singletonList(assetElement));

        return term;
    }

    private void getContextForDatabase(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail, AssetElement assetElement) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String method = "getContextForDatabase";

        List<EntityDetail> dataSets = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                entityDetail.getGUID(),
                DATABASE,
                DATA_CONTENT_FOR_DATA_SET_GUID,
                DATA_CONTENT_FOR_DATA_SET,
                0,
                0,
                method);

        if (dataSets != null && !dataSets.isEmpty()) {
            for (EntityDetail dataSet : dataSets) {
                getContextForDataSet(userId, knownAssetConnection, dataSet, assetElement);
            }
        }
    }

    private void getContextForDataSet(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail dataSet, AssetElement assetElement) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String method = "getContextForDataSet";

        EntityDetail schemaType = repositoryHandler.getEntityForRelationshipType(
                userId,
                dataSet.getGUID(),
                DATA_SET,
                ASSET_SCHEMA_TYPE_GUID,
                ASSET_SCHEMA_TYPE,
                method);


        if (schemaType == null) {
            return;
        }

        addElement(assetElement, schemaType);
        if (isComplexSchemaType(schemaType.getType().getTypeDefName()).isPresent()) {
            getContextForSchemaType(userId, assetElement, knownAssetConnection, schemaType);
        } else {
            getAsset(userId, assetElement, knownAssetConnection, schemaType);
        }
    }

    private void getContextForFileFolder(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail, AssetElement assetElement) throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, PropertyErrorException, EntityProxyOnlyException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, TypeErrorException, RepositoryErrorException, UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String method = "getContextForFileFolder";

        List<EntityDetail> connections = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                entityDetail.getGUID(),
                FILE_FOLDER,
                CONNECTION_TO_ASSET_GUID,
                CONNECTION_TO_ASSET,
                0,
                0,
                method);


        if (connections != null && !connections.isEmpty()) {
            setConnections(userId, assetElement, knownAssetConnection, entityDetail);
            return;
        }


        List<Relationship> parentFolderRelationships = repositoryHandler.getRelationshipsByType(userId,
                entityDetail.getGUID(), entityDetail.getType().getTypeDefName(),
                FOLDER_HIERARCHY_GUID, FOLDER_HIERARCHY, method);

        if (parentFolderRelationships == null || parentFolderRelationships.isEmpty()) {
            return;
        }

        parentFolderRelationships = parentFolderRelationships.stream().filter(s -> s.getEntityTwoProxy().getGUID().equals(entityDetail.getGUID())).collect(Collectors.toList());
        if (parentFolderRelationships.size() != 1) {
            return;
        }
        List<EntityDetail> parentFolders = getEntitiesDetailsFromRelationships(userId, entityDetail.getGUID(), parentFolderRelationships);

        getContextForEachParentFolder(userId, knownAssetConnection, assetElement, parentFolders);
    }

    private void getContextForEachParentFolder(String userId, Map<String, List<Connection>> knownAssetConnection, AssetElement assetElement, List<EntityDetail> parentFolders) throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, PropertyErrorException, EntityProxyOnlyException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, TypeErrorException, RepositoryErrorException, UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        for (EntityDetail folder : parentFolders) {
            addElement(assetElement, folder);

            getContextForFileFolder(userId, knownAssetConnection, folder, assetElement);
        }
    }

    private void getContextForDataFile(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail, AssetElement assetElement) throws org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, PropertyErrorException, EntityProxyOnlyException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, TypeErrorException, RepositoryErrorException, UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String method = "getContextForDataFile";

        List<EntityDetail> fileFolders = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                entityDetail.getGUID(),
                DATA_FILE,
                NESTED_FILE_GUID,
                NESTED_FILE,
                0,
                0,
                method);
        if (fileFolders == null || fileFolders.isEmpty()) {
            return;
        }

        getContextForEachParentFolder(userId, knownAssetConnection, assetElement, fileFolders);
    }


    private void getContextForSoftwareServerPlatform(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail, AssetElement assetElement) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String method = "getContextForSoftwareServerPlatform";

        EntityDetail host = repositoryHandler.getEntityForRelationshipType(
                userId,
                entityDetail.getGUID(),
                entityDetail.getType().getTypeDefName(),
                SOFTWARE_SERVER_PLATFORM_DEPLOYMENT_GUID,
                SOFTWARE_SERVER_PLATFORM_DEPLOYMENT,
                method);

        if (host != null) {
            addElement(assetElement, host);
            getContextForHost(userId, knownAssetConnection, host, assetElement);
        }
    }

    private void getContextForNetwork(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail, AssetElement assetElement) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String method = "getContextForNetwork";

        List<EntityDetail> networkGateways = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                entityDetail.getGUID(),
                NETWORK,
                NETWORK_GATEWAY_LINK_GUID,
                NETWORK_GATEWAY_LINK,
                0,
                0,
                method);

        if (networkGateways != null) {
            networkGateways.forEach(element -> addElement(assetElement, element));
        }

        List<EntityDetail> hosts = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                entityDetail.getGUID(),
                NETWORK,
                HOST_NETWORK_GUID,
                HOST_NETWORK,
                0,
                0,
                method);


        if (hosts != null && !hosts.isEmpty()) {
            for (EntityDetail host : hosts) {
                addElement(assetElement, host);
                getContextForHost(userId, knownAssetConnection, host, assetElement);
            }
        }
    }

    private void getContextForHost(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail, AssetElement assetElement) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String method = "getContextForHost";
        List<EntityDetail> hosts = null;
        if (entityDetail.getType().getTypeDefName().equals(VIRTUAL_CONTAINER)) {
            hosts = repositoryHandler.getEntitiesForRelationshipType(userId, entityDetail.getGUID(), HOST, DEPLOYED_VIRTUAL_CONTAINER_GUID, DEPLOYED_VIRTUAL_CONTAINER, 0, 0, method);


        } else if (entityDetail.getType().getTypeDefName().equals(HOST_CLUSTER)) {
            hosts = repositoryHandler.getEntitiesForRelationshipType(
                    userId,
                    entityDetail.getGUID(),
                    HOST,
                    HOST_CLUSTER_MEMBER_GUID,
                    HOST_CLUSTER_MEMBER,
                    0,
                    0,
                    method);
        }

        if (hosts != null) {
            hosts.forEach(element -> addElement(assetElement, element));
        }

        EntityDetail operatingPlatform = repositoryHandler.getEntityForRelationshipType(userId,
                entityDetail.getGUID(),
                entityDetail.getType().getTypeDefName(),
                HOST_OPERATING_PLATFORM_GUID,
                HOST_OPERATING_PLATFORM,
                method);


        addElement(assetElement, operatingPlatform);

        List<EntityDetail> locations = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                entityDetail.getGUID(),
                HOST,
                HOST_LOCATION_GUID,
                HOST_LOCATION,
                0,
                0,
                method);
        if (locations != null) {
            for (EntityDetail location : locations) {
                addElement(assetElement, location);
                getContextForLocation(userId, assetElement, knownAssetConnection, location);
            }
        }
    }

    private void getContextForLocation(String userId, AssetElement assetElement, Map<String, List<Connection>> knownAssetConnection, EntityDetail location) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String method = "getContextForLocation";

        List<EntityDetail> assetLocations = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                location.getGUID(),
                LOCATION,
                ASSET_LOCATION_GUID,
                ASSET_LOCATION,
                0,
                0,
                method);

        if (assetLocations != null && !assetLocations.isEmpty()) {
            for (EntityDetail assetLocation : assetLocations) {
                addElement(assetElement, assetLocation);
                getAsset(userId, assetElement, knownAssetConnection, assetLocation);
            }
        }

        List<EntityDetail> nestedLocations = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                location.getGUID(),
                LOCATION,
                NESTED_LOCATION_GUID,
                NESTED_LOCATION,
                0,
                0,
                method);


        if (nestedLocations != null && !nestedLocations.isEmpty()) {
            for (EntityDetail nestedLocation : nestedLocations) {
                addElement(assetElement, nestedLocation);
                getContextForLocation(userId, assetElement, knownAssetConnection, nestedLocation);
            }
        }
    }

    private void getContextForSoftwareServer(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail, AssetElement assetElement) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String method = "getContextForSoftwareServer";
        Element parentElement = null;

        EntityDetail softwareServerPlatform = repositoryHandler.getEntityForRelationshipType(
                userId,
                entityDetail.getGUID(),
                SOFTWARE_SERVER,
                SOFTWARE_SERVER_DEPLOYMENT_GUID,
                SOFTWARE_SERVER_DEPLOYMENT,
                method);


        if (softwareServerPlatform != null) {
            parentElement = getLastNode(assetElement);
            addElement(assetElement, softwareServerPlatform);
            getContextForSoftwareServerPlatform(userId, knownAssetConnection, softwareServerPlatform, assetElement);
        }

        EntityDetail endpoint = repositoryHandler.getEntityForRelationshipType(
                userId,
                entityDetail.getGUID(),
                SOFTWARE_SERVER,
                SERVER_ENDPOINT_GUID,
                SERVER_ENDPOINT,
                method);

        if (endpoint != null) {
            if (parentElement != null) {
                addChildElement(parentElement, Collections.singletonList(buildElement(endpoint)));
            } else {
                addContextElement(assetElement, endpoint);
            }
            getConnectionContext(userId, endpoint, assetElement);
        }

    }

    private void getConnectionContext(String userId, EntityDetail endpoint, AssetElement assetElement) throws UserNotAuthorizedException, PropertyServerException {
        String method = "getConnectionContext";

        List<EntityDetail> connections = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                endpoint.getGUID(),
                ENDPOINT,
                CONNECTION_ENDPOINT_GUID,
                CONNECTION_ENDPOINT,
                0,
                0,
                method);

        if (connections == null) {
            return;
        }

        for (EntityDetail connection : connections) {
            addElement(assetElement, connection);

            List<EntityDetail> elements = new ArrayList<>();
            EntityDetail connectorType = repositoryHandler.getEntityForRelationshipType(
                    userId,
                    connection.getGUID(),
                    CONNECTION,
                    CONNECTION_CONNECTOR_TYPE_GUID,
                    CONNECTION_CONNECTOR_TYPE,
                    method);

            if (connectorType != null) {
                elements.add(connectorType);
            }

            EntityDetail asset = repositoryHandler.getEntityForRelationshipType(
                    userId,
                    connection.getGUID(),
                    CONNECTION,
                    CONNECTION_TO_ASSET_GUID,
                    CONNECTION_TO_ASSET,
                    method);
            elements.add(asset);
            elements.forEach(element -> addElement(assetElement, element));
        }
    }

    private void findAsset(String userId, List<EntityDetail> entitiesByType, AssetElement assetElement,
                           Map<String, List<Connection>> knownAssetConnection) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {

        String method = "findAsset";
        for (EntityDetail entityDetail : entitiesByType) {
            List<EntityDetail> theEndOfRelationship = repositoryHandler.getEntitiesForRelationshipType(
                    userId,
                    entityDetail.getGUID(),
                    SCHEMA_ATTRIBUTE,
                    ATTRIBUTE_FOR_SCHEMA_GUID,
                    ATTRIBUTE_FOR_SCHEMA,
                    0,
                    0,
                    method);
            if (theEndOfRelationship == null || theEndOfRelationship.isEmpty()) {
                return;
            }


            for (EntityDetail entity : theEndOfRelationship) {
                addElement(assetElement, entity);

                Optional<TypeDef> isComplexSchemaType = isComplexSchemaType(entity.getType().getTypeDefName());
                if (isComplexSchemaType.isPresent()) {
                    setAssetDetails(userId, assetElement, knownAssetConnection, entity);
                    return;
                } else {
                    List<EntityDetail> schemaAttributeTypeEntities = repositoryHandler.getEntitiesForRelationshipType(
                            userId,
                            entityDetail.getGUID(),
                            entityDetail.getType().getTypeDefName(),
                            SCHEMA_ATTRIBUTE_TYPE_GUID,
                            SCHEMA_ATTRIBUTE_TYPE,
                            0,
                            0,
                            method);
                    if (schemaAttributeTypeEntities != null) {
                        if (!schemaAttributeTypeEntities.isEmpty()) {
                            schemaAttributeTypeEntities.forEach(element -> addElement(assetElement, element));
                        }

                        findAsset(userId, schemaAttributeTypeEntities, assetElement, knownAssetConnection);
                    }
                }
            }
        }
    }

    private void getContextForSchemaType(String userId, AssetElement assetElement, Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        Optional<TypeDef> isComplexSchemaType = isComplexSchemaType(entityDetail.getType().getTypeDefName());
        String method = "getContextForSchemaType";
        if (isComplexSchemaType.isPresent()) {
            setAssetDetails(userId, assetElement, knownAssetConnection, entityDetail);
        } else {
            List<EntityDetail> attributeForSchemas = repositoryHandler.getEntitiesForRelationshipType(
                    userId,
                    entityDetail.getGUID(),
                    entityDetail.getType().getTypeDefName(),
                    ATTRIBUTE_FOR_SCHEMA_GUID,
                    ATTRIBUTE_FOR_SCHEMA,
                    0,
                    0,
                    method);
            if (attributeForSchemas != null) {
                for (EntityDetail attributeForSchema : attributeForSchemas) {
                    addElement(assetElement, attributeForSchema);

                    if (isComplexSchemaType(attributeForSchema.getType().getTypeDefName()).isPresent()) {
                        setAssetDetails(userId, assetElement, knownAssetConnection, attributeForSchema);
                        return;
                    } else {
                        List<EntityDetail> schemaAttributeTypeEntities = repositoryHandler.getEntitiesForRelationshipType(
                                userId,
                                attributeForSchema.getGUID(),
                                attributeForSchema.getType().getTypeDefName(),
                                SCHEMA_ATTRIBUTE_TYPE_GUID,
                                SCHEMA_ATTRIBUTE_TYPE,
                                0,
                                0,
                                method);

                        schemaAttributeTypeEntities.forEach(element -> addElement(assetElement, element));
                        for (EntityDetail schema : schemaAttributeTypeEntities) {
                            getContextForSchemaType(userId, assetElement, knownAssetConnection, schema);
                        }
                    }
                }
            }
        }
    }

    private void setAssetDetails(String userId, AssetElement assetElement, Map<String, List<Connection>> knownAssetConnection, EntityDetail entity) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String method = "setAssetDetails";

        EntityDetail dataSet = repositoryHandler.getEntityForRelationshipType(userId,
                entity.getGUID(),
                entity.getType().getTypeDefName(),
                ASSET_SCHEMA_TYPE_GUID,
                ASSET_SCHEMA_TYPE,
                method);

        if (assetElement.getContext() != null && dataSet != null) {
            addElement(assetElement, dataSet);
        } else {
            assetElement.setContext(Collections.singletonList(buildElement(dataSet)));
        }

        getAsset(userId, assetElement, knownAssetConnection, dataSet);
    }

    private void getAsset(String userId, AssetElement assetElement, Map<String, List<Connection>> knownAssetConnection, EntityDetail dataSet) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        if (dataSet == null) return;

        String method = "getAsset";
        List<Relationship> assetToDataSetRelationships = repositoryHandler.getRelationshipsByType(userId,
                dataSet.getGUID(), dataSet.getType().getTypeDefName(),
                DATA_CONTENT_FOR_DATA_SET_GUID, DATA_CONTENT_FOR_DATA_SET, method);

        if (assetToDataSetRelationships == null || assetToDataSetRelationships.isEmpty()) {
            return;
        }

        for (Relationship assetToDataSetRelationship : assetToDataSetRelationships) {
            EntityProxy entityOneProxy = assetToDataSetRelationship.getEntityOneProxy();
            if (entityOneProxy.getGUID().equals(dataSet.getGUID())) {
                setConnections(userId, assetElement, knownAssetConnection, dataSet);
            } else {
                EntityDetail asset = repositoryHandler.getEntityByGUID(userId, entityOneProxy.getGUID(), "guidParameter", entityOneProxy.getType().getTypeDefName(), method);

                setAssetElementAttributes(assetElement, asset);
                setConnections(userId, assetElement, knownAssetConnection, asset);
            }
        }
    }

    private void setConnections(String userId, AssetElement assetElement, Map<String, List<Connection>> knownAssetConnection, EntityDetail asset) throws UserNotAuthorizedException, PropertyServerException {
        if (knownAssetConnection.containsKey(asset.getGUID())) {
            assetElement.setConnections(knownAssetConnection.get(asset.getGUID()));
        } else {
            List<Connection> connections = getConnections(userId, asset.getGUID());
            knownAssetConnection.put(asset.getGUID(), connections);
            assetElement.setConnections(connections);
        }
    }

    private void setAssetElementAttributes(AssetElement assetElement, EntityDetail asset) {
        String method = "setAssetElementAttributes";
        assetElement.setGuid(asset.getGUID());
        assetElement.setType(asset.getType().getTypeDefName());
        assetElement.setProperties(repositoryHelper.getInstancePropertiesAsMap(asset.getProperties()));
        assetElement.setQualifiedName(repositoryHelper.getStringProperty(ASSET_CATALOG_OMAS, QUALIFIED_NAME, asset.getProperties(), method));
    }

    private Element buildElement(EntityDetail entityDetail) {
        return buildTerm(entityDetail);
    }

    private Term buildTerm(EntityDetail entity) {
        String method = "buildTerm";

        Term term = new Term();
        term.setGuid(entity.getGUID());
        term.setType(entity.getType().getTypeDefName());
        term.setQualifiedName(repositoryHelper.getStringProperty(ASSET_CATALOG_OMAS, QUALIFIED_NAME, entity.getProperties(), method));
        term.setProperties(repositoryHelper.getInstancePropertiesAsMap(entity.getProperties()));

        return term;
    }

    private Optional<TypeDef> isComplexSchemaType(String typeDefName) {
        List<TypeDef> allTypes = repositoryHelper.getActiveTypeDefs();
        return allTypes.stream().filter(t -> t.getName().equals(typeDefName) && t.getSuperType().getName().equals(COMPLEX_SCHEMA_TYPE)).findAny();
    }

    private List<Connection> getConnections(String userId, String dataSetGuid) throws UserNotAuthorizedException, PropertyServerException {
        String method = "getConnections";
        List<EntityDetail> connections = repositoryHandler.getEntitiesForRelationshipType(
                userId,
                dataSetGuid,
                CONNECTION,
                CONNECTION_TO_ASSET_GUID,
                CONNECTION_TO_ASSET,
                0,
                0,
                method);


        if (connections != null && !connections.isEmpty()) {
            return connections.stream()
                    .map(t -> new Connection(t.getGUID(), repositoryHelper.getStringProperty(ASSET_CATALOG_OMAS, QUALIFIED_NAME, t.getProperties(), method)))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private List<EntityDetail> getEntitiesDetailsFromRelationships(String userId, String assetId, List<Relationship> relationships) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        List<EntityDetail> entityDetails = new ArrayList<>(relationships.size());
        for (Relationship relationship : relationships) {
            entityDetails.add(getThePairEntity(userId, assetId, relationship));
        }
        return entityDetails;
    }

    private EntityDetail getThePairEntity(String userId, String entityDetailGUID, Relationship relationship) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String method = "getThePairEntity";
        String guidParam = "guidParam";

        if (relationship.getEntityOneProxy().getGUID().equals(entityDetailGUID)) {
            return repositoryHandler.getEntityByGUID(userId, relationship.getEntityTwoProxy().getGUID(), guidParam, relationship.getEntityTwoProxy().getType().getTypeDefName(), method);
        } else {
            return repositoryHandler.getEntityByGUID(userId, relationship.getEntityOneProxy().getGUID(), guidParam, relationship.getEntityOneProxy().getType().getTypeDefName(), method);
        }
    }

    private Element lastElementAdded(Element tree) {
        List<Element> innerElement = tree.getParentElement();
        if (innerElement == null) {
            return tree;
        }
        return lastElementAdded(innerElement.get(innerElement.size() - 1));
    }

    private void addElement(AssetElement assetElement, EntityDetail entityDetail) {
        List<Element> context = assetElement.getContext();
        List<Element> elements = new ArrayList<>();
        elements.add(buildElement(entityDetail));

        if (context != null) {
            Element leaf = lastElementAdded(context.get(context.size() - 1));
            leaf.setParentElement(elements);
        } else {
            assetElement.setContext(elements);
        }
    }


    private Element getLastNode(AssetElement assetElement) {
        List<Element> context = assetElement.getContext();

        if (context != null) {
            return lastElementAdded(context.get(context.size() - 1));
        } else {
            return null;
        }
    }

    private void addChildElement(Element parentElement, List<Element> elements) {
        if (parentElement != null) {
            if (parentElement.getParentElement() != null) {
                parentElement.getParentElement().addAll(elements);
            } else {
                parentElement.setParentElement(elements);
            }
        }
    }

    private void addContextElement(AssetElement assetElement, EntityDetail entityDetail) {
        List<Element> context = assetElement.getContext();
        if (context == null) {
            context = new ArrayList<>();
        }
        context.add(buildElement(entityDetail));
        assetElement.setContext(context);
    }

}
