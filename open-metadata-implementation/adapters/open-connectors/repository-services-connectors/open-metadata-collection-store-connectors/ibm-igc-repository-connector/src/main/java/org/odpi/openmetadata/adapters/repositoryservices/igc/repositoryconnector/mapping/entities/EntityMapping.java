/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.RelationshipMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class EntityMapping {

    private static final Logger log = LoggerFactory.getLogger(EntityMapping.class);

    private static final Pattern INVALID_NAMING_CHARS = Pattern.compile("[()/& ]");

    // TODO: confirm whether to keep a static source name like this or take from repository name?
    public static final String SOURCE_NAME = "IBM InfoSphere Information Governance Catalog";
    public static final String IGC_REST_COMMON_MODEL_PKG = "org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common";
    public static final String IGC_REST_GENERATED_MODEL_PKG = "org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated";
    public static final String MAPPING_PKG_ENTITIES = "org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities";

    protected static final String[] MODIFICATION_DETAILS = new String[] {
            Reference.MOD_CREATED_BY,
            Reference.MOD_CREATED_ON,
            Reference.MOD_MODIFIED_BY,
            Reference.MOD_MODIFIED_ON
    };

    private String igcAssetType;
    private String igcAssetTypeDisplayName;
    private String omrsTypeDefName;
    private TypeDef omrsTypeDef;
    private Class igcPOJO;
    private String igcRidPrefix;

    private ArrayList<String> otherIgcTypes;
    private ArrayList<Class> otherPOJOs;

    private PropertyMappingSet propertyMappings;
    private ArrayList<RelationshipMapping> relationshipMappers;
    private HashSet<String> classificationPropertiesIgc;
    private HashSet<String> classificationTypesOmrs;
    //private ArrayList<String> alreadyMappedIgcProperties;

    protected IGCOMRSRepositoryConnector igcomrsRepositoryConnector;
    protected IGCOMRSMetadataCollection igcomrsMetadataCollection;
    protected String userId;
    protected Reference igcEntity;
    protected EntitySummary omrsSummary;
    protected EntityDetail omrsDetail;
    protected ArrayList<Classification> omrsClassifications;
    protected ArrayList<Relationship> omrsRelationships;

    public EntityMapping(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                         String igcAssetType,
                         String igcAssetTypeDisplayName,
                         String omrsTypeDefName,
                         String userId) {

        this(
                igcomrsRepositoryConnector,
                igcAssetType,
                igcAssetTypeDisplayName,
                omrsTypeDefName,
                userId,
                null
        );

    }

    public EntityMapping(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                         String igcAssetType,
                         String igcAssetTypeDisplayName,
                         String omrsTypeDefName,
                         String userId,
                         String igcRidPrefix) {

        this.igcomrsRepositoryConnector = igcomrsRepositoryConnector;
        this.igcomrsMetadataCollection = (IGCOMRSMetadataCollection) igcomrsRepositoryConnector.getMetadataCollection();
        this.userId = userId;
        this.igcAssetType = igcAssetType;
        this.igcAssetTypeDisplayName = igcAssetTypeDisplayName;
        this.omrsTypeDefName = omrsTypeDefName;
        this.igcRidPrefix = igcRidPrefix;

        this.propertyMappings = new PropertyMappingSet();
        this.relationshipMappers = new ArrayList<>();
        this.classificationPropertiesIgc = new HashSet<>();
        this.classificationTypesOmrs = new HashSet<>();

        this.omrsRelationships = new ArrayList<>();

        this.otherIgcTypes = new ArrayList<>();
        this.otherPOJOs = new ArrayList<>();
        //this.alreadyMappedIgcProperties = new ArrayList<>();

        StringBuilder sbPojoName = new StringBuilder();
        if (igcAssetType.equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE)) {
            sbPojoName.append(IGC_REST_COMMON_MODEL_PKG);
            sbPojoName.append(".MainObject");
        } else {
            sbPojoName.append(IGC_REST_GENERATED_MODEL_PKG);
            sbPojoName.append(".");
            sbPojoName.append(igcomrsRepositoryConnector.getIGCVersion());
            sbPojoName.append(".");
            sbPojoName.append(getCamelCase(igcAssetType));
        }
        try {
            this.igcPOJO = Class.forName(sbPojoName.toString());
        } catch (ClassNotFoundException e) {
            log.error("Unable to find POJO class: {}", sbPojoName.toString(), e);
        }

    }

    public String getIgcAssetType() { return this.igcAssetType; }
    public String getIgcAssetTypeDisplayName() { return this.igcAssetTypeDisplayName; }

    public void setOmrsTypeDef(TypeDef typeDef) { this.omrsTypeDef = typeDef; }
    public TypeDef getOmrsTypeDef() {
        if (omrsTypeDef == null) {
            omrsTypeDef = igcomrsRepositoryConnector.getRepositoryHelper().getTypeDefByName(
                    igcomrsRepositoryConnector.getRepositoryName(),
                    omrsTypeDefName
            );
        }
        return omrsTypeDef;
    }

    public Class getIgcPOJO() { return this.igcPOJO; }
    public boolean igcRidNeedsPrefix() { return (this.igcRidPrefix != null); }
    public String getIgcRidPrefix() { return this.igcRidPrefix; }

    /**
     * Add any other IGC asset type needed for this mapping.
     *
     * @param igcAssetTypeName name of additional IGC asset
     */
    public void addOtherIGCAssetType(String igcAssetTypeName) {
        this.otherIgcTypes.add(igcAssetTypeName);
        StringBuilder sbPojoName = new StringBuilder();
        sbPojoName.append(IGC_REST_GENERATED_MODEL_PKG);
        sbPojoName.append(".");
        sbPojoName.append(igcomrsRepositoryConnector.getIGCVersion());
        sbPojoName.append(".");
        sbPojoName.append(getCamelCase(igcAssetTypeName));
        try {
            Class pojo = Class.forName(sbPojoName.toString());
            this.otherPOJOs.add(pojo);
        } catch (ClassNotFoundException e) {
            log.error("Unable to find POJO with name: {}", sbPojoName.toString());
        }
    }

    /**
     * Retrieve listing of any additional IGC asset types needed by this mapping.
     *
     * @return List<String>
     */
    public List<String> getOtherIGCAssetTypes() { return this.otherIgcTypes; }

    /**
     * Retrieve listing of any additional IGC POJOs needed by this mapping.
     *
     * @return List<Class>
     */
    public List<Class> getOtherIGCPOJOs() { return this.otherPOJOs; }

    /**
     * Add a simple one-to-one property mapping between an IGC property and an OMRS property.
     *
     * @param igcPropertyName the IGC property name to be mapped
     * @param omrsPropertyName the OMRS property name to be mapped
     */
    public final void addSimplePropertyMapping(String igcPropertyName, String omrsPropertyName) {
        propertyMappings.addSimpleMapping(igcPropertyName, omrsPropertyName);
    }

    /**
     * Note the provided IGC property name as requiring more than a simple one-to-one mapping to an OMRS property.
     *
     * @param igcPropertyName the IGC property name
     */
    public final void addComplexIgcProperty(String igcPropertyName) {
        propertyMappings.addComplexIgc(igcPropertyName);
    }

    /**
     * Note the provided OMRS property name as requiring more than a simple one-to-one mapping to an IGC property.
     *
     * @param omrsPropertyName the OMRS property name
     */
    public final void addComplexOmrsProperty(String omrsPropertyName) {
        propertyMappings.addComplexOmrs(omrsPropertyName);
    }

    /**
     * Retrieve the set of property mappings that are defined by this Mapper.
     *
     * @return PropertyMappingSet
     */
    public PropertyMappingSet getPropertyMappings() { return this.propertyMappings; }

    /**
     * Retrieve a listing of the IGC property names that have already been mapped.
     *
     * @return List<String>
     */
    //protected List<String> getAlreadyMappedIgcProperties() { return this.alreadyMappedIgcProperties; }

    /**
     * Add the provided IGC property name to the list of those that have already been mapped.
     *
     * @param propertyName the already-mapped IGC property name
     */
    //protected void addAlreadyMappedIgcProperty(String propertyName) { this.alreadyMappedIgcProperties.add(propertyName); }

    /**
     * Adds the provided set of IGC property names to the list of those that have already been mapped.
     *
     * @param propertyNames the already-mapped IGC property names
     */
    //protected void addAlreadyMappedIgcProperties(Set<String> propertyNames) { this.alreadyMappedIgcProperties.addAll(propertyNames); }

    /**
     * Note the provided IGC property name as one to be used in setting up classifications for the OMRS entity.
     *
     * @param igcPropertyName the IGC property name
     */
    public final void addComplexIgcClassification(String igcPropertyName) {
        classificationPropertiesIgc.add(igcPropertyName);
    }

    /**
     * Note the provided OMRS classification type as one to be mapped by the Mapper.
     *
     * @param omrsClassificationType the OMRS classification type
     */
    public final void addComplexOmrsClassification(String omrsClassificationType) {
        classificationTypesOmrs.add(omrsClassificationType);
    }

    /**
     * Retrieve the set of IGC properties needed to determine classifications.
     *
     * @return Set<String>
     */
    public Set<String> getIgcClassificationProperties() { return this.classificationPropertiesIgc; }

    /**
     * Retrieve the set of OMRS classification types that are covered by the Mapper.
     *
     * @return Set<String>
     */
    public Set<String> getOmrsClassificationTypes() { return this.classificationTypesOmrs; }

    /**
     * Add the provided relationship mapping as one that describes relationships for this entity.
     *
     * @param relationshipMapping
     */
    public final void addRelationshipMapper(RelationshipMapping relationshipMapping) {
        relationshipMappers.add(relationshipMapping);
    }

    /**
     * Retrieve the relationship mappings for this entity.
     *
     * @return List<RelationshipMapping>
     */
    public List<RelationshipMapping> getRelationshipMappers() { return this.relationshipMappers; }

    /**
     * This method needs to be implemented to provide implementation-specific classification logic, if any.
     */
    protected abstract void getMappedClassifications();

    /**
     * This method needs to be implemented to define any complex property mapping logic, if any.
     *
     * @param instanceProperties the instance properties to which to add the complex-mapped properties
     */
    protected abstract void complexPropertyMappings(InstanceProperties instanceProperties);

    /**
     * This method needs to be implemented to define any complex relationship mapping logic, if any.
     */
    protected abstract void complexRelationshipMappings();

    /**
     * These methods must be implemented to define how to construct the EntitySummary and EntityDetail objects from
     * the provided mapping information.
     */
    protected abstract void mapIGCToOMRSEntitySummary(Set<String> classificationProperties);
    protected abstract void mapIGCToOMRSEntityDetail(PropertyMappingSet mappings, Set<String> classificationProperties);

    /**
     * Utility function to initialize an EntitySummary object based on the initialized IGC entity.
     */
    protected final void mapIGCToOMRSEntitySummary() {
        if (omrsSummary == null) {
            omrsSummary = new EntitySummary();
            omrsSummary.setGUID(igcEntity.getId());
            omrsSummary.setInstanceURL(igcEntity.getUrl());
        }
    }

    /**
     * Utility function to initalize an EntityDetail object based on the initialized IGC entity.
     */
    protected final void mapIGCToOMRSEntityDetail() {
        if (omrsDetail == null) {
            try {
                omrsDetail = igcomrsRepositoryConnector.getRepositoryHelper().getSkeletonEntity(
                        SOURCE_NAME,
                        igcomrsRepositoryConnector.getMetadataCollectionId(),
                        InstanceProvenanceType.LOCAL_COHORT,
                        userId,
                        omrsTypeDefName
                );
                omrsDetail.setStatus(InstanceStatus.ACTIVE);
                omrsDetail.setGUID(igcEntity.getId());
                omrsDetail.setInstanceURL(igcEntity.getUrl());
            } catch (TypeErrorException e) {
                log.error("Unable to get skeleton detail entity.", e);
            }
        }
    }

    /**
     * Returns the OMRS PrimitivePropertyValue represented by the provided value.
     *
     * @param value the value to represent as an OMRS PrimitivePropertyValue
     * @return PrimitivePropertyValue
     */
    public static PrimitivePropertyValue getPrimitivePropertyValue(Object value) {
        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();
        PrimitiveDef primitiveDef = new PrimitiveDef();
        if (value instanceof Boolean) {
            primitiveDef.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BOOLEAN);
        } else if (value instanceof Date) {
            primitiveDef.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
        } else if (value instanceof Integer) {
            primitiveDef.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
        } else if (value instanceof Number) {
            primitiveDef.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_FLOAT);
        } else {
            primitiveDef.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        }
        propertyValue.setPrimitiveValue(value);
        propertyValue.setPrimitiveDefCategory(primitiveDef.getPrimitiveDefCategory());
        propertyValue.setTypeGUID(primitiveDef.getGUID());
        propertyValue.setTypeName(primitiveDef.getName());
        return propertyValue;
    }

    /**
     * Converts an IGC type or property (something_like_this) into a camelcase class name (SomethingLikeThis).
     *
     * @param input
     * @return String
     */
    public static final String getCamelCase(String input) {
        log.debug("Attempting to camelCase from {}", input);
        Matcher m = INVALID_NAMING_CHARS.matcher(input);
        String invalidsRemoved = m.replaceAll("_");
        StringBuilder sb = new StringBuilder(invalidsRemoved.length());
        for (String token : invalidsRemoved.split("_")) {
            if (token.length() > 0) {
                sb.append(token.substring(0, 1).toUpperCase());
                sb.append(token.substring(1).toLowerCase());
            }
        }
        log.debug(" ... succeeded to {}", sb.toString());
        return sb.toString();
    }

}
