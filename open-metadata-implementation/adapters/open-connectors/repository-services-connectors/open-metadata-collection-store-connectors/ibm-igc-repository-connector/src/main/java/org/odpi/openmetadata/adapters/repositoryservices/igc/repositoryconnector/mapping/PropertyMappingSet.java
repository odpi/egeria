/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import java.util.ArrayList;

/**
 * Defines a set of mappings between an IGC object's properties and an OMRS entity's properties.
 */
public class PropertyMappingSet {

    private ArrayList<PropertyMapping> mappings;

    public PropertyMappingSet() {
        mappings = new ArrayList<>();
    }

    /**
     * Returns the array of property names needed from the IGC object to map to the OMRS object.
     *
     * @return String[]
     */
    public String[] getIgcPropertyNames() {
        ArrayList<String> propertyNames = new ArrayList<>();
        for (PropertyMapping mapping : mappings) {
            propertyNames.add(mapping.getIgcPropertyName());
        }
        return propertyNames.toArray(new String[propertyNames.size()]);
    }

    /**
     * Adds a new mapping to the set of mappings.
     *
     * @param igcPropertyName the IGC property name (in REST form)
     * @param omrsEntityAttrName the name of the target attribute within the OMRS entity
     */
    public void put(String igcPropertyName, String omrsEntityAttrName) {
        mappings.add(new PropertyMapping(igcPropertyName, omrsEntityAttrName));
    }

    /**
     * Retrieves the mapping from the specified index.
     *
     * @param index the index of the mapping to retrieve
     * @return Mapping
     */
    public PropertyMapping get(Integer index) {
        return mappings.get(index);
    }

    /**
     * Returns the total number of mappings contained in this set.
     *
     * @return Integer
     */
    public Integer size() {
        return mappings.size();
    }

    /**
     * Subclass to contain individual mappings.
     */
    public class PropertyMapping {

        private String igcPropertyName;
        private String omrsEntityAttr;

        public PropertyMapping(String igcPropertyName, String omrsEntityAttr) {
            this.igcPropertyName = igcPropertyName;
            this.omrsEntityAttr = omrsEntityAttr;
        }

        public String getIgcPropertyName() { return this.igcPropertyName; }
        public String getOmrsEntityAttr() { return this.omrsEntityAttr; }

    }

}
