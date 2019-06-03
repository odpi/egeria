/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.odpi.openmetadata.accessservices.assetlineage.model.assetContext.AssetElement;
import org.odpi.openmetadata.accessservices.assetlineage.model.assetContext.Connection;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.Element;
import org.odpi.openmetadata.accessservices.assetlineage.model.assetContext.Term;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.ConvertedAssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.util.Constants;
import org.odpi.openmetadata.accessservices.assetlineage.util.Converter;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.*;

public class ContextHandler {

    private Converter converter = new Converter();
    private TypeDefGallery allTypes = new TypeDefGallery();
    private static final String guidParameter = "guid";
    private String serviceName;
    private String serverName;
    private RepositoryHandler repositoryHandler;
    private OMRSRepositoryHelper repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;

    /**
     * Construct the discovery engine configuration handler caching the objects
     * needed to operate within a single server instance.
     *
     * @param serviceName             name of the consuming service
     * @param serverName              name of this server instance
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper        helper used by the converters
     * @param repositoryHandler       handler for calling the repository services
     */
    public ContextHandler(String serviceName,
                          String serverName,
                          InvalidParameterHandler invalidParameterHandler,
                          OMRSRepositoryHelper repositoryHelper,
                          RepositoryHandler repositoryHandler) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
    }

    public ConvertedAssetContext getAssetContext(String serverName, String userId, String GUID) throws InvalidParameterException, FunctionNotSupportedException, PropertyServerException, PropertyErrorException, EntityProxyOnlyException, org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, UserNotAuthorizedException, TypeErrorException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException, RepositoryErrorException {
        AssetContext assetContext = new AssetContext();
        ConvertedAssetContext convertedAssetContext = new ConvertedAssetContext();
        this.allTypes = repositoryHandler.getMetadataCollection().getAllTypes(userId);

        EntityDetail entityDetail = getEntityDetails(serverName, userId, GUID);
        Map<String, List<Connection>> knownAssetConnection = new HashMap<>();

        String typeDefName = entityDetail.getType().getTypeDefName();
        if (typeDefName.equals(GLOSSARY_TERM)) {
            Term term = getStructureForGlossaryTerm(userId, knownAssetConnection, entityDetail);
            assetContext.setAssets(Collections.singletonList(term));
        } else {
            Term term = buildTerm(entityDetail);
            AssetElement assetElement = new AssetElement();

            if (isAsset(typeDefName).isPresent()) {
                getAsset(userId, assetElement, knownAssetConnection, entityDetail);
            } else {
                buildContextForAsset(userId, assetElement, knownAssetConnection, entityDetail);
            }


            term.setElements(Collections.singletonList(assetElement));
            assetContext.setAssets(Collections.singletonList(term));


            convertedAssetContext = converter.convertAssetContext(term);;

        }

        return convertedAssetContext;
    }

    private Optional<TypeDef> isAsset(String typeDefName) {
        Optional<TypeDef> typeDefStream = allTypes.getTypeDefs().stream().filter(t -> t.getName().equals(typeDefName)).findAny();

        if (typeDefStream.isPresent()) {
            //TODO There should be a hasSuperType() in class entitytypes
            Optional<TypeDef> superType = allTypes.getTypeDefs().stream().filter(t -> t.getName().equals(typeDefStream.get().getSuperType().getName())).findAny();
            return typeDefStream.map(typeDef -> allTypes.getTypeDefs().stream().filter(t -> t.getName().equals(
                    superType.get().getName()) && t.getSuperType().getName().equals(ASSET)).findAny()).orElse(null);
        }
        return null;
    }


    private EntityDetail getEntityDetails(String serverName, String userId, String GUID) throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        String methodname = "getEntityDetails";
        EntityDetail entityDetail = repositoryHandler.getEntityByGUID(userId, GUID, guidParameter, "Any entity type", methodname);
        return entityDetail;
    }

    private Term getStructureForGlossaryTerm(String userId, Map<String, List<Connection>> knownAssetConnection, EntityDetail glossaryTerm) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException, org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
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
                           Map<String, List<Connection>> knownAssetConnection) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException, org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {

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


    private void buildContextForAsset(String userId, AssetElement assetElement, Map<String, List<Connection>> knownAssetConnection, EntityDetail entityDetail) throws InvalidParameterException, TypeDefNotKnownException, PropertyErrorException, EntityProxyOnlyException, EntityNotKnownException, FunctionNotSupportedException, PagingErrorException, UserNotAuthorizedException, TypeErrorException, RepositoryErrorException, org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        Optional<TypeDef> isComplexSchemaType = isComplexSchemaType(entityDetail.getType().getTypeDefName());

        if (isComplexSchemaType.isPresent()) {
            setAssetDetails(userId, assetElement, knownAssetConnection, entityDetail);
            return;
        } else {
            List<EntityDetail> attributeForSchemas = getTheEndsRelationship(userId, entityDetail.getGUID(), ATTRIBUTE_FOR_SCHEMA);
            for (EntityDetail attributeForSchema : attributeForSchemas) {
                Element element = buildElement(attributeForSchema);
                addElement(assetElement, element);
                if (isComplexSchemaType(attributeForSchema.getType().getTypeDefName()).isPresent()) {
                    setAssetDetails(userId, assetElement, knownAssetConnection, attributeForSchema);
                    return;
                } else {
                    List<EntityDetail> schemaAttributeTypeEntities = getTheEndsRelationship(userId, attributeForSchema.getGUID(), SCHEMA_ATTRIBUTE_TYPE);
                    getSubElements(assetElement, schemaAttributeTypeEntities);
                    for (EntityDetail schema : schemaAttributeTypeEntities) {
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
        if (elements == null || elements.isEmpty()) {
            return;
        }
        List<Element> existingElements = assetElement.getContext();
        if (existingElements != null) {
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

    private void getAsset(String userId, AssetElement assetElement, Map<String, List<Connection>> knownAssetConnection, EntityDetail dataSet) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException, org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        List<Relationship> relationshipsToColumnTypes = getRelationshipsByAssetId(userId, dataSet.getGUID(), DATA_CONTENT_FOR_DATA_SET);

        if (!relationshipsToColumnTypes.isEmpty() && relationshipsToColumnTypes.size() == 1) {
            if (relationshipsToColumnTypes.get(0).getEntityOneProxy().getGUID().equals(dataSet.getGUID())) {
                setConnections(userId, assetElement, knownAssetConnection, dataSet);
            } else {
                EntityDetail asset = getThePairEntity(userId, dataSet.getGUID(), relationshipsToColumnTypes.get(0));
                if (asset != null) {
                    setAssetElementAttributes(assetElement, asset);
                    setConnections(userId, assetElement, knownAssetConnection, asset);
                }
            }
        }
    }

    private void setConnections(String userId, AssetElement assetElement, Map<String, List<Connection>> knownAssetConnection, EntityDetail asset) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException, org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        if (knownAssetConnection.containsKey(asset.getGUID())) {
            assetElement.setConnections(knownAssetConnection.get(asset.getGUID()));
        } else {
            List<Connection> connections = getConnections(userId, asset.getGUID());
            knownAssetConnection.put(asset.getGUID(), connections);
            assetElement.setConnections(connections);
        }
    }

    private List<Connection> getConnections(String userId, String dataSetGuid) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException, org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        List<EntityDetail> connections = getTheEndsRelationship(userId, dataSetGuid, Constants.CONNECTION_TO_ASSET);

        if (!connections.isEmpty()) {
            return connections.stream()
                    .map(t -> new Connection(t.getGUID(), converter.getStringPropertyValue(t.getProperties(), QUALIFIED_NAME)))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private List<EntityDetail> getTheEndsRelationship(String userId, String GUID, String relationshipType) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException, org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {

        List<Relationship> relationships = getRelationshipsByAssetId(userId, GUID, relationshipType);

        if (relationships.isEmpty()) {
            return Collections.emptyList();
        }

        List<EntityDetail> entityDetails = new ArrayList<>(relationships.size());
        for (Relationship relationship : relationships) {
            entityDetails.add(getThePairEntity(userId, GUID, relationship));
        }
        return entityDetails;
    }


    private void setAssetElementAttributes(AssetElement assetElement, EntityDetail asset) {
        assetElement.setGuid(asset.getGUID());
        assetElement.setType(asset.getType().getTypeDefName());
        assetElement.setQualifiedName(converter.getStringPropertyValue(asset.getProperties(), QUALIFIED_NAME));
        assetElement.setProperties(converter.getMapProperties(asset.getProperties()));
    }

    private void setAssetDetails(String userId, AssetElement assetElement, Map<String, List<Connection>> knownAssetConnection, EntityDetail entity) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException, org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        EntityDetail dataSet = getTheEndOfRelationship(userId, entity.getGUID(), ASSET_SCHEMA_TYPE);
        if (assetElement.getContext() != null) {
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

    private EntityDetail getTheEndOfRelationship(String userId, String GUID, String relationshipType) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityProxyOnlyException, TypeDefNotKnownException, org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {

        List<Relationship> relationshipsToColumnTypes = getRelationshipsByAssetId(userId, GUID, relationshipType);

        if (relationshipsToColumnTypes.isEmpty() || relationshipsToColumnTypes.size() != 1) {
            return null;
        }

        return getThePairEntity(userId, GUID, relationshipsToColumnTypes.get(0));
    }

    private EntityDetail getThePairEntity(String userId, String entityDetailGUID, Relationship relationship) throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException {
        String methodname = "getThePairEntity";
        if (relationship.getEntityOneProxy().getGUID().equals(entityDetailGUID)) {
            EntityDetail entityDetail = repositoryHandler.getEntityByGUID(userId, relationship.getEntityTwoProxy().getGUID(), guidParameter, "Any entity type", methodname);
            return entityDetail;
        } else {
            EntityDetail entityDetail = repositoryHandler.getEntityByGUID(userId, relationship.getEntityOneProxy().getGUID(), guidParameter, "Any entity type", methodname);
            return entityDetail;
        }
    }

    private List<Relationship> getRelationshipsByAssetId(String userId, String entityId, String relationshipType) throws RepositoryErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, TypeErrorException, PagingErrorException, TypeDefNotKnownException {

        return getRelationshipByType(
                userId,
                entityId,
                relationshipType);
    }


    private List<Relationship> getRelationshipByType(String userId, String entityGUID, String relationshipType) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, TypeDefNotKnownException {
        String typeGuid = getTypeName(userId, relationshipType);

        List<InstanceStatus> instanceStatuses = new ArrayList<>(1);
        instanceStatuses.add(InstanceStatus.ACTIVE);

        List<Relationship> relationshipsForEntity = repositoryHandler.getMetadataCollection().getRelationshipsForEntity(
                userId,
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

    private String getTypeName(String userId, String typeName) throws InvalidParameterException, RepositoryErrorException, TypeDefNotKnownException, UserNotAuthorizedException {
        final TypeDef typeDefByName = repositoryHandler.getMetadataCollection().getTypeDefByName(userId, typeName);

        if (typeDefByName != null) {
            return typeDefByName.getGUID();
        }
        return null;
    }

}
