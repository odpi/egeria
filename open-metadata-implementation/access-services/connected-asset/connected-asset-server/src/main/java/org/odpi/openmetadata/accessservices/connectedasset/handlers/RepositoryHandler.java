/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.handlers;

import org.odpi.openmetadata.accessservices.connectedasset.converters.TypeConverter;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.UnrecognizedGUIDException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * RepositoryHandler is responsible for retrieving instances from the open metadata repositories and converting
 * the repository service exceptions into this access service's exceptions.
 */
public class RepositoryHandler
{
    private String               serviceName;
    private OMRSRepositoryHelper repositoryHelper;
    private String               serverName;
    private ErrorHandler         errorHandler;
    private TypeConverter        typeHandler = new TypeConverter();


    /**
     * Constructor takes information about the repository and context of request for error logging.
     *
     * @param serviceName  name of this service
     * @param serverName  name of this server
     * @param repositoryConnector  connector to the property server.
     */
    public RepositoryHandler(String                  serviceName,
                             String                  serverName,
                             OMRSRepositoryConnector repositoryConnector)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.repositoryHelper = repositoryConnector.getRepositoryHelper();
        this.errorHandler = new ErrorHandler(repositoryConnector);

    }


    /**
     * Scan the complete list of relationships and extract those that match the supplied type name.
     *
     * @param retrievedRelationships full list of relationships attached to the asset.
     * @param typeName relationship type name
     * @return list of selected relationships (or empty list)
     */
    public  List<Relationship> getRelationshipsOfACertainType(List<Relationship>  retrievedRelationships,
                                                              String              typeName)
    {
        List<Relationship>   results = new ArrayList<>();

        if (typeName != null)
        {
            if (retrievedRelationships != null)
            {
                for (Relationship   relationship : retrievedRelationships)
                {
                    if (relationship != null)
                    {
                        InstanceType instanceType = relationship.getType();

                        if (instanceType != null)
                        {
                            if (repositoryHelper.isTypeOf(serviceName, typeName, instanceType.getTypeDefName()))
                            {
                                results.add(relationship);
                            }
                        }
                    }
                }
            }
        }

        return results;
    }


    /**
     * Returns the entity object corresponding to the supplied asset GUID.
     *
     * @param userId  userId of user making request.
     * @param typeName  name of the type that the retrieved entity is expected to be for.
     * @param guid  the unique id for the entity within the property server.
     *
     * @return Connection retrieved from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException the supplied GUID is not recognized by the metadata repository.
     * @throws PropertyServerException there is a problem retrieving information from the property (metadata) server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public EntityDetail retrieveEntity(String     userId,
                                       String     typeName,
                                       String     guid) throws InvalidParameterException,
                                                               UnrecognizedGUIDException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final  String   methodName = "retrieveEntity";
        final  String   guidParameter = "guid";
        final  String   typeNameParameter = "typeName";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(guid, guidParameter, methodName);
        errorHandler.validateName(typeName, typeNameParameter, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        try
        {
            return metadataCollection.getEntityDetail(userId, guid);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException error)
        {
            errorHandler.handleUnrecognizedGUIDException(userId, methodName, serverName, typeName, guid, error.getErrorMessage());
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException error)
        {
            errorHandler.handleEntityProxyGUIDException(userId, methodName, serverName, typeName, guid, error.getErrorMessage());
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName, serverName, serviceName, error.getErrorMessage());
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, serverName, serviceName);
        }

        return null;
    }


    /**
     * Returns the list of relationships attached to the identified asset entity from the open metadata repositories.
     *
     * @param userId  userId of user making request.
     * @param entityTypeName type of entity that these relationships are connected to.
     * @param entityGUID  the unique id for the starting entity within the property server.
     * @param startingElement  starting element to return (may be so many elements that paging is needed).
     * @param pageSize  Maximum number of elements to return (may be so many elements that paging is needed).
     *
     * @return relationships retrieved from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException the supplied GUID is not recognized by the metadata repository.
     * @throws PropertyServerException there is a problem retrieving information from the property (metadata) server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<Relationship> retrieveAllRelationships(String     userId,
                                                       String     entityTypeName,
                                                       String     entityGUID,
                                                       int        startingElement,
                                                       int        pageSize) throws InvalidParameterException,
                                                                                UnrecognizedGUIDException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final  String   methodName = "retrieveAllRelationships";
        final  String   guidParameter = "entityGUID";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(entityGUID, guidParameter, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        try
        {
            return metadataCollection.getRelationshipsForEntity(userId,
                                                                entityGUID,
                                                                null,
                                                                startingElement,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                pageSize);

        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException error)
        {
            errorHandler.handleUnrecognizedGUIDException(userId, methodName, serverName, entityTypeName, entityGUID, error.getErrorMessage());
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName, serverName, serviceName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, serverName, serviceName);
        }

        return null;
    }


    /**
     * Returns the list of relationships attached to the identified asset entity from the open metadata repositories.
     *
     * @param userId  userId of user making request.
     * @param entityTypeName type of entity that these relationships are connected to.
     * @param entityGUID  the unique id for the starting entity within the property server.
     * @param startingElement  starting element to return (may be so many elements that paging is needed).
     * @param pageSize  Maximum number of elements to return (may be so many elements that paging is needed).
     *
     * @return relationships retrieved from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UnrecognizedGUIDException the supplied GUID is not recognized by the metadata repository.
     * @throws PropertyServerException there is a problem retrieving information from the property (metadata) server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<Relationship> retrieveRelationships(String     userId,
                                                    String     entityTypeName,
                                                    String     entityGUID,
                                                    String     relationshipTypeGUID,
                                                    int        startingElement,
                                                    int        pageSize) throws InvalidParameterException,
                                                                                   UnrecognizedGUIDException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final  String   methodName = "retrieveAllRelationships";
        final  String   guidParameter = "entityGUID";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(entityGUID, guidParameter, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        try
        {
            return metadataCollection.getRelationshipsForEntity(userId,
                                                                entityGUID,
                                                                relationshipTypeGUID,
                                                                startingElement,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                pageSize);

        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException error)
        {
            errorHandler.handleUnrecognizedGUIDException(userId, methodName, serverName, entityTypeName, entityGUID, error.getErrorMessage());
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId, methodName, serverName, serviceName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error, methodName, serverName, serviceName);
        }

        return null;
    }


}
