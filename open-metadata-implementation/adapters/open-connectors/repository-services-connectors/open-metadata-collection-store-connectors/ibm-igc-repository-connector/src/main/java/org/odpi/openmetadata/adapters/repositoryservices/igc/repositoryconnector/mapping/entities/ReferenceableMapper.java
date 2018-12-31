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
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.AttachedTagMapper;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.RelatedTermMapper;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.RelationshipMapping;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.SemanticAssignmentMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
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

    protected void getMappedClassifications() {
        // Nothing to do -- no generic classifications to handle by default
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
        mapIGCToOMRSEntitySummary(getIgcClassificationProperties());
        return omrsSummary;
    }

    /**
     * Map the IGC entity to an OMRS EntityDetail object.
     *
     * @return EntityDetail
     */
    public EntityDetail getOMRSEntityDetail() {
        mapIGCToOMRSEntityDetail(getPropertyMappings(), getIgcClassificationProperties());
        return omrsDetail;
    }

    /**
     * Map the IGC entity's classifications to OMRS Classification objects.
     * (This is called automatically as part of getOMRSEntitySummary and getOMRSEntityDetail)
     *
     * @return List<Classification>
     */
    protected List<Classification> getOMRSClassifications() {
        getMappedClassifications();
        return omrsClassifications;
    }

    /**
     * Maps the (IGC) object this class was initialised with to an OMRS EntitySummary object, using the provided
     * mappings.
     * @param classificationProperties any IGC properties needed to setup classifications in the OMRS EntityDetail object
     */
    @Override
    protected void mapIGCToOMRSEntitySummary(Set<String> classificationProperties) {

        // Merge together all the properties we want to map
        String[] allProps = concatAll(
                classificationProperties.toArray(new String[0]),
                getPropertyMappings().getAllMappedIgcProperties().toArray(new String[0])
        );

        // Retrieve the full details we'll require for summary BEFORE handing off to superclass,
        // but only if the asset we've been initialised with was not already fully-retrieved
        if (!igcEntity.isFullyRetrieved()) {
            igcEntity = igcEntity.getAssetWithSubsetOfProperties(igcomrsRepositoryConnector.getIGCRestClient(), allProps);
        }

        // Handle any super-generic mappings first
        mapIGCToOMRSEntitySummary();

        // Then handle generic mappings and classifications
        setupEntityObj(omrsSummary);

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

        // Retrieve the set of non-relationship properties for the asset
        List<String> nonRelationshipProperties = Reference.getNonRelationshipPropertiesFromPOJO(igcEntity.getClass());

        // Merge the detailed properties together (generic and more specific POJO mappings that were passed in)
        String[] allProps = concatAll(
                propertyMap.getAllMappedIgcProperties().toArray(new String[0]),
                classificationProperties.toArray(new String[0]),
                nonRelationshipProperties.toArray(new String[0])
        );

        // Retrieve only this set of properties for the object (no more, no less)
        // but only if the asset we've been initialised with was not already fully-retrieved
        if (!igcEntity.isFullyRetrieved()) {
            igcEntity = igcEntity.getAssetWithSubsetOfProperties(igcomrsRepositoryConnector.getIGCRestClient(), allProps);
        }

        // Handle any super-generic mappings first
        mapIGCToOMRSEntityDetail();

        // Then handle any generic mappings and classifications
        setupEntityObj(omrsDetail);

        // Use reflection to apply POJO-specific mappings
        InstanceProperties instanceProperties = getMappedInstanceProperties(propertyMap, nonRelationshipProperties);
        omrsDetail.setProperties(instanceProperties);

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
        if (omrsClassifications == null) {
            omrsClassifications = new ArrayList<>();
            getMappedClassifications();
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
        getMappedRelationships(
                getRelationshipMappers(),
                relationshipTypeGUID,
                fromRelationshipElement,
                sequencingOrder,
                pageSize);
        return omrsRelationships;
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
    private void getMappedRelationships(List<RelationshipMapping> mappings,
                                        String                   relationshipTypeGUID,
                                        int                      fromRelationshipElement,
                                        SequencingOrder          sequencingOrder,
                                        int                      pageSize) {

        // TODO: handle multi-page results with different starting points (ie. fromRelationshipElement != 0)

        // Retrieve the full details we'll require for the relationships
        // but only if the asset we've been initialised with was not already fully-retrieved
        if (!igcEntity.isFullyRetrieved()) {
            // Merge together all the properties we want to map
            ArrayList<String> relationshipProperties = new ArrayList<>();
            for (RelationshipMapping mapping : mappings) {
                log.debug("Adding properties from mapping: {}", mapping);
                relationshipProperties.addAll(mapping.getIgcRelationshipPropertiesForType(igcEntity.getType()));
            }
            String[] allProps = concatAll(
                    relationshipProperties.toArray(new String[0]),
                    MODIFICATION_DETAILS
            );
            IGCSearchSorting sort = IGCSearchSorting.sortFromNonPropertySequencingOrder(sequencingOrder);
            igcEntity = igcEntity.getAssetWithSubsetOfProperties(igcomrsRepositoryConnector.getIGCRestClient(), allProps, pageSize, sort);
        }

        getMappedRelationships(mappings, relationshipTypeGUID);

        complexRelationshipMappings();

    }

    /**
     * Utility function that actually does the Relationship object setup and addition to 'relationships' member.
     *
     * @param mappings the mappings to use for retrieving the relationships
     * @param relationshipTypeGUID String GUID of the the type of relationship required (null for all).
     */
    private void getMappedRelationships(List<RelationshipMapping> mappings, String relationshipTypeGUID) {

        // Iterate through the provided mappings to create a number of OMRS relationships
        for (RelationshipMapping mapping : mappings) {

            RelationshipDef omrsRelationshipDef = (RelationshipDef) igcomrsRepositoryConnector.getRepositoryHelper().getTypeDefByName(
                    SOURCE_NAME,
                    mapping.getOmrsRelationshipType()
            );

            // Only continue with a given relationship if we are mapping all relationships or it matches the
            // GUID of the relationship type we are mapping
            if (relationshipTypeGUID == null || relationshipTypeGUID.equals(omrsRelationshipDef.getGUID())) {

                RelationshipMapping.OptimalStart optimalStart = mapping.getOptimalStart();

                if (mapping.isSelfReferencing()) {
                    addSelfReferencingRelationship(mapping);
                } else if (!optimalStart.equals(RelationshipMapping.OptimalStart.CUSTOM)) {
                    if (igcEntity.isFullyRetrieved() || !optimalStart.equals(RelationshipMapping.OptimalStart.OPPOSITE)) {
                        addDirectRelationship(mapping);
                    } else if (optimalStart.equals(RelationshipMapping.OptimalStart.OPPOSITE)) {
                        addInvertedRelationship(mapping);
                    } else {
                        log.warn("Ran out of options for finding the relationship: {}", omrsRelationshipDef.getName());
                    }
                }

            }

        }

    }

    private void addSelfReferencingRelationship(RelationshipMapping mapping) {
        try {
            Relationship relationship = getMappedRelationship(
                    mapping,
                    igcEntity,
                    RelationshipMapping.SELF_REFERENCE_SENTINEL
            );
            log.debug("addSelfReferencingRelationship - adding relationship: {}", relationship);
            omrsRelationships.add(relationship);
        } catch (RepositoryErrorException e) {
            log.error("Unable to add self-referencing relationship for: {}", igcEntity, e);
        }
    }

    private void addDirectRelationship(RelationshipMapping mapping) {

        // If we already have all info about the entity, optimal path to retrieve relationships is to use
        // the ones that are already in-memory -- though if it is also not optimal (or possible) to retrieve
        // from a search (see below) we must also resort to this property-based retrieval
        for (String igcRelationshipName : mapping.getIgcRelationshipPropertiesForType(igcEntity.getType())) {

            Object relationships = igcEntity.getPropertyByName(igcRelationshipName);

            // Handle single instance relationship one way
            if (relationships != null && Reference.isReference(relationships)) {

                addSingleMappedRelationship(
                        mapping,
                        (Reference) relationships,
                        igcRelationshipName
                    );

            } else if (relationships != null && Reference.isReferenceList(relationships)) { // and list of relationships another

                addListOfMappedRelationships(
                        mapping,
                        (ReferenceList) relationships,
                        igcRelationshipName
                );

            } else {
                log.debug(" ... skipping relationship {}, either empty or neither reference or list {}", igcRelationshipName, relationships);
            }

        }

    }

    private void addInvertedRelationship(RelationshipMapping mapping) {

        String assetType = igcEntity.getType();

        log.debug("Adding inverted relationship for mapping: {}", mapping);

        if (mapping.sameTypeOnBothEnds()) {

            // If we have a hierarchical relationship, we need to run searches across both
            // properties and add both sets of relationships
            List<String> igcProperties = mapping.getIgcRelationshipPropertiesForType(assetType);
            for (String igcRelationshipName : igcProperties) {
                IGCSearchCondition condition = new IGCSearchCondition(igcRelationshipName, "=", igcEntity.getId());
                IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet(condition);
                addSearchResultsToRelationships(
                        mapping,
                        igcSearchConditionSet,
                        assetType,
                        igcRelationshipName
                );
            }

        } else {

            // Otherwise, use the optimal retrieval for the relationship (a new large-page search)
            RelationshipMapping.ProxyMapping otherSide = mapping.getOtherProxyFromType(assetType);
            log.debug(" ... found other proxy: {}", otherSide);
            RelationshipMapping.ProxyMapping thisSide = mapping.getProxyFromType(assetType);
            log.debug(" ... found this proxy: {}", thisSide);

            String anIgcRelationshipProperty = null;
            IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet();
            igcSearchConditionSet.setMatchAnyCondition(true);
            for (String igcRelationshipName : otherSide.getIgcRelationshipProperties()) {
                IGCSearchCondition condition = new IGCSearchCondition(igcRelationshipName, "=", igcEntity.getId());
                igcSearchConditionSet.addCondition(condition);
                anIgcRelationshipProperty = igcRelationshipName;
            }
            String sourceAssetType = otherSide.getIgcAssetType();
            addSearchResultsToRelationships(
                    mapping,
                    igcSearchConditionSet,
                    sourceAssetType,
                    anIgcRelationshipProperty
            );

        }

    }

    private void addSearchResultsToRelationships(RelationshipMapping mapping,
                                                 IGCSearchConditionSet igcSearchConditionSet,
                                                 String assetType,
                                                 String igcPropertyName) {

        IGCSearch igcSearch = new IGCSearch(assetType, igcSearchConditionSet);
        if (!assetType.equals(IGCOMRSMetadataCollection.DEFAULT_IGC_TYPE)) {
            Class pojo = igcomrsRepositoryConnector.getIGCRestClient().getPOJOForType(assetType);
            if (Reference.hasModificationDetails(pojo)) {
                igcSearch.addProperties(MODIFICATION_DETAILS);
            }
        }
        ReferenceList relationships = igcomrsRepositoryConnector.getIGCRestClient().search(igcSearch);
        addListOfMappedRelationships(
                mapping,
                relationships,
                igcPropertyName
        );

    }

    /**
     * Add the provided list of relationships as OMRS relationships.
     *
     * @param mapping the mapping to use in translating each relationship
     * @param igcRelationships the list of IGC relationships
     * @param igcPropertyName the name of the IGC relationship property
     */
    private void addListOfMappedRelationships(RelationshipMapping mapping,
                                              ReferenceList igcRelationships,
                                              String igcPropertyName) {

        log.debug(" ... list of references: {}", mapping.getOmrsRelationshipType());

        // TODO: paginate rather than always retrieving the full set
        igcRelationships.getAllPages(igcomrsRepositoryConnector.getIGCRestClient());

        // Iterate through all of the existing IGC relationships of that type to create an OMRS relationship
        // for each one
        for (Reference relation : igcRelationships.getItems()) {
            addSingleMappedRelationship(
                    mapping,
                    relation,
                    igcPropertyName
            );
        }

    }

    /**
     * Add the provided relationship as an OMRS relationship.
     *
     * @param mapping the mapping to use in translating each relationship
     * @param igcRelationship the IGC relationship
     * @param igcPropertyName the name of the IGC relationship property
     */
    private void addSingleMappedRelationship(RelationshipMapping mapping,
                                             Reference igcRelationship,
                                             String igcPropertyName) {

        log.debug(" ... single reference: {}", igcRelationship);
        if (igcRelationship != null
                && igcRelationship.getType() != null
                && !igcRelationship.getType().equals("null")) {

            try {
                Relationship omrsRelationship = getMappedRelationship(
                        mapping,
                        igcRelationship,
                        igcPropertyName
                );
                log.debug("addSingleMappedRelationship - adding relationship: {}", omrsRelationship);
                omrsRelationships.add(omrsRelationship);
            } catch (RepositoryErrorException e) {
                log.error("Unable to add relationship {} for object {}", mapping.getOmrsRelationshipType(), igcRelationship);
            }

        }

    }

    /**
     * Retrieve a Relationship instance based on the provided mapping information, automatically prefixing
     * where needed.
     *
     * @param mapping the mapping details to use
     * @param relation the related IGC object
     * @param igcPropertyName the name of the IGC relationship property
     * @return Relationship
     * @throws RepositoryErrorException
     */
    private Relationship getMappedRelationship(RelationshipMapping mapping,
                                               Reference relation,
                                               String igcPropertyName) throws RepositoryErrorException {

        RelationshipDef omrsRelationshipDef = (RelationshipDef) igcomrsRepositoryConnector.getRepositoryHelper().getTypeDefByName(
                SOURCE_NAME,
                mapping.getOmrsRelationshipType()
        );

        Relationship relationship = getMappedRelationship(
                mapping,
                omrsRelationshipDef,
                igcEntity,
                relation,
                igcPropertyName
        );

        return relationship;

    }

    /**
     * Retrieve a Relationship instance based on the provided definition and endpoints.
     *
     * @param relationshipMapping the definition of how to map the relationship
     * @param omrsRelationshipDef the OMRS relationship definition
     * @param proxyOne the IGC asset to use for endpoint 1 of the relationship
     * @param proxyTwo the IGC asset to use for endpoint 2 of the relationship
     * @param igcPropertyName the name of the IGC relationship property
     * @return Relationship
     * @throws RepositoryErrorException
     */
    protected Relationship getMappedRelationship(RelationshipMapping relationshipMapping,
                                                 RelationshipDef omrsRelationshipDef,
                                                 Reference proxyOne,
                                                 Reference proxyTwo,
                                                 String igcPropertyName) throws RepositoryErrorException {
        return getMappedRelationship(
                relationshipMapping,
                omrsRelationshipDef,
                proxyOne,
                proxyTwo,
                igcPropertyName,
                null
        );
    }

    /**
     * Retrieve a Relationship instance based on the provided definition, endpoints, and optional prefixes.
     *
     * @param relationshipMapping the definition of how to map the relationship
     * @param omrsRelationshipDef the OMRS relationship definition
     * @param proxyOne the IGC asset to consider for endpoint 1 of the relationship
     * @param proxyTwo the IGC asset to consider for endpoint 2 of the relationship
     * @param igcPropertyName the name of the IGC relationship property
     * @param relationshipLevelRid the IGC RID for the relationship itself (in rare instances where it exists)
     * @return Relationship
     * @throws RepositoryErrorException
     */
    public Relationship getMappedRelationship(RelationshipMapping relationshipMapping,
                                              RelationshipDef omrsRelationshipDef,
                                              Reference proxyOne,
                                              Reference proxyTwo,
                                              String igcPropertyName,
                                              String relationshipLevelRid) throws RepositoryErrorException {

        final String methodName = "getMappedRelationship";
        final String repositoryName = igcomrsRepositoryConnector.getRepositoryName();

        String omrsRelationshipName = omrsRelationshipDef.getName();

        Relationship relationship = new Relationship();

        try {
            InstanceType instanceType = igcomrsRepositoryConnector.getRepositoryHelper().getNewInstanceType(
                    SOURCE_NAME,
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
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

        return relationship;

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
            if (igcEntity.hasModificationDetails()) {
                classification.setUpdateTime((Date)igcEntity.getPropertyByName("modified_on"));
                classification.setUpdatedBy((String)igcEntity.getPropertyByName("modified_by"));
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
