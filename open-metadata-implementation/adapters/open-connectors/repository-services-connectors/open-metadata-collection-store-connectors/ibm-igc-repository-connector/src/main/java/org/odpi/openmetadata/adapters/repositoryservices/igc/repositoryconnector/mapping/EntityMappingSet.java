/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityMappingSet {

    private Map<String, EntityMapping> mappingByIgcAssetType;

    public EntityMappingSet() {
        mappingByIgcAssetType = new HashMap<>();
    }

    /**
     * Returns a list of asset type names implemented (mapped) for IGC.
     *
     * @return List<String>
     */
    public List<String> getIgcAssetTypes() {
        ArrayList<String> assetTypeNames = new ArrayList<>();
        for (EntityMapping mapping : mappingByIgcAssetType.values()) {
            assetTypeNames.add(mapping.getIgcAssetType());
        }
        return assetTypeNames;
    }

    /**
     * Returns a list of entity TypeDefs implemented (mapped) for OMRS.
     *
     * @return List<TypeDef>
     */
    public List<TypeDef> getOmrsEntityTypes() {
        ArrayList<TypeDef> entityTypes = new ArrayList<>();
        for (EntityMapping mapping : mappingByIgcAssetType.values()) {
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
        for (EntityMapping mapping : mappingByIgcAssetType.values()) {
            pojos.add(mapping.getIgcPOJO());
        }
        return pojos;
    }

    /**
     * Retrieves the EntityMapping defined for the provided IGC asset type.
     *
     * @param assetType the IGC asset type for which to retrieve an entity mapping
     * @return EntityMapping
     */
    public EntityMapping getByIgcAssetType(String assetType) {
        return mappingByIgcAssetType.get(assetType);
    }

    /**
     * Retrieves the POJO that can be used to (de-)serialise the provided IGC asset type.
     *
     * @param assetType the IGC asset type for which to retrieve the POJO
     * @return Class
     */
    public Class getIgcPOJOForAssetType(String assetType) {
        return isIgcAssetTypeMapped(assetType) ? getByIgcAssetType(assetType).getIgcPOJO() : null;
    }

    /**
     * Retrieves the Java class that maps between IGC and OMRS for the provided IGC asset type.
     *
     * @param assetType the IGC asset type for which to retrieve the mapping class
     * @return Class
     */
    public Class getMappingClassForAssetType(String assetType) {
        return isIgcAssetTypeMapped(assetType) ? getByIgcAssetType(assetType).getMappingClass() : null;
    }

    /**
     * Indicates whether the provided IGC asset type is mapped (true) or not (false)
     *
     * @param assetType the IGC asset type for which to check for a mapping
     * @return boolean
     */
    public boolean isIgcAssetTypeMapped(String assetType) {
        return mappingByIgcAssetType.containsKey(assetType);
    }

    /**
     * Adds a new mapping to the set of mappings.
     *
     * @param igcAssetTypeName the IGC asset type name (in REST form)
     * @param omrsTypeDef the TypeDef instance in OMRS that represents the same
     * @param mappingClass the class that defines how to map between the entities
     * @param igcPOJO the class that serialises / deserialises between JSON and Java for IGC asset
     */
    public void put(String igcAssetTypeName,
                    TypeDef omrsTypeDef,
                    Class mappingClass,
                    Class igcPOJO) {
        mappingByIgcAssetType.put(igcAssetTypeName, new EntityMapping(
                igcAssetTypeName,
                omrsTypeDef,
                mappingClass,
                igcPOJO
        ));
    }

    /**
     * Returns the total number of mappings contained in this set.
     *
     * @return Integer
     */
    public Integer size() {
        return mappingByIgcAssetType.size();
    }

    /**
     * Subclass to contain individual mappings.
     */
    public class EntityMapping {

        private String igcAssetType;
        private TypeDef omrsTypeDef;
        private Class mappingClass;
        private Class igcPOJO;

        public EntityMapping(String igcAssetType,
                             TypeDef omrsTypeDef,
                             Class mappingClass,
                             Class igcPOJO) {
            this.igcAssetType = igcAssetType;
            this.omrsTypeDef = omrsTypeDef;
            this.mappingClass = mappingClass;
            this.igcPOJO = igcPOJO;
        }

        public String getIgcAssetType() { return this.igcAssetType; }
        public TypeDef getOmrsTypeDef() { return this.omrsTypeDef; }
        public Class getMappingClass() { return this.mappingClass; }
        public Class getIgcPOJO() { return this.igcPOJO; }

    }

}
