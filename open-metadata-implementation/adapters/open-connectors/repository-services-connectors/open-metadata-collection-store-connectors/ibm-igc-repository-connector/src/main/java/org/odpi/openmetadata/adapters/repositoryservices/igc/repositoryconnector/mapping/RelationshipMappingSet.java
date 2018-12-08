/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import java.util.ArrayList;

/**
 * Defines a set of mappings between an IGC object's relationships and an OMRS entity's relationships.
 */
public class RelationshipMappingSet {

    private ArrayList<RelationshipMapping> mappings;

    public RelationshipMappingSet() {
        mappings = new ArrayList<>();
    }

    /**
     * Returns the array of property names needed from the IGC object to map to the OMRS object.
     *
     * @return String[]
     */
    public String[] getIgcPropertyNames() {
        ArrayList<String> propertyNames = new ArrayList<>();
        for (RelationshipMapping mapping : mappings) {
            propertyNames.add(mapping.getIgcRelationshipName());
        }
        return propertyNames.toArray(new String[propertyNames.size()]);
    }

    /**
     * Adds a new mapping to the set of mappings.
     *
     * @param igcRelationshipName the IGC property name for the relationship (in REST form)
     * @param omrsRelationshipType the name of the OMRS relationship entity (RelationshipDef)
     * @param omrsRelationshipSourceProperty the name of the OMRS relationship property that should be aligned to the
     *                                       source of the relationship in IGC
     * @param omrsRelationshipTargetProperty the name of the OMRS relationship property that should be aligned to the
     *                                       target of the relationship in IGC
     */
    public void put(String igcRelationshipName,
                    String omrsRelationshipType,
                    String omrsRelationshipSourceProperty,
                    String omrsRelationshipTargetProperty) {
        mappings.add(new RelationshipMapping(
                igcRelationshipName,
                omrsRelationshipType,
                omrsRelationshipSourceProperty,
                omrsRelationshipTargetProperty
        ));
    }

    /**
     * Retrieves the mapping from the specified index.
     *
     * @param index the index of the mapping to retrieve
     * @return Mapping
     */
    public RelationshipMapping get(Integer index) {
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
    public class RelationshipMapping {

        // TODO: hope is that if we need the relationship endpoints, we can retrieve these from the archive
        // definition somehow (so they don't need to be kept in-sync)
        private String igcRelationshipName;
        private String omrsRelationshipType;
        private String omrsRelationshipSourceProperty;
        private String omrsRelationshipTargetProperty;

        public RelationshipMapping(String igcRelationshipName,
                                   String omrsRelationshipType,
                                   String omrsRelationshipSourceProperty,
                                   String omrsRelationshipTargetProperty) {
            this.igcRelationshipName = igcRelationshipName;
            this.omrsRelationshipType = omrsRelationshipType;
            this.omrsRelationshipSourceProperty = omrsRelationshipSourceProperty;
            this.omrsRelationshipTargetProperty = omrsRelationshipTargetProperty;
        }

        public String getIgcRelationshipName() { return this.igcRelationshipName; }
        public String getOmrsRelationshipType() { return this.omrsRelationshipType; }
        public String getOmrsRelationshipSourceProperty() { return this.omrsRelationshipSourceProperty; }
        public String getOmrsRelationshipTargetProperty() { return this.omrsRelationshipTargetProperty; }

    }

}
