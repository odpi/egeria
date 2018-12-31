/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities.EntityMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public abstract class RelationshipMapping {

    private static final Logger log = LoggerFactory.getLogger(RelationshipMapping.class);
    public static final String SELF_REFERENCE_SENTINEL = "__SELF__";

    private ProxyMapping one;
    private ProxyMapping two;
    private String omrsRelationshipType;
    private OptimalStart optimalStart;

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
    }

    public String getOmrsRelationshipType() { return this.omrsRelationshipType; }
    public void setOptimalStart(OptimalStart optimalStart) { this.optimalStart = optimalStart; }
    public OptimalStart getOptimalStart() { return this.optimalStart; }

    public void addAlternativePropertyFromOne(String property) { this.one.addAlternativeIgcRelationshipProperty(property); }
    public void addAlternativePropertyFromTwo(String property) { this.two.addAlternativeIgcRelationshipProperty(property); }

    public boolean isSelfReferencing() { return (this.one.isSelfReferencing() || this.two.isSelfReferencing()); }

    public ProxyMapping getProxyOneMapping() { return this.one; }
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
            if (igcAssetType.equals(one.getIgcAssetType())) {
                same = this.one;
            } else if (igcAssetType.equals(two.getIgcAssetType())) {
                same = this.two;
            } else if (one.getIgcAssetType().equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE)) {
                same = this.one;
            } else if (two.getIgcAssetType().equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE)) {
                same = this.two;
            } else {
                log.error("getProxyFromType - Provided asset type does not match either proxy type: {}", igcAssetType);
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
            if (igcAssetType.equals(one.getIgcAssetType())) {
                other = this.two;
            } else if (igcAssetType.equals(two.getIgcAssetType())) {
                other = this.one;
            } else if (one.getIgcAssetType().equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE)) {
                other = this.two;
            } else if (two.getIgcAssetType().equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE)) {
                other = this.one;
            } else {
                log.error("getOtherProxyFromType - Provided asset type does not match either proxy type: {}", igcAssetType);
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
            if (sameTypeOnBothEnds() && igcAssetType.equals(one.getIgcAssetType())) {
                addRealPropertiesToList(one.getIgcRelationshipProperties(), properties);
                addRealPropertiesToList(two.getIgcRelationshipProperties(), properties);
            } else if (igcAssetType.equals(one.getIgcAssetType())) {
                addRealPropertiesToList(one.getIgcRelationshipProperties(), properties);
            } else if (igcAssetType.equals(two.getIgcAssetType())) {
                addRealPropertiesToList(two.getIgcRelationshipProperties(), properties);
            } else if (one.getIgcAssetType().equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE)) {
                addRealPropertiesToList(one.getIgcRelationshipProperties(), properties);
            } else if (two.getIgcAssetType().equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE)) {
                addRealPropertiesToList(two.getIgcRelationshipProperties(), properties);
            } else {
                log.warn("getIgcRelationshipPropertiesForType - Provided asset type does not match either proxy type: {}", igcAssetType);
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
            if (!propertyName.equals(SELF_REFERENCE_SENTINEL) && !realProperties.contains(propertyName)) {
                realProperties.add(propertyName);
            }
        }
    }

    public class ProxyMapping {

        private String igcAssetType;
        private ArrayList<String> igcRelationshipProperties;
        private String omrsRelationshipProperty;
        private String igcRidPrefix;

        public ProxyMapping(String igcAssetType,
                            String igcRelationshipProperty,
                            String omrsRelationshipProperty,
                            String igcRidPrefix) {

            this.igcAssetType = igcAssetType;
            this.igcRelationshipProperties = new ArrayList<>();
            this.igcRelationshipProperties.add(igcRelationshipProperty);
            this.omrsRelationshipProperty = omrsRelationshipProperty;
            this.igcRidPrefix = igcRidPrefix;

        }

        public String getIgcAssetType() { return this.igcAssetType; }
        public List<String> getIgcRelationshipProperties() { return this.igcRelationshipProperties; }
        public void addAlternativeIgcRelationshipProperty(String igcRelationshipProperty) { this.igcRelationshipProperties.add(igcRelationshipProperty); }
        public String getOmrsRelationshipProperty() { return this.omrsRelationshipProperty; }
        public String getIgcRidPrefix() { return this.igcRidPrefix; }

        public boolean isSelfReferencing() { return this.igcRelationshipProperties.contains(SELF_REFERENCE_SENTINEL); }

        public boolean matchesAssetType(String igcAssetType) {
            return this.igcAssetType.equals(igcAssetType) || this.igcAssetType.equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE);
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
        String endOneType = Reference.getAssetTypeForSearch(endOne);
        String endTwoType = Reference.getAssetTypeForSearch(endTwo);

        log.debug("Calculating relationship GUID from {} to {} via {} for {}", endOneType, endTwoType, igcPropertyName, omrsRelationshipName);

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
        } else {
            // Otherwise indicate something appears to be wrong
            log.error("Unable to find matching ends for relationship {} from {} to {} via {}", omrsRelationshipName, endOne.getId(), endTwo.getId(), igcPropertyName);
        }

        String proxyOnePrefix = pmOne.getIgcRidPrefix();
        String proxyTwoPrefix = pmTwo.getIgcRidPrefix();

        // TODO: if for relationship-specific RIDs we only output the relationship RID, need to be sure properly handled
        //  when reverse-looked up
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

    public static String getProxyOneGUIDFromRelationshipGUID(String relationshipGUID) {
        return getRelationshipGUIDToken(relationshipGUID, 0);
    }

    public static String getProxyTwoGUIDFromRelationshipGUID(String relationshipGUID) {
        return getRelationshipGUIDToken(relationshipGUID, 2);
    }

    public static String getRelationshipTypeFromRelationshipGUID(String relationshipGUID) {
        return getRelationshipGUIDToken(relationshipGUID, 1);
    }

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
                    entityProxy.setCreatedBy((String)igcObj.getPropertyByName(Reference.MOD_CREATED_BY));
                    entityProxy.setCreateTime((Date)igcObj.getPropertyByName(Reference.MOD_CREATED_ON));
                    entityProxy.setUpdatedBy((String)igcObj.getPropertyByName(Reference.MOD_MODIFIED_BY));
                    entityProxy.setUpdateTime((Date)igcObj.getPropertyByName(Reference.MOD_MODIFIED_ON));
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

}
