/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Defines a set of mappings between an IGC object's relationships and an OMRS entity's relationships.
 */
public class RelationshipMappingSet {

    public static final String SELF_REFERENCE_SENTINEL = "__SELF__";

    private ArrayList<RelationshipMapping> mappings;

    private HashSet<String> complexIgcRelationships;
    private HashSet<String> complexOmrsRelationships;

    public RelationshipMappingSet() {
        mappings = new ArrayList<>();
        complexIgcRelationships = new HashSet<>();
        complexOmrsRelationships = new HashSet<>();
    }

    /**
     * Returns the subset of property names needed from the IGC object to map one-to-one into an OMRS relationship.
     *
     * @return Set<String>
     */
    public Set<String> getSimpleMappedIgcRelationships() {
        HashSet<String> igcRelationships = new HashSet<>();
        for (RelationshipMapping mapping : mappings) {
            if (!mapping.isInvertedMapping()) {
                igcRelationships.add(mapping.getIgcRelationshipName());
            }
        }
        igcRelationships.remove(SELF_REFERENCE_SENTINEL);
        return igcRelationships;
    }

    /**
     * Returns the subset of property names needed from the IGC object to complex map into an OMRS relationship.
     *
     * @return Set<String>
     */
    public Set<String> getComplexMappedIgcRelationships() { return complexIgcRelationships; }

    /**
     * Returns all property names needed from the IGC object to map into OMRS relationships.
     *
     * @return Set<String>
     */
    public Set<String> getAllMappedIgcRelationships() {
        HashSet<String> igcRelationships = new HashSet<>(getSimpleMappedIgcRelationships());
        igcRelationships.addAll(complexIgcRelationships);
        return igcRelationships;
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
    public void addSimpleMapping(String igcRelationshipName,
                                 String omrsRelationshipType,
                                 String omrsRelationshipSourceProperty,
                                 String omrsRelationshipTargetProperty) {
        addSimpleMapping(
                igcRelationshipName,
                omrsRelationshipType,
                omrsRelationshipSourceProperty,
                omrsRelationshipTargetProperty,
                null,
                null
        );
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
     * @param igcSourceRidPrefix the prefix that needs to be added to the source of the IGC relationship's RID
     * @param igcTargetRidPrefix the prefix that needs to be added to the target of the IGC relationship's RID
     */
    public void addSimpleMapping(String igcRelationshipName,
                                 String omrsRelationshipType,
                                 String omrsRelationshipSourceProperty,
                                 String omrsRelationshipTargetProperty,
                                 String igcSourceRidPrefix,
                                 String igcTargetRidPrefix) {
        mappings.add(new RelationshipMapping(
                igcRelationshipName,
                omrsRelationshipType,
                omrsRelationshipSourceProperty,
                omrsRelationshipTargetProperty,
                igcSourceRidPrefix,
                igcTargetRidPrefix
        ));
    }

    /**
     * Adds a new inverted mapping to the set of mappings.
     *
     * @param igcSourceAssetType the type of IGC asset from which to draw the relationship
     * @param igcRelationshipName the IGC property name for the relationship (in REST form)
     * @param omrsRelationshipType the name of the OMRS relationship entity (RelationshipDef)
     * @param omrsRelationshipSourceProperty the name of the OMRS relationship property that should be aligned to the
     *                                       source of the relationship in IGC
     * @param omrsRelationshipTargetProperty the name of the OMRS relationship property that should be aligned to the
     *                                       target of the relationship in IGC
     */
    public void addInvertedMapping(String igcSourceAssetType,
                                   String igcRelationshipName,
                                   String omrsRelationshipType,
                                   String omrsRelationshipSourceProperty,
                                   String omrsRelationshipTargetProperty) {
        addInvertedMapping(
                igcSourceAssetType,
                igcRelationshipName,
                omrsRelationshipType,
                omrsRelationshipSourceProperty,
                omrsRelationshipTargetProperty,
                null,
                null);
    }

    /**
     * Adds a new inverted mapping to the set of mappings.
     *
     * @param igcSourceAssetType the type of IGC asset from which to draw the relationship
     * @param igcRelationshipName the IGC property name for the relationship (in REST form)
     * @param omrsRelationshipType the name of the OMRS relationship entity (RelationshipDef)
     * @param omrsRelationshipSourceProperty the name of the OMRS relationship property that should be aligned to the
     *                                       source of the relationship in IGC
     * @param omrsRelationshipTargetProperty the name of the OMRS relationship property that should be aligned to the
     *                                       target of the relationship in IGC
     * @param igcSourceRidPrefix the prefix that needs to be added to the source of the IGC relationship's RID
     * @param igcTargetRidPrefix the prefix that needs to be added to the target of the IGC relationship's RID
     */
    public void addInvertedMapping(String igcSourceAssetType,
                                   String igcRelationshipName,
                                   String omrsRelationshipType,
                                   String omrsRelationshipSourceProperty,
                                   String omrsRelationshipTargetProperty,
                                   String igcSourceRidPrefix,
                                   String igcTargetRidPrefix) {
        mappings.add(new RelationshipMapping(
                igcSourceAssetType,
                igcRelationshipName,
                omrsRelationshipType,
                omrsRelationshipSourceProperty,
                omrsRelationshipTargetProperty,
                igcSourceRidPrefix,
                igcTargetRidPrefix
        ));
    }

    /**
     * Notes the provided IGC relationship requires more than a simple one-to-one mapping.
     *
     * @param igcRelationshipName the IGC relationship property name (in REST form)
     */
    public void addComplexIgc(String igcRelationshipName) {
        complexIgcRelationships.add(igcRelationshipName);
    }

    /**
     * Notes the provided OMRS relationship requires more than a simple one-to-one mapping.
     *
     * @param omrsRelationshipType the OMRS relationship type
     */
    public void addComplexOmrs(String omrsRelationshipType) {
        complexOmrsRelationships.add(omrsRelationshipType);
    }

    /**
     * Determines whether the provided IGC relationship name requires a complex mapping (true) or not (false).
     *
     * @param igcRelationshipName the IGC relationship to check for a complex mapping
     * @return boolean
     */
    public boolean isIgcRelationshipComplexMapped(String igcRelationshipName) {
        return complexIgcRelationships.contains(igcRelationshipName);
    }

    /**
     * Determines whether the provided OMRS relationship type requires a complex mapping (true) or not (false).
     *
     * @param omrsRelationshipType the type of OMRS relationship to check for a complex mapping
     * @return boolean
     */
    public boolean isOmrsRelationshipComplexMapped(String omrsRelationshipType) {
        return complexOmrsRelationships.contains(omrsRelationshipType);
    }

    // TODO: implement an iterator of some form for simple mappings...

    /**
     * Retrieves the simple mapping from the specified index.
     *
     * @param index the index of the mapping to retrieve
     * @return Mapping
     */
    public RelationshipMapping getSimpleMapping(Integer index) {
        return mappings.get(index);
    }

    /**
     * Returns the total number of simple mappings contained in this set.
     *
     * @return int
     */
    public int numberOfSimpleMappings() {
        return mappings.size();
    }

    /**
     * Subclass to contain individual mappings.
     */
    public class RelationshipMapping {

        private String igcAssetType;
        private String igcRelationshipName;
        private String omrsRelationshipType;
        private String omrsRelationshipSourceProperty;
        private String omrsRelationshipTargetProperty;
        private String igcSourceRidPrefix;
        private String igcTargetRidPrefix;

        public RelationshipMapping(String igcRelationshipName,
                                   String omrsRelationshipType,
                                   String omrsRelationshipSourceProperty,
                                   String omrsRelationshipTargetProperty,
                                   String igcSourceRidPrefix,
                                   String igcTargetRidPrefix) {
            this.igcRelationshipName = igcRelationshipName;
            this.omrsRelationshipType = omrsRelationshipType;
            this.omrsRelationshipSourceProperty = omrsRelationshipSourceProperty;
            this.omrsRelationshipTargetProperty = omrsRelationshipTargetProperty;
            this.igcSourceRidPrefix = igcSourceRidPrefix;
            this.igcTargetRidPrefix = igcTargetRidPrefix;
        }

        public RelationshipMapping(String igcAssetType,
                                   String igcRelationshipName,
                                   String omrsRelationshipType,
                                   String omrsRelationshipSourceProperty,
                                   String omrsRelationshipTargetProperty,
                                   String igcSourceRidPrefix,
                                   String igcTargetRidPrefix) {
            this.igcAssetType = igcAssetType;
            this.igcRelationshipName = igcRelationshipName;
            this.omrsRelationshipType = omrsRelationshipType;
            this.omrsRelationshipSourceProperty = omrsRelationshipSourceProperty;
            this.omrsRelationshipTargetProperty = omrsRelationshipTargetProperty;
            this.igcSourceRidPrefix = igcSourceRidPrefix;
            this.igcTargetRidPrefix = igcTargetRidPrefix;
        }

        public boolean isInvertedMapping() { return (this.igcAssetType != null); }
        public String getIgcAssetType() { return this.igcAssetType; }
        public String getIgcRelationshipName() { return this.igcRelationshipName; }
        public String getOmrsRelationshipType() { return this.omrsRelationshipType; }
        public String getOmrsRelationshipSourceProperty() { return this.omrsRelationshipSourceProperty; }
        public String getOmrsRelationshipTargetProperty() { return this.omrsRelationshipTargetProperty; }
        public String getIgcSourceRidPrefix() { return this.igcSourceRidPrefix; }
        public String getIgcTargetRidPrefix() { return this.igcTargetRidPrefix; }
        public boolean needsIgcSourceRidPrefix() { return (this.igcSourceRidPrefix != null); }
        public boolean needsIgcTargetRidPrefix() { return (this.igcTargetRidPrefix != null); }

    }

}
