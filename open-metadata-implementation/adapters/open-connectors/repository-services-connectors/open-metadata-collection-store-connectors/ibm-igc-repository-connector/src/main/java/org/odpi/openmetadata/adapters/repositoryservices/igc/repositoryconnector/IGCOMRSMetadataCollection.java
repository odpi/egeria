/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearch;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchCondition;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchConditionSet;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchSorting;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollectionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class IGCOMRSMetadataCollection extends OMRSMetadataCollectionBase {

    private static final Logger log = LoggerFactory.getLogger(IGCOMRSMetadataCollection.class);

    private IGCRestClient igcRestClient;
    public static final String MAPPING_PKG = "org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.";

    private Class defaultMapper;
    private EntityMappingSet implementedEntities = new EntityMappingSet();
    private String igcVersion;

    /**
     * @param parentConnector      connector that this metadata collection supports.
     *                             The connector has the information to call the metadata repository.
     * @param repositoryName       name of this repository.
     * @param repositoryHelper     helper that provides methods to repository connectors and repository event mappers
     *                             to build valid type definitions (TypeDefs), entities and relationships.
     * @param repositoryValidator  validator class for checking open metadata repository objects and parameters
     * @param metadataCollectionId unique identifier for the repository
     */
    public IGCOMRSMetadataCollection(IGCOMRSRepositoryConnector parentConnector,
                                     String repositoryName,
                                     OMRSRepositoryHelper repositoryHelper,
                                     OMRSRepositoryValidator repositoryValidator,
                                     String metadataCollectionId) {
        super(parentConnector, repositoryName, repositoryHelper, repositoryValidator, metadataCollectionId);
        this.igcRestClient = parentConnector.getIGCRestClient();
        this.igcVersion = parentConnector.getIGCVersion();
        try {
            this.defaultMapper = Class.forName(MAPPING_PKG + "ReferenceableMapper");
        } catch (ClassNotFoundException e) {
            log.error("Unable to find default ReferenceableMapper class: " + MAPPING_PKG + "ReferenceableMapper");
            e.printStackTrace();
        }
    }

    /**
     * Verify that a definition of a TypeDef is either new or matches the definition already stored.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDef TypeDef structure describing the TypeDef to test.
     * @return boolean true means the TypeDef matches the local definition; false means the TypeDef is not known.
     * @throws InvalidParameterException the TypeDef is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws TypeDefNotSupportedException the repository is not able to support this TypeDef.
     * @throws TypeDefConflictException the new TypeDef conflicts with an existing TypeDef.
     * @throws InvalidTypeDefException the new TypeDef has invalid contents.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public boolean verifyTypeDef(String  userId,
                                 TypeDef typeDef) throws InvalidParameterException,
            RepositoryErrorException,
            TypeDefNotSupportedException,
            TypeDefConflictException,
            InvalidTypeDefException,
            UserNotAuthorizedException
    {
        final String  methodName           = "verifyTypeDef";
        final String  typeDefParameterName = "typeDef";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);
        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDef(repositoryName, typeDefParameterName, typeDef, methodName);

        // TODO: approach below will work for EntityDefs, but not for ClassificationDef or RelationshipDefs...
        //  - maybe only possible to cover all via getAllTypes?

        String omrsTypeDefName = typeDef.getName();

        boolean bMapperExists = false;

        // See if we have a Mapper defined for the class -- if so, it's implemented
        try {

            Class mappingClass = Class.forName(MAPPING_PKG + omrsTypeDefName + "Mapper");
            bMapperExists = true;

            // Assuming we have some mapping, get the name of the IGC object involved in the mapping and from that
            // attempt to retrieve a POJO object to (de-)serialise that IGC asset type
            String igcAssetTypeName = ReferenceMapper.getIgcTypeFromMapping(
                    mappingClass,
                    (IGCOMRSRepositoryConnector)parentConnector,
                    userId
            );
            if (igcAssetTypeName != null) {
                String pojoClassName = ReferenceMapper.getCamelCase(igcAssetTypeName);
                try {
                    Class pojoClass = Class.forName(ReferenceMapper.IGC_REST_GENERATED_MODEL_PKG + "." + igcVersion + "." + pojoClassName);
                    implementedEntities.put(
                            igcAssetTypeName,
                            typeDef,
                            mappingClass,
                            pojoClass
                    );
                    this.igcRestClient.registerPOJO(pojoClass);
                    // Check if there are any additional POJOs needed by the mapper, and if so register those as well
                    List<String> extraPOJOs = ReferenceMapper.getAdditionalIgcPOJOs(
                            mappingClass,
                            (IGCOMRSRepositoryConnector)parentConnector,
                            userId
                    );
                    for (String pojoName : extraPOJOs) {
                        this.igcRestClient.registerPOJO(Class.forName(pojoName));
                    }
                } catch (ClassNotFoundException e) {
                    log.info("Unable to find POJO for: " + pojoClassName);
                    e.printStackTrace();
                }

            }

        } catch (ClassNotFoundException e) {
            log.info("Unable to find Mapper for: " + omrsTypeDefName);
            e.printStackTrace();
        }

        return bMapperExists;

    }

    /**
     * Return the relationships for a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param entityGUID String unique identifier for the entity.
     * @param relationshipTypeGUID String GUID of the the type of relationship required (null for all).
     * @param fromRelationshipElement the starting element number of the relationships to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus Must be null (relationship status is not implemented for IGC).
     * @param asOfTime Must be null (history not implemented for IGC).
     * @param sequencingProperty Must be null (there are no properties on IGC relationships).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize -- the maximum number of result classifications that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return Relationships list.  Null means no relationships associated with the entity.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection.
     * @throws PropertyErrorException the sequencing property is not valid for the attached classifications.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public List<Relationship> getRelationshipsForEntity(String                     userId,
                                                        String                     entityGUID,
                                                        String                     relationshipTypeGUID,
                                                        int                        fromRelationshipElement,
                                                        List<InstanceStatus>       limitResultsByStatus,
                                                        Date                       asOfTime,
                                                        String                     sequencingProperty,
                                                        SequencingOrder            sequencingOrder,
                                                        int                        pageSize) throws InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            EntityNotKnownException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "getRelationshipsForEntity";
        final String  guidParameterName = "entityGUID";
        final String  typeGUIDParameter = "relationshipTypeGUID";
        final String  asOfTimeParameter = "asOfTime";
        final String  pageSizeParameter = "pageSize";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, entityGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName, typeGUIDParameter, relationshipTypeGUID, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);

        /*
         * Perform operation
         */
        List<Relationship> alRelationships = null;

        // Immediately throw unimplemented exception if trying to limit by status or retrieve historical view
        if (limitResultsByStatus != null
                || asOfTime != null
                || sequencingProperty != null
                || (sequencingOrder != null
                    &&
                    (sequencingOrder.equals(SequencingOrder.PROPERTY_ASCENDING)
                    || sequencingOrder.equals(SequencingOrder.PROPERTY_DESCENDING)))
        ) {
            OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    this.getClass().getName(),
                    repositoryName);
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } else {

            // 1. retrieve entity from IGC by GUID (RID)
            Reference asset = this.igcRestClient.getAssetRefById(entityGUID);

            // Ensure the entity actually exists (if not, throw error to that effect)
            if (asset == null) {
                OMRSErrorCode errorCode = OMRSErrorCode.ENTITY_NOT_KNOWN;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                        this.getClass().getName(),
                        repositoryName);
                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            } else {

                Class clazz = getMapperClass(asset.getType());

                try {

                    Constructor constructor = clazz.getConstructor(Reference.class, IGCOMRSRepositoryConnector.class, String.class);
                    ReferenceableMapper referenceMapper = (ReferenceableMapper) constructor.newInstance(
                            asset,
                            (IGCOMRSRepositoryConnector) parentConnector,
                            userId
                    );

                    // 2. Apply the mapping to the object, and retrieve the resulting relationships
                    alRelationships = referenceMapper.getOMRSRelationships(
                            relationshipTypeGUID,
                            fromRelationshipElement,
                            sequencingOrder,
                            pageSize);

                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    // If we cannot find what we expect in the Mapper class, print a stack trace and raise error that it is not known
                    e.printStackTrace();
                    OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_NOT_KNOWN;
                    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                            this.getClass().getName(),
                            repositoryName);
                    throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                            this.getClass().getName(),
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());
                }

            }

        }

        return alRelationships;

    }

    /**
     * Return a list of entities that match the supplied properties according to the match criteria.  The results
     * can be returned over many pages.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID String unique identifier for the entity type of interest (null means any entity type).
     * @param matchProperties List of entity properties to match to (null means match on entityTypeGUID only).
     * @param matchCriteria Enum defining how the properties should be matched to the entities in the repository.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus Must be null (relationship status is not implemented for IGC).
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Must be null (history not implemented for IGC).
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of entities matching the supplied criteria where null means no matching entities in the metadata
     * collection.
     *
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  List<EntityDetail> findEntitiesByProperty(String                    userId,
                                                      String                    entityTypeGUID,
                                                      InstanceProperties        matchProperties,
                                                      MatchCriteria             matchCriteria,
                                                      int                       fromEntityElement,
                                                      List<InstanceStatus>      limitResultsByStatus,
                                                      List<String>              limitResultsByClassification,
                                                      Date                      asOfTime,
                                                      String                    sequencingProperty,
                                                      SequencingOrder           sequencingOrder,
                                                      int                       pageSize) throws InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName                   = "findEntitiesByProperty";
        final String  matchCriteriaParameterName   = "matchCriteria";
        final String  matchPropertiesParameterName = "matchProperties";
        final String  typeGUIDParameterName        = "entityTypeGUID";
        final String  asOfTimeParameter            = "asOfTime";
        final String  pageSizeParameter            = "pageSize";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName, typeGUIDParameterName, entityTypeGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);
        repositoryValidator.validateMatchCriteria(repositoryName,
                matchCriteriaParameterName,
                matchPropertiesParameterName,
                matchCriteria,
                matchProperties,
                methodName);

        /*
         * Perform operation
         */
        ArrayList<EntityDetail> entityDetails = new ArrayList<>();

        // Immediately throw unimplemented exception if trying to limit by status or retrieve historical view
        if (limitResultsByStatus != null || asOfTime != null) {
            OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    this.getClass().getName(),
                    repositoryName);
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } else {

            // Otherwise run a search across all object types (by default),
            // or only the TypeDef provided
            EntityMappingSet.EntityMapping mapping = null;
            if (entityTypeGUID != null
                    && !entityTypeGUID.equals("")
                    && implementedEntities.isTypeDefMapped(entityTypeGUID)) {
                mapping = implementedEntities.getByTypeDefGUID(entityTypeGUID);
            }
            String igcTypeToSearch = (mapping == null) ? "main_object" : mapping.getIgcAssetType();

            // We need to first retrieve the mapping so we know how to translate
            // the provided OMRS property names to IGC property names
            PropertyMappingSet propertyMappingSet = null;
            if (mapping != null) {
                Class mappingClass = mapping.getMappingClass();
                propertyMappingSet = ReferenceMapper.getPropertiesFromMapping(
                        mappingClass,
                        (IGCOMRSRepositoryConnector)parentConnector,
                        userId
                );
            }

            // Provided there is a mapping, build up a list of IGC-specific properties
            // and search criteria, based on the values of the InstanceProperties provided
            ArrayList<String> properties = new ArrayList<>();
            IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet();

            Iterator iPropertyNames = matchProperties.getPropertyNames();
            while (propertyMappingSet != null && iPropertyNames.hasNext()) {
                String omrsPropertyName = (String) iPropertyNames.next();
                InstancePropertyValue value = matchProperties.getPropertyValue(omrsPropertyName);
                addSearchConditionFromValue(
                        igcSearchConditionSet,
                        omrsPropertyName,
                        properties,
                        propertyMappingSet,
                        value
                );
            }

            IGCSearchSorting igcSearchSorting = null;
            if (sequencingProperty == null && sequencingOrder != null) {
                igcSearchSorting = IGCSearchSorting.sortFromNonPropertySequencingOrder(sequencingOrder);
            }

            if (matchCriteria != null) {
                switch(matchCriteria) {
                    case ALL:
                        igcSearchConditionSet.setMatchAnyCondition(false);
                        break;
                    case ANY:
                        igcSearchConditionSet.setMatchAnyCondition(true);
                        break;
                    case NONE:
                        igcSearchConditionSet.setMatchAnyCondition(false);
                        igcSearchConditionSet.setNegateAll(true);
                        break;
                }
            }

            IGCSearch igcSearch = new IGCSearch(igcTypeToSearch, properties.toArray(new String[0]), igcSearchConditionSet);
            if (pageSize > 0) {
                // Only set pageSize if it has been provided; otherwise we'll end up defaulting to IGC's
                // minimal pageSize of 10 (so will need to make many calls to get all pages)
                igcSearch.setPageSize(pageSize);
            } else {
                // So if none has been specified, we'll set a large pageSize to be able to more efficiently
                // retrieve all pages of results
                igcSearch.setPageSize(parentConnector.getMaxPageSize());
            }
            igcSearch.setBeginAt(fromEntityElement);
            if (igcSearchSorting != null) {
                igcSearch.addSortingCriteria(igcSearchSorting);
            }
            ReferenceList results = this.igcRestClient.search(igcSearch);

            if (pageSize == 0) {
                // If the provided pageSize was 0, we need to retrieve ALL pages of
                // results...
                results.getAllPages(this.igcRestClient);
            }

            for (Reference reference : results.getItems()) {
                // Only proceed with retrieving the EntityDetail if the type from IGC is not explicitly
                // a 'main_object' (as these are non-API-accessible asset types in IGC like column analysis master,
                // etc and will simply result in 400-code Bad Request messages from the API)
                if (!reference.getType().equals("main_object")) {
                    EntityDetail ed = null;
                    try {
                        ed = getEntityDetail(userId, reference.getId());
                    } catch (EntityNotKnownException | EntityProxyOnlyException e) {
                        e.printStackTrace();
                    }
                    if (ed != null) {
                        entityDetails.add(ed);
                    }
                }
            }

        }

        return entityDetails;

    }

    /**
     * Return a list of entities whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
     * @param userId unique identifier for requesting user.
     * @param entityTypeGUID GUID of the type of entity to search for. Null means all types will
     *                       be searched (could be slow so not recommended).
     * @param searchCriteria String expression contained in any of the property values within the entities
     *                       of the supplied type.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus Must be null (status is not implemented for IGC).
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     *                                     (currently not implemented for IGC)
     * @param asOfTime Must be null (history not implemented for IGC).
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     *                           (currently not implemented for IGC)
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of entities matching the supplied criteria; null means no matching entities in the metadata
     * collection.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the sequencing property specified is not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public  List<EntityDetail> findEntitiesByPropertyValue(String                userId,
                                                           String                entityTypeGUID,
                                                           String                searchCriteria,
                                                           int                   fromEntityElement,
                                                           List<InstanceStatus>  limitResultsByStatus,
                                                           List<String>          limitResultsByClassification,
                                                           Date                  asOfTime,
                                                           String                sequencingProperty,
                                                           SequencingOrder       sequencingOrder,
                                                           int                   pageSize) throws InvalidParameterException,
            TypeErrorException,
            RepositoryErrorException,
            PropertyErrorException,
            PagingErrorException,
            FunctionNotSupportedException,
            UserNotAuthorizedException
    {
        final String  methodName = "findEntitiesByPropertyValue";
        final String  searchCriteriaParameterName = "searchCriteria";
        final String  typeGUIDParameter = "entityTypeGUID";
        final String  asOfTimeParameter = "asOfTime";
        final String  pageSizeParameter = "pageSize";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateSearchCriteria(repositoryName, searchCriteriaParameterName, searchCriteria, methodName);
        repositoryValidator.validateOptionalTypeGUID(repositoryName, typeGUIDParameter, entityTypeGUID, methodName);
        repositoryValidator.validateAsOfTime(repositoryName, asOfTimeParameter, asOfTime, methodName);
        repositoryValidator.validatePageSize(repositoryName, pageSizeParameter, pageSize, methodName);

        /*
         * Process operation
         */
        ArrayList<EntityDetail> entityDetails = new ArrayList<>();

        // Immediately throw unimplemented exception if trying to limit by status or retrieve historical view
        if (limitResultsByStatus != null || asOfTime != null) {
            OMRSErrorCode errorCode = OMRSErrorCode.METHOD_NOT_IMPLEMENTED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName,
                    this.getClass().getName(),
                    repositoryName);
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } else {

            // If possible (ie. provided), run the search only against only the single
            // asset type; in which case, pull the properties to search from its mapping
            EntityMappingSet.EntityMapping mapping = null;
            String[] properties = null;
            if (entityTypeGUID != null
                    && !entityTypeGUID.equals("")
                    && implementedEntities.isTypeDefMapped(entityTypeGUID)) {
                mapping = implementedEntities.getByTypeDefGUID(entityTypeGUID);
                Class mappingClass = mapping.getMappingClass();
                ReferenceableMapper referenceableMapper = ReferenceMapper.getMapper(
                        mappingClass,
                        (IGCOMRSRepositoryConnector)parentConnector,
                        userId
                );
                properties = referenceableMapper.getPropertyMappings().getIgcPropertyNames().toArray(new String[0]);
            } else {
                // Otherwise, since IGC requires the set of properties against which to search,
                // if no type has been provided we'll use the generic set of the following
                // properties for the search (common to all objects)
                properties = new String[] {
                        "name",
                        "short_description",
                        "long_description"
                };
            }

            String igcTypeToSearch = (mapping == null) ? "main_object" : mapping.getIgcAssetType();

            IGCSearchSorting igcSearchSorting = null;
            if (sequencingProperty == null && sequencingOrder != null) {
                igcSearchSorting = IGCSearchSorting.sortFromNonPropertySequencingOrder(sequencingOrder);
            }

            IGCSearchConditionSet igcSearchConditionSet = new IGCSearchConditionSet();
            igcSearchConditionSet.setMatchAnyCondition(true);
            for (String property : properties) {
                igcSearchConditionSet.addCondition(new IGCSearchCondition(
                        property,
                        "like %{0}%",
                        searchCriteria
                ));
            }
            IGCSearch igcSearch = new IGCSearch(igcTypeToSearch, new String[]{"name"}, igcSearchConditionSet);

            if (pageSize > 0) {
                // Only set pageSize if it has been provided; otherwise we'll end up defaulting to IGC's
                // minimal pageSize of 10 (so will need to make many calls to get all pages)
                igcSearch.setPageSize(pageSize);
            } else {
                // So if none has been specified, we'll set a large pageSize to be able to more efficiently
                // retrieve all pages of results
                igcSearch.setPageSize(parentConnector.getMaxPageSize());
            }

            igcSearch.setBeginAt(fromEntityElement);
            if (igcSearchSorting != null) {
                igcSearch.addSortingCriteria(igcSearchSorting);
            }
            ReferenceList results = this.igcRestClient.search(igcSearch);

            if (pageSize == 0) {
                // If the provided pageSize was 0, we need to retrieve ALL pages of
                // results...
                results.getAllPages(this.igcRestClient);
            }

            for (Reference reference : results.getItems()) {
                // Only proceed with retrieving the EntityDetail if the type from IGC is not explicitly
                // a 'main_object' (as these are non-API-accessible asset types in IGC like column analysis master,
                // etc and will simply result in 400-code Bad Request messages from the API)
                if (!reference.getType().equals("main_object")) {
                    EntityDetail ed = null;
                    try {
                        ed = getEntityDetail(userId, reference.getId());
                    } catch (EntityNotKnownException | EntityProxyOnlyException e) {
                        e.printStackTrace();
                    }
                    if (ed != null) {
                        entityDetails.add(ed);
                    }
                }
            }

        }

        return entityDetails;

    }

    /**
     * Return the header, classifications and properties of a specific entity.
     *
     * @param userId unique identifier for requesting user.
     * @param guid String unique identifier for the entity.
     * @return EntityDetail structure.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection.
     * @throws EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @Override
    public EntityDetail getEntityDetail(String userId,
                                        String guid) throws InvalidParameterException,
            RepositoryErrorException,
            EntityNotKnownException,
            EntityProxyOnlyException,
            UserNotAuthorizedException
    {
        final String  methodName        = "getEntityDetail";
        final String  guidParameterName = "guid";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);

        /*
         * Perform operation
         */

        // 1. retrieve entity from IGC by GUID (RID)
        Reference asset = this.igcRestClient.getAssetRefById(guid);
        EntityDetail detail = null;

        // If we could not find any asset by the provided guid, throw an ENTITY_NOT_KNOWN exception
        if (asset == null) {
            OMRSErrorCode errorCode = OMRSErrorCode.ENTITY_NOT_KNOWN;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(guid,
                    methodName,
                    repositoryName);
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } else if (asset.getType().equals("main_object")) {
            // If the asset type returned has an IGC-listed type of 'main_object', it isn't one that the REST API
            // of IGC supports (eg. a data rule detail object, a column analysis master object, etc)...
            // Trying to further process it will result in failed REST API requests; so we should skip these objects
            OMRSErrorCode errorCode = OMRSErrorCode.INVALID_ENTITY_FROM_STORE;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(guid,
                    repositoryName,
                    methodName,
                    asset.toString());
            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        } else {

            // Otherwise, retrieve the mapping dynamically based on the type of asset
            Class clazz = getMapperClass(asset.getType());

            try {

                Constructor constructor = clazz.getConstructor(Reference.class, IGCOMRSRepositoryConnector.class, String.class);
                ReferenceableMapper referenceMapper = (ReferenceableMapper) constructor.newInstance(
                        asset,
                        (IGCOMRSRepositoryConnector) parentConnector,
                        userId
                );

                // 2. Apply the mapping to the object, and retrieve the resulting EntityDetail
                detail = referenceMapper.getOMRSEntityDetail();

            } catch (ClassCastException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                // If we cannot find what we expect in the Mapper class, print a stack trace and raise error that it is not known
                e.printStackTrace();
                OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_NOT_KNOWN;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(asset.getType(),
                        null,
                        guid,
                        methodName,
                        repositoryName);
                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());
            }

        }

        return detail;

    }

    /**
     * Retrieves the class to use for mapping the provided IGC asset type to an OMRS entity.
     *
     * @param igcAssetType the name of the IGC asset type
     * @return Class
     */
    public Class getMapperClass(String igcAssetType) {
        Class mapperClass = implementedEntities.getMappingClassForAssetType(igcAssetType);
        if (mapperClass == null) {
            mapperClass = this.defaultMapper;
        }
        return mapperClass;
    }

    /**
     * Adds the provided value to the search criteria for IGC.
     *
     * @param igcSearchConditionSet
     * @param omrsPropertyName
     * @param igcProperties
     * @param propertyMappingSet
     * @param value
     */
    public void addSearchConditionFromValue(IGCSearchConditionSet igcSearchConditionSet,
                                            String omrsPropertyName,
                                            List<String> igcProperties,
                                            PropertyMappingSet propertyMappingSet,
                                            InstancePropertyValue value) {

        String igcPropertyName = propertyMappingSet.getIgcPropertyName(omrsPropertyName);
        if (igcPropertyName != null) {
            igcProperties.add(igcPropertyName);
            InstancePropertyCategory category = value.getInstancePropertyCategory();
            switch (category) {
                case PRIMITIVE:
                    PrimitivePropertyValue actualValue = (PrimitivePropertyValue) value;
                    PrimitiveDefCategory primitiveType = actualValue.getPrimitiveDefCategory();
                    switch (primitiveType) {
                        case OM_PRIMITIVE_TYPE_BOOLEAN:
                        case OM_PRIMITIVE_TYPE_BYTE:
                        case OM_PRIMITIVE_TYPE_CHAR:
                            igcSearchConditionSet.addCondition(new IGCSearchCondition(
                                    igcPropertyName,
                                    "=",
                                    actualValue.getPrimitiveValue().toString()
                            ));
                            break;
                        case OM_PRIMITIVE_TYPE_SHORT:
                        case OM_PRIMITIVE_TYPE_INT:
                        case OM_PRIMITIVE_TYPE_LONG:
                        case OM_PRIMITIVE_TYPE_FLOAT:
                        case OM_PRIMITIVE_TYPE_DOUBLE:
                        case OM_PRIMITIVE_TYPE_BIGINTEGER:
                        case OM_PRIMITIVE_TYPE_BIGDECIMAL:
                            igcSearchConditionSet.addCondition(new IGCSearchCondition(
                                    igcPropertyName,
                                    "=",
                                    actualValue.getPrimitiveValue().toString()
                            ));
                            break;
                        case OM_PRIMITIVE_TYPE_DATE:
                            Date date = (Date) actualValue.getPrimitiveValue();
                            igcSearchConditionSet.addCondition(new IGCSearchCondition(
                                    igcPropertyName,
                                    "=",
                                    "" + date.getTime()
                            ));
                            break;
                        case OM_PRIMITIVE_TYPE_STRING:
                        default:
                            igcSearchConditionSet.addCondition(new IGCSearchCondition(
                                    igcPropertyName,
                                    "like %{0}%",
                                    actualValue.getPrimitiveValue().toString()
                            ));
                            break;
                    }
                    break;
                case ENUM:
                    igcSearchConditionSet.addCondition(new IGCSearchCondition(
                            igcPropertyName,
                            "=",
                            ((EnumPropertyValue) value).getSymbolicName()
                    ));
                    break;
                case STRUCT:
                    Map<String, InstancePropertyValue> structValues = ((StructPropertyValue) value).getAttributes().getInstanceProperties();
                    for (String propertyName : structValues.keySet()) {
                        InstancePropertyValue nextValue = structValues.get(propertyName);
                        addSearchConditionFromValue(
                                igcSearchConditionSet,
                                propertyName,
                                igcProperties,
                                propertyMappingSet,
                                nextValue
                        );
                    }
                    break;
                case MAP:
                    Map<String, InstancePropertyValue> mapValues = ((MapPropertyValue) value).getMapValues().getInstanceProperties();
                    for (String propertyName : mapValues.keySet()) {
                        InstancePropertyValue nextValue = mapValues.get(propertyName);
                        addSearchConditionFromValue(
                                igcSearchConditionSet,
                                propertyName,
                                igcProperties,
                                propertyMappingSet,
                                nextValue
                        );
                    }
                    break;
                case ARRAY:
                    Map<String, InstancePropertyValue> arrayValues = ((ArrayPropertyValue) value).getArrayValues().getInstanceProperties();
                    for (String index : arrayValues.keySet()) {
                        InstancePropertyValue nextValue = arrayValues.get(index);
                        addSearchConditionFromValue(
                                igcSearchConditionSet,
                                igcPropertyName,
                                igcProperties,
                                propertyMappingSet,
                                nextValue
                        );
                    }
                    break;
            }
        }

    }

}
