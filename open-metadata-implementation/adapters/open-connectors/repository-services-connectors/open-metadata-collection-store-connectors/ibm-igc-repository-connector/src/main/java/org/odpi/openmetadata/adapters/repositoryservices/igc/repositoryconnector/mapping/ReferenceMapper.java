/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import org.apache.commons.collections.map.ReferenceMap;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Handles mapping the most basic of IGC objects' attributes to OMRS entity attributes.
 */
public abstract class ReferenceMapper {

    private static final Logger log = LoggerFactory.getLogger(ReferenceMapper.class);

    public static final String SOURCE_NAME = "IBM InfoSphere Information Governance Catalog";
    public static final String IGC_REST_COMMON_MODEL_PKG = "org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common";
    public static final String IGC_REST_GENERATED_MODEL_PKG = "org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated";

    protected IGCOMRSRepositoryConnector igcomrsRepositoryConnector;
    protected Reference me;

    private String igcType;
    protected String igcPOJO;
    private String omrsType;
    protected String userId;
    private String igcRidPrefix;

    private ArrayList<String> otherPOJOs;

    protected EntitySummary summary;
    protected EntityDetail detail;
    protected ArrayList<Relationship> relationships;
    protected ArrayList<Classification> classifications;
    protected ArrayList<String> alreadyMappedProperties;

    private PropertyMappingSet propertyMappings;
    private RelationshipMappingSet relationshipMappings;
    private HashSet<String> classificationPropertiesIgc;
    private HashSet<String> classificationTypesOmrs;

    public ReferenceMapper(Reference me,
                           String igcAssetTypeName,
                           String omrsEntityTypeName,
                           IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                           String userId) {

        this.me = me;
        this.igcType = igcAssetTypeName;
        this.omrsType = omrsEntityTypeName;
        this.igcomrsRepositoryConnector = igcomrsRepositoryConnector;
        this.userId = userId;
        this.relationships = new ArrayList<>();
        this.alreadyMappedProperties = new ArrayList<>();

        propertyMappings = new PropertyMappingSet();
        relationshipMappings = new RelationshipMappingSet();
        classificationPropertiesIgc = new HashSet<>();
        classificationTypesOmrs = new HashSet<>();

        this.otherPOJOs = new ArrayList<>();

        // fully-qualified POJO class path
        if (igcAssetTypeName.equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE)) {
            // TODO: confirm whether this is fine (only (de-)serialisation), or we need to swap to Reference class?
            this.igcPOJO = IGC_REST_COMMON_MODEL_PKG + ".MainObject";
        } else {
            this.igcPOJO = IGC_REST_GENERATED_MODEL_PKG + "."
                    + igcomrsRepositoryConnector.getIGCVersion() + "."
                    + ReferenceMapper.getCamelCase(igcAssetTypeName);
        }

    }

    /**
     * Retrieve the set of property mappings that are defined by this Mapper.
     *
     * @return PropertyMappingSet
     */
    public PropertyMappingSet getPropertyMappings() { return this.propertyMappings; }

    /**
     * Retrieve the set of relationship mappings that are defined by this Mapper.
     *
     * @return RelationshipMappingSet
     */
    public RelationshipMappingSet getRelationshipMappings() { return this.relationshipMappings; }

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
     * Adds a relationship mapping between OMRS assets based on the provided IGC property name (mapped to the side of
     * the OMRS relationship represented by the target OMRS property) and the IGC asset used to initialise this Mapper
     * class (mapped to the side of the OMRS relationship represented by the source OMRS property).
     *
     * @param igcRelationshipName the IGC property name for the relationship (in REST form)
     * @param omrsRelationshipType the name of the OMRS relationship entity (RelationshipDef)
     * @param omrsRelationshipSourceProperty the name of the OMRS relationship property that should be aligned to the
     *                                       source of the relationship in IGC (ie. the IGC object this Mapper initialised
     *                                       with)
     * @param omrsRelationshipTargetProperty the name of the OMRS relationship property that should be aligned to the
     *                                       target of the relationship in IGC (ie. the value(s) retrieved by accessing
     *                                       the IGC relationship property)
     */
    public final void addSimpleRelationshipMapping(String igcRelationshipName,
                                                   String omrsRelationshipType,
                                                   String omrsRelationshipSourceProperty,
                                                   String omrsRelationshipTargetProperty) {
        addSimpleRelationshipMapping(
                igcRelationshipName,
                omrsRelationshipType,
                omrsRelationshipSourceProperty,
                omrsRelationshipTargetProperty,
                null,
                null
        );
    }

    /**
     * Adds a relationship mapping between OMRS assets based on the provided IGC property name (mapped to the side of
     * the OMRS relationship represented by the target OMRS property) and the IGC asset used to initialise this Mapper
     * class (mapped to the side of the OMRS relationship represented by the source OMRS property).
     *
     * @param igcRelationshipName the IGC property name for the relationship (in REST form)
     * @param omrsRelationshipType the name of the OMRS relationship entity (RelationshipDef)
     * @param omrsRelationshipSourceProperty the name of the OMRS relationship property that should be aligned to the
     *                                       source of the relationship in IGC (ie. the IGC object this Mapper initialised
     *                                       with)
     * @param omrsRelationshipTargetProperty the name of the OMRS relationship property that should be aligned to the
     *                                       target of the relationship in IGC (ie. the value(s) retrieved by accessing
     *                                       the IGC relationship property)
     * @param igcSourceRidPrefix the prefix that needs to be added to the IGC object this Mapper was initialised with
     *                           to create a unique (non-real IGC) entity
     * @param igcTargetRidPrefix the prefix that needs to be added to the IGC object(s) pointed to by the relationship
     *                           property, to create a unique (non-real IGC) entity for each
     */
    public final void addSimpleRelationshipMapping(String igcRelationshipName,
                                                   String omrsRelationshipType,
                                                   String omrsRelationshipSourceProperty,
                                                   String omrsRelationshipTargetProperty,
                                                   String igcSourceRidPrefix,
                                                   String igcTargetRidPrefix) {
        relationshipMappings.addSimpleMapping(
            igcRelationshipName,
                omrsRelationshipType,
                omrsRelationshipSourceProperty,
                omrsRelationshipTargetProperty,
                igcSourceRidPrefix,
                igcTargetRidPrefix
        );
    }

    /**
     * Note the provided IGC relationship as requiring more than a simple one-to-one mapping to an OMRS relationship.
     *
     * @param igcRelationshipName the IGC relationship property name
     */
    public final void addComplexIgcRelationship(String igcRelationshipName) {
        relationshipMappings.addComplexIgc(igcRelationshipName);
    }

    /**
     * Note the provided OMRS relationship type as requiring more than a simple one-to-one mapping to an IGC relationship.
     *
     * @param omrsRelationshipType the OMRS relationship type
     */
    public final void addComplexOmrsRelationship(String omrsRelationshipType) {
        relationshipMappings.addComplexOmrs(omrsRelationshipType);
    }

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

    protected EntitySummary getSummary() { return this.summary; }
    protected EntityDetail getDetail() { return this.detail; }
    protected List<Relationship> getRelationships() { return this.relationships; }
    protected List<Classification> getClassifications() { return this.classifications; }
    protected List<String> getAlreadyMappedProperties() { return this.alreadyMappedProperties; }
    protected void addAlreadyMappedProperty(String propertyName) { this.alreadyMappedProperties.add(propertyName); }
    protected void addAlreadyMappedProperties(Set<String> propertyNames) { this.alreadyMappedProperties.addAll(propertyNames); }

    /**
     * Add any other IGC asset type needed for this mapping.
     *
     * @param igcAssetTypeName name of additional IGC asset
     */
    public void addOtherIGCAssetType(String igcAssetTypeName) {
        this.otherPOJOs.add(IGC_REST_GENERATED_MODEL_PKG + "."
                + igcomrsRepositoryConnector.getIGCVersion() + "."
                + ReferenceMapper.getCamelCase(igcAssetTypeName));
    }

    /**
     * Retrieve listing of any additional IGC asset types needed by ths mapping.
     *
     * @return List<String>
     */
    public List<String> getOtherIGCAssetTypes() { return this.otherPOJOs; }

    /**
     * Retrieve the primary IGC asset for which this mapping works.
     *
     * @return String
     */
    public String getIgcAssetType() { return this.igcType; }

    /**
     * Retrieve the prefix that should be appended to the IGC RID for this mapping.
     * This is used to create unique GUIDs for entities that do not actually exist as distinct entities
     * in IGC, but are distinct entities in OMRS.
     *
     * @return String
     */
    public String getIgcRidPrefix() { return this.igcRidPrefix; }

    /**
     * Set the prefix to use for the IGC RID to create a unique OMRS guid.
     * This is used to create unique GUIDs for entities that do not actually exist as distinct entities
     * in IGC, but are distinct entities in OMRS.
     *
     * @param prefix the prefix to use in front of the RID to construct a unique GUID
     */
    public void setIgcRidPrefix(String prefix) { this.igcRidPrefix = prefix; }

    public abstract EntitySummary getOMRSEntitySummary();
    public abstract EntityDetail getOMRSEntityDetail();
    public abstract List<Relationship> getOMRSRelationships(String          relationshipTypeGUID,
                                                            int             fromRelationshipElement,
                                                            SequencingOrder sequencingOrder,
                                                            int             pageSize);

    /**
     * Sets up the private 'classifications' member with all classifications for this object;
     * this method should be overridden if there are any implementation-specific classifications that need
     * to be catered for.
     */
    protected abstract void getMappedClassifications();

    protected abstract void mapIGCToOMRSEntitySummary(Set<String> classificationProperties);

    protected final void mapIGCToOMRSEntitySummary() {
        if (summary == null) {
            summary = new EntitySummary();
            summary.setGUID(me.getId());
            summary.setInstanceURL(me.getUrl());
        }
    }

    protected abstract void mapIGCToOMRSEntityDetail(PropertyMappingSet mappings, Set<String> classificationProperties);

    protected final void mapIGCToOMRSEntityDetail() {
        if (detail == null) {
            try {
                detail = igcomrsRepositoryConnector.getRepositoryHelper().getSkeletonEntity(
                        SOURCE_NAME,
                        igcomrsRepositoryConnector.getMetadataCollectionId(),
                        InstanceProvenanceType.LOCAL_COHORT,
                        userId,
                        omrsType
                );
                detail.setStatus(InstanceStatus.ACTIVE);
                detail.setGUID(me.getId());
                detail.setInstanceURL(me.getUrl());
            } catch (TypeErrorException e) {
                log.error("Unable to get skeleton detail entity.", e);
            }
        }
    }

    protected void complexPropertyMappings(InstanceProperties instanceProperties, Method getPropertyByName) { }
    protected void complexRelationshipMappings() { }

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
     * Merge together the values of all the provided arrays.
     * (From: https://stackoverflow.com/questions/80476/how-can-i-concatenate-two-arrays-in-java)
     *
     * @param first first array to merge
     * @param rest subsequent arrays to merge
     * @return T[]
     */
    @SafeVarargs
    public static <T> T[] concatAll(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
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
            qualifiedName = ReferenceMapper.getPrimitivePropertyValue(identity);

            // 'qualifiedName' is the only unique InstanceProperty we need on an EntityProxy
            InstanceProperties uniqueProperties = new InstanceProperties();
            uniqueProperties.setProperty("qualifiedName", qualifiedName);

            try {
                entityProxy = igcomrsRepositoryConnector.getRepositoryHelper().getNewEntityProxy(
                        SOURCE_NAME,
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

    /**
     * Retrieves an EntityProxy object for the provided IGC object.
     *
     * @param igcomrsRepositoryConnector OMRS connector to the IBM IGC repository
     * @param igcObj the IGC object for which to retrieve an EntityProxy
     * @param omrsTypeName the OMRS entity type
     * @param userId the user through which to retrieve the EntityProxy (unused)
     * @return EntityProxy
     */
    public static EntityProxy getEntityProxyForObject(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                                      Reference igcObj,
                                                      String omrsTypeName,
                                                      String userId) {

        return getEntityProxyForObject(
                igcomrsRepositoryConnector,
                igcObj,
                omrsTypeName,
                userId,
                null);

    }

    /**
     * Converts an IGC type or property (something_like_this) into a camelcase class name (SomethingLikeThis).
     *
     * @param input
     * @return String
     */
    public static final String getCamelCase(String input) {
        StringBuilder sb = new StringBuilder(input.length());
        for (String token : input.split("_")) {
            sb.append(token.substring(0, 1).toUpperCase());
            sb.append(token.substring(1).toLowerCase());
        }
        return sb.toString();
    }

    /**
     * Introspect a mapping class to retrieve a Mapper of that type.
     * (This Mapper can be introspected for its mappings, but won't be functional as it won't have been
     *  initialised with an IGC object.)
     *
     * @param mappingClass the mapping class to retrieve an instance of
     * @param igcomrsRepositoryConnector connectivity to an IGC environment via an OMRS connector
     * @param userId the user through which to retrieve the mapping (currently unused)
     * @return ReferenceableMapper
     */
    public static final ReferenceableMapper getMapper(Class mappingClass,
                                                      IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                                      String userId) {
        return ReferenceMapper.getMapper(
                mappingClass,
                igcomrsRepositoryConnector,
                null,
                userId);
    }

    /**
     * Retrieve a mapping class based on the provided parameters.
     *
     * @param mappingClass the mapping class to retrieve an instance of
     * @param igcomrsRepositoryConnector connectivity to an IGC environment via an OMRS connector
     * @param igcObject the IGC asset to initialise the Mapper with
     * @param userId the user through which to retrieve the mapping (currently unused)
     * @return ReferenceableMapper
     */
    public static final ReferenceableMapper getMapper(Class mappingClass,
                                                      IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                                      Reference igcObject,
                                                      String userId) {
        ReferenceableMapper referenceableMapper = null;
        try {
            Constructor constructor = mappingClass.getConstructor(Reference.class, IGCOMRSRepositoryConnector.class, String.class);
            referenceableMapper = (ReferenceableMapper) constructor.newInstance(
                    igcObject,
                    igcomrsRepositoryConnector,
                    userId
            );
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error("Unable to find or instantiate Mapper class.", e);
        }
        return referenceableMapper;
    }

    /**
     * Retrieve the IGC asset type covered by the provided mapping.
     *
     * @param mappingClass the mapping class to retrieve the IGC asset type for
     * @param igcomrsRepositoryConnector connectivity to an IGC environment via an OMRS connector
     * @param userId the user through which to retrieve the mapping (currently unused)
     * @return String
     */
    public static final String getIgcTypeFromMapping(Class mappingClass, IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {
        ReferenceableMapper referenceableMapper = ReferenceMapper.getMapper(mappingClass, igcomrsRepositoryConnector, userId);
        return (referenceableMapper == null) ? null : referenceableMapper.getIgcAssetType();
    }

    /**
     * Retrieve the IGC RID prefix (if any) needed by the provided mapping.
     *
     * @param mappingClass the mapping class for which to retrieve the IGC RID prefix
     * @param igcomrsRepositoryConnector connectivity to an IGC environment via an OMRS connector
     * @param userId the user through which to retrieve the mapping (currently unused)
     * @return String
     */
    public static final String getIgcRidPrefixFromMapping(Class mappingClass, IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {
        ReferenceableMapper referenceableMapper = ReferenceMapper.getMapper(mappingClass, igcomrsRepositoryConnector, userId);
        return (referenceableMapper == null) ? null : referenceableMapper.getIgcRidPrefix();
    }

    /**
     * Retrieve any additional POJO objects that are required by the provided mapping.
     *
     * @param mappingClass the mapping class for which to retrieve any additional POJOs
     * @param igcomrsRepositoryConnector connectivity to an IGC environment via an OMRS connector
     * @param userId the user through which to retrieve the mapping (currently unused)
     * @return
     */
    public static final List<String> getAdditionalIgcPOJOs(Class mappingClass, IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {
        ReferenceableMapper referenceableMapper = ReferenceMapper.getMapper(mappingClass, igcomrsRepositoryConnector, userId);
        return (referenceableMapper == null) ? null : referenceableMapper.getOtherIGCAssetTypes();
    }

    /**
     * Retrieve the properties mapped by the provided class.
     *
     * @param mappingClass the mapping class that defines the mapped properties
     * @param igcomrsRepositoryConnector connectivity to an IGC environment via an OMRS connector
     * @param userId the user through which to retrieve the mapping (currently unused)
     * @return PropertyMappingSet
     */
    public static final PropertyMappingSet getPropertiesFromMapping(Class mappingClass, IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {
        ReferenceableMapper referenceableMapper = ReferenceMapper.getMapper(mappingClass, igcomrsRepositoryConnector, userId);
        return (referenceableMapper == null) ? null : referenceableMapper.getPropertyMappings();
    }

}
