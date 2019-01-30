/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.stores;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities.EntityMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Store of implemented entity mappings for the repository.
 */
public class EntityMappingStore {

    private static final Logger log = LoggerFactory.getLogger(EntityMappingStore.class);

    private List<TypeDef> typeDefs;

    private Map<String, EntityMapping> omrsGuidToMapping;
    private Map<String, List<String>> igcAssetTypeToOmrsGuids;
    private Map<String, String> igcAssetDisplayNameToOmrsGuid;
    private Map<String, String> igcAssetTypeAndPrefixToOmrsGuid;
    private Map<String, String> omrsNameToGuid;

    public EntityMappingStore() {
        typeDefs = new ArrayList<>();
        omrsGuidToMapping = new HashMap<>();
        igcAssetTypeToOmrsGuids = new HashMap<>();
        igcAssetDisplayNameToOmrsGuid = new HashMap<>();
        igcAssetTypeAndPrefixToOmrsGuid = new HashMap<>();
        omrsNameToGuid = new HashMap<>();
    }

    /**
     * Retrieves a listing of all TypeDefs for which entity mappings have been implemented.
     *
     * @return List<TypeDef>
     */
    public List<TypeDef> getTypeDefs() { return this.typeDefs; }

    /**
     * Adds an entity mapping for the provided TypeDef, using the provided EntityMapping Java class and repository details.
     *
     * @param omrsTypeDef the OMRS TypeDef for which the entity mapping is implemented
     * @param mappingClass the Java class providing the EntityMapping
     * @param igcomrsRepositoryConnector connectivity via an IGC OMRS Repository Connector
     * @param userId the userId through which to apply the mapping
     * @return boolean false if unable to configure an EntityMapping from the provided class
     */
    public boolean addMapping(TypeDef omrsTypeDef, Class mappingClass, IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        EntityMapping mapping = getEntityMapper(mappingClass, igcomrsRepositoryConnector, userId);

        if (mapping != null) {
            typeDefs.add(omrsTypeDef);
            String guid = omrsTypeDef.getGUID();
            omrsGuidToMapping.put(guid, mapping);
            omrsNameToGuid.put(omrsTypeDef.getName(), guid);
            String igcAssetType = mapping.getIgcAssetType();
            addIgcAssetTypeToGuid(igcAssetType, guid);
            igcAssetDisplayNameToOmrsGuid.put(mapping.getIgcAssetTypeDisplayName(), guid);
            String prefix = "";
            if (mapping.getIgcRidPrefix() != null) {
                prefix = mapping.getIgcRidPrefix();
            }
            igcAssetTypeAndPrefixToOmrsGuid.put(prefix + igcAssetType, guid);
            for (String otherType : mapping.getOtherIGCAssetTypes()) {
                addIgcAssetTypeToGuid(otherType, guid);
            }
            IGCRestClient igcRestClient = igcomrsRepositoryConnector.getIGCRestClient();
            igcRestClient.registerPOJO(mapping.getIgcPOJO());
            List<Class> otherPOJOs = mapping.getOtherIGCPOJOs();
            if (otherPOJOs != null && !otherPOJOs.isEmpty()) {
                for (Class pojo : mapping.getOtherIGCPOJOs()) {
                    igcRestClient.registerPOJO(pojo);
                }
            }
        }

        return (mapping != null);

    }

    /**
     * Adds a mapping between IGC asset type and GUID of the OMRS TypeDef.
     *
     * @param igcAssetType the IGC asset type
     * @param guid of the OMRS TypeDef
     */
    private void addIgcAssetTypeToGuid(String igcAssetType, String guid) {
        if (!igcAssetTypeToOmrsGuids.containsKey(igcAssetType)) {
            igcAssetTypeToOmrsGuids.put(igcAssetType, new ArrayList<>());
        }
        igcAssetTypeToOmrsGuids.get(igcAssetType).add(guid);
    }

    /**
     * Retrieves the entity mapping by GUID of the OMRS TypeDef.
     *
     * @param guid of the OMRS TypeDef
     * @return EntityMapping
     */
    public EntityMapping getMappingByOmrsTypeGUID(String guid) {
        if (omrsGuidToMapping.containsKey(guid)) {
            return omrsGuidToMapping.get(guid);
        } else {
            log.warn("Unable to find mapping for OMRS type: {}", guid);
            return null;
        }
    }

    /**
     * Retrieves all entity mappings that could be applied to the provided IGC asset type.
     * There can be multiple mappings in cases where a single IGC asset maps to multiple OMRS entities, and must use
     * a generated prefix for one or more of them (this method will return both the prefix and non-prefix mappings).
     *
     * @param assetType IGC asset type
     * @return List<EntityMapping>
     */
    public List<EntityMapping> getMappingsByIgcAssetType(String assetType) {
        String simpleType = Reference.getAssetTypeForSearch(assetType);
        if (igcAssetTypeToOmrsGuids.containsKey(simpleType)) {
            List<String> guids = igcAssetTypeToOmrsGuids.get(simpleType);
            ArrayList<EntityMapping> mappings = new ArrayList<>();
            for (String guid : guids) {
                mappings.add(getMappingByOmrsTypeGUID(guid));
            }
            return mappings;
        } else {
            log.warn("Unable to find mapping for IGC type: {}", simpleType);
            return null;
        }
    }

    /**
     * Retrieves the entity mapping that applies to the provided IGC asset type, by its display name.
     *
     * @param assetDisplayName IGC asset type's display name
     * @return EntityMapping
     */
    public EntityMapping getMappingByIgcAssetDisplayName(String assetDisplayName) {
        if (igcAssetDisplayNameToOmrsGuid.containsKey(assetDisplayName)) {
            String guid = igcAssetDisplayNameToOmrsGuid.get(assetDisplayName);
            return getMappingByOmrsTypeGUID(guid);
        } else {
            log.warn("Unable to find mapping for IGC asset display name: {}", assetDisplayName);
            return null;
        }
    }

    /**
     * Retrieves the entity mapping that applies to the provided IGC asset type and prefix (if any).
     *
     * @param assetType IGC asset type
     * @param prefix the prefix for the entity, or null if no prefix
     * @return EntityMapping
     */
    public EntityMapping getMappingByIgcAssetTypeAndPrefix(String assetType, String prefix) {
        String simpleType = Reference.getAssetTypeForSearch(assetType);
        String key = (prefix == null ? "" : prefix) + simpleType;
        if (igcAssetTypeAndPrefixToOmrsGuid.containsKey(key)) {
            String guid = igcAssetTypeAndPrefixToOmrsGuid.get(key);
            return getMappingByOmrsTypeGUID(guid);
        } else {
            log.warn("Unable to find mapping for IGC asset type with prefix: {}", key);
            return null;
        }
    }

    /**
     * Retrieves the entity mapping that applies to the provided OMRS entity type name.
     *
     * @param name of the OMRS entity type
     * @return EntityMapping
     */
    public EntityMapping getMappingByOmrsTypeName(String name) {
        if (omrsNameToGuid.containsKey(name)) {
            String guid = omrsNameToGuid.get(name);
            return getMappingByOmrsTypeGUID(guid);
        } else {
            log.warn("Unable to find mapping for OMRS type: {}", name);
            return null;
        }
    }

    /**
     * Retrieves all entity mappings that are implemented for this repository.
     *
     * @return List<EntityMapping>
     */
    public List<EntityMapping> getAllMappings() {
        return new ArrayList<>(omrsGuidToMapping.values());
    }

    /**
     * Retrieves the entity mapping that can be applied by default to any entity.
     *
     * @param igcomrsRepositoryConnector connectivity through an IGC OMRS Repository Connector
     * @param userId userId through which to retrieve the mapping
     * @return EntityMapping
     */
    public EntityMapping getDefaultEntityMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                                String userId) {
        EntityMapping referenceable = null;
        try {
            Class mappingClass = Class.forName(IGCOMRSMetadataCollection.MAPPING_PKG + "entities.ReferenceableMapper");
            referenceable = getEntityMapper(mappingClass, igcomrsRepositoryConnector, userId);
        } catch (ClassNotFoundException e) {
            log.error("Unable to find default ReferenceableMapper class: " + IGCOMRSMetadataCollection.MAPPING_PKG + "entities.ReferenceableMapper", e);
        }
        return referenceable;
    }

    /**
     * Retrieves the entity mapping for the provided Java class and repository connectivity.
     *
     * @param mappingClass the Java class implementing the EntityMapping
     * @param igcomrsRepositoryConnector connectivity through an IGC OMRS Repository Connector
     * @param userId userId through which to retrieve the mapping
     * @return EntityMapping
     */
    private EntityMapping getEntityMapper(Class mappingClass,
                                          IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                          String userId) {
        EntityMapping entityMapper = null;
        try {
            Constructor constructor = mappingClass.getConstructor(IGCOMRSRepositoryConnector.class, String.class);
            entityMapper = (EntityMapping) constructor.newInstance(
                    igcomrsRepositoryConnector,
                    userId
            );
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error("Unable to find or instantiate EntityMapping class: {}", mappingClass, e);
        }
        return entityMapper;
    }

}
