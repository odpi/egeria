/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestConstants;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.classifications.ClassificationMapping;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.RelationshipMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class EntityMapping {

    private static final Logger log = LoggerFactory.getLogger(EntityMapping.class);

    private static final Pattern INVALID_NAMING_CHARS = Pattern.compile("[()/& ]");

    private String igcAssetType;
    private String igcAssetTypeDisplayName;
    private String omrsTypeDefName;
    private Class igcPOJO;
    private String igcRidPrefix;

    private ArrayList<String> otherIgcTypes;
    private ArrayList<Class> otherPOJOs;

    private PropertyMappingSet propertyMappings;
    private ArrayList<RelationshipMapping> relationshipMappers;
    private ArrayList<ClassificationMapping> classificationMappers;

    protected IGCOMRSRepositoryConnector igcomrsRepositoryConnector;
    protected IGCOMRSMetadataCollection igcomrsMetadataCollection;
    protected String userId;
    protected Reference igcEntity;
    protected Reference igcEntityAlternative;
    protected EntitySummary omrsSummary;
    protected EntityDetail omrsDetail;
    protected ArrayList<Classification> omrsClassifications;
    protected ArrayList<Relationship> omrsRelationships;

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
        this.classificationMappers = new ArrayList<>();

        this.omrsRelationships = new ArrayList<>();

        this.otherIgcTypes = new ArrayList<>();
        this.otherPOJOs = new ArrayList<>();

        StringBuilder sbPojoName = new StringBuilder();
        if (igcAssetType.equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE)) {
            sbPojoName.append(IGCRestConstants.IGC_REST_COMMON_MODEL_PKG);
            sbPojoName.append(".MainObject");
        } else {
            sbPojoName.append(IGCRestConstants.IGC_REST_GENERATED_MODEL_PKG);
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
    public Class getIgcPOJO() { return this.igcPOJO; }
    public boolean igcRidNeedsPrefix() { return (this.igcRidPrefix != null); }
    public String getIgcRidPrefix() { return this.igcRidPrefix; }
    public String getOmrsTypeDefName() { return this.omrsTypeDefName; }

    /**
     * Indicates whether this entity mapping matches the provided IGC asset type: that is, this mapping
     * can be used to translate to the provided IGC asset type.
     *
     * @param igcAssetType the IGC asset type to check the mapping against
     * @return boolean
     */
    public boolean matchesAssetType(String igcAssetType) {
        String matchType = Reference.getAssetTypeForSearch(igcAssetType);
        log.debug("checking for matching asset between {} and {}", this.igcAssetType, matchType);
        return (
                this.igcAssetType.equals(matchType)
                        || this.igcAssetType.equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE)
        );
    }

    /**
     * Add any other IGC asset type needed for this mapping.
     *
     * @param igcAssetTypeName name of additional IGC asset
     */
    public void addOtherIGCAssetType(String igcAssetTypeName) {
        this.otherIgcTypes.add(igcAssetTypeName);
        StringBuilder sbPojoName = new StringBuilder();
        sbPojoName.append(IGCRestConstants.IGC_REST_GENERATED_MODEL_PKG);
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
     * Retrieve the base IGC asset expected for the mapper from one of its alternative assets. By default, and in the
     * vast majority of cases, there are no alternatives so will simply return the asset as-is. Override this method
     * in any mappers where alternative assets are defined.
     *
     * @param otherAsset the alternative asset to translate into a base asset
     * @return Reference - the base asset
     */
    public Reference getBaseIgcAssetFromAlternative(Reference otherAsset) {
        return otherAsset;
    }

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
     * Add the provided classification mapping as one that describes classifications for this entity.
     *
     * @param classificationMapping
     */
    public final void addClassificationMapper(ClassificationMapping classificationMapping) {
        classificationMappers.add(classificationMapping);
    }

    /**
     * Retrieve the classification mappings for this entity.
     *
     * @return List<ClassificationMapping>
     */
    public List<ClassificationMapping> getClassificationMappers() { return this.classificationMappers; }

    /**
     * This method needs to be implemented to define any complex property mapping logic, if any.
     *
     * @param instanceProperties the instance properties to which to add the complex-mapped properties
     */
    protected abstract void complexPropertyMappings(InstanceProperties instanceProperties);

    /**
     * Utility function to initialize an EntitySummary object based on the initialized IGC entity.
     */
    protected final void mapIGCToOMRSEntitySummary() {
        if (omrsSummary == null) {
            omrsSummary = new EntitySummary();
            String guid = igcEntity.getId();
            if (igcRidPrefix != null) {
                guid = igcRidPrefix + guid;
            }
            omrsSummary.setGUID(guid);
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
                        igcomrsRepositoryConnector.getRepositoryName(),
                        igcomrsRepositoryConnector.getMetadataCollectionId(),
                        InstanceProvenanceType.LOCAL_COHORT,
                        userId,
                        omrsTypeDefName
                );
                omrsDetail.setStatus(InstanceStatus.ACTIVE);
                String guid = igcEntity.getId();
                if (igcRidPrefix != null) {
                    guid = igcRidPrefix + guid;
                }
                omrsDetail.setGUID(guid);
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
