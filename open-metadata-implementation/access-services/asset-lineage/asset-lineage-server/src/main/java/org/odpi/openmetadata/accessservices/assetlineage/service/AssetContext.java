/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.service;

import org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception.*;
import org.odpi.openmetadata.accessservices.assetlineage.model.*;
import org.odpi.openmetadata.accessservices.assetlineage.model.rest.responses.AssetResponse;
import org.odpi.openmetadata.accessservices.assetlineage.util.Constants;
import org.odpi.openmetadata.accessservices.assetlineage.util.Converter;
import org.odpi.openmetadata.accessservices.assetlineage.util.ExceptionHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.*;

public class AssetContext {

    private static AssetLineageInstanceHandler instanceHandler = new AssetLineageInstanceHandler();
    private OMRSMetadataCollection metadataCollectionForSearch;
    private Converter converter = new Converter();
    private TypeDefGallery allTypes = new TypeDefGallery();
    private String serverName;
    private ExceptionHandler exceptionUtil = new ExceptionHandler();


    public AssetResponse buildAssetContext(String serverName, String userId, String assetId){
        AssetResponse response = new AssetResponse();

        try {
            setMetadataRepositoryDetails(serverName, userId);
            EntityDetail entityDetail = getEntityDetails(serverName, userId, assetId);
            Map<String, List<Connection>> knownAssetConnection = new HashMap<>();

            String typeDefName = entityDetail.getType().getTypeDefName();
            if(typeDefName.equals(GLOSSARY_TERM)){
                Term term = getStructureForGlossaryTerm(userId, knownAssetConnection, entityDetail);
                response.setAssets(Collections.singletonList(term));
            } else {
                Term term = buildTerm(entityDetail);
                AssetElement assetElement = new AssetElement();

                if(isAsset(typeDefName).isPresent()){
                    getAsset(userId, assetElement, knownAssetConnection, entityDetail);
                } else {
                    buildContextForAsset(userId, assetElement, knownAssetConnection, entityDetail);
                }

                term.setElements(Collections.singletonList(assetElement));
                response.setAssets(Collections.singletonList(term));
            }

        } catch (UserNotAuthorizedException | PagingErrorException | TypeErrorException | PropertyErrorException | RepositoryErrorException | InvalidParameterException | FunctionNotSupportedException | TypeDefNotKnownException | EntityNotKnownException | EntityProxyOnlyException e) {
            exceptionUtil.captureOMRSCheckedExceptionBase(response, e);
        } catch (PropertyServerException | AssetNotFoundException e) {
            exceptionUtil.captureAssetLineageExeption(response, e);
        }

        return response;
    }

    private Optional<TypeDef> isAsset(String typeDefName){
        Optional<TypeDef> typeDefStream = allTypes.getTypeDefs().stream().filter(t -> t.getName().equals(typeDefName)).findAny();

        if(typeDefStream.isPresent()) {
            Optional<TypeDef> superType = allTypes.getTypeDefs().stream().filter(t -> t.getName().equals(typeDefStream.get().getSuperType().getName())).findAny();
            return typeDefStream.map(typeDef -> allTypes.getTypeDefs().stream().filter(t -> t.getName().equals(
                    superType.get().getName()) && t.getSuperType().getName().equals(ASSET)).findAny()).orElse(null);
        }
        return null;
    }


    private EntityDetail getEntityDetails(String serverName, String userId, String assetId) throws UserNotAuthorizedException, RepositoryErrorException, EntityProxyOnlyException, InvalidParameterException, EntityNotKnownException, AssetNotFoundException, PropertyServerException {
        OMRSMetadataCollection metadataCollection = instanceHandler.getMetadataCollection(serverName);
        EntityDetail entityDetail = metadataCollection.getEntityDetail(userId, assetId);

        if (entityDetail == null) {
            AssetLineageErrorCode errorCode = AssetLineageErrorCode.ASSET_NOT_FOUND;
            String errorMessage = errorCode.getErrorMessageId() +
                    errorCode.getFormattedErrorMessage(assetId, serverName);

            throw new AssetNotFoundException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    "searchForRelationships",
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return entityDetail;
    }
    
    private Term getStructureForGlossaryTerm(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail glossaryTerm) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {
        Term term = buildTerm(glossaryTerm);

        List<EntityDetail> schemas = getTheEndsRelationship(userId, glossaryTerm.getGUID(), SEMANTIC_ASSIGNMENT);
        List<AssetElement> assets = new ArrayList<>(schemas.size());

        for (EntityDetail schema : schemas) {
            AssetElement assetElement = new AssetElement();
            Element firstElement = buildElement(schema);
            List<Element> elements = new ArrayList<>();
            elements.add(firstElement);
            assetElement.setContext(elements);

            findAsset(userId, Collections.singletonList(schema), assetElement, knownAssetConnection);
            assets.add(assetElement);
        }

        term.setElements(assets);

        return term;
    }

    private void findAsset(String userId, List<EntityDetail> entitiesByType, AssetElement assetElement,
                           Map<String, List<Connection>> knownAssetConnection) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {

        for (EntityDetail entityDetail : entitiesByType) {
            List<EntityDetail> theEndOfRelationship = getTheEndsRelationship(userId, entityDetail.getGUID(), ATTRIBUTE_FOR_SCHEMA);
            for (EntityDetail entity : theEndOfRelationship) {
                addElement(assetElement, buildElement(entity));

                Optional<TypeDef> isComplexSchemaType = isComplexSchemaType(entity.getType().getTypeDefName());
                if (isComplexSchemaType.isPresent()) {
                    setAssetDetails(userId, assetElement, knownAssetConnection, entity);
                    return;
                } else {
                    List<EntityDetail> schemaAttributeTypeEntities = getTheEndsRelationship(userId, entity.getGUID(), SCHEMA_ATTRIBUTE_TYPE);
                    getSubElements(assetElement, schemaAttributeTypeEntities);

                    findAsset(userId, schemaAttributeTypeEntities, assetElement, knownAssetConnection);
                }
            }
        }
    }


    private void setMetadataRepositoryDetails(String serverName, String userId) throws PropertyServerException, RepositoryErrorException, UserNotAuthorizedException {
        this.metadataCollectionForSearch = instanceHandler.getMetadataCollection(serverName);
        this.allTypes = metadataCollectionForSearch.getAllTypes(userId);
        this.serverName = serverName;
    }


    private void buildContextForAsset(String userId, AssetElement assetElement,  Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail) throws InvalidParameterException, TypeDefNotKnownException, PropertyErrorException, EntityProxyOnlyException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, UserNotAuthorizedException, TypeErrorException, RepositoryErrorException {
        Optional<TypeDef> isComplexSchemaType = isComplexSchemaType(entityDetail.getType().getTypeDefName());

        if (isComplexSchemaType.isPresent()) {
            setAssetDetails(userId, assetElement, knownAssetConnection, entityDetail);
            return;
        } else {
            List<EntityDetail> attributeForSchemas = getTheEndsRelationship(userId, entityDetail.getGUID(), ATTRIBUTE_FOR_SCHEMA);
            for(EntityDetail attributeForSchema : attributeForSchemas){
                Element element = buildElement(attributeForSchema);
                addElement(assetElement, element);
                if (isComplexSchemaType(attributeForSchema.getType().getTypeDefName()).isPresent()) {
                    setAssetDetails(userId, assetElement, knownAssetConnection, attributeForSchema);
                    return;
                } else {
                    List<EntityDetail> schemaAttributeTypeEntities = getTheEndsRelationship(userId, attributeForSchema.getGUID(), SCHEMA_ATTRIBUTE_TYPE);
                    getSubElements(assetElement, schemaAttributeTypeEntities);
                    for(EntityDetail schema : schemaAttributeTypeEntities){
                        buildContextForAsset(userId, assetElement, knownAssetConnection, schema);
                    }
                }
            }
        }
    }

    private Optional<TypeDef> isComplexSchemaType(String typeDefName) {
        return allTypes.getTypeDefs().stream().filter(t -> t.getName().equals(typeDefName) && t.getSuperType().getName().equals(COMPLEX_SCHEMA_TYPE)).findAny();
    }

    private void addElement(AssetElement assetElement, Element element) {
        if (assetElement.getContext() != null) {
            assetElement.getContext().add(element);
        } else {
            List<Element> elements = new ArrayList<>();
            elements.add(element);
            assetElement.setContext(elements);
        }
    }


    private void getSubElements(AssetElement assetElement, List<EntityDetail> schemaAttributeTypeEntities) {
        List<Element> elements = getElements(schemaAttributeTypeEntities);
        if(elements == null || elements.isEmpty()) {
            return;
        }
        List<Element> existingElements = assetElement.getContext();
        if(existingElements != null){
            existingElements.addAll(elements);
        } else {
            assetElement.setContext(elements);
        }

    }

    private List<Element> getElements(List<EntityDetail> schemaAttributeTypeEntities) {
        List<Element> elements = new ArrayList<>();

        for (EntityDetail schemaAttributeType : schemaAttributeTypeEntities) {
            Element element = buildElement(schemaAttributeType);
            elements.add(element);
        }

        return elements;
    }

    private void getAsset(String userId, AssetElement assetElement, Map<String, List<Connection>> knownAssetConnection, EntityDetail dataSet) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {
        List<Relationship> relationshipsToColumnTypes = getRelationshipsByAssetId(userId, dataSet.getGUID(), DATA_CONTENT_FOR_DATA_SET);

        if (!relationshipsToColumnTypes.isEmpty() && relationshipsToColumnTypes.size() == 1) {
            if (relationshipsToColumnTypes.get(0).getEntityOneProxy().getGUID().equals(dataSet.getGUID())) {
                setConnections(userId, assetElement, knownAssetConnection, dataSet);
            } else {
                EntityDetail asset = getThePairEntity(metadataCollectionForSearch, userId, dataSet.getGUID(), relationshipsToColumnTypes.get(0));
                if (asset != null) {
                    setAssetElementAttributes(assetElement, asset);
                    setConnections(userId, assetElement, knownAssetConnection, asset);
                }
            }
        }
    }

    private void setConnections(String userId, AssetElement assetElement, Map<String, List<Connection>> knownAssetConnection, EntityDetail asset) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {
        if (knownAssetConnection.containsKey(asset.getGUID())) {
            assetElement.setConnections(knownAssetConnection.get(asset.getGUID()));
        } else {
            List<Connection> connections = getConnections(userId, asset.getGUID());
            knownAssetConnection.put(asset.getGUID(), connections);
            assetElement.setConnections(connections);
        }
    }

    private List<Connection> getConnections(String userId, String dataSetGuid) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {
        List<EntityDetail> connections = getTheEndsRelationship(userId, dataSetGuid, Constants.CONNECTION_TO_ASSET);

        if (!connections.isEmpty()) {
            return connections.stream()
                    .map(t -> new Connection(t.getGUID(), converter.getStringPropertyValue(t.getProperties(), QUALIFIED_NAME)))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private List<EntityDetail> getTheEndsRelationship(String userId, String assetId, String relationshipType) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {

        List<Relationship> relationships = getRelationshipsByAssetId(userId, assetId, relationshipType);

        if (relationships.isEmpty()) {
            return Collections.emptyList();
        }

        List<EntityDetail> entityDetails = new ArrayList<>(relationships.size());
        for (Relationship relationship : relationships) {
            entityDetails.add(getThePairEntity(metadataCollectionForSearch, userId, assetId, relationship));
        }
        return entityDetails;
    }


    private void setAssetElementAttributes(AssetElement assetElement, EntityDetail asset) {
        assetElement.setGuid(asset.getGUID());
        assetElement.setType(asset.getType().getTypeDefName());
        assetElement.setQualifiedName(converter.getStringPropertyValue(asset.getProperties(), QUALIFIED_NAME));
        assetElement.setProperties(converter.getMapProperties(asset.getProperties()));
    }

    private void setAssetDetails(String userId, AssetElement assetElement, Map<String, List<Connection>> knownAssetConnection, EntityDetail entity) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {
        EntityDetail dataSet = getTheEndOfRelationship(userId, entity.getGUID(), ASSET_SCHEMA_TYPE);
        if(assetElement.getContext() != null) {
            assetElement.getContext().add(buildElement(dataSet));
        } else {
            assetElement.setContext(Collections.singletonList(buildElement(dataSet)));
        }

        getAsset(userId, assetElement, knownAssetConnection, dataSet);
    }

    private Element buildElement(EntityDetail entityDetail) {
        return buildTerm(entityDetail);
    }

    private Term buildTerm(EntityDetail glossaryTerm) {
        Term term = new Term();
        term.setGuid(glossaryTerm.getGUID());
        term.setType(glossaryTerm.getType().getTypeDefName());
        term.setQualifiedName(converter.getStringPropertyValue(glossaryTerm.getProperties(), QUALIFIED_NAME));
        term.setProperties(converter.getMapProperties(glossaryTerm.getProperties()));
        return term;
    }

    private EntityDetail getTheEndOfRelationship(String userId, String assetId, String relationshipType) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException {

        List<Relationship> relationshipsToColumnTypes = getRelationshipsByAssetId(userId, assetId, relationshipType);

        if (relationshipsToColumnTypes.isEmpty() || relationshipsToColumnTypes.size() != 1) {
            return null;
        }

        return getThePairEntity(metadataCollectionForSearch, userId, assetId, relationshipsToColumnTypes.get(0));
    }

    private EntityDetail getThePairEntity(OMRSMetadataCollection metadataCollection, String userId, String entityDetailGUID, Relationship relationship) throws UserNotAuthorizedException, RepositoryErrorException, EntityProxyOnlyException, InvalidParameterException, EntityNotKnownException {
        if (relationship.getEntityOneProxy().getGUID().equals(entityDetailGUID)) {
            return metadataCollection.getEntityDetail(userId, relationship.getEntityTwoProxy().getGUID());
        } else {
            return metadataCollection.getEntityDetail(userId, relationship.getEntityOneProxy().getGUID());
        }
    }

    private List<Relationship> getRelationshipsByAssetId(String userId, String entityId, String relationshipType) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, TypeDefNotKnownException {

        return getRelationshipByType(metadataCollectionForSearch,
                userId,
                entityId,
                relationshipType);
    }


    private List<Relationship> getRelationshipByType(OMRSMetadataCollection metadataCollection, String userId, String entityGUID, String relationshipType) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, TypeDefNotKnownException {
        String typeGuid = getTypeName(userId, relationshipType, metadataCollection);

        List<InstanceStatus> instanceStatuses = new ArrayList<>(1);
        instanceStatuses.add(InstanceStatus.ACTIVE);

        List<Relationship> relationshipsForEntity = metadataCollection.getRelationshipsForEntity(userId,
                entityGUID,
                typeGuid,
                0,
                instanceStatuses,
                null,
                null,
                SequencingOrder.ANY,
                0);
        if (relationshipsForEntity != null) {
            return relationshipsForEntity;
        }

        return new ArrayList<>();
    }

    private String getTypeName(String userId, String typeName, OMRSMetadataCollection metadataCollection) throws InvalidParameterException, RepositoryErrorException, TypeDefNotKnownException, UserNotAuthorizedException {
        final TypeDef typeDefByName = metadataCollection.getTypeDefByName(userId, typeName);

        if (typeDefByName != null) {
            return typeDefByName.getGUID();
        }
        return null;
    }

}
