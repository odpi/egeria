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

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ExternalIdentifierHandler manages ExternalIdentifier objects.  It runs server-side in
 * the OMAG Server Platform and retrieves ExternalId entities through the OMRSRepositoryConnector.
 */
public class ExternalIdentifierHandler<B> extends ReferenceableHandler<B>
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
    public ExternalIdentifierHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Create a new ExternalIdentifier linked to the element that it represents and the scope where it is applicable (typically
     * this is the software server capability of the external metadata source.
     *
     * @param userId calling userId
     * @param elementGUID unique identifier of the
     * @param elementGUIDParameterName
     * @param elementTypeName
     * @param identifier
     * @param identifierKeyPattern
     * @param identifierDescription
     * @param identifierUsage
     * @param identifierSource
     * @param identifierMappingProperties
     * @param propertiesLastVerified
     * @param scopeGUID
     * @param scopeGUIDParameterName
     * @param scopeTypeName
     * @param permittedSynchronization
     * @param synchronizationDescription
     * @param methodName calling method
     */
    public void createExternalIdentifier(String              userId,
                                         String              elementGUID,
                                         String              elementGUIDParameterName,
                                         String              elementTypeName,
                                         String              identifier,
                                         int                 identifierKeyPattern,
                                         String              identifierDescription,
                                         String              identifierUsage,
                                         String              identifierSource,
                                         Map<String, String> identifierMappingProperties,
                                         Date                propertiesLastVerified,
                                         String              scopeGUID,
                                         String              scopeGUIDParameterName,
                                         String              scopeTypeName,
                                         int                 permittedSynchronization,
                                         String              synchronizationDescription,
                                         String              methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {

    }



    /**
     * Update the description of a specific external identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param openMetadataGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataGUIDParameterName parameter supplying openMetadataGUID
     * @param externalIdentifier unique identifier of this element in the external asset manager
     * @param externalIdentifierParameterName parameter supplying externalIdentifier
     * @param keyPattern style of the external identifier
     * @param description description of the identifier
     * @param mappingProperties additional mapping properties
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void updateExternalIdentifier(String              userId,
                                         String              assetManagerGUID,
                                         String              assetManagerName,
                                         String              openMetadataGUID,
                                         String              openMetadataGUIDParameterName,
                                         String              externalIdentifier,
                                         String              externalIdentifierParameterName,
                                         int                 keyPattern,
                                         String              description,
                                         Map<String, String> mappingProperties,
                                         String              methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        // todo
    }


    /**
     * Confirm that the values of a particular metadata element have been synchronized.  This is important
     * from an audit points of view, and to allow bidirectional updates of metadata using optimistic locking.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param openMetadataGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataGUIDParameterName parameter supplying openMetadataGUID
     * @param externalIdentifier unique identifier of this element in the external asset manager
     * @param externalIdentifierParameterName parameter supplying externalIdentifier
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void confirmSynchronization(String userId,
                                       String assetManagerGUID,
                                       String assetManagerName,
                                       String openMetadataGUID,
                                       String openMetadataGUIDParameterName,
                                       String externalIdentifier,
                                       String externalIdentifierParameterName,
                                       String methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        // todo
    }


    /**
     * Count the number of external identifiers attached to an anchor entity.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return count of attached objects
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem detected in the repository services
     */
    public int countExternalIdentifiers(String   userId,
                                        String   elementGUID,
                                        String   methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        return super.countAttachments(userId,
                                      elementGUID,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TO_EXTERNAL_ID_TYPE_GUID,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TO_EXTERNAL_ID_TYPE_NAME,
                                      methodName);
    }


    /**
     * Return the external identifiers attached to a referenceable by the ExternalIdLink.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the identifier is attached to
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
    public List<B>  getExternalIdentifiers(String       userId,
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
        return this.getAttachedElements(userId,
                                        elementGUID,
                                        elementGUIDParameterName,
                                        elementTypeName,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_EXTERNAL_ID_TYPE_GUID,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_EXTERNAL_ID_TYPE_NAME,
                                        OpenMetadataAPIMapper.EXTERNAL_IDENTIFIER_TYPE_NAME,
                                        serviceSupportedZones,
                                        startingFrom,
                                        pageSize,
                                        methodName);
    }



}
