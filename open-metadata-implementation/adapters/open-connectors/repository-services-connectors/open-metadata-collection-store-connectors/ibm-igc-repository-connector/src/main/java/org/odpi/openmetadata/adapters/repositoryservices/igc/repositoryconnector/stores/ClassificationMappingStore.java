/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.stores;

import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.classifications.ClassificationMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Store of implemented classification mappings for the repository.
 */
public class ClassificationMappingStore {

    private static final Logger log = LoggerFactory.getLogger(ClassificationMappingStore.class);

    private List<TypeDef> typeDefs;

    private Map<String, ClassificationMapping> omrsGuidToMapping;
    private Map<String, String> omrsNameToGuid;

    public ClassificationMappingStore() {
        typeDefs = new ArrayList<>();
        omrsGuidToMapping = new HashMap<>();
        omrsNameToGuid = new HashMap<>();
    }

    /**
     * Adds a classification mapping for the provided TypeDef, using the provided Java class for the mapping.
     *
     * @param omrsTypeDef the OMRS TypeDef
     * @param mappingClass the ClassificationMapping Java class
     * @return boolean false when unable to retrieve ClassificationMapping from provided class
     */
    public boolean addMapping(TypeDef omrsTypeDef, Class mappingClass) {

        ClassificationMapping mapping = getClassificationMapper(mappingClass);

        if (mapping != null) {
            typeDefs.add(omrsTypeDef);
            String guid = omrsTypeDef.getGUID();
            omrsGuidToMapping.put(guid, mapping);
            omrsNameToGuid.put(omrsTypeDef.getName(), guid);
        }

        return (mapping != null);

    }

    /**
     * Retrieves the listing of all TypeDefs for which classification mappings are implemented.
     *
     * @return List<TypeDef>
     */
    public List<TypeDef> getTypeDefs() { return this.typeDefs; }

    /**
     * Retrieves a classification mapping based on the GUID of the OMRS classification type.
     *
     * @param guid of the OMRS classification type
     * @return ClassificationMapping
     */
    public ClassificationMapping getMappingByOmrsTypeGUID(String guid) {
        if (omrsGuidToMapping.containsKey(guid)) {
            return omrsGuidToMapping.get(guid);
        } else {
            log.warn("Unable to find mapping for OMRS type: {}", guid);
            return null;
        }
    }

    /**
     * Retrieves a classification mapping based ont he name of the OMRS classification type.
     *
     * @param name of the OMRS classification type
     * @return ClassificationMapping
     */
    public ClassificationMapping getMappingByOmrsTypeName(String name) {
        if (omrsNameToGuid.containsKey(name)) {
            String guid = omrsNameToGuid.get(name);
            return getMappingByOmrsTypeGUID(guid);
        } else {
            log.warn("Unable to find mapping for OMRS type: {}", name);
            return null;
        }
    }

    /**
     * Introspect a mapping class to retrieve a ClassificationMapping.
     *
     * @param mappingClass the mapping class to retrieve an instance of
     * @return ClassificationMapping
     */
    private ClassificationMapping getClassificationMapper(Class mappingClass) {
        ClassificationMapping classificationMapper = null;
        try {
            Method getInstance = mappingClass.getMethod("getInstance");
            classificationMapper = (ClassificationMapping) getInstance.invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Unable to find or instantiate ClassificationMapping class: {}", mappingClass, e);
        }
        return classificationMapper;
    }

}
