/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearch;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchCondition;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchConditionSet;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchSorting;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSMetadataCollection;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.classifications.ClassificationMapping;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.AttachedTagMapper;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.RelatedTermMapper;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.RelationshipMapping;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.SemanticAssignmentMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ReferenceableMapper extends EntityMapping {

    private static final Logger log = LoggerFactory.getLogger(ReferenceableMapper.class);

    // By default (if no IGC type or OMRS type defined), map between 'main_object' (IGC) and Referenceable (OMRS)
    public ReferenceableMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {
        this(
                igcomrsRepositoryConnector,
                IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE,
                IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE_DISPLAY_NAME,
                "Referenceable",
                userId
        );
    }

    public ReferenceableMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                               String igcAssetTypeName,
                               String igcAssetTypeDisplayName,
                               String omrsEntityTypeName,
                               String userId) {
        this(
                igcomrsRepositoryConnector,
                igcAssetTypeName,
                igcAssetTypeDisplayName,
                omrsEntityTypeName,
                userId,
                null
        );
    }

    public ReferenceableMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                               String igcAssetTypeName,
                               String igcAssetTypeDisplayName,
                               String omrsEntityTypeName,
                               String userId,
                               String igcRidPrefix) {
        this(
                igcomrsRepositoryConnector,
                igcAssetTypeName,
                igcAssetTypeDisplayName,
                omrsEntityTypeName,
                userId,
                igcRidPrefix,
                true
        );
    }

    public ReferenceableMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                               String igcAssetTypeName,
                               String igcAssetTypeDisplayName,
                               String omrsEntityTypeName,
                               String userId,
                               String igcRidPrefix,
                               boolean includeDefaultRelationships) {

        super(
                igcomrsRepositoryConnector,
                igcAssetTypeName,
                igcAssetTypeDisplayName,
                omrsEntityTypeName,
                userId,
                igcRidPrefix
        );

        if (includeDefaultRelationships) {
            // common set of relationships that could apply to all IGC objects (and all OMRS Referenceables)
            addRelationshipMapper(SemanticAssignmentMapper.getInstance());
            addRelationshipMapper(AttachedTagMapper.getInstance());
        }

        // common set of classifications that apply to all IGC objects (and all OMRS Referenceables) [none]

    }

    /**
     * Retrieves a new instance of this same class, which is needed to ensure there are distinct protected members
     * to populate for each instance of an object that needs to be mapped.
     *
     * @return EntityMapping
     */
    protected ReferenceableMapper getNewInstance() {

        ReferenceableMapper mapper = null;
        Class clazz = this.getClass();

        try {
            Constructor constructor = clazz.getConstructor(
                    IGCOMRSRepositoryConnector.class,
                    String.class
            );
            mapper = (ReferenceableMapper) constructor.newInstance(
                    this.igcomrsRepositoryConnector,
                    this.userId
            );
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error("Unable to construct new instance of: {}", clazz.getCanonicalName(), e);
        }

        return mapper;

    }

    /**
     * Initialize the mapper with an IGC object to be mapped.
     * This method MUST be called before any of the subsequent mapping functions!
     *
     * @param igcObject the IGC object to be mapped into an OMRS entity
     * @return ReferenceableMapper initialised with the provided object
     */
    public ReferenceableMapper initialize(Reference igcObject) {

        ReferenceableMapper mapper = getNewInstance();
        mapper.igcEntity = igcObject;

        if (mapper.igcEntity != null && mapper.igcEntity.hasModificationDetails()) {
            for (String property : MODIFICATION_DETAILS) {
                mapper.addComplexIgcProperty(property);
            }
        }
        return mapper;

    }

    protected void complexPropertyMappings(InstanceProperties instanceProperties) {
        // Nothing to do -- no complex properties by default
        // (only modification details, but because we want those on EntitySummary as well they're handled elsewhere)
    }

    protected void complexRelationshipMappings() {
        // Nothing to do -- no complex relationships to handle by default
    }

    /**
     * Map the IGC entity to an OMRS EntitySummary object.
     *
     * @return EntitySummary
     */
    public EntitySummary getOMRSEntitySummary() {

        // Merge together all the properties we want to map
        ArrayList<String> allProperties = new ArrayList<>();
        for (ClassificationMapping classificationMapping : getClassificationMappers()) {
            allProperties.addAll(classificationMapping.getIgcRelationshipProperties());
        }
        allProperties.addAll(getPropertyMappings().getAllMappedIgcProperties());

        // Retrieve the full details we'll require for summary BEFORE handing off to superclass,
        // but only if the asset we've been initialised with was not already fully-retrieved
        if (!igcEntity.isFullyRetrieved()) {
            igcEntity = igcEntity.getAssetWithSubsetOfProperties(
                    igcomrsRepositoryConnector.getIGCRestClient(),
                    allProperties.toArray(new String[0])
            );
        }

        // Handle any super-generic mappings first
        mapIGCToOMRSEntitySummary();

        // Then handle generic mappings and classifications
        setupEntityObj(omrsSummary);
        return omrsSummary;

    }

    /**
     * Map the IGC entity to an OMRS EntityDetail object.
     *
     * @return EntityDetail
     */
    public EntityDetail getOMRSEntityDetail() {

        // Retrieve the set of non-relationship properties for the asset
        List<String> nonRelationshipProperties = Reference.getNonRelationshipPropertiesFromPOJO(igcEntity.getClass());

        // Merge the detailed properties together (generic and more specific POJO mappings that were passed in)
        ArrayList<String> allProperties = new ArrayList<>();
        PropertyMappingSet propertyMappingSet = getPropertyMappings();
        allProperties.addAll(propertyMappingSet.getAllMappedIgcProperties());
        for (ClassificationMapping classificationMapping : getClassificationMappers()) {
            allProperties.addAll(classificationMapping.getIgcRelationshipProperties());
        }
        allProperties.addAll(nonRelationshipProperties);

        // Retrieve only this set of properties for the object (no more, no less)
        // but only if the asset we've been initialised with was not already fully-retrieved
        if (!igcEntity.isFullyRetrieved()) {
            igcEntity = igcEntity.getAssetWithSubsetOfProperties(
                    igcomrsRepositoryConnector.getIGCRestClient(),
                    allProperties.toArray(new String[0])
            );
        }

        // Handle any super-generic mappings first
        mapIGCToOMRSEntityDetail();

        // Then handle any generic mappings and classifications
        setupEntityObj(omrsDetail);

        // Use reflection to apply POJO-specific mappings
        InstanceProperties instanceProperties = getMappedInstanceProperties(propertyMappingSet, nonRelationshipProperties);
        omrsDetail.setProperties(instanceProperties);

        return omrsDetail;

    }

    /**
     * Simple utility function to avoid implementing shared EntitySummary and EntityDetail setup twice.
     *
     * @param omrsObj OMRS object to map into (EntityDetail or EntitySummary)
     */
    private void setupEntityObj(EntitySummary omrsObj) {

        if (igcEntity.hasModificationDetails()) {
            omrsObj.setCreatedBy((String)igcEntity.getPropertyByName(Reference.MOD_CREATED_BY));
            omrsObj.setCreateTime((Date)igcEntity.getPropertyByName(Reference.MOD_CREATED_ON));
            omrsObj.setUpdatedBy((String)igcEntity.getPropertyByName(Reference.MOD_MODIFIED_BY));
            omrsObj.setUpdateTime((Date)igcEntity.getPropertyByName(Reference.MOD_MODIFIED_ON));
            if (omrsObj.getUpdateTime() != null) {
                omrsObj.setVersion(omrsObj.getUpdateTime().getTime());
            }
        }

        // Avoid doing this multiple times: if one has retrieved classifications it'll
        // be the same classifications for the other
        List<ClassificationMapping> classificationMappings = getClassificationMappers();
        if (!classificationMappings.isEmpty() && omrsClassifications == null) {
            omrsClassifications = new ArrayList<>();
            for (ClassificationMapping classificationMapping : classificationMappings) {
                classificationMapping.addMappedOMRSClassifications(
                        igcomrsRepositoryConnector,
                        omrsClassifications,
                        igcEntity,
                        userId
                );
            }
        }

        omrsObj.setClassifications(omrsClassifications);

    }

    /**
     * Retrieves the InstanceProperties based on the mappings defined in the provided PropertyMappingSet.
     *
     * @param mappings the mappings to use for retrieving a set of InstanceProperties
     * @param nonRelationshipProperties the full list of properties for the asset that are not relationships
     * @return InstanceProperties
     */
    private InstanceProperties getMappedInstanceProperties(PropertyMappingSet mappings, List<String> nonRelationshipProperties) {

        InstanceProperties instanceProperties = new InstanceProperties();

        // We'll always start by using the Identity string as the qualified name
        String qualifiedName = igcEntity.getIdentity(igcomrsRepositoryConnector.getIGCRestClient()).toString();
        instanceProperties.setProperty("qualifiedName", getPrimitivePropertyValue(qualifiedName));

        // Then we'll iterate through the provided mappings to set an OMRS instance property for each one
        for (String igcPropertyName : mappings.getSimpleMappedIgcProperties()) {
            String omrsAttribute = mappings.getOmrsPropertyName(igcPropertyName);
            instanceProperties.setProperty(
                    omrsAttribute,
                    getPrimitivePropertyValue(igcEntity.getPropertyByName(igcPropertyName))
            );
        }

        // Then we'll apply any complex property mappings
        complexPropertyMappings(instanceProperties);

        // Finally we'll map any simple (non-relationship) properties that remain
        // to Referenceable's 'additionalProperties'
        Set<String> alreadyMapped = mappings.getAllMappedIgcProperties();
        if (nonRelationshipProperties != null) {

            // Remove all of the already-mapped properties from our list of non-relationship properties
            Set<String> nonRelationshipsSet = new HashSet<>(nonRelationshipProperties);
            nonRelationshipsSet.removeAll(alreadyMapped);

            // Iterate through the remaining property names, and add them to a map
            MapPropertyValue mapValue = new MapPropertyValue();
            for (String propertyName : nonRelationshipsSet) {
                Object propertyValue = igcEntity.getPropertyByName(propertyName);
                if (propertyValue instanceof ArrayList) {
                    ArrayPropertyValue arrayValue = new ArrayPropertyValue();
                    arrayValue.setArrayCount(((ArrayList<Object>) propertyValue).size());
                    int count = 0;
                    for (Object value : (ArrayList<Object>)propertyValue) {
                        arrayValue.setArrayValue(
                                count,
                                getPrimitivePropertyValue(value)
                        );
                        count++;
                    }
                    mapValue.setMapValue(
                            propertyName,
                            arrayValue
                    );
                } else {
                    mapValue.setMapValue(
                            propertyName,
                            getPrimitivePropertyValue(propertyValue));
                }
            }

            // Then set that map as the "additionalProperties" of the OMRS entity
            instanceProperties.setProperty(
                    "additionalProperties",
                    mapValue
            );

        }

        return instanceProperties;

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

        // TODO: handle multi-page results with different starting points (ie. fromRelationshipElement != 0)

        List<RelationshipMapping> relationshipMappers = getRelationshipMappers();

        // Retrieve the full details we'll require for the relationships
        // but only if the asset we've been initialised with was not already fully-retrieved
        if (!igcEntity.isFullyRetrieved()) {
            // Merge together all the properties we want to map
            ArrayList<String> allProperties = new ArrayList<>();
            for (RelationshipMapping mapping : relationshipMappers) {
                log.debug("Adding properties from mapping: {}", mapping);
                allProperties.addAll(mapping.getIgcRelationshipPropertiesForType(igcEntity.getType()));
            }
            allProperties.addAll(Arrays.asList(MODIFICATION_DETAILS));
            IGCSearchSorting sort = IGCSearchSorting.sortFromNonPropertySequencingOrder(sequencingOrder);
            igcEntity = igcEntity.getAssetWithSubsetOfProperties(
                    igcomrsRepositoryConnector.getIGCRestClient(),
                    allProperties.toArray(new String[0]),
                    pageSize,
                    sort
            );
        }

        RelationshipMapping.getMappedRelationships(
                igcomrsRepositoryConnector,
                omrsRelationships,
                relationshipMappers,
                relationshipTypeGUID,
                igcEntity,
                userId
        );

        return omrsRelationships;

    }

}
