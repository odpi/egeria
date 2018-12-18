/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchSorting;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Handles mapping the majority of IGC objects' attributes to OMRS entity attributes.
 * <br><br>
 * Also handles the most basic mapping between an IGC asset of any type to an OMRS 'Referenceable' object.
 */
public class ReferenceableMapper extends ReferenceMapper {

    private static final Logger log = LoggerFactory.getLogger(ReferenceableMapper.class);

    // By default (if no IGC type or OMRS type defined), map between 'main_object' (IGC) and Referenceable (OMRS)
    public ReferenceableMapper(Reference igcObject,
                               IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                               String userId) {
        this(
                igcObject,
                IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE,
                "Referenceable",
                igcomrsRepositoryConnector,
                userId
        );
    }

    public ReferenceableMapper(Reference igcObject,
                               String igcAssetTypeName,
                               String omrsEntityTypeName,
                               IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                               String userId) {

        this(
                igcObject,
                igcAssetTypeName,
                omrsEntityTypeName,
                igcomrsRepositoryConnector,
                userId,
                true
        );

    }

    public ReferenceableMapper(Reference igcObject,
                               String igcAssetTypeName,
                               String omrsEntityTypeName,
                               IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                               String userId,
                               boolean includeDefaultRelationships) {

        super(
                igcObject,
                igcAssetTypeName,
                omrsEntityTypeName,
                igcomrsRepositoryConnector,
                userId
        );

        // common set of properties used by all IGC objects (and all OMRS Referenceables)
        if (igcObject != null && igcObject.hasModificationDetails()) {
            for (String property : ReferenceMapper.MODIFICATION_DETAILS) {
                addComplexIgcProperty(property);
            }
        }

        if (includeDefaultRelationships) {
            // common set of relationships that could apply to all IGC objects (and all OMRS Referenceables)
            addSimpleRelationshipMapping(
                    "assigned_to_terms",
                    "SemanticAssignment",
                    "assignedElements",
                    "meaning"
            );
            addSimpleRelationshipMapping(
                    "labels",
                    "AttachedTag",
                    "taggedElement",
                    "tags"
            );
        }

        // common set of classifications that apply to all IGC objects (and all OMRS Referenceables) [none]

    }

    protected void getMappedClassifications() {
        // Nothing to do -- no generic classifications
    }

    /**
     * Map the IGC entity to an OMRS EntitySummary object.
     *
     * @return EntitySummary
     */
    public EntitySummary getOMRSEntitySummary() {
        mapIGCToOMRSEntitySummary(getIgcClassificationProperties());
        return getSummary();
    }

    /**
     * Map the IGC entity to an OMRS EntityDetail object.
     *
     * @return EntityDetail
     */
    public EntityDetail getOMRSEntityDetail() {
        mapIGCToOMRSEntityDetail(getPropertyMappings(), getIgcClassificationProperties());
        return getDetail();
    }

    /**
     * Map the IGC entity's relationships to OMRS Relationship objects.
     *
     * @param relationshipTypeGUID String GUID of the the type of relationship required (null for all).
     * @param fromRelationshipElement the starting element number of the relationships to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result classifications that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return List<Relationship>
     */
    public List<Relationship> getOMRSRelationships(String          relationshipTypeGUID,
                                                   int             fromRelationshipElement,
                                                   SequencingOrder sequencingOrder,
                                                   int             pageSize) {
        getMappedRelationships(
                getRelationshipMappings(),
                relationshipTypeGUID,
                fromRelationshipElement,
                sequencingOrder,
                pageSize);
        return getRelationships();
    }

    /**
     * Map the IGC entity's classifications to OMRS Classification objects.
     * (This is called automatically as part of getOMRSEntitySummary and getOMRSEntityDetail)
     *
     * @return List<Classification>
     */
    protected List<Classification> getOMRSClassifications() {
        getMappedClassifications();
        return getClassifications();
    }

    /**
     * Maps the (IGC) object this class was initialised with to an OMRS EntitySummary object, using the provided
     * mappings.
     * @param classificationProperties any IGC properties needed to setup classifications in the OMRS EntityDetail object
     */
    @Override
    protected void mapIGCToOMRSEntitySummary(Set<String> classificationProperties) {

        // Merge together all the properties we want to map
        String[] allProps = ReferenceMapper.concatAll(
                classificationProperties.toArray(new String[0]),
                getPropertyMappings().getAllMappedIgcProperties().toArray(new String[0])
        );

        // Retrieve the full details we'll require for summary BEFORE handing off to superclass
        me = me.getAssetWithSubsetOfProperties(igcomrsRepositoryConnector.getIGCRestClient(), allProps);

        // Handle any super-generic mappings first
        mapIGCToOMRSEntitySummary();

        // Then handle generic mappings and classifications
        setupEntityObj(summary);

    }

    /**
     * Maps the (IGC) object this class was initialised with to an OMRS EntityDetail object, using the provided
     * mappings.
     *
     * @param propertyMap the property mappings to use in creating the OMRS EntityDetail object
     * @param classificationProperties any IGC properties needed to setup classifications in the OMRS EntityDetail object
     */
    @Override
    protected void mapIGCToOMRSEntityDetail(PropertyMappingSet propertyMap, Set<String> classificationProperties) {

        // Merge the detailed properties together (generic and more specific POJO mappings that were passed in)
        String[] allProps = ReferenceMapper.concatAll(
                propertyMap.getAllMappedIgcProperties().toArray(new String[0]),
                classificationProperties.toArray(new String[0])
        );

        // Retrieve only this set of properties for the object (no more, no less)
        me = me.getAssetWithSubsetOfProperties(igcomrsRepositoryConnector.getIGCRestClient(), allProps);

        // Handle any super-generic mappings first
        mapIGCToOMRSEntityDetail();

        // Then handle any generic mappings and classifications
        setupEntityObj(detail);

        // Use reflection to apply POJO-specific mappings
        InstanceProperties instanceProperties = getMappedInstanceProperties(propertyMap);
        detail.setProperties(instanceProperties);

    }

    /**
     * Simple utility function to avoid implementing shared EntitySummary and EntityDetail setup twice.
     *
     * @param omrsObj OMRS object to map into (EntityDetail or EntitySummary)
     */
    private void setupEntityObj(EntitySummary omrsObj) {

        if (me.hasModificationDetails()) {
            omrsObj.setCreatedBy((String)me.getPropertyByName(Reference.MOD_CREATED_BY));
            omrsObj.setCreateTime((Date)me.getPropertyByName(Reference.MOD_CREATED_ON));
            omrsObj.setUpdatedBy((String)me.getPropertyByName(Reference.MOD_MODIFIED_BY));
            omrsObj.setUpdateTime((Date)me.getPropertyByName(Reference.MOD_MODIFIED_ON));
            if (omrsObj.getUpdateTime() != null) {
                omrsObj.setVersion(omrsObj.getUpdateTime().getTime());
            }
        }

        // Avoid doing this multiple times: if one has retrieved classifications it'll
        // be the same classifications for the other
        if (classifications == null) {
            classifications = new ArrayList<>();
            getMappedClassifications();
        }

        omrsObj.setClassifications(classifications);

    }

    /**
     * Retrieves the InstanceProperties based on the mappings defined in the provided PropertyMappingSet.
     *
     * @param mappings the mappings to use for retrieving a set of InstanceProperties
     * @return InstanceProperties
     */
    private InstanceProperties getMappedInstanceProperties(PropertyMappingSet mappings) {

        InstanceProperties instanceProperties = new InstanceProperties();
        ClassLoader classLoader = this.getClass().getClassLoader();

        try {

            Class clazz = classLoader.loadClass(igcPOJO);
            Method getPropertyByName = clazz.getMethod("getPropertyByName", String.class);

            // We'll always start by using the Identity string as the qualified name
            Method getIdentity = clazz.getMethod("getIdentity", IGCRestClient.class);
            String qualifiedName = getIdentity.invoke(me, igcomrsRepositoryConnector.getIGCRestClient()).toString();
            instanceProperties.setProperty("qualifiedName", getPrimitivePropertyValue(qualifiedName));

            // Then we'll iterate through the provided mappings to set an OMRS instance property for each one
            for (String igcPropertyName : mappings.getSimpleMappedIgcProperties()) {
                String omrsAttribute = mappings.getOmrsPropertyName(igcPropertyName);
                instanceProperties.setProperty(
                        omrsAttribute,
                        getPrimitivePropertyValue(getPropertyByName.invoke(me, igcPropertyName))
                );
            }

            complexPropertyMappings(instanceProperties, getPropertyByName);

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Unable to retrieve property by name via reflection.", e);
        }

        return instanceProperties;

    }

    /**
     * Adds any relationships defined through the provided RelationshipMappingSet to the the private 'relationships'
     * member, for use in retrieving via getOMRSRelationships. First looks up any *Mapper-specific mappings to apply,
     * and then iterates through any common relationships we could expect across any IGC object. (This allows *Mapper-
     * specific mappings to effectively "override" any common mappings, in cases where the same relationship in IGC
     * may represent multiple meanings depending on the asset type the relationship is used on.)
     *
     * @param mappings the mappings to use for retrieving the relationships
     * @param relationshipTypeGUID String GUID of the the type of relationship required (null for all).
     * @param fromRelationshipElement the starting element number of the relationships to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result classifications that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     */
    private void getMappedRelationships(RelationshipMappingSet mappings,
                                        String                 relationshipTypeGUID,
                                        int                    fromRelationshipElement,
                                        SequencingOrder        sequencingOrder,
                                        int                    pageSize) {

        // Start off by marking that any properties used for classifications should not be used again
        // for relationships
        addAlreadyMappedProperties(getIgcClassificationProperties());

        // Merge together all the properties we want to map
        String[] allProps = ReferenceMapper.concatAll(
                mappings.getAllMappedIgcRelationships().toArray(new String[0]),
                ReferenceMapper.MODIFICATION_DETAILS
        );

        // TODO: Filter down to only the relationship specified (ie. relationshipTypeGUID != null)

        IGCSearchSorting sort = IGCSearchSorting.sortFromNonPropertySequencingOrder(sequencingOrder);

        // TODO: handle multi-page results with different starting points (ie. fromRelationshipElement != 0)

        // Retrieve the full details we'll require for the relationships
        me = me.getAssetWithSubsetOfProperties(igcomrsRepositoryConnector.getIGCRestClient(), allProps, pageSize, sort);

        getMappedRelationships(mappings);

        complexRelationshipMappings();

    }

    /**
     * Utility function that actually does the Relationship object setup and addition to 'relationships' member.
     *
     * @param mappings the mappings to use for retrieving the relationships
     */
    private void getMappedRelationships(RelationshipMappingSet mappings) {

        ClassLoader classLoader = this.getClass().getClassLoader();
        IGCRestClient igcRestClient = igcomrsRepositoryConnector.getIGCRestClient();
        List<String> alreadyUsedProperties = getAlreadyMappedProperties();

        try {

            Class clazz = classLoader.loadClass(igcPOJO);
            Method getPropertyByName = clazz.getMethod("getPropertyByName", String.class);

            // Iterate through the provided mappings to create a number of OMRS relationships
            for (int i = 0; i < mappings.numberOfSimpleMappings(); i++) {

                RelationshipMappingSet.RelationshipMapping mapping = mappings.getSimpleMapping(i);

                String igcRelationshipName = mapping.getIgcRelationshipName();
                log.debug(" ... attempting to map relationship: {}", igcRelationshipName);

                // If the relationship is self-referencing, then there won't be any related object to retrieve
                if (igcRelationshipName.equals(RelationshipMappingSet.SELF_REFERENCE_SENTINEL)) {
                    log.debug(" ... self-referential: {}", igcRelationshipName);
                    // (as it's self-referencing, send through itself as the relationship
                    Relationship omrsRelationship = getMappedRelationship(
                            mapping,
                            igcRelationshipName,
                            me
                    );
                    relationships.add(omrsRelationship);
                } else if (!alreadyUsedProperties.contains(igcRelationshipName)) {

                    // Otherwise, only continue if we haven't already handled that relationship
                    Object igcRelationshipObj = getPropertyByName.invoke(me, igcRelationshipName);

                    // Handle single instance relationship one way
                    if (igcRelationshipObj != null && Reference.isReference(igcRelationshipObj)) {

                        log.debug(" ... single reference: {}", igcRelationshipName);
                        Reference igcRelationship = (Reference) igcRelationshipObj;
                        if (igcRelationship != null
                                && igcRelationship.getType() != null
                                && !igcRelationship.getType().equals("null")) {
                            Relationship omrsRelationship = getMappedRelationship(
                                    mapping,
                                    igcRelationshipName,
                                    igcRelationship
                            );
                            relationships.add(omrsRelationship);
                        }
                        addAlreadyMappedProperty(igcRelationshipName);

                    } else if (igcRelationshipObj != null && Reference.isReferenceList(igcRelationshipObj)) { // and list of relationships another

                        log.debug(" ... list of references: {}", igcRelationshipName);
                        ReferenceList igcRelationships = (ReferenceList) getPropertyByName.invoke(me, igcRelationshipName);

                        // TODO: paginate rather than always retrieving the full set
                        igcRelationships.getAllPages(igcRestClient);

                        // Iterate through all of the existing IGC relationships of that type to create an OMRS relationship
                        // for each one
                        for (Reference relation : igcRelationships.getItems()) {

                            Relationship omrsRelationship = getMappedRelationship(
                                    mapping,
                                    igcRelationshipName,
                                    relation
                            );
                            relationships.add(omrsRelationship);

                        }

                        addAlreadyMappedProperty(igcRelationshipName);

                    } else {
                        log.debug(" ... skipping relationship {}, either empty or neither reference or list {}", igcRelationshipName, igcRelationshipObj);
                    }

                } else {
                    log.debug(" ... skipping relationship {}, already used.", igcRelationshipName);
                }

            }

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | RepositoryErrorException e) {
            log.error("Unable to setup new relationship.", e);
        }

    }

    /**
     * Retrieve a Relationship instance based on the provided mapping information, automatically prefixing
     * where needed.
     *
     * @param mapping the mapping details to use
     * @param igcRelationshipName the name of the relationship property in IGC
     * @param relation the related IGC object
     * @return Relationship
     * @throws RepositoryErrorException
     */
    protected Relationship getMappedRelationship(RelationshipMappingSet.RelationshipMapping mapping,
                                                 String igcRelationshipName,
                                                 Reference relation) throws RepositoryErrorException {

        String omrsRelationshipName = mapping.getOmrsRelationshipType();
        String omrsSourceProperty = mapping.getOmrsRelationshipSourceProperty();
        String omrsTargetProperty = mapping.getOmrsRelationshipTargetProperty();

        RelationshipDef omrsRelationshipDef = (RelationshipDef) igcomrsRepositoryConnector.getRepositoryHelper().getTypeDefByName(
                SOURCE_NAME,
                omrsRelationshipName
        );

        return getMappedRelationship(igcRelationshipName,
                omrsRelationshipDef,
                omrsSourceProperty,
                omrsTargetProperty,
                relation,
                mapping.needsIgcSourceRidPrefix(),
                mapping.needsIgcTargetRidPrefix(),
                mapping.getIgcSourceRidPrefix(),
                mapping.getIgcTargetRidPrefix());

    }

    /**
     * Retrieve a Relationship instance based on the provided mapping information.
     * (No prefixing will be done when using this method.)
     *
     * @param igcRelationshipName the name of the relationship property in IGC
     * @param omrsRelationshipDef the OMRS relationship definition
     * @param omrsSourceProperty the name of the property in OMRS that maps to the source object in IGC
     * @param omrsTargetProperty the name of the property in OMRS that maps to the target object in IGC
     * @param relation the related IGC object
     * @return Relationship
     * @throws RepositoryErrorException
     */
    protected Relationship getMappedRelationship(String igcRelationshipName,
                                                 RelationshipDef omrsRelationshipDef,
                                                 String omrsSourceProperty,
                                                 String omrsTargetProperty,
                                                 Reference relation) throws RepositoryErrorException {
        return getMappedRelationship(
                igcRelationshipName,
                omrsRelationshipDef,
                omrsSourceProperty,
                omrsTargetProperty,
                relation,
                false,
                false,
                null,
                null);
    }

    /**
     * Retrieve a Relationship instance based on the provided mapping information.
     * (This method allows prefixing, where needed, to "split" an IGC entity into multiple entities.)
     *
     * @param igcRelationshipName the name of the relationship property in IGC
     * @param omrsRelationshipDef the OMRS relationship definition
     * @param omrsSourceProperty the name of the property in OMRS that maps to the source object in IGC
     * @param omrsTargetProperty the name of the property in OMRS that maps to the target object in IGC
     * @param relation the related IGC object
     * @param needsSourceRidPrefix indicates whether the source side of the relationship needs to be prefixed to make
     *                             it unique (true) or not (false)
     * @param needsTargetRidPrefix indicates whether the target side of the relationship needs to be prefixed to make
     *                             it unique (true) or not (false)
     * @param sourceRidPrefix provides the prefix to use when needsSourceRidPrefix is true
     * @param targetRidPrefix provides the prefix to use when needsTargetRidPrefix is true
     * @return Relationship
     * @throws RepositoryErrorException
     */
    protected Relationship getMappedRelationship(String igcRelationshipName,
                                                 RelationshipDef omrsRelationshipDef,
                                                 String omrsSourceProperty,
                                                 String omrsTargetProperty,
                                                 Reference relation,
                                                 boolean needsSourceRidPrefix,
                                                 boolean needsTargetRidPrefix,
                                                 String sourceRidPrefix,
                                                 String targetRidPrefix) throws RepositoryErrorException {

        final String methodName = "getMappedRelationship";
        final String repositoryName = igcomrsRepositoryConnector.getRepositoryName();

        String omrsRelationshipName = omrsRelationshipDef.getName();

        Relationship omrsRelationship = new Relationship();

        try {
            InstanceType instanceType = igcomrsRepositoryConnector.getRepositoryHelper().getNewInstanceType(
                    SOURCE_NAME,
                    omrsRelationshipDef
            );
            omrsRelationship.setType(instanceType);
        } catch (TypeErrorException e) {
            log.error("Unable to set instance type.", e);
        }

        if (igcRelationshipName.equals(RelationshipMappingSet.SELF_REFERENCE_SENTINEL)) {
            if (needsSourceRidPrefix) {
                // Set the GUID of the relationship to <prefix><source_entity_RID>_<reln_name>_<source_entity_RID>
                omrsRelationship.setGUID(
                        sourceRidPrefix + me.getId() + "_" + omrsRelationshipName + "_" + relation.getId()
                );
            } else if (needsTargetRidPrefix) {
                // Set the GUID of the relationship to <source_entity_RID>_<reln_name>_<prefix><source_entity_RID>
                omrsRelationship.setGUID(
                         me.getId() + "_" + omrsRelationshipName + "_" + targetRidPrefix + relation.getId()
                );
            }
        } else if (relation != me) {
            // Set the GUID of the relationship to <source_entity_RID>_<property_name>_<target_entity_RID>
            omrsRelationship.setGUID(me.getId() + "_" + igcRelationshipName + "_" + relation.getId());
        } else {
            log.error("Relation was same object, but not marked self-referencing: {}", igcRelationshipName);
        }

        omrsRelationship.setStatus(InstanceStatus.ACTIVE);

        EntityProxy ep1 = null;
        EntityProxy ep2 = null;
        String omrsEndOneProperty = omrsRelationshipDef.getEndDef1().getAttributeName();

        // If end one property matches the OMRS property linked to IGC source, use this object
        if (omrsSourceProperty.equals(omrsEndOneProperty)) {
            ep1 = ReferenceMapper.getEntityProxyForObject(
                    igcomrsRepositoryConnector,
                    me,
                    omrsRelationshipDef.getEndDef1().getEntityType().getName(),
                    userId,
                    needsSourceRidPrefix ? sourceRidPrefix : null
            );
            ep2 = ReferenceMapper.getEntityProxyForObject(
                    igcomrsRepositoryConnector,
                    relation,
                    omrsRelationshipDef.getEndDef2().getEntityType().getName(),
                    userId,
                    needsTargetRidPrefix ? targetRidPrefix : null
            );
            if (ep1 != null && ep1.getUpdateTime() != null) {
                // ... and in this case, set the version to the epoch time of the ep1 (source) proxy
                omrsRelationship.setVersion(ep1.getUpdateTime().getTime());
            }
        } else if (omrsTargetProperty.equals(omrsEndOneProperty)) {
            // If end one property matches the OMRS property linked to IGC target, use the relation object
            ep1 = ReferenceMapper.getEntityProxyForObject(
                    igcomrsRepositoryConnector,
                    relation,
                    omrsRelationshipDef.getEndDef1().getEntityType().getName(),
                    userId,
                    needsTargetRidPrefix ? targetRidPrefix : null
            );
            ep2 = ReferenceMapper.getEntityProxyForObject(
                    igcomrsRepositoryConnector,
                    me,
                    omrsRelationshipDef.getEndDef2().getEntityType().getName(),
                    userId,
                    needsSourceRidPrefix ? sourceRidPrefix : null
            );
            if (ep2 != null && ep2.getUpdateTime() != null) {
                // ... and in this case, set the version to the epoch time of the ep2 (source) proxy
                omrsRelationship.setVersion(ep2.getUpdateTime().getTime());
            }
        } else {
            String omrsEndTwoProperty = omrsRelationshipDef.getEndDef2().getAttributeName();
            OMRSErrorCode errorCode = OMRSErrorCode.INVALID_RELATIONSHIP_ENDS;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    repositoryName,
                    omrsRelationshipName,
                    omrsEndOneProperty + ":" + omrsSourceProperty,
                    omrsEndTwoProperty + ":" + omrsTargetProperty);
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        if (ep1 != null && ep2 != null) {
            omrsRelationship.setEntityOneProxy(ep1);
            omrsRelationship.setEntityTwoProxy(ep2);
        }

        return omrsRelationship;

    }

    /**
     * Retrieve a Classification instance based on the provided information.
     *
     * @param omrsClassificationType the type name of the OMRS classification
     * @param omrsEntityType the OMRS type of entity the OMRS classification is being linked against
     * @param classificationProperties the properties to setup on the classification
     * @return Classification
     * @throws RepositoryErrorException
     */
    protected Classification getMappedClassification(String omrsClassificationType,
                                                     String omrsEntityType,
                                                     InstanceProperties classificationProperties) throws RepositoryErrorException {

        final String methodName = "getMappedClassification";

        Classification classification = null;

        try {
            // In such cases, create a new OMRS "Confidentiality" classification,
            // and add this to the list of classifications for this mapping
            classification = igcomrsRepositoryConnector.getRepositoryHelper().getNewClassification(
                    SOURCE_NAME,
                    userId,
                    omrsClassificationType,
                    omrsEntityType,
                    ClassificationOrigin.ASSIGNED,
                    null,
                    classificationProperties
            );
            if (me.hasModificationDetails()) {
                classification.setUpdateTime((Date)me.getPropertyByName("modified_on"));
                classification.setUpdatedBy((String)me.getPropertyByName("modified_by"));
                if (classification.getUpdateTime() != null) {
                    classification.setVersion(classification.getUpdateTime().getTime());
                }
            }
        } catch (TypeErrorException e) {
            log.error("Unable to create a new classification.", e);
            OMRSErrorCode errorCode = OMRSErrorCode.INVALID_CLASSIFICATION_FOR_ENTITY;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(
                    omrsClassificationType,
                    omrsEntityType);
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return classification;

    }

}
