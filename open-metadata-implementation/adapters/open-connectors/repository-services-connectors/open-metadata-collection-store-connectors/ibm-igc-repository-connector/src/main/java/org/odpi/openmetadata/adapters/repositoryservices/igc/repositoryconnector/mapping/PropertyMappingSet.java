/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Defines a set of mappings between an IGC object's properties and an OMRS entity's properties.
 */
public class PropertyMappingSet {

    private Map<String, PropertyMapping> mappingByIgcProperty;
    private Map<String, PropertyMapping> mappingByOmrsProperty;

    public PropertyMappingSet() {
        mappingByIgcProperty = new HashMap<>();
        mappingByOmrsProperty = new HashMap<>();
    }

    /**
     * Returns the set of IGC property names that are mapped to OMRS properties.
     *
     * @return Set<String>
     */
    public Set<String> getIgcPropertyNames() {
        return mappingByIgcProperty.keySet();
    }

    /**
     * Returns the set of OMRS property names that are mapped to IGC properties.
     *
     * @return Set<String>
     */
    public Set<String> getOmrsPropertyNames() {
        return mappingByOmrsProperty.keySet();
    }

    /**
     * Adds a new mapping to the set of mappings.
     *
     * @param igcPropertyName the IGC property name (in REST form)
     * @param omrsPropertyName the name of the target attribute within the OMRS entity
     */
    public void put(String igcPropertyName, String omrsPropertyName) {
        PropertyMapping pm = new PropertyMapping(igcPropertyName, omrsPropertyName);
        mappingByOmrsProperty.put(omrsPropertyName, pm);
        mappingByIgcProperty.put(igcPropertyName, pm);
    }

    /**
     * Retrieves the IGC property name mapped to the provided OMRS property name.
     *
     * @param omrsPropertyName the OMRS property name for which to get the mapped IGC property name
     * @return String
     */
    public String getIgcPropertyName(String omrsPropertyName) {
        return isOmrsPropertyNameMapped(omrsPropertyName) ? mappingByOmrsProperty.get(omrsPropertyName).getIgcPropertyName() : null;
    }

    /**
     * Retrieves the OMRS property name mapped to the provided IGC property name.
     *
     * @param igcPropertyName the IGC property name for which to get the mapped OMRS property name.
     * @return String
     */
    public String getOmrsPropertyName(String igcPropertyName) {
        return isIgcPropertyMapped(igcPropertyName) ? mappingByIgcProperty.get(igcPropertyName).getOmrsPropertyName() : null;
    }

    /**
     * Determines whether the provided IGC property name is mapped (true) or not (false).
     *
     * @param igcPropertyName the IGC property name to check for a mapping
     * @return boolean
     */
    public boolean isIgcPropertyMapped(String igcPropertyName) {
        return mappingByIgcProperty.containsKey(igcPropertyName);
    }

    /**
     * Determines whether the provided OMRS property name is mapped (true) or not (false).
     *
     * @param omrsPropertyName the OMRS property name to check for a mapping
     * @return boolean
     */
    public boolean isOmrsPropertyNameMapped(String omrsPropertyName) {
        return mappingByOmrsProperty.containsKey(omrsPropertyName);
    }

    /**
     * Returns the total number of mappings contained in this set.
     *
     * @return Integer
     */
    public Integer size() {
        return mappingByOmrsProperty.size();
    }

    /**
     * Subclass to contain individual mappings.
     */
    public class PropertyMapping {

        private String igcPropertyName;
        private String omrsPropertyName;

        public PropertyMapping(String igcPropertyName, String omrsPropertyName) {
            this.igcPropertyName = igcPropertyName;
            this.omrsPropertyName = omrsPropertyName;
        }

        public String getIgcPropertyName() { return this.igcPropertyName; }
        public String getOmrsPropertyName() { return this.omrsPropertyName; }

    }

}
