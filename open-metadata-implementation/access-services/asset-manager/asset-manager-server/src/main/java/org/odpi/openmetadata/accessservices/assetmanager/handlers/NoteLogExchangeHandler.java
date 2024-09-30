/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.handlers;

import org.odpi.openmetadata.accessservices.assetmanager.converters.NoteConverter;
import org.odpi.openmetadata.accessservices.assetmanager.converters.NoteLogConverter;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.CorrelatedMetadataElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.NoteElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.NoteLogElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.NoteHandler;
import org.odpi.openmetadata.commonservices.generichandlers.NoteLogHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.NoteLogProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.NoteProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.List;

/**
 * NoteLogExchangeHandler is the server side handler for managing note logs and their associated notes.
 */
public class NoteLogExchangeHandler extends ExchangeHandlerBase
{
    private final NoteLogHandler<NoteLogElement> noteLogHandler;
    private final NoteHandler<NoteElement>         noteHandler;

    private final static String noteLogGUIDParameterName  = "noteLogGUID";
    private final static String noteGUIDParameterName      = "noteGUID";

    /**
     * Construct the note log exchange handler with information needed to work with note log related objects
     * for Asset Manager OMAS.
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve instances from.
     * @param defaultZones list of zones that the access service should set in all new instances.
     * @param publishZones list of zones that the access service sets up in published instances.
     * @param auditLog destination for audit log events.
     */
    public NoteLogExchangeHandler(String                             serviceName,
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
        super(serviceName,
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


        noteLogHandler = new NoteLogHandler<>(new NoteLogConverter<>(repositoryHelper, serviceName, serverName),
                                              NoteLogElement.class,
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

        noteHandler = new NoteHandler<>(new NoteConverter<>(repositoryHelper, serviceName, serverName),
                                        NoteElement.class,
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



    /* ========================================================
     * Managing the externalIds and related correlation properties.
     */


    /**
     * Update each returned element with details of the correlation properties for the supplied asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param results list of elements
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private void addCorrelationPropertiesToNoteLogs(String                        userId,
                                                    String                        assetManagerGUID,
                                                    String                        assetManagerName,
                                                    List<NoteLogElement> results,
                                                    boolean                       forLineage,
                                                    boolean                       forDuplicateProcessing,
                                                    Date                          effectiveTime,
                                                    String                        methodName) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        if (results != null)
        {
            for (CorrelatedMetadataElement noteLog : results)
            {
                if ((noteLog != null) && (noteLog.getElementHeader() != null) && (noteLog.getElementHeader().getGUID() != null))
                {
                    noteLog.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                noteLog.getElementHeader().getGUID(),
                                                                                noteLogGUIDParameterName,
                                                                                OpenMetadataType.NOTE_LOG.typeName,
                                                                                assetManagerGUID,
                                                                                assetManagerName,
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                effectiveTime,
                                                                                methodName));
                }
            }
        }
    }




    /**
     * Update each returned element with details of the correlation properties for the supplied asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param results list of elements
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private void addCorrelationPropertiesToNotes(String                    userId,
                                                 String                    assetManagerGUID,
                                                 String                    assetManagerName,
                                                 List<NoteElement> results,
                                                 boolean                   forLineage,
                                                 boolean                   forDuplicateProcessing,
                                                 Date                      effectiveTime,
                                                 String                    methodName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        if (results != null)
        {
            for (CorrelatedMetadataElement note : results)
            {
                if ((note != null) && (note.getElementHeader() != null) && (note.getElementHeader().getGUID() != null))
                {
                    note.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                             note.getElementHeader().getGUID(),
                                                                             noteGUIDParameterName,
                                                                             OpenMetadataType.NOTE_ENTRY.typeName,
                                                                             assetManagerGUID,
                                                                             assetManagerName,
                                                                             forLineage,
                                                                             forDuplicateProcessing,
                                                                             effectiveTime,
                                                                             methodName));
                }
            }
        }
    }


    /* =====================================================================================================================
     * A element may have one or more note logs
     */

    /**
     * Create a new metadata element to represent a note log.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element where the note log is located
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome ensure that only the asset manager can update this element
     * @param noteLogProperties properties about the note log to store
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param isPublic      should this be visible to all or private to the caller
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return unique identifier of the new note log
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createNoteLog(String                        userId,
                                String                        elementGUID,
                                MetadataCorrelationProperties correlationProperties,
                                boolean                       assetManagerIsHome,
                                NoteLogProperties             noteLogProperties,
                                boolean                       isPublic,
                                boolean                       forLineage,
                                boolean                       forDuplicateProcessing,
                                Date                          effectiveTime,
                                String                        methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String propertiesParameterName    = "noteLogProperties";
        final String qualifiedNameParameterName = "noteLogProperties.qualifiedName";
        final String elementGUIDParameterName   = "elementGUID";

        invalidParameterHandler.validateObject(noteLogProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(noteLogProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String noteLogGUID = noteLogHandler.attachNewNoteLog(userId,
                                                             getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                             getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                             elementGUID,
                                                             elementGUID,
                                                             elementGUIDParameterName,
                                                             noteLogProperties.getQualifiedName(),
                                                             noteLogProperties.getDisplayName(),
                                                             noteLogProperties.getDescription(),
                                                             isPublic,
                                                             noteLogProperties.getEffectiveFrom(),
                                                             noteLogProperties.getEffectiveTo(),
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime,
                                                             methodName);

        if (noteLogGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          noteLogGUID,
                                          noteLogGUIDParameterName,
                                          OpenMetadataType.NOTE_LOG.typeName,
                                          correlationProperties,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
        }

        return noteLogGUID;
    }


    /**
     * Update the metadata element representing a note log.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param noteLogGUID unique identifier of the metadata element to update
     * @param noteLogProperties new properties for the metadata element
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param isPublic                 is this element visible to other people.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateNoteLog(String                        userId,
                              MetadataCorrelationProperties correlationProperties,
                              String                        noteLogGUID,
                              NoteLogProperties             noteLogProperties,
                              boolean                       isMergeUpdate,
                              boolean                       isPublic,
                              boolean                       forLineage,
                              boolean                       forDuplicateProcessing,
                              Date                          effectiveTime,
                              String                        methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String propertiesParameterName    = "noteLogProperties";
        final String qualifiedNameParameterName = "noteLogProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(noteLogGUID, noteLogGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(noteLogProperties, propertiesParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(noteLogProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        this.validateExternalIdentifier(userId,
                                        noteLogGUID,
                                        noteLogGUIDParameterName,
                                        OpenMetadataType.NOTE_LOG.typeName,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        noteLogHandler.updateNoteLog(userId,
                                     getExternalSourceGUID(correlationProperties),
                                     getExternalSourceName(correlationProperties),
                                     noteLogGUID,
                                     noteLogGUIDParameterName,
                                     noteLogProperties.getQualifiedName(),
                                     noteLogProperties.getDisplayName(),
                                     noteLogProperties.getDescription(),
                                     isPublic,
                                     isMergeUpdate,
                                     noteLogProperties.getEffectiveFrom(),
                                     noteLogProperties.getEffectiveTo(),
                                     forLineage,
                                     forDuplicateProcessing,
                                     effectiveTime,
                                     methodName);
    }


    /**
     * Remove the metadata element representing a note log.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param noteLogGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeNoteLog(String                        userId,
                              MetadataCorrelationProperties correlationProperties,
                              String                        noteLogGUID,
                              boolean                       forLineage,
                              boolean                       forDuplicateProcessing,
                              Date                          effectiveTime,
                              String                        methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        this.validateExternalIdentifier(userId,
                                        noteLogGUID,
                                        noteLogGUIDParameterName,
                                        OpenMetadataType.NOTE_LOG.typeName,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        noteLogHandler.removeNoteLogFromElement(userId,
                                                getExternalSourceGUID(correlationProperties),
                                                getExternalSourceName(correlationProperties),
                                                noteLogGUID,
                                                noteLogGUIDParameterName,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Retrieve the list of note log metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param searchStringParameterName name of parameter for searchString
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<NoteLogElement>   findNoteLogs(String  userId,
                                               String  assetManagerGUID,
                                               String  assetManagerName,
                                               String  searchString,
                                               String  searchStringParameterName,
                                               int     startFrom,
                                               int     pageSize,
                                               Date    effectiveTime,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing,
                                               String  methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        List<NoteLogElement> results = noteLogHandler.findNoteLogs(userId,
                                                                   searchString,
                                                                   searchStringParameterName,
                                                                   startFrom,
                                                                   pageSize,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   effectiveTime,
                                                                   methodName);

        this.addCorrelationPropertiesToNoteLogs(userId,
                                                assetManagerGUID,
                                                assetManagerName,
                                                results,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);

        return results;
    }


    /**
     * Retrieve the list of note log metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param nameParameterName parameter name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<NoteLogElement>   getNoteLogsByName(String  userId,
                                                    String  assetManagerGUID,
                                                    String  assetManagerName,
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
        List<NoteLogElement> results = noteLogHandler.getNoteLogsByName(userId,
                                                                        name,
                                                                        nameParameterName,
                                                                        startFrom,
                                                                        pageSize,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

        this.addCorrelationPropertiesToNoteLogs(userId,
                                                assetManagerGUID,
                                                assetManagerName,
                                                results,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);

        return results;
    }


    /**
     * Retrieve the list of note log metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param elementGUID guid to search for
     * @param elementParameterName parameter name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<NoteLogElement>   getNoteLogsForElement(String  userId,
                                                        String  assetManagerGUID,
                                                        String  assetManagerName,
                                                        String  elementGUID,
                                                        String  elementParameterName,
                                                        int     startFrom,
                                                        int     pageSize,
                                                        boolean forLineage,
                                                        boolean forDuplicateProcessing,
                                                        Date    effectiveTime,
                                                        String  methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        List<NoteLogElement> results = noteLogHandler.getAttachedNoteLogs(userId,
                                                                          elementGUID,
                                                                          elementParameterName,
                                                                          OpenMetadataType.REFERENCEABLE.typeName,
                                                                          startFrom,
                                                                          pageSize,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName);

        this.addCorrelationPropertiesToNoteLogs(userId,
                                                assetManagerGUID,
                                                assetManagerName,
                                                results,
                                                forLineage,
                                                forDuplicateProcessing,
                                                effectiveTime,
                                                methodName);

        return results;
    }


    /**
     * Retrieve the note log metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param openMetadataGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteLogElement getNoteLogByGUID(String  userId,
                                           String  assetManagerGUID,
                                           String  assetManagerName,
                                           String  openMetadataGUID,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime,
                                           String  methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String guidParameterName  = "openMetadataGUID";

        NoteLogElement noteLog = noteLogHandler.getBeanFromRepository(userId,
                                                                      openMetadataGUID,
                                                                      guidParameterName,
                                                                      OpenMetadataType.NOTE_LOG.typeName,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime,
                                                                      methodName);

        if (noteLog != null)
        {
            noteLog.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                        openMetadataGUID,
                                                                        guidParameterName,
                                                                        OpenMetadataType.NOTE_LOG.typeName,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName));
        }

        return noteLog;
    }


    /* ===============================================================================
     * A note log typically contains many notes, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a note.
     *
     * @param userId calling user
     * @param noteLogGUID unique identifier of the element where the note is located
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome  ensure that only the asset manager can update this element
     * @param noteProperties properties for the note
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element for the note
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createNote(String                        userId,
                             String                        noteLogGUID,
                             MetadataCorrelationProperties correlationProperties,
                             boolean                       assetManagerIsHome,
                             NoteProperties                noteProperties,
                             Date                          effectiveTime,
                             boolean                       forLineage,
                             boolean                       forDuplicateProcessing,
                             String                        methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String propertiesParameterName     = "noteProperties";
        final String qualifiedNameParameterName  = "noteProperties.qualifiedName";
        final String elementGUIDParameterName    = "noteLogGUID";

        invalidParameterHandler.validateObject(noteProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(noteProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String noteGUID = noteHandler.attachNewNote(userId,
                                                    getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                    getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                    noteLogGUID,
                                                    noteLogGUID,
                                                    elementGUIDParameterName,
                                                    noteProperties.getQualifiedName(),
                                                    noteProperties.getTitle(),
                                                    noteProperties.getText(),
                                                    noteProperties.getEffectiveFrom(),
                                                    noteProperties.getEffectiveTo(),
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);

        if (noteGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          noteGUID,
                                          noteGUIDParameterName,
                                          OpenMetadataType.NOTE_ENTRY.typeName,
                                          correlationProperties,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
        }

        return noteGUID;
    }


    /**
     * Update the properties of the metadata element representing a note.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param noteGUID unique identifier of the note to update
     * @param noteProperties new properties for the note
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateNote(String                        userId,
                           MetadataCorrelationProperties correlationProperties,
                           String                        noteGUID,
                           NoteProperties                noteProperties,
                           boolean                       isMergeUpdate,
                           boolean                       forLineage,
                           boolean                       forDuplicateProcessing,
                           Date                          effectiveTime,
                           String                        methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String propertiesParameterName    = "noteProperties";
        final String qualifiedNameParameterName = "noteProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(noteGUID, noteGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(noteProperties, propertiesParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(noteProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        this.validateExternalIdentifier(userId,
                                        noteGUID,
                                        noteGUIDParameterName,
                                        OpenMetadataType.NOTE_ENTRY.typeName,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        noteHandler.updateNote(userId,
                               getExternalSourceGUID(correlationProperties),
                               getExternalSourceName(correlationProperties),
                               noteGUID,
                               noteGUIDParameterName,
                               noteProperties.getQualifiedName(),
                               noteProperties.getTitle(),
                               noteProperties.getText(),
                               isMergeUpdate,
                               noteProperties.getEffectiveFrom(),
                               noteProperties.getEffectiveTo(),
                               forLineage,
                               forDuplicateProcessing,
                               effectiveTime,
                               methodName);
    }


    /**
     * Remove the metadata element representing a note.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param noteGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeNote(String                        userId,
                           MetadataCorrelationProperties correlationProperties,
                           String                        noteGUID,
                           boolean                       forLineage,
                           boolean                       forDuplicateProcessing,
                           Date                          effectiveTime,
                           String                        methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(noteGUID, noteGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        noteGUID,
                                        noteGUIDParameterName,
                                        OpenMetadataType.NOTE_ENTRY.typeName,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        noteHandler.removeNoteFromElement(userId,
                                          getExternalSourceGUID(correlationProperties),
                                          getExternalSourceName(correlationProperties),
                                          noteGUID,
                                          noteGUIDParameterName,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
    }


    /**
     * Retrieve the list of note metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param searchStringParameterName parameter supplying search string
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<NoteElement>   findNotes(String                   userId,
                                         String                   assetManagerGUID,
                                         String                   assetManagerName,
                                         String                   searchString,
                                         String                   searchStringParameterName,
                                         int                      startFrom,
                                         int                      pageSize,
                                         boolean                  forLineage,
                                         boolean                  forDuplicateProcessing,
                                         Date                     effectiveTime,
                                         String                   methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        List<NoteElement> results = noteHandler.findNotes(userId,
                                                          searchString,
                                                          searchStringParameterName,
                                                          startFrom,
                                                          pageSize,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          effectiveTime,
                                                          methodName);

        this.addCorrelationPropertiesToNotes(userId,
                                             assetManagerGUID,
                                             assetManagerName,
                                             results,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);

        return results;
    }
    
    
    /**
     * Retrieve the list of notes associated with a note log.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param noteLogGUID unique identifier of the note log of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<NoteElement>    getNotesForNoteLog(String  userId,
                                                   String  assetManagerGUID,
                                                   String  assetManagerName,
                                                   String  noteLogGUID,
                                                   int     startFrom,
                                                   int     pageSize,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing,
                                                   Date    effectiveTime,
                                                   String  methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        List<NoteElement> results = noteHandler.getNotes(userId,
                                                         noteLogGUID,
                                                         noteLogGUIDParameterName,
                                                         OpenMetadataType.NOTE_LOG.typeName,
                                                         startFrom,
                                                         pageSize,
                                                         forLineage,
                                                         forDuplicateProcessing,
                                                         effectiveTime,
                                                         methodName);

        this.addCorrelationPropertiesToNotes(userId,
                                             assetManagerGUID,
                                             assetManagerName,
                                             results,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);

        return results;
    }


    /**
     * Retrieve the note metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param guid unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteElement getNoteByGUID(String  userId,
                                     String  assetManagerGUID,
                                     String  assetManagerName,
                                     String  guid,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime,
                                     String  methodName) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String guidParameterName = "guid";

        NoteElement note = noteHandler.getBeanFromRepository(userId,
                                                             guid,
                                                             guidParameterName,
                                                             OpenMetadataType.NOTE_ENTRY.typeName,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime,
                                                             methodName);

        if (note != null)
        {
            note.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                     guid,
                                                                     guidParameterName,
                                                                     OpenMetadataType.NOTE_ENTRY.typeName,
                                                                     assetManagerGUID,
                                                                     assetManagerName,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     effectiveTime,
                                                                     methodName));
        }

        return note;
    }
}
