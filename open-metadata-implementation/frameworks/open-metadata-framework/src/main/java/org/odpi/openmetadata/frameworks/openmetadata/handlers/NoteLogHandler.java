/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;


/**
 * NoteLogHandler is the handler for managing note log elements.
 * A note log maintains an ordered list of notes.  It can be used to support release note, blogs and similar
 * broadcast information.  Note logs are typically maintained by the owners/stewards of an element.
 */
public class NoteLogHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public NoteLogHandler(String             localServerName,
                          AuditLog           auditLog,
                          String             serviceName,
                          OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, OpenMetadataType.NOTE_LOG.typeName);
    }



    /**
     * Create a new metadata element to represent a note log and attach it to an element (if supplied).
     * Any supplied element becomes the note log's anchor, causing the note log to be deleted if/when the element is deleted.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element where the note log is located
     * @param metadataSourceOptions  options to control access to open metadata
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param noteLogProperties properties about the note log to store
     *
     * @return unique identifier of the new note log
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  String createNoteLog(String                                userId,
                                 String                                elementGUID,
                                 MetadataSourceOptions                 metadataSourceOptions,
                                 Map<String, ClassificationProperties> initialClassifications,
                                 NoteLogProperties                     noteLogProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        NewElementOptions newElementOptions = new NewElementOptions(metadataSourceOptions);

        if (elementGUID != null)
        {
            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorGUID(elementGUID);
            newElementOptions.setParentGUID(elementGUID);
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ATTACHED_NOTE_LOG_RELATIONSHIP.typeName);
        }
        else
        {
            newElementOptions.setIsOwnAnchor(true);
        }

        return openMetadataClient.createMetadataElementInStore(userId,
                                                               OpenMetadataType.NOTE_LOG.typeName,
                                                               newElementOptions,
                                                               classificationBuilder.getInitialClassifications(initialClassifications),
                                                               elementBuilder.getNewElementProperties(noteLogProperties),
                                                               null);
    }


    /**
     * Create a new freestanding note log.
     *
     * @param userId                       userId of user making request.
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createNoteLog(String                                userId,
                                NewElementOptions                     newElementOptions,
                                Map<String, ClassificationProperties> initialClassifications,
                                NoteLogProperties                     properties,
                                RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                     PropertyServerException,
                                                                                                                     UserNotAuthorizedException
    {
        final String methodName = "createNoteLog";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a note log using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new location.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createNoteLogFromTemplate(String                 userId,
                                            TemplateOptions        templateOptions,
                                            String                 templateGUID,
                                            EntityProperties       replacementProperties,
                                            Map<String, String>    placeholderProperties,
                                            RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        return super.createElementFromTemplate(userId,
                                               templateOptions,
                                               templateGUID,
                                               replacementProperties,
                                               placeholderProperties,
                                               parentRelationshipProperties);
    }


    /**
     * Update the metadata element representing a note log.
     *
     * @param userId calling user
     * @param noteLogGUID unique identifier of the metadata element to update
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param noteLogProperties new properties for the metadata element
     *
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public boolean updateNoteLog(String            userId,
                                 String            noteLogGUID,
                                 UpdateOptions     updateOptions,
                                 NoteLogProperties noteLogProperties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        return openMetadataClient.updateMetadataElementInStore(userId,
                                                               noteLogGUID,
                                                               updateOptions,
                                                               elementBuilder.getElementProperties(noteLogProperties));
    }


    /**
     * Remove the metadata element representing a note log.
     *
     * @param userId calling user
     * @param noteLogGUID unique identifier of the metadata element to remove
     * @param deleteOptions options for a delete request
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeNoteLog(String        userId,
                              String        noteLogGUID,
                              DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        openMetadataClient.deleteMetadataElementInStore(userId, noteLogGUID, deleteOptions);
    }


    /**
     * Retrieve the list of note log metadata elements that contain the search string.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param searchOptions multiple options to control the query
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findNoteLogs(String        userId,
                                                      String        searchString,
                                                      SearchOptions searchOptions) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName = "findNoteLogs";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }


    /**
     * Retrieve the list of note log metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param queryOptions multiple options to control the query

     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> getNoteLogsByName(String       userId,
                                                           String       name,
                                                           QueryOptions queryOptions) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "getNoteLogsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Retrieve the list of note log metadata elements attached to the element.
     *
     * @param userId calling user
     * @param elementGUID element to start from
     * @param queryOptions multiple options to control the query
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> getNoteLogsForElement(String       userId,
                                                               String       elementGUID,
                                                               QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName = "getNoteLogsForElement";
        final String guidParameterName = "elementGUID";

        return super.getRelatedRootElements(userId,
                                            elementGUID,
                                            guidParameterName,
                                            1,
                                            OpenMetadataType.ATTACHED_NOTE_LOG_RELATIONSHIP.typeName,
                                            OpenMetadataType.NOTE_LOG.typeName,
                                            queryOptions,
                                            methodName);
    }


    /**
     * Retrieve the note log metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param noteLogGUID unique identifier of the requested metadata element
     * @param getOptions multiple options to control the query
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElement getNoteLogByGUID(String     userId,
                                                    String     noteLogGUID,
                                                    GetOptions getOptions) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "getNoteLogByGUID";

        return super.getRootElementByGUID(userId, noteLogGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of note log metadata elements attached to the element.
     *
     * @param userId calling user
     * @param noteLogGUID element to start from
     * @param queryOptions multiple options to control the query
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> getNotesForNoteLog(String       userId,
                                                            String       noteLogGUID,
                                                            QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName = "getNotesForNoteLog";
        final String guidParameterName = "noteLogGUID";

        return super.getRelatedRootElements(userId,
                                            noteLogGUID,
                                            guidParameterName,
                                            1,
                                            OpenMetadataType.ATTACHED_NOTE_LOG_ENTRY_RELATIONSHIP.typeName,
                                            OpenMetadataType.NOTIFICATION.typeName,
                                            queryOptions,
                                            methodName);
    }
}
