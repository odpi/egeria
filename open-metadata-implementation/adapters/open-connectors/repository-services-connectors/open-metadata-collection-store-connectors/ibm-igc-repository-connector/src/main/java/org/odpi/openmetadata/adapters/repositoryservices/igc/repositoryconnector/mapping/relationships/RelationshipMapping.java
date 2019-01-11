/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestConstants;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearch;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchCondition;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchConditionSet;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities.EntityMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Provides the base class for all relationship mappings, as well as base methods for calculating the transformed
 * relationships.
 */
public abstract class RelationshipMapping {

    private static final Logger log = LoggerFactory.getLogger(RelationshipMapping.class);
    public static final String SELF_REFERENCE_SENTINEL = "__SELF__";

    private ProxyMapping one;
    private ProxyMapping two;
    private String omrsRelationshipType;
    private OptimalStart optimalStart;

    private List<RelationshipMapping> subtypes;
    private String relationshipLevelIgcAsset;
    private String linkingAssetType;

    /**
     * The optimal endpoint from which to retrieve the relationship:
     *  - ONE = from ProxyOne
     *  - TWO = from ProxyTwo
     *  - OPPOSITE = from whichever Proxy does not match the entity for which relationships are being retrieved
     *  - CUSTOM = must be custom implemented via a complexRelationshipMappings method
     */
    public enum OptimalStart { ONE, TWO, OPPOSITE, CUSTOM }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RelationshipMapping: ");
        sb.append("omrsRelationshipType=");
        sb.append(omrsRelationshipType);
        sb.append(", ");
        sb.append("one={ ");
        sb.append(one);
        sb.append(" }, ");
        sb.append("two={ ");
        sb.append(two);
        sb.append(" }");
        return sb.toString();
    }

    protected RelationshipMapping(String igcAssetTypeProxyOne,
                                  String igcAssetTypeProxyTwo,
                                  String igcRelationshipPropertyFromOne,
                                  String igcRelationshipPropertyFromTwo,
                                  String omrsRelationshipType,
                                  String omrsRelationshipProxyOneProperty,
                                  String omrsRelationshipProxyTwoProperty) {
        this(
                igcAssetTypeProxyOne,
                igcAssetTypeProxyTwo,
                igcRelationshipPropertyFromOne,
                igcRelationshipPropertyFromTwo,
                omrsRelationshipType,
                omrsRelationshipProxyOneProperty,
                omrsRelationshipProxyTwoProperty,
                null,
                null
        );
    }

    protected RelationshipMapping(String igcAssetTypeProxyOne,
                                  String igcAssetTypeProxyTwo,
                                  String igcRelationshipPropertyFromOne,
                                  String igcRelationshipPropertyFromTwo,
                                  String omrsRelationshipType,
                                  String omrsRelationshipProxyOneProperty,
                                  String omrsRelationshipProxyTwoProperty,
                                  String igcProxyOneRidPrefix,
                                  String igcProxyTwoRidPrefix) {
        this.one = new ProxyMapping(
                igcAssetTypeProxyOne,
                igcRelationshipPropertyFromOne,
                omrsRelationshipProxyOneProperty,
                igcProxyOneRidPrefix
        );
        this.two = new ProxyMapping(
                igcAssetTypeProxyTwo,
                igcRelationshipPropertyFromTwo,
                omrsRelationshipProxyTwoProperty,
                igcProxyTwoRidPrefix
        );
        this.omrsRelationshipType = omrsRelationshipType;
        this.optimalStart = OptimalStart.OPPOSITE;
        this.subtypes = new ArrayList<>();
    }

    /**
     * Sets a relationship-level IGC asset type (these very rarely exist, only known example is 'classification').
     *
     * @param igcAssetType the name of the IGC asset that represents this relationship in IGC
     */
    public void setRelationshipLevelIgcAsset(String igcAssetType) { this.relationshipLevelIgcAsset = igcAssetType; }

    /**
     * Indicates whether this mapping has an IGC asset that represents the relationship directly (true) or not (false).
     *
     * @return boolean
     */
    public boolean hasRelationshipLevelAsset() { return this.relationshipLevelIgcAsset != null; }

    /**
     * Retrieve the name of the IGC asset type that represents the relationship itself.
     *
     * @return String
     */
    public String getRelationshipLevelIgcAsset() { return this.relationshipLevelIgcAsset; }

    /**
     * Define an IGC asset type as linking together the two endpoints of the relationship translated by this mapping.
     *
     * @param igcAssetType the IGC asset type that links together the two endpoints of the relationship
     */
    public void setLinkingAssetType(String igcAssetType) { this.linkingAssetType = igcAssetType; }

    /**
     * Indicates whether this mapping needs an IGC asset linking the endpoints (true) or not (false).
     *
     * @return boolean
     */
    public boolean hasLinkingAsset() { return this.linkingAssetType != null; }

    /**
     * Retrieve the name of the asset type needed to link together the two endpoints of the relationship (if any), or null.
     *
     * @return String
     */
    public String getLinkingAssetType() { return this.linkingAssetType; }

    /**
     * By default, return the asset itself. Override this method if the relationship mapping has a relationship-level
     * asset, or other complexity (multi-hops) to translate from a provided asset to the actual asset for the
     * first endpoint of the relationship in IGC.
     *
     * @param relationshipAsset the asset to use to lookup the actual first endpoint in IGC
     * @param igcRestClient REST API connectivity
     * @return Reference - the asset to be used for endpoint one of the relationship
     */
    public List<Reference> getProxyOneAssetFromAsset(Reference relationshipAsset, IGCRestClient igcRestClient) {
        ArrayList<Reference> referenceAsList = new ArrayList<>();
        referenceAsList.add(relationshipAsset);
        return referenceAsList;
    }

    /**
     * By default, return the asset itself. Override this method if the relationship mapping has a relationship-level
     * asset, or other complexity (multi-hops) to translate from a provided asset to the actual asset for the
     * second endpoint of the relationship in IGC.
     *
     * @param relationshipAsset the asset to use to lookup the actual second endpoint in IGC
     * @param igcRestClient REST API connectivity
     * @return Reference - the asset to be used for endpoint two of the relationship
     */
    public List<Reference> getProxyTwoAssetFromAsset(Reference relationshipAsset, IGCRestClient igcRestClient) {
        ArrayList<Reference> referenceAsList = new ArrayList<>();
        referenceAsList.add(relationshipAsset);
        return referenceAsList;
    }

    /**
     * Indicates whether this relationship mapping has subtypes (true) or not (false).
     * Subtypes can be used where the same relationship may represent relationships between a number of different
     * IGC objects and each needs to be distinguished to appropriately apply a mapping.
     *
     * @return boolean
     */
    public boolean hasSubTypes() { return !this.subtypes.isEmpty(); }

    /**
     * Adds a subtype to this relationship mapping.
     * Subtypes can be used where the same relationship may represent relationships between a number of different
     * IGC objects and each needs to be distinguished to appropriately apply a mapping.
     *
     * @param subRelationshipMapping the relationship mapping that defines a subtype of this mapping
     */
    public void addSubType(RelationshipMapping subRelationshipMapping) {
        this.subtypes.add(subRelationshipMapping);
    }

    /**
     * Retrieve the listing of sub-relationship mappings of this mapping.
     * Subtypes can be used where the same relationship may represent relationships between a number of different
     * IGC objects and each needs to be distinguished to appropriately apply a mapping.
     *
     * @return List<RelationshipMapping>
     */
    public List<RelationshipMapping> getSubTypes() { return this.subtypes; }

    /**
     * Must be implemented to define how to map the relationships defined by the mapper, if there are any complex
     * custom mappings to be done for relationships. (Simple mappings are handled automatically by the class.)
     *
     * @param igcomrsRepositoryConnector
     * @param relationships
     * @param fromIgcObject
     * @param userId
     */
    public void addMappedOMRSRelationships(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                           List<Relationship> relationships,
                                           Reference fromIgcObject,
                                           String userId) {
        // By default there are no complex / custom mappings
    }

    /**
     * Retrieve the type of OMRS relationship this mapping handles.
     *
     * @return String
     */
    public String getOmrsRelationshipType() { return this.omrsRelationshipType; }

    /**
     * Set the optimal starting point for traversing this relationship.
     *
     * @param optimalStart
     */
    public void setOptimalStart(OptimalStart optimalStart) { this.optimalStart = optimalStart; }

    /**
     * Get the optimal starting point for traversing this relationship.
     *
     * @return
     */
    public OptimalStart getOptimalStart() { return this.optimalStart; }

    /**
     * Add an alternative IGC property that can be used to traverse the relationship from proxy one to proxy two.
     *
     * @param property the name of the additional IGC relationship property
     */
    public void addAlternativePropertyFromOne(String property) { this.one.addAlternativeIgcRelationshipProperty(property); }

    /**
     * Add an alternative IGC property that can be used to traverse the relationship from proxy two to proxy one.
     *
     * @param property the name of the additional IGC relationship property
     */
    public void addAlternativePropertyFromTwo(String property) { this.two.addAlternativeIgcRelationshipProperty(property); }

    /**
     * Indicates whether the relationship references the same IGC object (true) or not (false). This is useful for
     * determining if the relationship is actually to a type that will be generated for OMRS, but does not exist as a
     * distinct instance within IGC.
     *
     * @return boolean
     */
    public boolean isSelfReferencing() { return (this.one.isSelfReferencing() || this.two.isSelfReferencing()); }

    /**
     * Retrieves the mapping details for endpoint 1 (proxy 1) of the OMRS relationship.
     *
     * @return ProxyMapping
     */
    public ProxyMapping getProxyOneMapping() { return this.one; }

    /**
     * Retrieves the mapping detals for endpoint 2 (proxy 2) of the OMRS relationship.
     *
     * @return ProxyMapping
     */
    public ProxyMapping getProxyTwoMapping() { return this.two; }

    /**
     * Indicates whether the same asset / entity type is used on both ends of the relationship (true) or not (false).
     *
     * @return boolean
     */
    public boolean sameTypeOnBothEnds() {
        return one.getIgcAssetType().equals(two.getIgcAssetType());
    }

    /**
     * Indicates whether the same relationship properties are used on both ends of the relationship (true) or not (false).
     *
     * @return boolean
     */
    public boolean samePropertiesOnBothEnds() {
        List<String> pOneProperties = one.getIgcRelationshipProperties();
        List<String> pTwoProperties = two.getIgcRelationshipProperties();
        return new HashSet<>(pOneProperties).equals(new HashSet<>(pTwoProperties));
    }

    /**
     * Retrieve the proxy details for the side of the relationship given by the asset type provided.
     *
     * @param igcAssetType the IGC asset type for which to find the same side of the relationship
     * @return ProxyMapping
     */
    public ProxyMapping getProxyFromType(String igcAssetType) {

        ProxyMapping same = null;

        if (igcAssetType == null) {
            log.error("No asset type provided: {}", igcAssetType);
        } else {
            String simpleType = Reference.getAssetTypeForSearch(igcAssetType);
            if (simpleType.equals(one.getIgcAssetType())) {
                same = this.one;
            } else if (simpleType.equals(two.getIgcAssetType())) {
                same = this.two;
            } else if (one.getIgcAssetType().equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE) && !one.excludeIgcAssetType.contains(simpleType)) {
                same = this.one;
            } else if (two.getIgcAssetType().equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE) && !two.excludeIgcAssetType.contains(simpleType)) {
                same = this.two;
            } else {
                log.error("getProxyFromType - Provided asset type does not match either proxy type (or was explicitly excluded): {}", simpleType);
            }
        }

        return same;

    }

    /**
     * Retrieve the proxy details for the other side of the relationship from the IGC asset type provided.
     *
     * @param igcAssetType the IGC asset type for which to find the other side of the relationship
     * @return ProxyMapping
     */
    public ProxyMapping getOtherProxyFromType(String igcAssetType) {

        ProxyMapping other = null;

        if (igcAssetType == null) {
            log.error("No asset type provided: {}", igcAssetType);
        } else {
            String simpleType = Reference.getAssetTypeForSearch(igcAssetType);
            if (simpleType.equals(one.getIgcAssetType())) {
                other = this.two;
            } else if (simpleType.equals(two.getIgcAssetType())) {
                other = this.one;
            } else if (one.getIgcAssetType().equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE) && !one.excludeIgcAssetType.contains(simpleType)) {
                other = this.two;
            } else if (two.getIgcAssetType().equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE) && !two.excludeIgcAssetType.contains(simpleType)) {
                other = this.one;
            } else {
                log.error("getOtherProxyFromType - Provided asset type does not match either proxy type (or was explicitly excluded): {}", simpleType);
            }
        }

        return other;

    }

    /**
     * Retrieve the IGC relationship properties that define the relationship for the provided IGC asset type.
     *
     * @param igcAssetType the IGC asset type for which to retrieve the relationship properties
     * @return List<String>
     */
    public List<String> getIgcRelationshipPropertiesForType(String igcAssetType) {

        List<String> properties = new ArrayList<>();

        if (igcAssetType == null) {
            log.error("No asset type provided: {}", igcAssetType);
        } else {
            String simpleType = Reference.getAssetTypeForSearch(igcAssetType);
            if (sameTypeOnBothEnds() && simpleType.equals(one.getIgcAssetType())) {
                addRealPropertiesToList(one.getIgcRelationshipProperties(), properties);
                addRealPropertiesToList(two.getIgcRelationshipProperties(), properties);
            } else if (simpleType.equals(one.getIgcAssetType())) {
                addRealPropertiesToList(one.getIgcRelationshipProperties(), properties);
            } else if (simpleType.equals(two.getIgcAssetType())) {
                addRealPropertiesToList(two.getIgcRelationshipProperties(), properties);
            } else if (one.getIgcAssetType().equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE) && !one.excludeIgcAssetType.contains(simpleType)) {
                addRealPropertiesToList(one.getIgcRelationshipProperties(), properties);
            } else if (two.getIgcAssetType().equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE) && !two.excludeIgcAssetType.contains(simpleType)) {
                addRealPropertiesToList(two.getIgcRelationshipProperties(), properties);
            } else {
                log.warn("getIgcRelationshipPropertiesForType - Provided asset type does not match either proxy type (or was explicitly excluded): {}", simpleType);
            }
        }

        return properties;

    }

    /**
     * Keep unique properties in the list, and avoid the SELF_REFERENCE_SENTINEL value.
     *
     * @param candidates the candidate list to add to the list of unique, real properties
     * @param realProperties the list of unique, real properties
     */
    private void addRealPropertiesToList(List<String> candidates, List<String> realProperties) {
        for (String propertyName : candidates) {
            if (!propertyName.equals(SELF_REFERENCE_SENTINEL) && !propertyName.equals("") && !realProperties.contains(propertyName)) {
                realProperties.add(propertyName);
            }
        }
    }

    /**
     * Class to represent each side of a relationship mapping, keeping the details of that side of the mapping together.
     */
    public class ProxyMapping {

        private String igcAssetType;
        private ArrayList<String> igcRelationshipProperties;
        private String omrsRelationshipProperty;
        private String igcRidPrefix;
        private Set<String> excludeIgcAssetType;

        public ProxyMapping(String igcAssetType,
                            String igcRelationshipProperty,
                            String omrsRelationshipProperty,
                            String igcRidPrefix) {

            this.igcAssetType = igcAssetType;
            this.igcRelationshipProperties = new ArrayList<>();
            this.igcRelationshipProperties.add(igcRelationshipProperty);
            this.omrsRelationshipProperty = omrsRelationshipProperty;
            this.igcRidPrefix = igcRidPrefix;
            this.excludeIgcAssetType = new HashSet<>();

        }

        /**
         * Retrieve the type of IGC asset that is expected by this side of the relationship mapping.
         *
         * @return String
         */
        public String getIgcAssetType() { return this.igcAssetType; }

        /**
         * Retrieve the list of IGC relationship properties that can be used from this side of the relationship
         * mapping to traverse to the other side of the relationship. (In most cases this will just be a single
         * relationship in the list.)
         *
         * @return List<String>
         */
        public List<String> getIgcRelationshipProperties() { return this.igcRelationshipProperties; }

        /**
         * Add an alternative IGC relationship property to this side of the relationship, that can be additionally
         * used to traverse to the other side of the relationship.
         *
         * @param igcRelationshipProperty the name of the additional IGC relationship property
         */
        public void addAlternativeIgcRelationshipProperty(String igcRelationshipProperty) { this.igcRelationshipProperties.add(igcRelationshipProperty); }

        /**
         * Retrieve the name of the OMRS relationship property that represents this side of the relationship.
         *
         * @return String
         */
        public String getOmrsRelationshipProperty() { return this.omrsRelationshipProperty; }

        /**
         * Retrieve the prefix that should be added to the IGC Repository ID (RID) in order to make this side of the
         * relationship point to a generated IGC entity instance (ie. one that does not exist distinctly within IGC).
         * If no such entity is required, this will be null.
         *
         * @return String
         */
        public String getIgcRidPrefix() { return this.igcRidPrefix; }

        /**
         * Indicates whether this side of the relationship is self-referencing (true) or not (false). Generally when
         * the relationship is self-referencing, at least one end of the relationship will require a prefix.
         *
         * @return boolean
         */
        public boolean isSelfReferencing() { return this.igcRelationshipProperties.contains(SELF_REFERENCE_SENTINEL); }

        /**
         * When the asset this applies to is a 'main_object', use this method to add any objects that should NOT be
         * included under that umbrella.
         *
         * @param igcAssetType the IGC asset type to exclude from 'main_object' consideration
         */
        public void addExcludedIgcAssetType(String igcAssetType) { this.excludeIgcAssetType.add(igcAssetType); }

        /**
         * Indicates whether this side of the relationship matches the provided IGC asset type: that is, this side of
         * the relationship can be used to relate to the provided IGC asset type.
         *
         * @param igcAssetType the IGC asset type to check this side of the relationship against
         * @return boolean
         */
        public boolean matchesAssetType(String igcAssetType) {
            String simplifiedType = Reference.getAssetTypeForSearch(igcAssetType);
            log.debug("checking for matching asset between {} and {}", this.igcAssetType, simplifiedType);
            return (
                    this.igcAssetType.equals(simplifiedType)
                    || (this.igcAssetType.equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE) && !this.excludeIgcAssetType.contains(simplifiedType))
                    || (hasLinkingAsset() && simplifiedType.equals(getLinkingAssetType()))
            );
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("igcAssetType=");
            sb.append(igcAssetType);
            sb.append(", omrsRelationshipProperty=");
            sb.append(omrsRelationshipProperty);
            sb.append(", igcRidPrefix=");
            sb.append(igcRidPrefix);
            sb.append(", igcRelationshipProperties=");
            sb.append(igcRelationshipProperties);
            return sb.toString();
        }

    }

    /**
     * Generates a unique relationship GUID based on the provided parameters. The method will check the provided
     * candidate endpoints against the mapping provided to ensure that they are appropriately matched to endpoint 1
     * and endpoint 2 of the OMRS relationship. (Therefore endOne and endTwo may not actually be proxyOne and proxyTwo
     * of the OMRS relationship, but could be reversed.)
     * <br><br>
     * The intention of the method is to guarantee a unique, consistent GUID irrespective of the direction in which
     * the relationship was traversed. For example: an IGC 'parent_category' relationship from B to A for a
     * CategoryHierarchyLink should result in the same GUID as an IGC 'subcategories' relationship from A to B.
     *
     * @param relationshipMapping the relationship mapping defining how an IGC relationship maps to an OMRS relationship
     * @param endOne the candidate to consider for endpoint 1 of the relationship
     * @param endTwo the candidate to consider for endpoint 2 of the relationship
     * @param igcPropertyName the name of the IGC property for which the relationship is being generated
     * @param relationshipLevelRid the relationship-level RID (if any) within IGC (these are very rare)
     * @return String - the unique GUID for the relationship
     */
    public static String getRelationshipGUID(RelationshipMapping relationshipMapping,
                                             Reference endOne,
                                             Reference endTwo,
                                             String igcPropertyName,
                                             String relationshipLevelRid) {

        String omrsRelationshipName = relationshipMapping.getOmrsRelationshipType();
        // Lookup types via this helper function, to translate any alias types (eg. host_(engine) and host)
        String endOneType = Reference.getAssetTypeForSearch(endOne.getType());
        String endTwoType = Reference.getAssetTypeForSearch(endTwo.getType());

        log.debug("Calculating relationship GUID from {} to {} via {} for {} (with mapper: {})", endOneType, endTwoType, igcPropertyName, omrsRelationshipName, relationshipMapping.getClass().getCanonicalName());

        // If the relationship mapping includes a relationship-level asset, check if either provided endpoint is one
        // (and if so, setup the relationshipLevelRid based on its ID)
        if (relationshipMapping.hasRelationshipLevelAsset()) {
            String relationshipLevelType = relationshipMapping.getRelationshipLevelIgcAsset();
            if (endOneType.equals(relationshipLevelType)) {
                relationshipLevelRid = endOne.getId();
            } else if (endTwoType.equals(relationshipLevelType)) {
                relationshipLevelRid = endTwo.getId();
            }
        }

        ProxyMapping pmOne = relationshipMapping.getProxyOneMapping();
        ProxyMapping pmTwo = relationshipMapping.getProxyTwoMapping();
        List<String> pmOneProperties = pmOne.getIgcRelationshipProperties();
        List<String> pmTwoProperties = pmTwo.getIgcRelationshipProperties();

        String proxyOneRid = null;
        String proxyTwoRid = null;

        if (relationshipMapping.sameTypeOnBothEnds()
                && pmOne.matchesAssetType(endOneType)) {
            if (relationshipMapping.samePropertiesOnBothEnds()) {
                // If both the types and property names of both ends of the mapping are the same (eg. synonyms and
                // translations on terms), then only option is to sort the RIDs themselves to give consistency
                String endOneRid = endOne.getId();
                String endTwoRid = endTwo.getId();
                log.debug(" ... same types, same properties: alphabetically sorting RIDs.");
                if (endOneRid.compareTo(endTwoRid) > 0) {
                    proxyOneRid = endOneRid;
                    proxyTwoRid = endTwoRid;
                } else {
                    proxyOneRid = endTwoRid;
                    proxyTwoRid = endOneRid;
                }
            } else {
                // Otherwise if only the types are the same on both ends, the property is key to determining which
                // end is which, and also relies on the direction in which they were retrieved
                switch (relationshipMapping.getOptimalStart()) {
                    case OPPOSITE:
                        if (pmOneProperties.contains(igcPropertyName)) {
                            log.debug(" ... same types, opposite lookup, property matches one: reversing RIDs.");
                            proxyOneRid = endTwo.getId();
                            proxyTwoRid = endOne.getId();
                        } else if (pmTwoProperties.contains(igcPropertyName)) {
                            log.debug(" ... same types, opposite lookup, property matches two: keeping RID direction.");
                            proxyOneRid = endOne.getId();
                            proxyTwoRid = endTwo.getId();
                        }
                        break;
                    default:
                        if (pmOneProperties.contains(igcPropertyName)) {
                            log.debug(" ... same types, direct lookup, property matches one: keeping RID direction.");
                            proxyOneRid = endOne.getId();
                            proxyTwoRid = endTwo.getId();
                        } else if (pmTwoProperties.contains(igcPropertyName)) {
                            log.debug(" ... same types, direct lookup, property matches two: reversing RIDs.");
                            proxyOneRid = endTwo.getId();
                            proxyTwoRid = endOne.getId();
                        }
                        break;
                }
            }
        } else if (pmOne.matchesAssetType(endOneType)
                && (pmOneProperties.contains(igcPropertyName) || pmTwoProperties.contains(igcPropertyName))
                && pmTwo.matchesAssetType(endTwoType) ) {
            // Otherwise if one aligns with one and two aligns with two, and property appears somewhere, go with those
            // (this should also apply when the relationship is self-referencing)
            log.debug(" ... one matches one, two matches two: keeping RID direction.");
            proxyOneRid = endOne.getId();
            proxyTwoRid = endTwo.getId();
        } else if (pmTwo.matchesAssetType(endOneType)
                && (pmOneProperties.contains(igcPropertyName) || pmTwoProperties.contains(igcPropertyName))
                && pmOne.matchesAssetType(endTwoType) ) {
            // Or if two aligns with one and one aligns with two, and property appears somewhere, reverse them
            log.debug(" ... two matches one, one matches two: reversing RIDs.");
            proxyOneRid = endTwo.getId();
            proxyTwoRid = endOne.getId();
        } else if (relationshipLevelRid == null) {
            // Otherwise indicate something appears to be wrong
            log.error("Unable to find matching ends for relationship {} from {} to {} via {}", omrsRelationshipName, endOne.getId(), endTwo.getId(), igcPropertyName);
        }

        String proxyOnePrefix = pmOne.getIgcRidPrefix();
        String proxyTwoPrefix = pmTwo.getIgcRidPrefix();

        // For relationship-specific RIDs (very rare) we'll output only the relationship-level RID
        StringBuilder sbGUID = new StringBuilder();
        if (relationshipLevelRid != null) {
            sbGUID.append(relationshipLevelRid);
            sbGUID.append("::");
            sbGUID.append(omrsRelationshipName);
            sbGUID.append("::");
            sbGUID.append(relationshipLevelRid);
        } else {
            if (proxyOnePrefix != null) {
                proxyOneRid = proxyOnePrefix + proxyOneRid;
            }
            if (proxyTwoPrefix != null) {
                proxyTwoRid = proxyTwoPrefix + proxyTwoRid;
            }
            sbGUID.append(proxyOneRid);
            sbGUID.append("::");
            sbGUID.append(omrsRelationshipName);
            sbGUID.append("::");
            sbGUID.append(proxyTwoRid);
        }
        return sbGUID.toString();
    }

    /**
     * Retrieve the portion of the provided relationship GUID that represents the GUID for proxy one of the relationship.
     *
     * @param relationshipGUID the relationship GUID
     * @return String - giving just the GUID for proxy one of the relationship
     */
    public static String getProxyOneGUIDFromRelationshipGUID(String relationshipGUID) {
        return getRelationshipGUIDToken(relationshipGUID, 0);
    }

    /**
     * Retrieve the portion of the provided relationship GUID that represents the GUID for proxy two of the relationship.
     *
     * @param relationshipGUID the relationship GUID
     * @return String - giving just the GUID for proxy two of the relationship
     */
    public static String getProxyTwoGUIDFromRelationshipGUID(String relationshipGUID) {
        return getRelationshipGUIDToken(relationshipGUID, 2);
    }

    /**
     * Retrieve the portion of the provided relationship GUID that represents the OMRS relationship type.
     * @param relationshipGUID the relationship GUID
     * @return String - giving just the name of the OMRS relationship type
     */
    public static String getRelationshipTypeFromRelationshipGUID(String relationshipGUID) {
        return getRelationshipGUIDToken(relationshipGUID, 1);
    }

    /**
     * Utility method to parse the different portions of a relationship GUID.
     *
     * @param relationshipGUID the relationship GUID
     * @param index which token to retrieve from the relationship GUID
     * @return String - the string at the token provided of the provided relationship GUID
     */
    private static String getRelationshipGUIDToken(String relationshipGUID, int index) {
        String[] aTokens = relationshipGUID.split("::");
        if (aTokens.length != 3) {
            log.warn("Unexpected number of tokens from relationship GUID: {}", relationshipGUID);
        }
        if (aTokens.length >= index) {
            return aTokens[index];
        } else {
            log.error("Unable to translate provided relationship GUID: {}", relationshipGUID);
            return null;
        }
    }

    /**
     * Retrieves an EntityProxy object for the provided IGC object.
     *
     * @param igcomrsRepositoryConnector OMRS connector to the IBM IGC repository
     * @param igcObj the IGC object for which to retrieve an EntityProxy
     * @param omrsTypeName the OMRS entity type
     * @param userId the user through which to retrieve the EntityProxy (unused)
     * @param ridPrefix any prefix required on the object's ID to make it unique
     * @return EntityProxy
     */
    public static EntityProxy getEntityProxyForObject(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                                      Reference igcObj,
                                                      String omrsTypeName,
                                                      String userId,
                                                      String ridPrefix) {

        IGCRestClient igcRestClient = igcomrsRepositoryConnector.getIGCRestClient();
        String igcType = igcObj.getType();
        PrimitivePropertyValue qualifiedName = null;

        EntityProxy entityProxy = null;

        if (igcType != null) {
            // Construct 'qualifiedName' from the Identity of the object
            String identity = igcObj.getIdentity(igcRestClient).toString();
            if (ridPrefix != null) {
                identity = ridPrefix + identity;
            }
            qualifiedName = EntityMapping.getPrimitivePropertyValue(identity);

            // 'qualifiedName' is the only unique InstanceProperty we need on an EntityProxy
            InstanceProperties uniqueProperties = new InstanceProperties();
            uniqueProperties.setProperty("qualifiedName", qualifiedName);

            try {
                entityProxy = igcomrsRepositoryConnector.getRepositoryHelper().getNewEntityProxy(
                        igcomrsRepositoryConnector.getRepositoryName(),
                        igcomrsRepositoryConnector.getMetadataCollectionId(),
                        InstanceProvenanceType.LOCAL_COHORT,
                        userId,
                        omrsTypeName,
                        uniqueProperties,
                        null
                );
                if (ridPrefix != null) {
                    entityProxy.setGUID(ridPrefix + igcObj.getId());
                } else {
                    entityProxy.setGUID(igcObj.getId());
                }

                if (igcObj.hasModificationDetails()) {
                    entityProxy.setCreatedBy((String)igcObj.getPropertyByName(IGCRestConstants.MOD_CREATED_BY));
                    entityProxy.setCreateTime((Date)igcObj.getPropertyByName(IGCRestConstants.MOD_CREATED_ON));
                    entityProxy.setUpdatedBy((String)igcObj.getPropertyByName(IGCRestConstants.MOD_MODIFIED_BY));
                    entityProxy.setUpdateTime((Date)igcObj.getPropertyByName(IGCRestConstants.MOD_MODIFIED_ON));
                    if (entityProxy.getUpdateTime() != null) {
                        entityProxy.setVersion(entityProxy.getUpdateTime().getTime());
                    }
                }

            } catch (TypeErrorException e) {
                log.error("Unable to create new EntityProxy.", e);
            }
        }

        return entityProxy;

    }

    /**
     * Utility function that actually does the Relationship object setup and addition to 'relationships' member.
     *
     * @param igcomrsRepositoryConnector connectivity to an IGC environment
     * @param relationships the list of relationships to append to
     * @param mappings the mappings to use for retrieving the relationships
     * @param relationshipTypeGUID String GUID of the the type of relationship required (null for all).
     * @param fromIgcObject the IGC object that is the source of the relationships
     * @param userId
     */
    public static void getMappedRelationships(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                              List<Relationship> relationships,
                                              List<RelationshipMapping> mappings,
                                              String relationshipTypeGUID,
                                              Reference fromIgcObject,
                                              String userId) {

        // Iterate through the provided mappings to create a number of OMRS relationships
        for (RelationshipMapping mapping : mappings) {

            RelationshipDef omrsRelationshipDef = (RelationshipDef) igcomrsRepositoryConnector.getRepositoryHelper().getTypeDefByName(
                    igcomrsRepositoryConnector.getRepositoryName(),
                    mapping.getOmrsRelationshipType()
            );

            // Only continue with a given relationship if we are mapping all relationships or it matches the
            // GUID of the relationship type we are mapping
            if (relationshipTypeGUID == null || relationshipTypeGUID.equals(omrsRelationshipDef.getGUID())) {

                RelationshipMapping.OptimalStart optimalStart = mapping.getOptimalStart();
                String fromAssetType = fromIgcObject.getType();
                ProxyMapping pmOne = mapping.getProxyOneMapping();
                ProxyMapping pmTwo = mapping.getProxyTwoMapping();

                if (mapping.isSelfReferencing()) {
                    addSelfReferencingRelationship(igcomrsRepositoryConnector, mapping, relationships, fromIgcObject, userId);
                } else if (!optimalStart.equals(RelationshipMapping.OptimalStart.CUSTOM)) {
                    if (fromIgcObject == null) {
                        log.error("Object received to lookup {} relationship was null, cannot proceed.", relationshipTypeGUID);
                    } else if (fromIgcObject.isFullyRetrieved()
                            || (optimalStart.equals(OptimalStart.ONE) && pmOne.matchesAssetType(fromAssetType) )
                            || (optimalStart.equals(OptimalStart.TWO) && pmTwo.matchesAssetType(fromAssetType)) ) {
                        addDirectRelationship(igcomrsRepositoryConnector, mapping, relationships, fromIgcObject, userId);
                    } else if (optimalStart.equals(OptimalStart.OPPOSITE)
                            || (optimalStart.equals(OptimalStart.TWO) && pmOne.matchesAssetType(fromAssetType))
                            || (optimalStart.equals(OptimalStart.ONE) && pmTwo.matchesAssetType(fromAssetType)) ) {
                        addInvertedRelationship(igcomrsRepositoryConnector, mapping, relationships, fromIgcObject, userId);
                    } else {
                        log.warn("Ran out of options for finding the relationship: {}", omrsRelationshipDef.getName());
                    }
                }

                // Then call any complex / custom relationship mappings defined
                mapping.addMappedOMRSRelationships(
                        igcomrsRepositoryConnector,
                        relationships,
                        fromIgcObject,
                        userId
                );

            }

        }

    }

    /**
     * Adds a self-referencing relationship based on the provided mapping information.
     *
     * @param mapping the mapping for the self-referencing relationship
     * @param relationships the list of relationships to append to
     * @param fromIgcObject the IGC object that is the source (and target) of the self-referencing relationship
     * @param userId
     */
    private static void addSelfReferencingRelationship(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                                       RelationshipMapping mapping,
                                                       List<Relationship> relationships,
                                                       Reference fromIgcObject,
                                                       String userId) {
        try {
            Relationship relationship = getMappedRelationship(
                    igcomrsRepositoryConnector,
                    mapping,
                    fromIgcObject,
                    fromIgcObject,
                    RelationshipMapping.SELF_REFERENCE_SENTINEL,
                    userId
            );
            log.debug("addSelfReferencingRelationship - adding relationship: {}", relationship);
            relationships.add(relationship);
        } catch (RepositoryErrorException e) {
            log.error("Unable to add self-referencing relationship for: {}", fromIgcObject, e);
        }
    }

    /**
     * Adds a relationship that is straightforward to lookup directly from the originating IGC asset.
     *
     * @param igcomrsRepositoryConnector connectivity to the IGC repository
     * @param mapping the mapping for the direct relationship
     * @param relationships the list of relationships to append to
     * @param fromIgcObject the IGC object that is the source of the direct relationship
     * @param userId
     */
    private static void addDirectRelationship(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                              RelationshipMapping mapping,
                                              List<Relationship> relationships,
                                              Reference fromIgcObject,
                                              String userId) {

        // If we already have all info about the entity, optimal path to retrieve relationships is to use
        // the ones that are already in-memory -- though if it is also not optimal (or possible) to retrieve
        // from a search (see below) we must also resort to this property-based retrieval
        for (String igcRelationshipName : mapping.getIgcRelationshipPropertiesForType(fromIgcObject.getType())) {

            Object directRelationships = fromIgcObject.getPropertyByName(igcRelationshipName);

            // Handle single instance relationship one way
            if (directRelationships != null && Reference.isReference(directRelationships)) {

                addSingleMappedRelationship(
                        igcomrsRepositoryConnector,
                        mapping,
                        relationships,
                        fromIgcObject,
                        (Reference) directRelationships,
                        igcRelationshipName,
                        userId
                );

            } else if (directRelationships != null && Reference.isReferenceList(directRelationships)) { // and list of relationships another

                addListOfMappedRelationships(
                        igcomrsRepositoryConnector,
                        mapping,
                        relationships,
                        fromIgcObject,
                        (ReferenceList) directRelationships,
                        igcRelationshipName,
                        userId
                );

            } else {
                log.debug(" ... skipping relationship {}, either empty or neither reference or list {}", igcRelationshipName, directRelationships);
            }

        }

    }

    /**
     * Adds a relationship that is best looked-up in reverse (from target back to source asset).
     *
     * @param igcomrsRepositoryConnector connectivity to the IGC repository
     * @param mapping the mapping for the inverted relationship
     * @param relationships the list of relationships to append to
     * @param fromIgcObject the IGC object that is the source of the inverted relationship (or really the target)
     * @param userId
     */
    private static void addInvertedRelationship(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                                RelationshipMapping mapping,
                                                List<Relationship> relationships,
                                                Reference fromIgcObject,
                                                String userId) {

        String assetType = fromIgcObject.getType();

        log.debug("Adding inverted relationship for mapping: {}", mapping);

        if (mapping.sameTypeOnBothEnds()) {

            // If we have a hierarchical relationship, we need to run searches across both
            // properties and add both sets of relationships
            List<String> igcProperties = mapping.getIgcRelationshipPropertiesForType(assetType);
            for (String igcRelationshipName : igcProperties) {
                IGCSearchCondition condition = new IGCSearchCondition(igcRelationshipName, "=", fromIgcObject.getId());
                IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet(condition);
                addSearchResultsToRelationships(
                        igcomrsRepositoryConnector,
                        mapping,
                        relationships,
                        fromIgcObject,
                        igcSearchConditionSet,
                        assetType,
                        igcRelationshipName,
                        userId
                );
            }

        } else {

            // Otherwise, use the optimal retrieval for the relationship (a search that will batch-retrieve _context)
            RelationshipMapping.ProxyMapping otherSide = mapping.getOtherProxyFromType(assetType);
            log.debug(" ... found other proxy: {}", otherSide);
            RelationshipMapping.ProxyMapping thisSide = mapping.getProxyFromType(assetType);
            log.debug(" ... found this proxy: {}", thisSide);

            String anIgcRelationshipProperty = null;
            IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet();
            igcSearchConditionSet.setMatchAnyCondition(true);
            for (String igcRelationshipName : otherSide.getIgcRelationshipProperties()) {
                IGCSearchCondition condition = new IGCSearchCondition(igcRelationshipName, "=", fromIgcObject.getId());
                igcSearchConditionSet.addCondition(condition);
                anIgcRelationshipProperty = igcRelationshipName;
            }
            String sourceAssetType = otherSide.getIgcAssetType();
            addSearchResultsToRelationships(
                    igcomrsRepositoryConnector,
                    mapping,
                    relationships,
                    fromIgcObject,
                    igcSearchConditionSet,
                    sourceAssetType,
                    anIgcRelationshipProperty,
                    userId
            );

        }

    }

    /**
     * Executes a search against the IGC repository based on the provided criteria, and adds all results to the list
     * of relationships.
     *
     * @param igcomrsRepositoryConnector connectivity to the IGC repository
     * @param mapping the mapping for the inverted relationship
     * @param relationships the list of relationships to append to
     * @param fromIgcObject the object that is the source of the IGC relationship
     * @param igcSearchConditionSet the search criteria to use for the search
     * @param assetType the type of IGC asset for which to search
     * @param igcPropertyName the name of the IGC property to search against
     * @param userId
     */
    private static void addSearchResultsToRelationships(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                                        RelationshipMapping mapping,
                                                        List<Relationship> relationships,
                                                        Reference fromIgcObject,
                                                        IGCSearchConditionSet igcSearchConditionSet,
                                                        String assetType,
                                                        String igcPropertyName,
                                                        String userId) {

        IGCSearch igcSearch = new IGCSearch(assetType, igcSearchConditionSet);
        if (!assetType.equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE)) {
            Class pojo = igcomrsRepositoryConnector.getIGCRestClient().getPOJOForType(assetType);
            if (pojo != null && Reference.hasModificationDetails(pojo)) {
                igcSearch.addProperties(IGCRestConstants.getInstance().MODIFICATION_DETAILS());
            }
        }
        ReferenceList foundRelationships = igcomrsRepositoryConnector.getIGCRestClient().search(igcSearch);
        addListOfMappedRelationships(
                igcomrsRepositoryConnector,
                mapping,
                relationships,
                fromIgcObject,
                foundRelationships,
                igcPropertyName,
                userId
        );

    }

    /**
     * Add the provided list of relationships as OMRS relationships.
     *
     * @param igcomrsRepositoryConnector connectivity to the IGC repository
     * @param mapping the mapping to use in translating each relationship
     * @param relationships the list of relationships to append to
     * @param fromIgcObject the asset that is the source of the IGC relationship
     * @param igcRelationships the list of IGC relationships
     * @param igcPropertyName the name of the IGC relationship property
     * @param userId
     */
    private static void addListOfMappedRelationships(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                                     RelationshipMapping mapping,
                                                     List<Relationship> relationships,
                                                     Reference fromIgcObject,
                                                     ReferenceList igcRelationships,
                                                     String igcPropertyName,
                                                     String userId) {

        log.debug(" ... list of references: {}", mapping.getOmrsRelationshipType());

        // TODO: paginate rather than always retrieving the full set
        igcRelationships.getAllPages(igcomrsRepositoryConnector.getIGCRestClient());

        // Iterate through all of the existing IGC relationships of that type to create an OMRS relationship
        // for each one
        for (Reference relation : igcRelationships.getItems()) {
            addSingleMappedRelationship(
                    igcomrsRepositoryConnector,
                    mapping,
                    relationships,
                    fromIgcObject,
                    relation,
                    igcPropertyName,
                    userId
            );
        }

    }

    /**
     * Add the provided relationship as an OMRS relationship.
     *
     * @param igcomrsRepositoryConnector connectivity to the IGC repository
     * @param mapping the mapping to use in translating each relationship
     * @param relationships the list of relationships to append to
     * @param fromIgcObject the asset that is the source of the IGC relationship
     * @param igcRelationship the IGC relationship
     * @param igcPropertyName the name of the IGC relationship property
     * @param userId
     */
    private static void addSingleMappedRelationship(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                                    RelationshipMapping mapping,
                                                    List<Relationship> relationships,
                                                    Reference fromIgcObject,
                                                    Reference igcRelationship,
                                                    String igcPropertyName,
                                                    String userId) {

        log.debug(" ... single reference: {}", igcRelationship);
        if (igcRelationship != null
                && igcRelationship.getType() != null
                && !igcRelationship.getType().equals("null")) {

            try {
                Relationship omrsRelationship = getMappedRelationship(
                        igcomrsRepositoryConnector,
                        mapping,
                        fromIgcObject,
                        igcRelationship,
                        igcPropertyName,
                        userId
                );
                log.debug("addSingleMappedRelationship - adding relationship: {}", omrsRelationship);
                relationships.add(omrsRelationship);
            } catch (RepositoryErrorException e) {
                log.error("Unable to add relationship {} for object {}", mapping.getOmrsRelationshipType(), igcRelationship);
            }

        }

    }

    /**
     * Retrieve a Relationship instance based on the provided mapping information, automatically prefixing
     * where needed.
     *
     * @param igcomrsRepositoryConnector connectivity to the IGC repository
     * @param mapping the mapping details to use
     * @param fromIgcObject the asset that is the source of the IGC relationship
     * @param relation the related IGC object
     * @param igcPropertyName the name of the IGC relationship property
     * @param userId
     * @return Relationship
     * @throws RepositoryErrorException
     */
    private static Relationship getMappedRelationship(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                                      RelationshipMapping mapping,
                                                      Reference fromIgcObject,
                                                      Reference relation,
                                                      String igcPropertyName,
                                                      String userId) throws RepositoryErrorException {

        RelationshipDef omrsRelationshipDef = (RelationshipDef) igcomrsRepositoryConnector.getRepositoryHelper().getTypeDefByName(
                igcomrsRepositoryConnector.getRepositoryName(),
                mapping.getOmrsRelationshipType()
        );

        return getMappedRelationship(
                igcomrsRepositoryConnector,
                mapping,
                omrsRelationshipDef,
                fromIgcObject,
                relation,
                igcPropertyName,
                userId
        );

    }

    /**
     * Retrieve a Relationship instance based on the provided definition and endpoints.
     *
     * @param igcomrsRepositoryConnector connectivity to the IGC repository
     * @param relationshipMapping the definition of how to map the relationship
     * @param omrsRelationshipDef the OMRS relationship definition
     * @param proxyOne the IGC asset to use for endpoint 1 of the relationship
     * @param proxyTwo the IGC asset to use for endpoint 2 of the relationship
     * @param igcPropertyName the name of the IGC relationship property
     * @param userId
     * @return Relationship
     * @throws RepositoryErrorException
     */
    protected static Relationship getMappedRelationship(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                                        RelationshipMapping relationshipMapping,
                                                        RelationshipDef omrsRelationshipDef,
                                                        Reference proxyOne,
                                                        Reference proxyTwo,
                                                        String igcPropertyName,
                                                        String userId) throws RepositoryErrorException {
        return getMappedRelationship(
                igcomrsRepositoryConnector,
                relationshipMapping,
                omrsRelationshipDef,
                proxyOne,
                proxyTwo,
                igcPropertyName,
                userId,
                null
        );
    }

    /**
     * Retrieve a Relationship instance based on the provided definition, endpoints, and optional prefixes.
     *
     * @param igcomrsRepositoryConnector connectivity to the IGC repository
     * @param relationshipMapping the definition of how to map the relationship
     * @param omrsRelationshipDef the OMRS relationship definition
     * @param proxyOne the IGC asset to consider for endpoint 1 of the relationship
     * @param proxyTwo the IGC asset to consider for endpoint 2 of the relationship
     * @param igcPropertyName the name of the IGC relationship property
     * @param userId
     * @param relationshipLevelRid the IGC RID for the relationship itself (in rare instances where it exists)
     * @return Relationship
     * @throws RepositoryErrorException
     */
    public static Relationship getMappedRelationship(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                                     RelationshipMapping relationshipMapping,
                                                     RelationshipDef omrsRelationshipDef,
                                                     Reference proxyOne,
                                                     Reference proxyTwo,
                                                     String igcPropertyName,
                                                     String userId,
                                                     String relationshipLevelRid) throws RepositoryErrorException {

        final String methodName = "getMappedRelationship";
        final String repositoryName = igcomrsRepositoryConnector.getRepositoryName();

        String omrsRelationshipName = omrsRelationshipDef.getName();

        Relationship relationship = new Relationship();

        try {
            InstanceType instanceType = igcomrsRepositoryConnector.getRepositoryHelper().getNewInstanceType(
                    igcomrsRepositoryConnector.getRepositoryName(),
                    omrsRelationshipDef
            );
            relationship.setType(instanceType);
        } catch (TypeErrorException e) {
            log.error("Unable to set instance type.", e);
        }

        if (proxyOne != null && proxyTwo != null) {

            String relationshipGUID = RelationshipMapping.getRelationshipGUID(
                    relationshipMapping,
                    proxyOne,
                    proxyTwo,
                    igcPropertyName,
                    relationshipLevelRid
            );

            if (relationshipGUID == null) {
                log.error("Unable to construct relationship GUID -- skipping relationship: {}", omrsRelationshipName);
            } else {
                relationship.setGUID(relationshipGUID);
            }

            relationship.setMetadataCollectionId(igcomrsRepositoryConnector.getMetadataCollectionId());
            relationship.setStatus(InstanceStatus.ACTIVE);

            String guidForEP1 = RelationshipMapping.getProxyOneGUIDFromRelationshipGUID(relationshipGUID);
            String guidForEP2 = RelationshipMapping.getProxyTwoGUIDFromRelationshipGUID(relationshipGUID);
            String ridForEP1 = IGCOMRSMetadataCollection.getRidFromGeneratedId(guidForEP1);
            String ridForEP2 = IGCOMRSMetadataCollection.getRidFromGeneratedId(guidForEP2);

            EntityProxy ep1 = null;
            EntityProxy ep2 = null;

            if (relationshipLevelRid != null
                    || (ridForEP1.equals(proxyOne.getId()) && ridForEP2.equals(proxyTwo.getId()))) {
                ep1 = RelationshipMapping.getEntityProxyForObject(
                        igcomrsRepositoryConnector,
                        proxyOne,
                        omrsRelationshipDef.getEndDef1().getEntityType().getName(),
                        userId,
                        relationshipMapping.getProxyOneMapping().getIgcRidPrefix()
                );
                ep2 = RelationshipMapping.getEntityProxyForObject(
                        igcomrsRepositoryConnector,
                        proxyTwo,
                        omrsRelationshipDef.getEndDef2().getEntityType().getName(),
                        userId,
                        relationshipMapping.getProxyTwoMapping().getIgcRidPrefix()
                );
            } else if (ridForEP2.equals(proxyOne.getId()) && ridForEP1.equals(proxyTwo.getId())) {
                ep1 = RelationshipMapping.getEntityProxyForObject(
                        igcomrsRepositoryConnector,
                        proxyTwo,
                        omrsRelationshipDef.getEndDef1().getEntityType().getName(),
                        userId,
                        relationshipMapping.getProxyOneMapping().getIgcRidPrefix()
                );
                ep2 = RelationshipMapping.getEntityProxyForObject(
                        igcomrsRepositoryConnector,
                        proxyOne,
                        omrsRelationshipDef.getEndDef2().getEntityType().getName(),
                        userId,
                        relationshipMapping.getProxyTwoMapping().getIgcRidPrefix()
                );
            } else {
                log.error("Unable to determine both ends of the relationship {} from {} to {}", omrsRelationshipName, proxyOne.getId(), proxyTwo.getId());
            }

            // Set the the version of the relationship to the epoch time of whichever end of the relationship has
            // modification details (they should be the same if both have modification details)
            if (ep1 != null && ep1.getUpdateTime() != null) {
                relationship.setVersion(ep1.getUpdateTime().getTime());
            } else if (ep2 != null && ep2.getUpdateTime() != null) {
                relationship.setVersion(ep2.getUpdateTime().getTime());
            }

            if (ep1 != null && ep2 != null) {
                relationship.setEntityOneProxy(ep1);
                relationship.setEntityTwoProxy(ep2);
            }

        } else {
            String omrsEndOneProperty = omrsRelationshipDef.getEndDef1().getAttributeName();
            String omrsEndTwoProperty = omrsRelationshipDef.getEndDef2().getAttributeName();
            OMRSErrorCode errorCode = OMRSErrorCode.INVALID_RELATIONSHIP_ENDS;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    repositoryName,
                    omrsRelationshipName,
                    omrsEndOneProperty,
                    omrsEndTwoProperty);
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    RelationshipMapping.class.getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return relationship;

    }

}
