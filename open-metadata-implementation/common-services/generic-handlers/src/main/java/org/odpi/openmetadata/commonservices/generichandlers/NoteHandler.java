/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.List;

/**
 * NoteLogHandler manages NoteLog objects.  It runs server-side in
 * the OMAG Server Platform and retrieves NoteLog entities through the OMRSRepositoryConnector.
 */
public class NoteHandler<B> extends ReferenceableHandler<B>
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
    public NoteHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Adds a note and link it to the supplied parent entity.
     *
     * @param userId        String - userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param anchorGUID    head of the note chain
     * @param noteLogGUID    String - unique id for a referenceable entity that the note is to be attached to.
     * @param noteLogGUIDParameterName name of parameter that supplied the entity's unique identifier.
     * @param title   ordinal of note enum.
     * @param text   String - the text of the note.
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName    calling method
     *
     * @return guid of new note.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException There is a problem adding the asset properties to
     *                                   the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public  String attachNewNote(String      userId,
                                 String      externalSourceGUID,
                                 String      externalSourceName,
                                 String      anchorGUID,
                                 String      noteLogGUID,
                                 String      noteLogGUIDParameterName,
                                 String      title,
                                 String      text,
                                 Date        effectiveFrom,
                                 Date        effectiveTo,
                                 boolean     forLineage,
                                 boolean     forDuplicateProcessing,
                                 Date        effectiveTime,
                                 String      methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String textParameter = "text";
        final String noteGUIDParameter = "noteGUID";
        final String anchorGUIDParameter = "anchorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(noteLogGUID, noteLogGUIDParameterName, methodName);
        invalidParameterHandler.validateText(text, textParameter, methodName);

        /*
         * A note is a referenceable.  It needs a unique qualified name.  There is no obvious value to use so
         * a UUID is used to create a unique string.
         */
        String qualifiedName = "Note:" + new Date().getTime();
        NoteBuilder builder = new NoteBuilder(qualifiedName,
                                              title,
                                              text,
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

        String  noteGUID = this.createBeanInRepository(userId,
                                                       externalSourceGUID,
                                                       externalSourceName,
                                                       OpenMetadataType.NOTE_TYPE_GUID,
                                                       OpenMetadataType.NOTE_TYPE_NAME,
                                                       builder,
                                                       effectiveTime,
                                                       methodName);

        if (noteGUID != null)
        {
            this.uncheckedLinkElementToElement(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               noteLogGUID,
                                               noteLogGUIDParameterName,
                                               OpenMetadataType.REFERENCEABLE.typeName,
                                               noteGUID,
                                               noteGUIDParameter,
                                               OpenMetadataType.NOTE_TYPE_NAME,
                                               forLineage,
                                               forDuplicateProcessing,
                                               supportedZones,
                                               OpenMetadataType.NOTE_LOG_ENTRIES_RELATIONSHIP_TYPE_GUID,
                                               OpenMetadataType.NOTE_LOG_ENTRIES_RELATIONSHIP_TYPE_NAME,
                                               null,
                                               effectiveFrom,
                                               methodName);
        }

        return noteGUID;
    }


    /**
     * Update an existing note.
     *
     * @param userId        userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param noteGUID   unique identifier for the note to change
     * @param noteGUIDParameterName name of parameter for noteGUID
     * @param qualifiedName unique name of the note
     * @param title   title of the note
     * @param noteText   the text of the note
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
    public void   updateNote(String              userId,
                             String              externalSourceGUID,
                             String              externalSourceName,
                             String              noteGUID,
                             String              noteGUIDParameterName,
                             String              qualifiedName,
                             String              title,
                             String              noteText,
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
        final String textParameter = "noteText";

        invalidParameterHandler.validateText(noteText, textParameter, methodName);

        EntityDetail startingEntity = repositoryHandler.getEntityByGUID(userId,
                                                                        noteGUID,
                                                                        noteGUIDParameterName,
                                                                        OpenMetadataType.NOTE_TYPE_NAME,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

        NoteBuilder builder = new NoteBuilder(qualifiedName,
                                              title,
                                              noteText,
                                              repositoryHelper,
                                              serviceName,
                                              serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    startingEntity,
                                    noteGUIDParameterName,
                                    OpenMetadataType.NOTE_TYPE_GUID,
                                    OpenMetadataType.NOTE_TYPE_NAME,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Removes a note added to the parent by this user.
     *
     * @param userId       userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param noteGUIDParameterName parameter supplying the
     * @param noteGUID  unique identifier for the note object.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName    calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public void removeNoteFromElement(String     userId,
                                      String     externalSourceGUID,
                                      String     externalSourceName,
                                      String     noteGUID,
                                      String     noteGUIDParameterName,
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
                                    noteGUID,
                                    noteGUIDParameterName,
                                    OpenMetadataType.NOTE_TYPE_GUID,
                                    OpenMetadataType.NOTE_TYPE_NAME,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Return the notes attached to an anchor note log.
     *
     * @param userId     calling user
     * @param noteLogGUID identifier for the entity that the note is attached to
     * @param noteLogGUIDParameterName name of parameter supplying the GUID
     * @param noteLogTypeName name of the type of object being attached to
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
    public List<B>  getNotes(String       userId,
                             String       noteLogGUID,
                             String       noteLogGUIDParameterName,
                             String       noteLogTypeName,
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
                                        noteLogGUID,
                                        noteLogGUIDParameterName,
                                        noteLogTypeName,
                                        OpenMetadataType.NOTE_LOG_ENTRIES_RELATIONSHIP_TYPE_GUID,
                                        OpenMetadataType.NOTE_LOG_ENTRIES_RELATIONSHIP_TYPE_NAME,
                                        OpenMetadataType.NOTE_TYPE_NAME,
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
     * Return the notes attached to an anchor note log.
     *
     * @param userId     calling user
     * @param noteLogGUID identifier for the entity that the note is attached to
     * @param noteLogGUIDParameterName name of parameter supplying the GUID
     * @param noteLogTypeName name of the type of object being attached to
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
    public List<B>  getNotes(String       userId,
                             String       noteLogGUID,
                             String       noteLogGUIDParameterName,
                             String       noteLogTypeName,
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
                                        noteLogGUID,
                                        noteLogGUIDParameterName,
                                        noteLogTypeName,
                                        OpenMetadataType.NOTE_LOG_ENTRIES_RELATIONSHIP_TYPE_GUID,
                                        OpenMetadataType.NOTE_LOG_ENTRIES_RELATIONSHIP_TYPE_NAME,
                                        OpenMetadataType.NOTE_TYPE_NAME,
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
    public List<B> findNotes(String  userId,
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
                              OpenMetadataType.NOTE_TYPE_GUID,
                              OpenMetadataType.NOTE_TYPE_NAME,
                              null,
                              startFrom,
                              pageSize,
                              forLineage,
                              forDuplicateProcessing,
                              effectiveTime,
                              methodName);
    }
}
