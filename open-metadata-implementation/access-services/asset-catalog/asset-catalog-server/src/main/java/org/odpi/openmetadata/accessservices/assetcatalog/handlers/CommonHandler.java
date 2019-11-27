/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Type;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.ASSET_CATALOG_OMAS;
import static org.odpi.openmetadata.accessservices.assetcatalog.util.Constants.REFERENCEABLE;

/**
 * Common  Handler supports the lookup types and metadata collection.
 * These methods are used in the multiple handlers.
 * It runs on the server-side of the Asset Catalog OMAS, fetches the types, metadata collection using the RepositoryHandler.
 */
public class CommonHandler {

    private final RepositoryHandler repositoryHandler;
    private final OMRSRepositoryHelper repositoryHelper;

    CommonHandler(RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper) {
        this.repositoryHandler = repositoryHandler;
        this.repositoryHelper = repositoryHelper;
    }

    OMRSMetadataCollection getOMRSMetadataCollection() {
        return repositoryHandler.getMetadataCollection();
    }

    Type getTypeContext(String userId, String typeDefName) {
        TypeDef typeDefByName = repositoryHelper.getTypeDefByName(userId, typeDefName);

        if (typeDefByName != null) {
            Type type = convertType(typeDefByName);
            List<TypeDef> activeTypeDefs = repositoryHelper.getActiveTypeDefs();

            getSubTypes(activeTypeDefs, type);

            collectSubTypes(type, activeTypeDefs);
            collectSuperTypes2(userId, typeDefByName, type);

            return type;
        }

        return new Type();
    }

    private void collectSubTypes(Type type, List<TypeDef> activeTypeDefs) {
        if (type.getSubTypes().isEmpty()) {
            return;
        }

        for (Type currentSubType : type.getSubTypes()) {
            getSubTypes(activeTypeDefs, currentSubType);
            collectSubTypes(currentSubType, activeTypeDefs);
        }
    }

    private void getSubTypes(List<TypeDef> activeTypeDefs, Type type) {
        String typeName = type.getName();

        List<Type> subTypes;
        if (type.getSubTypes() == null) {
            type.setSubTypes(new ArrayList<>());
        }
        subTypes = type.getSubTypes();

        for (TypeDef typeDef : activeTypeDefs) {
            if (typeDef.getSuperType() != null && typeDef.getSuperType().getName().equals(typeName)) {

                subTypes.add(convertType(typeDef));
            }
        }

        type.setSubTypes(subTypes);
    }

    private void collectSuperTypes2(String userId, TypeDef openType, Type type) {
        if (openType.getName().equals(REFERENCEABLE) || openType.getSuperType() == null) {
            return;
        }

        buildType(openType, type);

        TypeDef superType = repositoryHelper.getTypeDefByName(userId, openType.getSuperType().getName());
        if (superType != null) {
            type.setSuperType(convertType(superType));
            collectSuperTypes2(userId, superType, type.getSuperType());
        }
    }

    private Type convertType(TypeDef openType) {
        Type type = new Type();
        buildType(openType, type);
        return type;
    }

    private void buildType(TypeDef openType, Type type) {
        type.setName(openType.getName());
        type.setDescription(openType.getDescription());
        type.setVersion(openType.getVersion());
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

    /**
     * Fetch the zone membership property
     *
     * @param properties asset properties
     * @return the list that contains the zone membership
     */
    public List<String> getAssetZoneMembership(InstanceProperties properties) {
        String methodName = "getAssetZoneMembership";
        List<String> zoneMembership = repositoryHelper.getStringArrayProperty(ASSET_CATALOG_OMAS, "zoneMembership", properties, methodName);
        if (CollectionUtils.isNotEmpty(zoneMembership)) {
            return zoneMembership;
        }
        return Collections.emptyList();
    }
}
