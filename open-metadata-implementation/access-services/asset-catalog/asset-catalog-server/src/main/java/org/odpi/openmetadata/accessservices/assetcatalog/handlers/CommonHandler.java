/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetcatalog.converters.AssetCatalogConverter;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetCatalogBean;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Type;
import org.odpi.openmetadata.accessservices.assetcatalog.service.ClockService;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.ASSET_ZONE_MEMBERSHIP;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.DISPLAY_NAME;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.GUID_PARAMETER;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.REFERENCEABLE;

/**
 * Common  Handler supports the lookup types and metadata collection.
 * These methods are used in the multiple handlers.
 * It runs on the server-side of the Asset Catalog OMAS, fetches the types, metadata collection using the RepositoryHandler.
 */
public class CommonHandler {

    public static final String ZONE_MEMBERSHIP = "zoneMembership";
    public static final String NONE = "none";
    private final String sourceName;
    private final RepositoryHandler repositoryHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final OpenMetadataAPIGenericHandler<AssetCatalogBean> assetHandler;
    private final RepositoryErrorHandler errorHandler;
    private final ClockService clockService;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param sourceName        the name of the component
     * @param repositoryHandler manages calls to the repository services
     * @param repositoryHelper  provides utilities for manipulating the repository services objects
     * @param assetHandler      provides utilities for manipulating asset catalog objects using a generic handler
     * @param errorHandler      provides common validation routines for the other handler classes
     */
    CommonHandler(String sourceName, RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper,
                  OpenMetadataAPIGenericHandler<AssetCatalogBean> assetHandler, RepositoryErrorHandler errorHandler,
                  ClockService clockService) {
        this.sourceName = sourceName;
        this.repositoryHandler = repositoryHandler;
        this.repositoryHelper = repositoryHelper;
        this.assetHandler = assetHandler;
        this.errorHandler = errorHandler;
        this.clockService = clockService;
    }

    OMRSMetadataCollection getOMRSMetadataCollection() {
        return repositoryHandler.getMetadataCollection();
    }

    /**
     * Returns a list containing the type and all the sub-types of the provided type
     *
     * @param userId      user identifier that issues the call
     * @param typeDefName the type definition name
     * @return a list of sub-types recursive
     */
    List<Type> getTypeContext(String userId, String typeDefName) {
        List<Type> response = new ArrayList<>();
        TypeDef typeDefByName = repositoryHelper.getTypeDefByName(userId, typeDefName);
        AssetCatalogConverter<AssetCatalogBean> converter = new AssetCatalogConverter<>(repositoryHelper, sourceName, assetHandler.getServerName());

        if (typeDefByName != null) {
            if (repositoryHelper.getKnownTypeDefGallery() == null
                    || CollectionUtils.isEmpty(repositoryHelper.getKnownTypeDefGallery().getTypeDefs())) {
                return response;
            }
            List<TypeDef> typeDefs = repositoryHelper.getKnownTypeDefGallery().getTypeDefs();

            Type type = converter.convertType(typeDefByName);
            List<Type> subTypes = getSubTypes(typeDefs, type);
            response.add(type);
            response.addAll(subTypes);

            collectSubTypes(subTypes, typeDefs, response);
            response.sort(Comparator.comparing(Type::getName));
        }

        return response;
    }

    /**
     *
     * @param userId      user identifier that issues the call
     * @param typeDefName the type definition name
     * @return the Type if exists, otherwise null
     */
    Type getTypeByTypeDefName(String userId, String typeDefName) {

        TypeDef typeDefByName = repositoryHelper.getTypeDefByName(userId, typeDefName);
        AssetCatalogConverter<AssetCatalogBean> converter = new AssetCatalogConverter<>(repositoryHelper, sourceName, assetHandler.getServerName());

        if (typeDefByName != null) {
            return converter.convertType(typeDefByName);
        }
        return null;
    }

    /**
     * Returns the global identifier of the type using the type def name
     *
     * @param userId      user identifier that issues the call
     * @param typeDefName the type definition name
     * @return the global identifier of the type
     */
    public String getTypeDefGUID(String userId, String typeDefName) {
        if (typeDefName == null) {
            return null;
        }

        TypeDef typeDefByName = repositoryHelper.getTypeDefByName(userId, typeDefName);
        return Optional.ofNullable(typeDefByName).map(TypeDefLink::getGUID).orElse(null);
    }

    public boolean hasDisplayName(String userId, String typeDefGUID) throws InvalidParameterException {
        String methodName = "hasDisplayName";
        TypeDef typeDefByName = null;
        try {
            typeDefByName = repositoryHelper.getTypeDef(userId, GUID_PARAMETER, typeDefGUID, methodName);
        } catch (TypeErrorException typeErrorException) {
            errorHandler.handleUnsupportedType(typeErrorException, methodName, GUID_PARAMETER);
        }
        List<TypeDefAttribute> allPropertiesForTypeDef = repositoryHelper.getAllPropertiesForTypeDef(sourceName, typeDefByName, methodName);
        if (allPropertiesForTypeDef == null) {
            return false;
        } else {
            return allPropertiesForTypeDef.stream().anyMatch(property -> property.getAttributeName().equals(DISPLAY_NAME));
        }

    }

    /**
     * Fetch the zone membership property
     *
     * @param classifications asset properties
     * @return the list that contains the zone membership
     */
    List<String> getAssetZoneMembership(List<Classification> classifications) {
        String methodName = "getAssetZoneMembership";
        if (CollectionUtils.isEmpty(classifications)) {
            return Collections.emptyList();
        }

        Optional<Classification> assetZoneMembership = getAssetZoneMembershipClassification(classifications);

        if (assetZoneMembership.isPresent()) {
            List<String> zoneMembership = repositoryHelper.getStringArrayProperty(sourceName,
                    ZONE_MEMBERSHIP, assetZoneMembership.get().getProperties(), methodName);

            if (CollectionUtils.isNotEmpty(zoneMembership)) {
                return zoneMembership;
            }
        }

        return Collections.emptyList();
    }

    /**
     * Return the requested entity, converting any errors from the repository services into the local
     * OMAS exceptions.
     *
     * @param userId calling user
     * @param guid   unique identifier for the entity
     * @param type   entity type name
     * @return entity detail object
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    problem retrieving the entity.
     */
    EntityDetail getEntityByGUID(String userId,
                                 String guid,
                                 String type) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {

        String entityTypeName = type;
        if(NONE.equals(type)) {
            entityTypeName = null;
        }
        String methodName = "getEntityByGUID";
        return assetHandler.getEntityFromRepository(userId, guid, GUID_PARAMETER, entityTypeName,
                null, null, false, false,
                clockService.getNow(), methodName);
    }

    /**
     * Return a list of the types def GUIDs
     *
     * @param userId calling user
     * @param types  list of the type def names
     * @return a list of type def GUIDs
     */
    List<String> getTypesGUID(String userId, List<String> types) {
        if (CollectionUtils.isEmpty(types)) {
            return Collections.emptyList();
        }
        return types.stream().
                map(type -> repositoryHelper.getTypeDefByName(userId, type)).
                filter(Objects::nonNull).
                map(TypeDef::getGUID).
                collect(Collectors.toList());
    }

    /**
     * Return a map of the types def GUIDs and their associated type names
     *
     * @param userId calling user
     * @param types  list of the type def names
     * @return a list of type def GUIDs
     */
    Map<String, String> getTypesAndGUIDs(String userId, List<String> types) {
        if (CollectionUtils.isEmpty(types)) {
            return Collections.emptyMap();
        }
        Map<String, String> typesAndGUIDs = new HashMap<>();
        for (String type : types) {
            TypeDef typeDef = repositoryHelper.getTypeDefByName(userId, type);
            String typeDefGUID = null;
            if(typeDef != null) {
                typeDefGUID = typeDef.getGUID();
            }
            typesAndGUIDs.put(type, typeDefGUID);
        }
        return typesAndGUIDs;
    }

    private void collectSubTypes(List<Type> types, List<TypeDef> activeTypeDefs, List<Type> collector) {
        for (Type currentSubType : types) {
            List<Type> subTypes = getSubTypes(activeTypeDefs, currentSubType);
            collector.addAll(subTypes);
            collectSubTypes(subTypes, activeTypeDefs, collector);
        }
    }

    private List<Type> getSubTypes(List<TypeDef> activeTypeDefs, Type type) {
        String typeName = type.getName();
        AssetCatalogConverter<AssetCatalogBean> converter = new AssetCatalogConverter<>(repositoryHelper, sourceName, assetHandler.getServerName());

        List<Type> subTypes = new ArrayList<>();
        for (TypeDef typeDef : activeTypeDefs) {
            if (typeDef.getSuperType() != null && typeDef.getSuperType().getName().equals(typeName)) {

                subTypes.add(converter.convertType(typeDef));
            }
        }
        return subTypes;
    }

    private Optional<Classification> getAssetZoneMembershipClassification(List<Classification> classifications) {
        return classifications.stream()
                .filter(classification -> classification.getName().equals(ASSET_ZONE_MEMBERSHIP))
                .findFirst();
    }

    Set<String> collectSuperTypes(String userId, String typeDefName) {
        Set<String> superTypes = new HashSet<>();

        TypeDef typeDefByName = repositoryHelper.getTypeDefByName(userId, typeDefName);
        if (typeDefByName != null) {
            collectSuperTypes(userId, typeDefByName, superTypes);
        }

        return superTypes;
    }

    private void collectSuperTypes(String userId, TypeDef type, Set<String> superTypes) {
        if (type.getName().equals(REFERENCEABLE)) {
            return;
        }
        superTypes.add(type.getName());
        TypeDef typeDefByName = repositoryHelper.getTypeDefByName(userId, type.getSuperType().getName());
        if (typeDefByName != null) {
            collectSuperTypes(userId, typeDefByName, superTypes);
        }
    }
}
