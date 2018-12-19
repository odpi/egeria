/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;

import java.util.*;

public class EntityMappingSet {

    private Map<String, List<EntityMapping>> mappingByIgcAssetType;
    private Map<String, EntityMapping> mappingByTypeDefGUID;

    public EntityMappingSet() {
        mappingByIgcAssetType = new HashMap<>();
        mappingByTypeDefGUID = new HashMap<>();
    }

    /**
     * Returns a set of asset type names implemented (mapped) for IGC.
     *
     * @return Set<String>
     */
    public Set<String> getIgcAssetTypes() {
        HashSet<String> assetTypeNames = new HashSet<>();
        for (List<EntityMapping> mappings : mappingByIgcAssetType.values()) {
            for (EntityMapping mapping : mappings) {
                assetTypeNames.add(mapping.getIgcAssetType());
            }
        }
        return assetTypeNames;
    }

    /**
     * Returns a set of entity TypeDefs implemented (mapped) for OMRS.
     *
     * @return Set<TypeDef>
     */
    public Set<TypeDef> getOmrsEntityTypes() {
        HashSet<TypeDef> entityTypes = new HashSet<>();
        for (EntityMapping mapping : mappingByTypeDefGUID.values()) {
            entityTypes.add(mapping.getOmrsTypeDef());
        }
        return entityTypes;
    }

    /**
     * Returns a list of POJOs that are used to (de-)serialise JSON for IGC asset types.
     *
     * @return List<Class>
     */
    public List<Class> getIgcPOJOs() {
        ArrayList<Class> pojos = new ArrayList<>();
        for (List<EntityMapping> mappings : mappingByIgcAssetType.values()) {
            for (EntityMapping mapping : mappings) {
                pojos.add(mapping.getIgcPOJO());
            }
        }
        return pojos;
    }

    /**
     * Retrieves the EntityMappings defined for the provided IGC asset type.
     *
     * @param assetType the IGC asset type for which to retrieve entity mappings
     * @return List<EntityMapping>
     */
    public List<EntityMapping> getByIgcAssetType(String assetType) {
        return mappingByIgcAssetType.get(assetType);
    }

    /**
     * Retrieves the Java classes that map between IGC and OMRS for the provided IGC asset type.
     *
     * @param assetType the IGC asset type for which to retrieve the mapping classes
     * @return List<Class>
     */
    public List<Class> getMappingClassesForIgcAssetType(String assetType) {
        ArrayList<Class> mappingClasses = new ArrayList<>();
        if (isIgcAssetTypeMapped(assetType)) {
            for (EntityMapping mapping : getByIgcAssetType(assetType)) {
                mappingClasses.add(mapping.getMappingClass());
            }
        }
        return mappingClasses;
    }

    /**
     * Retrieves the POJO that (de-)serialises the provided IGC asset type.
     *
     * @param assetType the IGC asset type for which to retrieve the POJO
     * @return Class
     */
    public Class getPOJOForIgcAssetType(String assetType) {
        Class pojo = null;
        if (isIgcAssetTypeMapped(assetType)) {
            List<EntityMapping> mappings = getByIgcAssetType(assetType);
            if (mappings != null && !mappings.isEmpty()) {
                pojo = mappings.get(0).getIgcPOJO();
            }
        }
        return pojo;
    }

    /**
     * Indicates whether the provided IGC asset type is mapped (true) or not (false).
     *
     * @param assetType the IGC asset type for which to check for a mapping
     * @return boolean
     */
    public boolean isIgcAssetTypeMapped(String assetType) {
        return mappingByIgcAssetType.containsKey(assetType);
    }

    /**
     * Retrieves the EntityMappings defined for the provided OMRS TypeDef.
     *
     * @param guid unique ID of the OMRS TypeDef
     * @return EntityMapping
     */
    public EntityMapping getByTypeDefGUID(String guid) { return mappingByTypeDefGUID.get(guid); }

    /**
     * Indicates whether the provided OMRS TypeDef is mapped (true) or not (false).
     *
     * @param guid unique ID of the OMRS TypeDef
     * @return boolean
     */
    public boolean isTypeDefMapped(String guid) { return mappingByTypeDefGUID.containsKey(guid); }

    /**
     * Adds a new mapping to the set of mappings.
     *
     * @param igcAssetTypeName the IGC asset type name (in REST form)
     * @param omrsTypeDef the TypeDef instance in OMRS that represents the same
     * @param mappingClass the class that defines how to map between the entities
     * @param igcPOJO the class that serialises / deserialises between JSON and Java for the IGC asset
     * @param ridPrefix the prefix to add to IGC RIDs to make a unique GUID (if any)
     */
    public void add(String igcAssetTypeName,
                    TypeDef omrsTypeDef,
                    Class mappingClass,
                    Class igcPOJO,
                    String ridPrefix) {

        EntityMapping em = new EntityMapping(
                igcAssetTypeName,
                omrsTypeDef,
                mappingClass,
                igcPOJO,
                ridPrefix
        );

        if (!mappingByIgcAssetType.containsKey(igcAssetTypeName)) {
            mappingByIgcAssetType.put(igcAssetTypeName, new ArrayList<>());
        }
        // TODO: confirm chained add below actually works (not failing due to abstract type?)
        getByIgcAssetType(igcAssetTypeName).add(em);

        mappingByTypeDefGUID.put(omrsTypeDef.getGUID(), em);

    }

    /**
     * Replaces any existing mapping (by IGC asset type / OMRS TypeDef) with the one provided.
     * (If there is none existing, will add the mapping.)
     *
     * @param igcAssetTypeName the IGC asset type name (in REST form)
     * @param omrsTypeDef the TypeDef instance in OMRS that represents the same
     * @param mappingClass the class that defines how to map between the entities
     * @param igcPOJO the class that (de-)serialises between JSON and Java for the IGC asset
     * @param ridPrefix the prefix to add to IGC RIDs to make a unique GUID (if any)
     */
    public void replace(String igcAssetTypeName,
                        TypeDef omrsTypeDef,
                        Class mappingClass,
                        Class igcPOJO,
                        String ridPrefix) {
        EntityMapping em = new EntityMapping(
                igcAssetTypeName,
                omrsTypeDef,
                mappingClass,
                igcPOJO,
                ridPrefix
        );
        ArrayList<EntityMapping> alEM = new ArrayList<>();
        alEM.add(em);
        mappingByIgcAssetType.put(igcAssetTypeName, alEM);
        mappingByTypeDefGUID.put(omrsTypeDef.getGUID(), em);
    }

    /**
     * Subclass to contain individual mappings.
     */
    public class EntityMapping {

        private String igcAssetType;
        private TypeDef omrsTypeDef;
        private Class mappingClass;
        private Class igcPOJO;
        private String igcRidPrefix;

        public EntityMapping(String igcAssetType,
                             TypeDef omrsTypeDef,
                             Class mappingClass,
                             Class igcPOJO) {
            this(igcAssetType,
                    omrsTypeDef,
                    mappingClass,
                    igcPOJO,
                    null);
        }

        public EntityMapping(String igcAssetType,
                             TypeDef omrsTypeDef,
                             Class mappingClass,
                             Class igcPOJO,
                             String igcRidPrefix) {
            this.igcAssetType = igcAssetType;
            this.omrsTypeDef = omrsTypeDef;
            this.mappingClass = mappingClass;
            this.igcPOJO = igcPOJO;
            this.igcRidPrefix = igcRidPrefix;
        }

        public String getIgcAssetType() { return this.igcAssetType; }
        public TypeDef getOmrsTypeDef() { return this.omrsTypeDef; }
        public Class getMappingClass() { return this.mappingClass; }
        public Class getIgcPOJO() { return this.igcPOJO; }
        public boolean igcRidNeedsPrefix() { return (this.igcRidPrefix != null); }
        public String getIgcRidPrefix() { return this.igcRidPrefix; }

    }

}
