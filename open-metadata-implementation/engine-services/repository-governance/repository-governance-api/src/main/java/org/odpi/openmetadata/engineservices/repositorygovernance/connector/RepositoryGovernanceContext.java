/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.repositorygovernance.connector;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CompletionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RequestSourceElement;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicRepositoryEventListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.HistorySequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefProperties;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * RepositoryGovernanceContext provides the archive service with access to information about
 * the archive request along with the open metadata repository interfaces.
 */
public abstract class RepositoryGovernanceContext
{
    protected String                   userId;

    protected String                   requestType;
    protected Map<String, String>      requestParameters;

    private List<RequestSourceElement> requestSourceElements;
    private List<ActionTargetElement>  actionTargetElements;


    /**
     * Constructor sets up the key parameters for processing the request to the governance action service.
     *
     * @param userId calling user
     * @param requestType unique identifier of the asset that the annotations should be attached to
     * @param requestParameters name-value properties to control the governance action service
     * @param requestSourceElements metadata elements associated with the request to the governance action service
     * @param actionTargetElements metadata elements that need to be worked on by the governance action service
     */
    public RepositoryGovernanceContext(String                             userId,
                                       String                             requestType,
                                       Map<String, String>                requestParameters,
                                       List<RequestSourceElement>         requestSourceElements,
                                       List<ActionTargetElement>          actionTargetElements)
    {
        this.userId = userId;
        this.requestType = requestType;
        this.requestParameters = requestParameters;
        this.requestSourceElements = requestSourceElements;
        this.actionTargetElements = actionTargetElements;
    }


    /**
     * Return the unique identifier of the asset being discovered.
     *
     * @return string guid
     */
    public String getRequestType()
    {
        return requestType;
    }


    /**
     * Return the properties that hold the parameters used to drive the governance action service's processing.
     *
     * @return property map
     */
    public Map<String, String> getRequestParameters()
    {
        return requestParameters;
    }


    /**
     * Return the list of metadata elements associated with the request to the governance action service.
     * This list will not change during the lifetime of the service.
     *
     * @return list of request source elements
     */
    public List<RequestSourceElement> getRequestSourceElements()
    {
        return requestSourceElements;
    }


    /**
     * Return the list of elements that this governance action service should work on.
     *
     * @return cached list of action target metadata elements
     */
    public List<ActionTargetElement> getActionTargetElements()
    {
        return actionTargetElements;
    }


    /**
     * Returns the identifier of the metadata repository.  This is the identifier used to register the
     * metadata repository with the metadata repository cohort.  It is also the identifier used to
     * identify the home repository of a metadata instance.
     *
     * @return String metadata collection id.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository.
     */
    public abstract String getMetadataCollectionId() throws RepositoryErrorException;


    /**
     * Register a listener to receive events about changes to metadata elements in the open metadata repositories.
     *
     * @param listener listener object to receive events
     *
     * @throws InvalidParameterException one or more of the type names are unrecognized
     * @throws UserNotAuthorizedException user is not authorized to register a listener
     * @throws RepositoryErrorException the metadata server is not available or in error
     * @throws ConnectionCheckedException the connection to the event topic is in error
     * @throws ConnectorCheckedException the connector to the event topic is in error
     */
    public abstract void registerListener(OMRSTopicRepositoryEventListener listener) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            RepositoryErrorException,
                                                                                            ConnectionCheckedException,
                                                                                            ConnectorCheckedException;

    /**
     * Disconnect from the topic connector - events will no longer be passed to the registered listeners.
     *
     * @throws ConnectorCheckedException problem with the topic connector
     */
    public abstract void disconnectFromEnterpriseTopic() throws ConnectorCheckedException;


    /**
     * Returns the list of different types of metadata organized into two groups.  The first are the
     * attribute type definitions (AttributeTypeDefs).  These provide types for properties in full
     * type definitions.  Full type definitions (TypeDefs) describe types for entities, relationships
     * and classifications.
     *
     * @return TypeDefGallery List of different categories of type definitions.
     * @throws InvalidParameterException the userId is null
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract TypeDefGallery getAllTypes() throws InvalidParameterException,
                                                        RepositoryErrorException,
                                                        UserNotAuthorizedException;


    /**
     * Returns a list of type definitions that have the specified name.  Type names should be unique.  This
     * method allows wildcard character to be included in the name.  These are * (asterisk) for an
     * arbitrary string of characters and ampersand for an arbitrary character.
     *
     * @param name   name of the TypeDefs to return (including wildcard characters).
     * @return TypeDefGallery list.
     * @throws InvalidParameterException  the name of the TypeDef is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract TypeDefGallery findTypesByName(String name) throws InvalidParameterException,
                                                                       RepositoryErrorException,
                                                                       UserNotAuthorizedException;


    /**
     * Returns all the TypeDefs for a specific category.
     *
     * @param category enum value for the category of TypeDef to return.
     * @return TypeDefs list.
     * @throws InvalidParameterException  the TypeDefCategory is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract List<TypeDef> findTypeDefsByCategory(TypeDefCategory category) throws InvalidParameterException,
                                                                                          RepositoryErrorException,
                                                                                          UserNotAuthorizedException;




    /**
     * Returns all the AttributeTypeDefs for a specific category.
     *
     * @param category enum value for the category of an AttributeTypeDef to return.
     * @return TypeDefs list.
     * @throws InvalidParameterException  the TypeDefCategory is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract List<AttributeTypeDef> findAttributeTypeDefsByCategory(AttributeTypeDefCategory category) throws InvalidParameterException,
                                                                                                                     RepositoryErrorException,
                                                                                                                     UserNotAuthorizedException;



    /**
     * Return the TypeDefs that have the properties matching the supplied match criteria.
     *
     * @param matchCriteria TypeDefProperties a list of property names.
     * @return TypeDefs list.
     * @throws InvalidParameterException  the matchCriteria is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract List<TypeDef> findTypeDefsByProperty(TypeDefProperties matchCriteria) throws InvalidParameterException,
                                                                                                 RepositoryErrorException,
                                                                                                 UserNotAuthorizedException;


    /**
     * Return the types that are linked to the elements from the specified standard.
     *
     * @param standard     name of the standard null means any.
     * @param organization name of the organization null means any.
     * @param identifier   identifier of the element in the standard null means any.
     * @return TypeDefs list each entry in the list contains a typedef.  This is is a structure
     * describing the TypeDef's category and properties.
     * @throws InvalidParameterException  all attributes of the external id are null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract List<TypeDef> findTypesByExternalID(String standard,
                                                        String organization,
                                                        String identifier) throws InvalidParameterException,
                                                                                  RepositoryErrorException,
                                                                                  UserNotAuthorizedException;


    /**
     * Return the TypeDefs that match the search criteria.
     *
     * @param searchCriteria String search criteria.
     * @return TypeDefs list each entry in the list contains a typedef.  This is is a structure
     * describing the TypeDef's category and properties.
     * @throws InvalidParameterException  the searchCriteria is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract List<TypeDef> searchForTypeDefs(String searchCriteria) throws InvalidParameterException,
                                                                                  RepositoryErrorException,
                                                                                  UserNotAuthorizedException;


    /**
     * Return the TypeDef identified by the GUID.
     *
     * @param guid   String unique id of the TypeDef.
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException  the guid is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeDefNotKnownException   The requested TypeDef is not known in the metadata collection.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract TypeDef getTypeDefByGUID(String guid) throws InvalidParameterException,
                                                                 RepositoryErrorException,
                                                                 TypeDefNotKnownException,
                                                                 UserNotAuthorizedException;



    /**
     * Return the AttributeTypeDef identified by the GUID.
     *
     * @param guid   String unique id of the TypeDef
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException  the guid is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeDefNotKnownException   The requested TypeDef is not known in the metadata collection.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract AttributeTypeDef getAttributeTypeDefByGUID(String guid) throws InvalidParameterException,
                                                                                   RepositoryErrorException,
                                                                                   TypeDefNotKnownException,
                                                                                   UserNotAuthorizedException;


    /**
     * Return the TypeDef identified by the unique name.
     *
     * @param name   String name of the TypeDef.
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException  the name is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeDefNotKnownException   the requested TypeDef is not found in the metadata collection.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract TypeDef getTypeDefByName(String name) throws InvalidParameterException,
                                                                 RepositoryErrorException,
                                                                 TypeDefNotKnownException,
                                                                 UserNotAuthorizedException;



    /**
     * Return the AttributeTypeDef identified by the unique name.
     *
     * @param name   String name of the TypeDef.
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException  the name is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws TypeDefNotKnownException   the requested TypeDef is not found in the metadata collection.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract AttributeTypeDef getAttributeTypeDefByName(String name) throws InvalidParameterException,
                                                                                   RepositoryErrorException,
                                                                                   TypeDefNotKnownException,
                                                                                   UserNotAuthorizedException;




    /**
     * Returns the entity if the entity is stored in the metadata collection, otherwise null.
     *
     * @param guid   String unique identifier for the entity
     * @return the entity details if the entity is found in the metadata collection; otherwise return null
     * @throws InvalidParameterException  the guid is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract EntityDetail isEntityKnown(String guid) throws InvalidParameterException,
                                                                   RepositoryErrorException,
                                                                   UserNotAuthorizedException;



    /**
     * Return the header and classifications for a specific entity.  The returned entity summary may be from
     * a full entity object or an entity proxy.
     *
     * @param guid   String unique identifier for the entity
     * @return EntitySummary structure
     * @throws InvalidParameterException  the guid is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException    the requested entity instance is not known in the metadata collection.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract EntitySummary getEntitySummary(String guid) throws InvalidParameterException,
                                                                       RepositoryErrorException,
                                                                       EntityNotKnownException,
                                                                       UserNotAuthorizedException;



    /**
     * Return the header, classifications and properties of a specific entity.
     *
     * @param guid   String unique identifier for the entity.
     * @return EntityDetail structure.
     * @throws InvalidParameterException  the guid is null.
     * @throws RepositoryErrorException   there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws EntityNotKnownException    the requested entity instance is not known in the metadata collection.
     * @throws EntityProxyOnlyException   the requested entity instance is only a proxy in the metadata collection.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract EntityDetail getEntityDetail(String guid) throws InvalidParameterException,
                                                                     RepositoryErrorException,
                                                                     EntityNotKnownException,
                                                                     EntityProxyOnlyException,
                                                                     UserNotAuthorizedException;


    /**
     * Return a historical version of an entity includes the header, classifications and properties of the entity.
     *
     * @param guid     String unique identifier for the entity.
     * @param asOfTime the time used to determine which version of the entity that is desired.
     * @return EntityDetail structure.
     * @throws InvalidParameterException     the guid or date is null, or the asOfTime property is for a future time
     * @throws RepositoryErrorException      there is a problem communicating with the metadata repository where
     *                                       the metadata collection is stored.
     * @throws EntityNotKnownException       the requested entity instance is not known in the metadata collection
     *                                       at the time requested.
     * @throws EntityProxyOnlyException      the requested entity instance is only a proxy in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException    the user is not permitted to perform this operation.
     */
    public abstract EntityDetail getEntityDetail(String guid,
                                                 Date   asOfTime) throws InvalidParameterException,
                                                                         RepositoryErrorException,
                                                                         EntityNotKnownException,
                                                                         EntityProxyOnlyException,
                                                                         FunctionNotSupportedException,
                                                                         UserNotAuthorizedException;



    /**
     * Return all historical versions of an entity within the bounds of the provided timestamps. To retrieve all historical
     * versions of an entity, set both the 'fromTime' and 'toTime' to null.
     *
     * @param guid String unique identifier for the entity.
     * @param fromTime the earliest point in time from which to retrieve historical versions of the entity (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the entity (exclusive)
     * @param startFromElement the starting element number of the historical versions to return. This is used when retrieving
     *                         versions beyond the first page of results. Zero means start from the first element.
     * @param pageSize the maximum number of result versions that can be returned on this request. Zero means unrestricted
     *                 return results size.
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @return {@code List<EntityDetail>} of each historical version of the entity detail within the bounds, and in the order requested.
     * @throws InvalidParameterException the guid or date is null or fromTime is after the toTime
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws EntityNotKnownException the requested entity instance is not known in the metadata collection
     *                                   at the time requested.
     * @throws EntityProxyOnlyException the requested entity instance is only a proxy in the metadata collection.
     * @throws FunctionNotSupportedException the repository does not support history.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract List<EntityDetail> getEntityDetailHistory(String                 guid,
                                                              Date                   fromTime,
                                                              Date                   toTime,
                                                              int                    startFromElement,
                                                              int                    pageSize,
                                                              HistorySequencingOrder sequencingOrder) throws InvalidParameterException,
                                                                                                             RepositoryErrorException,
                                                                                                             EntityNotKnownException,
                                                                                                             EntityProxyOnlyException,
                                                                                                             FunctionNotSupportedException,
                                                                                                             UserNotAuthorizedException;


    /**
     * Return the relationships for a specific entity.
     *
     * @param entityGUID              String unique identifier for the entity.
     * @param relationshipTypeGUID    String GUID of the type of relationship required (null for all).
     * @param fromRelationshipElement the starting element number of the relationships to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus    By default, relationships in all statuses are returned.  However, it is possible
     *                                to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                                status values.
     * @param asOfTime                Requests a historical query of the relationships for the entity.  Null means return the
     *                                present values.
     * @param sequencingProperty      String name of the property that is to be used to sequence the results.
     *                                Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder         Enum defining how the results should be ordered.
     * @param pageSize                -- the maximum number of result classifications that can be returned on this request.  Zero means
     *                                unrestricted return results size.
     * @return Relationships list.  Null means no relationships associated with the entity.
     * @throws InvalidParameterException     a parameter is invalid or null.
     * @throws TypeErrorException            the type guid passed on the request is not known by the
     *                                       metadata collection.
     * @throws RepositoryErrorException      there is a problem communicating with the metadata repository where
     *                                       the metadata collection is stored.
     * @throws EntityNotKnownException       the requested entity instance is not known in the metadata collection.
     * @throws PropertyErrorException        the sequencing property is not valid for the attached classifications.
     * @throws PagingErrorException          the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException    the user is not permitted to perform this operation.
     */
    public abstract List<Relationship> getRelationshipsForEntity(String               entityGUID,
                                                                 String               relationshipTypeGUID,
                                                                 int                  fromRelationshipElement,
                                                                 List<InstanceStatus> limitResultsByStatus,
                                                                 Date                 asOfTime,
                                                                 String               sequencingProperty,
                                                                 SequencingOrder      sequencingOrder,
                                                                 int                  pageSize) throws InvalidParameterException,
                                                                                                       TypeErrorException,
                                                                                                       RepositoryErrorException,
                                                                                                       EntityNotKnownException,
                                                                                                       PropertyErrorException,
                                                                                                       PagingErrorException,
                                                                                                       FunctionNotSupportedException,
                                                                                                       UserNotAuthorizedException;


    /**
     * Return a list of entities that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param entityTypeGUID String unique identifier for the entity type of interest (null means any entity type).
     * @param entitySubtypeGUIDs optional list of the unique identifiers (guids) for subtypes of the entityTypeGUID to
     *                           include in the search results. Null means all subtypes.
     * @param matchProperties Optional list of entity property conditions to match.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param matchClassifications Optional list of entity classifications to match.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
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
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support this optional method.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract List<EntityDetail> findEntities(String                    entityTypeGUID,
                                                    List<String>              entitySubtypeGUIDs,
                                                    SearchProperties          matchProperties,
                                                    int                       fromEntityElement,
                                                    List<InstanceStatus>      limitResultsByStatus,
                                                    SearchClassifications     matchClassifications,
                                                    Date                      asOfTime,
                                                    String                    sequencingProperty,
                                                    SequencingOrder           sequencingOrder,
                                                    int                       pageSize) throws InvalidParameterException,
                                                                                               RepositoryErrorException,
                                                                                               TypeErrorException,
                                                                                               PropertyErrorException,
                                                                                               PagingErrorException,
                                                                                               FunctionNotSupportedException,
                                                                                               UserNotAuthorizedException;


    /**
     * Return a list of entities that match the supplied properties according to the match criteria.  The results
     * can be returned over many pages.
     *
     * @param entityTypeGUID String unique identifier for the entity type of interest (null means any entity type).
     * @param matchProperties Optional list of entity properties to match (where any String property's value should
     *                        be defined as a Java regular expression, even if it should be an exact match).
     * @param matchCriteria Enum defining how the match properties should be matched to the entities in the repository.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
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
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support one of the provided parameters.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract List<EntityDetail> findEntitiesByProperty(String                    entityTypeGUID,
                                                              InstanceProperties        matchProperties,
                                                              MatchCriteria             matchCriteria,
                                                              int                       fromEntityElement,
                                                              List<InstanceStatus>      limitResultsByStatus,
                                                              List<String>              limitResultsByClassification,
                                                              Date                      asOfTime,
                                                              String                    sequencingProperty,
                                                              SequencingOrder           sequencingOrder,
                                                              int                       pageSize) throws InvalidParameterException,
                                                                                                         RepositoryErrorException,
                                                                                                         TypeErrorException,
                                                                                                         PropertyErrorException,
                                                                                                         PagingErrorException,
                                                                                                         FunctionNotSupportedException,
                                                                                                         UserNotAuthorizedException;


    /**
     * Return a list of entities that have the requested type of classifications attached.
     *
     * @param entityTypeGUID unique identifier for the type of entity requested.  Null means any type of entity
     *                       (but could be slow so not recommended.
     * @param classificationName name of the classification, note a null is not valid.
     * @param matchClassificationProperties list of classification properties used to narrow the search (where any String
     *                                      property's value should be defined as a Java regular expression, even if it
     *                                      should be an exact match).
     * @param matchCriteria Enum defining how the match properties should be matched to the classifications in the repository.
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
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
     * @throws ClassificationErrorException the classification request is not known to the metadata collection.
     * @throws PropertyErrorException the properties specified are not valid for the requested type of
     *                                  classification.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support one of the provided parameters.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract List<EntityDetail> findEntitiesByClassification(String                    entityTypeGUID,
                                                                    String                    classificationName,
                                                                    InstanceProperties        matchClassificationProperties,
                                                                    MatchCriteria             matchCriteria,
                                                                    int                       fromEntityElement,
                                                                    List<InstanceStatus>      limitResultsByStatus,
                                                                    Date                      asOfTime,
                                                                    String                    sequencingProperty,
                                                                    SequencingOrder           sequencingOrder,
                                                                    int                       pageSize) throws InvalidParameterException,
                                                                                                               TypeErrorException,
                                                                                                               RepositoryErrorException,
                                                                                                               ClassificationErrorException,
                                                                                                               PropertyErrorException,
                                                                                                               PagingErrorException,
                                                                                                               FunctionNotSupportedException,
                                                                                                               UserNotAuthorizedException;



    /**
     * Return a list of entities whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
     * @param entityTypeGUID GUID of the type of entity to search for. Null means all types will
     *                       be searched (could be slow so not recommended).
     * @param searchCriteria String Java regular expression used to match against any of the String property values
     *                       within the entities of the supplied type, even if it should be an exact match.
     *                       (Retrieve all entities of the supplied type if this is either null or an empty string.)
     * @param fromEntityElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param limitResultsByClassification List of classifications that must be present on all returned entities.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of entities matching the supplied criteria null means no matching entities in the metadata
     * collection.
     * @throws InvalidParameterException a parameter is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the sequencing property specified is not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support one of the provided parameters.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract List<EntityDetail> findEntitiesByPropertyValue(String                entityTypeGUID,
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
                                                                                                          UserNotAuthorizedException;


    /**
     * Returns a boolean indicating if the relationship is stored in the metadata collection.
     *
     * @param guid String unique identifier for the relationship.
     * @return relationship details if the relationship is found in the metadata collection; otherwise return null.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract Relationship isRelationshipKnown(String guid) throws InvalidParameterException,
                                                                         RepositoryErrorException,
                                                                         UserNotAuthorizedException;


    /**
     * Return a requested relationship.
     *
     * @param guid String unique identifier for the relationship.
     * @return a relationship structure.
     * @throws InvalidParameterException the guid is null.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws RelationshipNotKnownException the metadata collection does not have a relationship with
     *                                         the requested GUID stored.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract Relationship getRelationship(String guid) throws InvalidParameterException,
                                                                     RepositoryErrorException,
                                                                     RelationshipNotKnownException,
                                                                     UserNotAuthorizedException;


    /**
     * Return a historical version of a relationship.
     *
     * @param guid String unique identifier for the relationship.
     * @param asOfTime the time used to determine which version of the entity that is desired.
     * @return Relationship structure.
     * @throws InvalidParameterException the guid or date is null or the asOfTime property is for a future time.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested entity instance is not known in the metadata collection
     *                                   at the time requested.
     * @throws FunctionNotSupportedException the repository does not support the asOfTime parameter.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract Relationship getRelationship(String    guid,
                                                 Date      asOfTime) throws InvalidParameterException,
                                                                            RepositoryErrorException,
                                                                            RelationshipNotKnownException,
                                                                            FunctionNotSupportedException,
                                                                            UserNotAuthorizedException;


    /**
     * Return all historical versions of a relationship within the bounds of the provided timestamps. To retrieve all
     * historical versions of a relationship, set both the 'fromTime' and 'toTime' to null.
     *
     * @param guid String unique identifier for the entity.
     * @param fromTime the earliest point in time from which to retrieve historical versions of the entity (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the entity (exclusive)
     * @param startFromElement the starting element number of the historical versions to return. This is used when retrieving
     *                         versions beyond the first page of results. Zero means start from the first element.
     * @param pageSize the maximum number of result versions that can be returned on this request. Zero means unrestricted
     *                 return results size.
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @return {@code List<Relationship>} of each historical version of the relationship within the bounds, and in the order requested.
     * @throws InvalidParameterException the guid or date is null or fromTime is after the toTime
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                 the metadata collection is stored.
     * @throws RelationshipNotKnownException the requested relationship instance is not known in the metadata collection
     *                                       at the time requested.
     * @throws FunctionNotSupportedException the repository does not support history.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract List<Relationship> getRelationshipHistory(String                 guid,
                                                              Date                   fromTime,
                                                              Date                   toTime,
                                                              int                    startFromElement,
                                                              int                    pageSize,
                                                              HistorySequencingOrder sequencingOrder) throws InvalidParameterException,
                                                                                                             RepositoryErrorException,
                                                                                                             RelationshipNotKnownException,
                                                                                                             FunctionNotSupportedException,
                                                                                                             UserNotAuthorizedException;


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of
     * pages.
     *
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param relationshipSubtypeGUIDs optional list of the unique identifiers (guids) for subtypes of the
     *                                 relationshipTypeGUID to include in the search results. Null means all subtypes.
     * @param matchProperties Optional list of relationship property conditions to match.
     * @param fromRelationshipElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result relationships that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  relationships.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support one of the provided parameters.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract List<Relationship> findRelationships(String                    relationshipTypeGUID,
                                                         List<String>              relationshipSubtypeGUIDs,
                                                         SearchProperties matchProperties,
                                                         int                       fromRelationshipElement,
                                                         List<InstanceStatus>      limitResultsByStatus,
                                                         Date                      asOfTime,
                                                         String                    sequencingProperty,
                                                         SequencingOrder           sequencingOrder,
                                                         int                       pageSize) throws InvalidParameterException,
                                                                                                    TypeErrorException,
                                                                                                    RepositoryErrorException,
                                                                                                    PropertyErrorException,
                                                                                                    PagingErrorException,
                                                                                                    FunctionNotSupportedException,
                                                                                                    UserNotAuthorizedException;


    /**
     * Return a list of relationships that match the requested properties by the matching criteria.   The results
     * can be received as a series of pages.
     *
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.  Null means all types
     *                             (but may be slow so not recommended).
     * @param matchProperties Optional list of relationship properties to match (where any String property's value should
     *                        be defined as a Java regular expression, even if it should be an exact match).
     * @param matchCriteria Enum defining how the properties should be matched to the relationships in the repository.
     * @param fromRelationshipElement the starting element number of the entities to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result relationships that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the
     *                              metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws PropertyErrorException the properties specified are not valid for any of the requested types of
     *                                  relationships.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support one of the provided parameters.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract List<Relationship> findRelationshipsByProperty(String                    relationshipTypeGUID,
                                                                   InstanceProperties        matchProperties,
                                                                   MatchCriteria             matchCriteria,
                                                                   int                       fromRelationshipElement,
                                                                   List<InstanceStatus>      limitResultsByStatus,
                                                                   Date                      asOfTime,
                                                                   String                    sequencingProperty,
                                                                   SequencingOrder           sequencingOrder,
                                                                   int                       pageSize) throws InvalidParameterException,
                                                                                                              TypeErrorException,
                                                                                                              RepositoryErrorException,
                                                                                                              PropertyErrorException,
                                                                                                              PagingErrorException,
                                                                                                              FunctionNotSupportedException,
                                                                                                              UserNotAuthorizedException;


    /**
     * Return a list of relationships whose string based property values match the search criteria.  The
     * search criteria may include regex style wild cards.
     *
     * @param relationshipTypeGUID GUID of the type of entity to search for. Null means all types will
     *                       be searched (could be slow so not recommended).
     * @param searchCriteria String Java regular expression used to match against any of the String property values
     *                       within the relationships of the supplied type, even if it should be an exact match.
     * @param fromRelationshipElement Element number of the results to skip to when building the results list
     *                                to return.  Zero means begin at the start of the results.  This is used
     *                                to retrieve the results over a number of pages.
     * @param limitResultsByStatus By default, relationships in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the relationships for the entity.  Null means return the
     *                 present values.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize the maximum number of result relationships that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return a list of relationships.  Null means no matching relationships.
     * @throws InvalidParameterException one of the parameters is invalid or null.
     * @throws TypeErrorException the type guid passed on the request is not known by the metadata collection.
     * @throws RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws PropertyErrorException there is a problem with one of the other parameters.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     * @throws FunctionNotSupportedException the repository does not support one of the provided parameters.
     * @throws UserNotAuthorizedException the user is not permitted to perform this operation.
     */
    public abstract List<Relationship> findRelationshipsByPropertyValue(String                    relationshipTypeGUID,
                                                                        String                    searchCriteria,
                                                                        int                       fromRelationshipElement,
                                                                        List<InstanceStatus>      limitResultsByStatus,
                                                                        Date                      asOfTime,
                                                                        String                    sequencingProperty,
                                                                        SequencingOrder           sequencingOrder,
                                                                        int                       pageSize) throws InvalidParameterException,
                                                                                                                   TypeErrorException,
                                                                                                                   RepositoryErrorException,
                                                                                                                   PropertyErrorException,
                                                                                                                   PagingErrorException,
                                                                                                                   FunctionNotSupportedException,
                                                                                                                   UserNotAuthorizedException;


    /**
     * Declare that all the processing for the governance action service is finished and the status of the work.
     *
     * @param status completion status enum value
     * @param outputGuards optional guard strings for triggering subsequent action(s)
     * @param newRequestParameters additional request parameters.  These override/augment any request parameters defined for the next invoked service
     * @param newActionTargets list of action target names to GUIDs for the resulting governance action service
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance
     *                                     action service completion status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    public abstract  void recordCompletionStatus(CompletionStatus      status,
                                                 List<String>          outputGuards,
                                                 Map<String, String>   newRequestParameters,
                                                 List<NewActionTarget> newActionTargets) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException;


    /**
     * Return any completion status from the governance action service.
     *
     * @return completion status enum
     */
    public abstract CompletionStatus getCompletionStatus();




    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RepositoryGovernanceContext{" +
                       "userId='" + userId + '\'' +
                       ", requestType='" + requestType + '\'' +
                       ", requestParameters=" + requestParameters +
                       ", requestSourceElements=" + requestSourceElements +
                       ", actionTargetElements=" + actionTargetElements +
                       '}';
    }
}
