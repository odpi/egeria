/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Defines a set of mappings between an IGC object's properties and an OMRS entity's properties.
 */
public class PropertyMappingSet {

    public static final String COMPLEX_MAPPING_SENTINEL = "__COMPLEX_PROPERTY__";

    private Map<String, PropertyMapping> mappingByIgcProperty;
    private Map<String, PropertyMapping> mappingByOmrsProperty;

    private HashSet<String> complexIgcProperties;
    private HashSet<String> complexOmrsProperties;

    public PropertyMappingSet() {
        mappingByIgcProperty = new HashMap<>();
        mappingByOmrsProperty = new HashMap<>();
        complexIgcProperties = new HashSet<>();
        complexOmrsProperties = new HashSet<>();
    }

    /**
     * Returns only the subset of mapped IGC properties that are simple one-to-one mappings to OMRS properties.
     *
     * @return Set<String>
     */
    public Set<String> getSimpleMappedIgcProperties() {
        return mappingByIgcProperty.keySet();
    }

    /**
     * Returns only the subset of mapped IGC properties that are complex mappings to OMRS properties.
     *
     * @return Set<String>
     */
    public Set<String> getComplexMappedIgcProperties() {
        return complexIgcProperties;
    }

    /**
     * Returns the set of all IGC properties that are mapped to OMRS properties.
     *
     * @return Set<String>
     */
    public Set<String> getAllMappedIgcProperties() {
        HashSet<String> igcProperties = new HashSet<>(getSimpleMappedIgcProperties());
        igcProperties.addAll(complexIgcProperties);
        return igcProperties;
    }

    /**
     * Returns only the subset of mapped OMRS properties that are simple one-to-one mappings to IGC properties.
     *
     * @return Set<String>
     */
    public Set<String> getSimpleMappedOmrsProperties() {
        return mappingByOmrsProperty.keySet();
    }

    /**
     * Returns only the subset of mapped OMRS properties that are complex mappings to IGC properties.
     *
     * @return Set<String>
     */
    public Set<String> getComplexMappedOmrsProperties() {
        return complexOmrsProperties;
    }

    /**
     * Returns the set of OMRS property names that are mapped to IGC properties.
     *
     * @return Set<String>
     */
    public Set<String> getAllMappedOmrsProperties() {
        Set<String> omrsProperties = getSimpleMappedOmrsProperties();
        omrsProperties.addAll(complexOmrsProperties);
        return omrsProperties;
    }

    /**
     * Adds a new mapping to the set of mappings.
     *
     * @param igcPropertyName the IGC property name (in REST form)
     * @param omrsPropertyName the name of the target attribute within the OMRS entity
     */
    public void addSimpleMapping(String igcPropertyName, String omrsPropertyName) {
        PropertyMapping pm = new PropertyMapping(igcPropertyName, omrsPropertyName);
        mappingByOmrsProperty.put(omrsPropertyName, pm);
        mappingByIgcProperty.put(igcPropertyName, pm);
    }

    /**
     * Notes the provided IGC property name requires more than a simple one-to-one mapping.
     *
     * @param igcPropertyName the IGC property name (in REST form)
     */
    public void addComplexIgc(String igcPropertyName) {
        complexIgcProperties.add(igcPropertyName);
    }

    /**
     * Notes the provided OMRS property name requires more than a simple one-to-one mapping.
     *
     * @param omrsPropertyName the OMRS property name
     */
    public void addComplexOmrs(String omrsPropertyName) {
        complexOmrsProperties.add(omrsPropertyName);
    }

    /**
     * Retrieves the IGC property name mapped to the provided OMRS property name.
     *
     * @param omrsPropertyName the OMRS property name for which to get the mapped IGC property name
     * @return String
     */
    public String getIgcPropertyName(String omrsPropertyName) {
        String igcPropertyName = null;
        if (isOmrsPropertySimpleMapped(omrsPropertyName)) {
            igcPropertyName = mappingByOmrsProperty.get(omrsPropertyName).getIgcPropertyName();
        } else if (isOmrsPropertyComplexMapped(omrsPropertyName)) {
            igcPropertyName = COMPLEX_MAPPING_SENTINEL;
        }
        return igcPropertyName;
    }

    /**
     * Retrieves the OMRS property name mapped to the provided IGC property name.
     *
     * @param igcPropertyName the IGC property name for which to get the mapped OMRS property name.
     * @return String
     */
    public String getOmrsPropertyName(String igcPropertyName) {
        String omrsPropertyName = null;
        if (isIgcPropertySimpleMapped(igcPropertyName)) {
            omrsPropertyName = mappingByIgcProperty.get(igcPropertyName).getOmrsPropertyName();
        } else if (isIgcPropertyComplexMapped(igcPropertyName)) {
            omrsPropertyName = COMPLEX_MAPPING_SENTINEL;
        }
        return omrsPropertyName;
    }

    /**
     * Determines whether the provided IGC property name is simple (one-to-one) mapped (true) or not (false).
     *
     * @param igcPropertyName the IGC property name to check for a simple mapping
     * @return boolean
     */
    public boolean isIgcPropertySimpleMapped(String igcPropertyName) {
        return mappingByIgcProperty.containsKey(igcPropertyName);
    }

    /**
     * Determines whether the provided IGC property name is complex mapped (true) or not (false).
     *
     * @param igcPropertyName the IGC property name to check for a complex mapping
     * @return boolean
     */
    public boolean isIgcPropertyComplexMapped(String igcPropertyName) {
        return complexIgcProperties.contains(igcPropertyName);
    }

    /**
     * Determines whether the provided OMRS property name is simple (one-to-one) mapped (true) or not (false).
     *
     * @param omrsPropertyName the OMRS property name to check for a simple mapping
     * @return boolean
     */
    public boolean isOmrsPropertySimpleMapped(String omrsPropertyName) {
        return mappingByOmrsProperty.containsKey(omrsPropertyName);
    }

    /**
     * Determines whether the provided OMRS property name is complex mapped (true) or not (false).
     *
     * @param omrsPropertyName the OMRS property name to check for a complex mapping
     * @return boolean
     */
    public boolean isOmrsPropertyComplexMapped(String omrsPropertyName) {
        return complexOmrsProperties.contains(omrsPropertyName);
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
