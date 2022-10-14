/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * EventTypeHandler provides the exchange of metadata about EventType schema types between the repository and the OMAS.
 *
 * @param <B> class that represents the event type
 */
public class EventTypeHandler<B> extends ReferenceableHandler<B>
{
    /**
     * Construct the handler with information needed to work with B objects.
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName name of this service
     * @param serverName name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve B instances from
     * @param defaultZones list of zones that the access service should set in all new B instances
     * @param publishZones list of zones that the access service sets up in published B instances
     * @param auditLog destination for audit log events
     */
    public EventTypeHandler(OpenMetadataAPIGenericConverter<B> converter,
                            Class<B>                           beanClass,
                            String                             serviceName,
                            String                             serverName,
                            InvalidParameterHandler            invalidParameterHandler,
                            RepositoryHandler                  repositoryHandler,
                            OMRSRepositoryHelper               repositoryHelper,
                            String                             localServerUserId,
                            OpenMetadataServerSecurityVerifier securityVerifier,
                            List<String>                       supportedZones,
                            List<String>                       defaultZones,
                            List<String>                       publishZones,
                            AuditLog                           auditLog)
    {
        super(converter,
              beanClass,
              serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog);
    }


    /**
     * Create the event type object.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param topicGUID unique identifier of the owning topic
     * @param topicGUIDParameterName parameter supplying topicGUID
     * @param qualifiedName unique name for the event type - used in other configuration
     * @param displayName short display name for the event type
     * @param description description of the event type
     * @param versionNumber version of the schema type.
     * @param isDeprecated is the schema type deprecated
     * @param author name of the author
     * @param usage guidance on how the schema should be used.
     * @param encodingStandard format of the schema
     * @param namespace namespace where the schema is defined.
     * @param additionalProperties additional properties for an event type
     * @param suppliedTypeName type name from the caller (enables creation of subtypes)
     * @param extendedProperties  properties for an event type subtype
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new event type object
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public String createEventType(String              userId,
                                  String              externalSourceGUID,
                                  String              externalSourceName,
                                  String              topicGUID,
                                  String              topicGUIDParameterName,
                                  String              qualifiedName,
                                  String              displayName,
                                  String              description,
                                  String              versionNumber,
                                  boolean             isDeprecated,
                                  String              author,
                                  String              usage,
                                  String              encodingStandard,
                                  String              namespace,
                                  Map<String, String> additionalProperties,
                                  String              suppliedTypeName,
                                  Map<String, Object> extendedProperties,
                                  Date                effectiveFrom,
                                  Date                effectiveTo,
                                  boolean             forLineage,
                                  boolean             forDuplicateProcessing,
                                  Date                effectiveTime,
                                  String              methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(topicGUID, topicGUIDParameterName, methodName);

        String typeName = OpenMetadataAPIMapper.EVENT_TYPE_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.EVENT_TYPE_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        final String eventTypeListGUIDParameterName = "eventTypeListGUID";

        String eventTypeListGUID = this.getEventTypeListGUID(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             topicGUID,
                                                             topicGUIDParameterName,
                                                             qualifiedName,
                                                             effectiveFrom,
                                                             effectiveTo,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveFrom,
                                                             methodName);

        SchemaTypeBuilder builder = new SchemaTypeBuilder(qualifiedName,
                                                          displayName,
                                                          description,
                                                          versionNumber,
                                                          isDeprecated,
                                                          author,
                                                          usage,
                                                          encodingStandard,
                                                          namespace,
                                                          additionalProperties,
                                                          typeGUID,
                                                          typeName,
                                                          extendedProperties,
                                                          repositoryHelper,
                                                          serviceName,
                                                          serverName);

        builder.setAnchors(userId, topicGUID, methodName);

        String eventTypeGUID = this.createBeanInRepository(userId,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           typeGUID,
                                                           typeName,
                                                           builder,
                                                           effectiveTime,
                                                           methodName);

        if (eventTypeGUID != null)
        {
            /*
             * Link the event type to the topic's event list.
             */
            final String eventTypeGUIDParameterName = "eventTypeGUID";

            this.uncheckedLinkElementToElement(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               eventTypeListGUID,
                                               eventTypeListGUIDParameterName,
                                               OpenMetadataAPIMapper.EVENT_TYPE_LIST_TYPE_NAME,
                                               eventTypeGUID,
                                               eventTypeGUIDParameterName,
                                               OpenMetadataAPIMapper.EVENT_TYPE_TYPE_NAME,
                                               forLineage,
                                               forDuplicateProcessing,
                                               supportedZones,
                                               OpenMetadataAPIMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_GUID,
                                               OpenMetadataAPIMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_NAME,
                                               null,
                                               effectiveFrom,
                                               methodName);
        }

        return eventTypeGUID;
    }


    /**
     * Create an event type from a template.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param topicGUID unique identifier of the owning topic
     * @param topicGUIDParameterName parameter supplying topicGUID
     * @param templateGUID unique identifier of the metadata element to copy
     * @param qualifiedName unique name for the event type - used in other configuration
     * @param displayName short display name for the event type
     * @param description description of the event type
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEventTypeFromTemplate(String  userId,
                                              String  externalSourceGUID,
                                              String  externalSourceName,
                                              String  topicGUID,
                                              String  topicGUIDParameterName,
                                              String  templateGUID,
                                              String  qualifiedName,
                                              String  displayName,
                                              String  description,
                                              Date    effectiveFrom,
                                              Date    effectiveTo,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing,
                                              Date    effectiveTime,
                                              String  methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String templateGUIDParameterName   = "templateGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        final String eventTypeListGUIDParameterName = "eventTypeListGUID";

        String eventTypeListGUID = this.getEventTypeListGUID(userId,
                                                             externalSourceGUID,
                                                             externalSourceName,
                                                             topicGUID,
                                                             topicGUIDParameterName,
                                                             qualifiedName,
                                                             effectiveFrom,
                                                             effectiveTo,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveFrom,
                                                             methodName);

        SchemaTypeBuilder builder = new SchemaTypeBuilder(qualifiedName,
                                                          displayName,
                                                          description,
                                                          repositoryHelper,
                                                          serviceName,
                                                          serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        String eventTypeGUID = this.createBeanFromTemplate(userId,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           templateGUID,
                                                           templateGUIDParameterName,
                                                           OpenMetadataAPIMapper.EVENT_TYPE_TYPE_GUID,
                                                           OpenMetadataAPIMapper.EVENT_TYPE_TYPE_NAME,
                                                           qualifiedName,
                                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                           builder,
                                                           supportedZones,
                                                           methodName);

        /*
         * Link the event type to the topic's event list.
         */
        final String eventTypeGUIDParameterName = "eventTypeGUID";

        this.uncheckedLinkElementToElement(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           eventTypeListGUID,
                                           eventTypeListGUIDParameterName,
                                           OpenMetadataAPIMapper.EVENT_TYPE_LIST_TYPE_NAME,
                                           eventTypeGUID,
                                           eventTypeGUIDParameterName,
                                           OpenMetadataAPIMapper.EVENT_TYPE_TYPE_NAME,
                                           forLineage,
                                           forDuplicateProcessing,
                                           supportedZones,
                                           OpenMetadataAPIMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_GUID,
                                           OpenMetadataAPIMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_NAME,
                                           null,
                                           effectiveTime,
                                           methodName);

        return eventTypeGUID;
    }


    /**
     * Update the event type.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param eventTypeGUID unique identifier for the event type to update
     * @param eventTypeGUIDParameterName parameter supplying the event type
     * @param qualifiedName unique name for the event type - used in other configuration
     * @param displayName short display name for the event type
     * @param description description of the governance event type
     * @param versionNumber version of the schema type.
     * @param isDeprecated is the schema type deprecated
     * @param author name of the author
     * @param usage guidance on how the schema should be used.
     * @param encodingStandard format of the schema.
     * @param namespace namespace where the schema is defined.
     * @param additionalProperties additional properties for an event type
     * @param suppliedTypeName type of term
     * @param extendedProperties  properties for a governance event type subtype
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void   updateEventType(String              userId,
                                  String              externalSourceGUID,
                                  String              externalSourceName,
                                  String              eventTypeGUID,
                                  String              eventTypeGUIDParameterName,
                                  String              qualifiedName,
                                  String              displayName,
                                  String              description,
                                  String              versionNumber,
                                  boolean             isDeprecated,
                                  String              author,
                                  String              usage,
                                  String              encodingStandard,
                                  String              namespace,
                                  Map<String, String> additionalProperties,
                                  String              suppliedTypeName,
                                  Map<String, Object> extendedProperties,
                                  Date                effectiveFrom,
                                  Date                effectiveTo,
                                  boolean             isMergeUpdate,
                                  boolean             forLineage,
                                  boolean             forDuplicateProcessing,
                                  Date                effectiveTime,
                                  String              methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String qualifiedNameParameterName = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(eventTypeGUID, eventTypeGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataAPIMapper.EVENT_TYPE_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.EVENT_TYPE_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        SchemaTypeBuilder builder = new SchemaTypeBuilder(qualifiedName,
                                                          displayName,
                                                          description,
                                                          versionNumber,
                                                          isDeprecated,
                                                          author,
                                                          usage,
                                                          encodingStandard,
                                                          namespace,
                                                          additionalProperties,
                                                          typeGUID,
                                                          typeName,
                                                          extendedProperties,
                                                          repositoryHelper,
                                                          serviceName,
                                                          serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    eventTypeGUID,
                                    eventTypeGUIDParameterName,
                                    typeGUID,
                                    typeName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Remove the metadata element representing an event types.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param eventTypeGUID unique identifier of the metadata element to remove
     * @param eventTypeGUIDParameterName parameter for eventTypeGUID
     * @param qualifiedName validating property
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeEventType(String  userId,
                                String  externalSourceGUID,
                                String  externalSourceName,
                                String  eventTypeGUID,
                                String  eventTypeGUIDParameterName,
                                String  qualifiedName,
                                boolean forLineage,
                                boolean forDuplicateProcessing,
                                Date    effectiveTime,
                                String  methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    eventTypeGUID,
                                    eventTypeGUIDParameterName,
                                    OpenMetadataAPIMapper.EVENT_TYPE_TYPE_GUID,
                                    OpenMetadataAPIMapper.EVENT_TYPE_TYPE_NAME,
                                    OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                    qualifiedName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the list of event types metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param searchStringParameterName name of parameter supplying the search string
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> findEventTypes(String  userId,
                                  String  searchString,
                                  String  searchStringParameterName,
                                  int     startFrom,
                                  int     pageSize,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing,
                                  Date    effectiveTime,
                                  String  methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        return this.findBeans(userId,
                              searchString,
                              searchStringParameterName,
                              OpenMetadataAPIMapper.EVENT_TYPE_TYPE_GUID,
                              OpenMetadataAPIMapper.EVENT_TYPE_TYPE_NAME,
                              null,
                              startFrom,
                              pageSize,
                              forLineage,
                              forDuplicateProcessing,
                              effectiveTime,
                              methodName);
    }



    /**
     * Return the list of the event types defined in an event set (collection).
     *
     * @param userId calling user
     * @param eventSetGUID unique identifier of the event set to query
     * @param eventSetGUIDParameterName name of the parameter supplying eventSetGUID
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of metadata elements describing the event types associated with the requested event set
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>   getEventTypesForEventSet(String  userId,
                                              String  eventSetGUID,
                                              String  eventSetGUIDParameterName,
                                              int     startFrom,
                                              int     pageSize,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing,
                                              Date    effectiveTime,
                                              String  methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        return this.getAttachedElements(userId,
                                        eventSetGUID,
                                        eventSetGUIDParameterName,
                                        OpenMetadataAPIMapper.EVENT_SET_TYPE_NAME,
                                        OpenMetadataAPIMapper.COLLECTION_MEMBERSHIP_TYPE_GUID,
                                        OpenMetadataAPIMapper.COLLECTION_MEMBERSHIP_TYPE_NAME,
                                        OpenMetadataAPIMapper.EVENT_TYPE_TYPE_NAME,
                                        null,
                                        null,
                                        0,
                                        forLineage,
                                        forDuplicateProcessing,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Retrieve the list of event type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param nameParameterName parameter supplying name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B>   getEventTypesByName(String  userId,
                                         String  name,
                                         String  nameParameterName,
                                         int     startFrom,
                                         int     pageSize,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing,
                                         Date    effectiveTime,
                                         String  methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    OpenMetadataAPIMapper.EVENT_TYPE_TYPE_GUID,
                                    OpenMetadataAPIMapper.EVENT_TYPE_TYPE_NAME,
                                    specificMatchPropertyNames,
                                    true,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    null,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }



    /**
     * Retrieve the event types metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param topicGUID unique identifier of the requested metadata element
     * @param topicGUIDParameterName parameter name of the topicGUID
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of event types element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> getEventTypesForTopic(String  userId,
                                         String  topicGUID,
                                         String  topicGUIDParameterName,
                                         int     startFrom,
                                         int     pageSize,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing,
                                         Date    effectiveTime,
                                         String  methodName) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        /*
         * The event types are attached via an event list.
         */
        EntityDetail eventTypeListEntity = this.getAttachedEntity(userId,
                                                                  topicGUID,
                                                                  topicGUIDParameterName,
                                                                  OpenMetadataAPIMapper.TOPIC_TYPE_NAME,
                                                                  OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                                                  OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                                                  OpenMetadataAPIMapper.EVENT_TYPE_LIST_TYPE_NAME,
                                                                  2,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  supportedZones,
                                                                  effectiveTime,
                                                                  methodName);

        if (eventTypeListEntity != null)
        {
            final String eventTypeListGUIDParameterName = "eventTypeListGUID";

            return this.getAttachedElements(userId,
                                            eventTypeListEntity.getGUID(),
                                            eventTypeListGUIDParameterName,
                                            OpenMetadataAPIMapper.EVENT_TYPE_LIST_TYPE_NAME,
                                            OpenMetadataAPIMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_GUID,
                                            OpenMetadataAPIMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_NAME,
                                            OpenMetadataAPIMapper.EVENT_TYPE_TYPE_NAME,
                                            null,
                                            null,
                                            2,
                                            forLineage,
                                            forDuplicateProcessing,
                                            startFrom,
                                            pageSize,
                                            effectiveTime,
                                            methodName);
        }

        return null;
    }


    /**
     * Create/retrieve the event list for the topic.
     *
     * @param userId calling user
     * @param externalSourceGUID unique identifier of software capability representing the caller
     * @param externalSourceName unique name of software capability representing the caller
     * @param topicGUID topic to retrieve from
     * @param topicGUIDParameterName parameter name or topicGUID
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of event list
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private String getEventTypeListGUID(String  userId,
                                        String  externalSourceGUID,
                                        String  externalSourceName,
                                        String  topicGUID,
                                        String  topicGUIDParameterName,
                                        String  topicQualifiedName,
                                        Date    effectiveFrom,
                                        Date    effectiveTo,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing,
                                        Date    effectiveTime,
                                        String  methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        String eventTypeListGUID;

        EntityDetail eventListEntity = this.getAttachedEntity(userId,
                                                              topicGUID,
                                                              topicGUIDParameterName,
                                                              OpenMetadataAPIMapper.TOPIC_TYPE_NAME,
                                                              OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                                              OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                                              OpenMetadataAPIMapper.EVENT_TYPE_LIST_TYPE_NAME,
                                                              2,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              supportedZones,
                                                              effectiveTime,
                                                              methodName);

        if (eventListEntity == null)
        {
            SchemaTypeBuilder builder = new SchemaTypeBuilder(topicQualifiedName + "_EventList",
                                                              OpenMetadataAPIMapper.EVENT_TYPE_LIST_TYPE_GUID,
                                                              OpenMetadataAPIMapper.EVENT_TYPE_LIST_TYPE_NAME,
                                                              repositoryHelper,
                                                              serviceName,
                                                              serverName);

            builder.setAnchors(userId, topicGUID, methodName);
            builder.setEffectivityDates(effectiveFrom, effectiveTo);

            eventTypeListGUID = repositoryHandler.createEntity(userId,
                                                               OpenMetadataAPIMapper.EVENT_TYPE_LIST_TYPE_GUID,
                                                               OpenMetadataAPIMapper.EVENT_TYPE_LIST_TYPE_NAME,
                                                               externalSourceGUID,
                                                               externalSourceName,
                                                               builder.getInstanceProperties(methodName),
                                                               builder.getEntityClassifications(),
                                                               builder.getInstanceStatus(),
                                                               methodName);

            if (eventTypeListGUID != null)
            {
                final String eventTypeListGUIDParameterName = "eventTypeListGUID";

                this.linkElementToElement(userId,
                                          externalSourceGUID,
                                          externalSourceName,
                                          topicGUID,
                                          topicGUIDParameterName,
                                          OpenMetadataAPIMapper.TOPIC_TYPE_NAME,
                                          eventTypeListGUID,
                                          eventTypeListGUIDParameterName,
                                          OpenMetadataAPIMapper.EVENT_TYPE_LIST_TYPE_NAME,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID,
                                          OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                          null,
                                          effectiveFrom,
                                          effectiveTo,
                                          effectiveTime,
                                          methodName);
            }
            else
            {
                errorHandler.logNullInstance(OpenMetadataAPIMapper.EVENT_TYPE_LIST_TYPE_NAME, methodName);
            }
        }
        else
        {
            eventTypeListGUID = eventListEntity.getGUID();
        }

        return eventTypeListGUID;
    }


    /**
     * Retrieve the event type metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param guidParameterName parameter name of guid
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getEventTypeByGUID(String  userId,
                                String  guid,
                                String  guidParameterName,
                                boolean forLineage,
                                boolean forDuplicateProcessing,
                                Date    effectiveTime,
                                String  methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        return this.getBeanFromRepository(userId,
                                          guid,
                                          guidParameterName,
                                          OpenMetadataAPIMapper.EVENT_TYPE_TYPE_NAME,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          effectiveTime,
                                          methodName);

    }
}
