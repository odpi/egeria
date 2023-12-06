/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * NoteLogHandler manages NoteLog objects.  It runs server-side in
 * the OMAG Server Platform and retrieves NoteEntry entities through the OMRSRepositoryConnector.
 */
public class NoteLogHandler<B> extends ReferenceableHandler<B>
{
    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones list of zones that the access service should set in all new Asset instances.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public NoteLogHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Count the number of noteLogs attached to an anchor entity.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the object is attached to
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @return count of attached objects
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int countAttachedNoteLogs(String  userId,
                                     String  elementGUID,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime,
                                     String  methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        return super.countAttachments(userId,
                                      elementGUID,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TO_NOTE_LOG_TYPE_GUID,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TO_NOTE_LOG_TYPE_NAME,
                                      2,
                                      forLineage,
                                      forDuplicateProcessing,
                                      effectiveTime,
                                      methodName);
    }



    /**
     * Adds a noteLog and link it to the supplied parent entity.
     *
     * @param userId        String - userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param anchorGUID    head of the noteLog chain
     * @param parentGUID    String - unique id for a referenceable entity that the noteLog is to be attached to.
     * @param parentGUIDParameterName name of parameter that supplied the entity's unique identifier.
     * @param qualifiedName unique name of the note log
     * @param name   name of the note log
     * @param description   String - the text of the noteLog.
     * @param isPublic      should this be visible to all or private to the caller
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName    calling method
     *
     * @return guid of new noteLog.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException There is a problem adding the asset properties to
     *                                   the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public  String attachNewNoteLog(String      userId,
                                    String      externalSourceGUID,
                                    String      externalSourceName,
                                    String      anchorGUID,
                                    String      parentGUID,
                                    String      parentGUIDParameterName,
                                    String      qualifiedName,
                                    String      name,
                                    String      description,
                                    boolean     isPublic,
                                    Date        effectiveFrom,
                                    Date        effectiveTo,
                                    boolean     forLineage,
                                    boolean     forDuplicateProcessing,
                                    Date        effectiveTime,
                                    String      methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String nameParameter = "name";
        final String noteLogGUIDParameter = "noteLogGUID";
        final String anchorGUIDParameter = "anchorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentGUID, parentGUIDParameterName, methodName);
        invalidParameterHandler.validateText(nameParameter, nameParameter, methodName);

        /*
         * A noteLog is a referenceable.  It needs a unique qualified name.  There is no obvious value to use so
         * a UUID is used to create a unique string.
         */
        NoteLogBuilder builder = new NoteLogBuilder(qualifiedName,
                                                    name,
                                                    description,
                                                    isPublic,
                                                    repositoryHelper,
                                                    serviceName,
                                                    serverName);

        this.addAnchorGUIDToBuilder(userId,
                                    anchorGUID,
                                    anchorGUIDParameter,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    supportedZones,
                                    builder,
                                    methodName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        String  noteLogGUID = this.createBeanInRepository(userId,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          OpenMetadataAPIMapper.NOTE_LOG_TYPE_GUID,
                                                          OpenMetadataAPIMapper.NOTE_LOG_TYPE_NAME,
                                                          builder,
                                                          effectiveTime,
                                                          methodName);

        if (noteLogGUID != null)
        {
            this.uncheckedLinkElementToElement(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               parentGUID,
                                               parentGUIDParameterName,
                                               OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                               noteLogGUID,
                                               noteLogGUIDParameter,
                                               OpenMetadataAPIMapper.NOTE_LOG_TYPE_NAME,
                                               forLineage,
                                               forDuplicateProcessing,
                                               supportedZones,
                                               OpenMetadataAPIMapper.REFERENCEABLE_TO_NOTE_LOG_TYPE_GUID,
                                               OpenMetadataAPIMapper.REFERENCEABLE_TO_NOTE_LOG_TYPE_NAME,
                                               builder.getRelationshipInstanceProperties(methodName),
                                               effectiveFrom,
                                               methodName);
        }

        return noteLogGUID;
    }


    /**
     * Update an existing noteLog.
     *
     * @param userId        userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param noteLogGUID   unique identifier for the noteLog to change
     * @param noteLogGUIDParameterName name of parameter for noteLogGUID
     * @param qualifiedName unique name of the noteLog
     * @param noteLogType   type of noteLog enum.
     * @param noteLogText   the text of the noteLog.
     * @param isPublic      indicates whether the feedback should be shared or only be visible to the originating user
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName    calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   updateNoteLog(String              userId,
                                String              externalSourceGUID,
                                String              externalSourceName,
                                String              noteLogGUID,
                                String              noteLogGUIDParameterName,
                                String              qualifiedName,
                                String              noteLogType,
                                String              noteLogText,
                                boolean             isPublic,
                                boolean             isMergeUpdate,
                                Date                effectiveFrom,
                                Date                effectiveTo,
                                boolean             forLineage,
                                boolean             forDuplicateProcessing,
                                Date                effectiveTime,
                                String              methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String textParameter = "noteLogText";

        invalidParameterHandler.validateText(noteLogText, textParameter, methodName);

        EntityDetail startingEntity = repositoryHandler.getEntityByGUID(userId,
                                                                        noteLogGUID,
                                                                        noteLogGUIDParameterName,
                                                                        OpenMetadataAPIMapper.NOTE_LOG_TYPE_NAME,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

        NoteLogBuilder builder = new NoteLogBuilder(qualifiedName,
                                                    noteLogType,
                                                    noteLogText,
                                                    isPublic,
                                                    repositoryHelper,
                                                    serviceName,
                                                    serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    startingEntity,
                                    noteLogGUIDParameterName,
                                    OpenMetadataAPIMapper.NOTE_LOG_TYPE_GUID,
                                    OpenMetadataAPIMapper.NOTE_LOG_TYPE_NAME,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);

        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   startingEntity,
                                                                   noteLogGUIDParameterName,
                                                                   OpenMetadataAPIMapper.NOTE_LOG_TYPE_NAME,
                                                                   OpenMetadataAPIMapper.REFERENCEABLE_TO_NOTE_LOG_TYPE_GUID,
                                                                   OpenMetadataAPIMapper.REFERENCEABLE_TO_NOTE_LOG_TYPE_NAME,
                                                                   null,
                                                                   OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                                   1,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   supportedZones,
                                                                   0,
                                                                   0,
                                                                   effectiveTime,
                                                                   methodName);


        if ((relationships == null) || (relationships.isEmpty()))
        {
            errorHandler.handleNoRelationship(noteLogGUID,
                                              OpenMetadataAPIMapper.NOTE_LOG_TYPE_NAME,
                                              OpenMetadataAPIMapper.REFERENCEABLE_TO_NOTE_LOG_TYPE_NAME,
                                              methodName);
        }
        else if (relationships.size() == 1)
        {
            repositoryHandler.updateRelationshipProperties(userId,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           relationships.get(0),
                                                           builder.getRelationshipInstanceProperties(methodName),
                                                           methodName);
        }
        else
        {
            errorHandler.handleAmbiguousRelationships(noteLogGUID,
                                                      OpenMetadataAPIMapper.NOTE_LOG_TYPE_NAME,
                                                      OpenMetadataAPIMapper.REFERENCEABLE_TO_NOTE_LOG_TYPE_NAME,
                                                      relationships,
                                                      methodName);
        }

    }


    /**
     * Removes a noteLog added to the parent by this user.
     *
     * @param userId       userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param noteLogGUIDParameterName parameter supplying the
     * @param noteLogGUID  unique identifier for the noteLog object.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName    calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public void removeNoteLogFromElement(String     userId,
                                         String     externalSourceGUID,
                                         String     externalSourceName,
                                         String     noteLogGUID,
                                         String     noteLogGUIDParameterName,
                                         boolean    forLineage,
                                         boolean    forDuplicateProcessing,
                                         Date       effectiveTime,
                                         String     methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    noteLogGUID,
                                    noteLogGUIDParameterName,
                                    OpenMetadataAPIMapper.NOTE_LOG_TYPE_GUID,
                                    OpenMetadataAPIMapper.NOTE_LOG_TYPE_NAME,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }






    /**
     * Return the note logs attached to an anchor entity.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the note log is attached to
     * @param elementGUIDParameterName name of parameter supplying the GUID
     * @param elementTypeName name of the type of object being attached to
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getAttachedNoteLogs(String       userId,
                                        String       elementGUID,
                                        String       elementGUIDParameterName,
                                        String       elementTypeName,
                                        List<String> serviceSupportedZones,
                                        int          startingFrom,
                                        int          pageSize,
                                        boolean      forLineage,
                                        boolean      forDuplicateProcessing,
                                        Date         effectiveTime,
                                        String       methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        elementGUID,
                                        elementGUIDParameterName,
                                        elementTypeName,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_NOTE_LOG_TYPE_GUID,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_NOTE_LOG_TYPE_NAME,
                                        OpenMetadataAPIMapper.NOTE_LOG_TYPE_NAME,
                                        null,
                                        null,
                                        2,
                                        forLineage,
                                        forDuplicateProcessing,
                                        serviceSupportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Return the note logs attached to an anchor entity.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the note log is attached to
     * @param elementGUIDParameterName name of parameter supplying the GUID
     * @param elementTypeName name of the type of object being attached to
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getAttachedNoteLogs(String       userId,
                                        String       elementGUID,
                                        String       elementGUIDParameterName,
                                        String       elementTypeName,
                                        int          startingFrom,
                                        int          pageSize,
                                        boolean      forLineage,
                                        boolean      forDuplicateProcessing,
                                        Date         effectiveTime,
                                        String       methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        elementGUID,
                                        elementGUIDParameterName,
                                        elementTypeName,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_NOTE_LOG_TYPE_GUID,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_NOTE_LOG_TYPE_NAME,
                                        OpenMetadataAPIMapper.NOTE_LOG_TYPE_NAME,
                                        null,
                                        null,
                                        2,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Retrieve the list of metadata elements that contain the search string.
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
    public List<B> findNoteLogs(String  userId,
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
                              OpenMetadataAPIMapper.NOTE_LOG_TYPE_GUID,
                              OpenMetadataAPIMapper.NOTE_LOG_TYPE_NAME,
                              null,
                              startFrom,
                              pageSize,
                              forLineage,
                              forDuplicateProcessing,
                              effectiveTime,
                              methodName);
    }


    /**
     * Retrieve the list of metadata elements with a matching qualified name, display name or network address.
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
    public List<B> getNoteLogsByName(String  userId,
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
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
        specificMatchPropertyNames.add(OpenMetadataAPIMapper.NAME_PROPERTY_NAME);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    OpenMetadataAPIMapper.NOTE_LOG_TYPE_GUID,
                                    OpenMetadataAPIMapper.NOTE_LOG_TYPE_NAME,
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

}
