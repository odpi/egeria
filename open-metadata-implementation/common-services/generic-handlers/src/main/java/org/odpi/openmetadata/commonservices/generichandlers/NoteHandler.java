/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

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
     * Count the number of notes attached to an anchor note log.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return count of the attached objects
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int countAttachedNotes(String   userId,
                                  String   elementGUID,
                                  String   methodName) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        return super.countAttachments(userId,
                                      elementGUID,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                      OpenMetadataAPIMapper.NOTE_ENTRY_TYPE_GUID,
                                      OpenMetadataAPIMapper.NOTE_ENTRY_TYPE_NAME,
                                      methodName);
    }


    /**
     * Return the notes attached to an anchor note log.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the note is attached to
     * @param elementGUIDParameterName name of parameter supplying the GUID
     * @param elementTypeName name of the type of object being attached to
     * @param serviceSupportedZones supported zones for calling service
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getNotes(String       userId,
                             String       elementGUID,
                             String       elementGUIDParameterName,
                             String       elementTypeName,
                             List<String> serviceSupportedZones,
                             int          startingFrom,
                             int          pageSize,
                             String       methodName) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        // todo
        return null;
    }
}
