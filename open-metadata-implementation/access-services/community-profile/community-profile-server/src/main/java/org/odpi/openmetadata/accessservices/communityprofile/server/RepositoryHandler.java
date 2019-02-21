/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;

import org.odpi.openmetadata.accessservices.communityprofile.ffdc.CommunityProfileErrorCode;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * BasicHandler provides common functions to access metadata from the metadata repositories.
 */
public class RepositoryHandler
{
    private static final Logger log = LoggerFactory.getLogger(RepositoryHandler.class);

    private String                        serviceName;
    private ErrorHandler                  errorHandler;
    private OMRSRepositoryHelper          repositoryHelper = null;
    private OMRSRepositoryValidator       repositoryValidator = null;
    private String                        serverName       = null;

    /**
     * Construct the basic handler with a link to the property server's connector and this access service's
     * official name.
     *
     * @param serviceName  name of this service
     * @param repositoryConnector  connector to the property server.
     */
    RepositoryHandler(String                  serviceName,
                      OMRSRepositoryConnector repositoryConnector)
    {
        this.serviceName = serviceName;
        this.errorHandler = new ErrorHandler(repositoryConnector);

        if (repositoryConnector != null)
        {
            this.repositoryHelper = repositoryConnector.getRepositoryHelper();
            this.repositoryValidator = repositoryConnector.getRepositoryValidator();
            this.serverName = repositoryConnector.getServerName();
        }
    }


    /**
     * Return the requested entity.
     *
     * @param guid unique identifier of the entity
     * @param guidParameterName name of the parameter that passed the guid
     * @param userId user id of the requesting user
     * @param methodName name of the calling method
     * @param expectedType expected type of entity
     * @return entity
     * @throws InvalidParameterException one of the parameters passed is invalid
     * @throws PropertyServerException the metadata repositories are not available
     * @throws UserNotAuthorizedException the user does not have access to the entity
     */
    EntityDetail getEntityByGUID(String                  guid,
                                 String                  guidParameterName,
                                 String                  userId,
                                 String                  methodName,
                                 String                  expectedType) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(guid, guidParameterName, expectedType, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        try
        {
            EntityDetail  entity  = metadataCollection.getEntityDetail(userId, guid);

            if (entity != null)
            {
                if (repositoryValidator.isATypeOf(serverName, entity, expectedType, methodName))
                {
                    log.debug("Entity retrieved: " + entity.toString());

                    return entity;
                }
                else
                {
                    InstanceType  type = entity.getType();
                    String        typeName = "<null>";

                    if (type != null)
                    {
                        typeName = type.getTypeDefName();
                    }

                    errorHandler.handleWrongTypeForGUIDException(guid, methodName, typeName, expectedType);
                }
            }
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException error)
        {
            errorHandler.handleUnrecognizedGUIDException(userId, methodName, serverName, expectedType, guid);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName, serverName, serviceName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, serverName, serviceName);
        }

        log.debug("Null return from method: " + methodName);

        return null;
    }



    /**
     * Delete or purge an instance depending on the support of the owning repository.
     *
     * @param userId calling user
     * @param guid unique identifier of the instance
     * @param typeGUID unique identifier of the type
     * @param typeName name of the type
     * @param methodName name of calling method
     * @throws PropertyServerException unable to delete or purge the instance - probably because of a mismatch between
     *                                 the type and the instance guid
     */
    void deleteEntity(String    userId,
                      String    guid,
                      String    typeGUID,
                      String    typeName,
                      String    methodName) throws PropertyServerException
    {
        OMRSMetadataCollection  metadataCollection = errorHandler.validateRepositoryConnector(methodName);
        Throwable               deleteException;

        try
        {
            metadataCollection.deleteEntity(userId,
                                            typeGUID,
                                            typeName,
                                            guid);

            log.debug("Entity soft-deleted: " + guid);

            return;
        }
        catch (FunctionNotSupportedException exception)
        {
            log.debug("Soft-delete not supported: " + serverName);

            try
            {
                metadataCollection.purgeEntity(userId,
                                               typeGUID,
                                               typeName,
                                               guid);

                log.debug("Entity purged: " + guid);

                return;
            }
            catch (Throwable  error)
            {
                log.debug("Entity purge failed: " + error.getMessage());

                deleteException = error;
            }
        }
        catch (Throwable  error)
        {
            log.debug("Entity soft-delete failed: " + error.getMessage());

            deleteException = error;
        }

        CommunityProfileErrorCode errorCode    = CommunityProfileErrorCode.INSTANCE_NOT_DELETED;
        String                    errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(guid);

        throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                          this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction(),
                                          deleteException);
    }


    /**
     * Return the list of relationships attached to the requested entity.
     *
     * @param guid unique identifier of the entity
     * @param guidParameterName name of the parameter that passed the guid
     * @param userId user id of the requesting user
     * @param methodName name of the calling method
     * @param expectedEntityType expected type of entity
     * @param requestedRelationshipType guid of requested type of returned relationships - null means get all of them.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of attached relationships (or null)
     *
     * @throws InvalidParameterException one of the parameters passed is invalid
     * @throws PropertyServerException the metadata repositories are not available
     * @throws UserNotAuthorizedException the user does not have access to the relationships
     */
    List<Relationship> getRelationshipsForEntity(String                  guid,
                                                 String                  guidParameterName,
                                                 String                  userId,
                                                 String                  methodName,
                                                 String                  expectedEntityType,
                                                 String                  requestedRelationshipType,
                                                 int                     startFrom,
                                                 int                     pageSize) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        List<Relationship>   returnList = new ArrayList<>();

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(guid, guidParameterName, expectedEntityType, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        try
        {
            returnList = metadataCollection.getRelationshipsForEntity(userId,
                                                                      guid,
                                                                      requestedRelationshipType,
                                                                      startFrom,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      pageSize);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException error)
        {
            errorHandler.handleUnrecognizedGUIDException(userId, methodName, serverName, expectedEntityType, guid);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName, serverName, serviceName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, serverName, serviceName);
        }

        if (returnList.isEmpty())
        {
            return null;
        }
        else
        {
            return returnList;
        }
    }


    /**
     * Return the list of relationships of the requested type.
     *
     * @param relationships list of relationships connected to an entity.
     * @param typeName requested type (returned relationships can be a subtype)
     * @return list of relationships or null for no matches
     */
    List<Relationship> getRelationshipsOfType(List<Relationship> relationships,
                                              String             typeName)
    {
        List<Relationship>   returnList = new ArrayList<>();

        if ((relationships != null) && (typeName != null))
        {
            for (Relationship  relationship : relationships)
            {
                if (relationship != null)
                {
                    InstanceType  instanceType = relationship.getType();

                    if (instanceType != null)
                    {
                        if (repositoryHelper.isTypeOf(serviceName, instanceType.getTypeDefName(), typeName))
                        {
                            returnList.add(relationship);
                        }
                    }
                }
            }
        }

        if (returnList.isEmpty())
        {
            return null;
        }
        else
        {
            return returnList;
        }
    }
}
