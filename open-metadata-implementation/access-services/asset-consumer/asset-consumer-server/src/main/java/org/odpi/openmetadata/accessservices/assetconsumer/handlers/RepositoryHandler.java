/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.handlers;

import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

public class RepositoryHandler
{

    private String       serviceName;
    private String       serverName   = null;
    private ErrorHandler errorHandler = null;

    /**
     * Construct the feedback handler with a link to the property server's connector and this access service's
     * official name.
     *
     * @param serviceName  name of this service
     * @param repositoryConnector  connector to the property server.
     */
    RepositoryHandler(String                  serviceName,
                      OMRSRepositoryConnector repositoryConnector)
    {
        this.serviceName = serviceName;

        if (repositoryConnector != null)
        {
            this.serverName = repositoryConnector.getServerName();
            errorHandler = new ErrorHandler(repositoryConnector);
        }
    }

    /**
     * Validate that the supplied GUID is for a real entity.
     *
     * @param userId  user making the request.
     * @param guid  unique identifier of the entity.
     * @param metadataCollection  repository's metadata collection
     * @param methodName  name of method called.
     *
     * @throws InvalidParameterException entity not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void validateEntityGUID(String                  userId,
                            String                  guid,
                            String                  assetTypeName,
                            OMRSMetadataCollection  metadataCollection,
                            String                  methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        try
        {
            metadataCollection.getEntitySummary(userId, guid);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException error)
        {
            errorHandler.handleUnknownAsset(error,
                                            guid,
                                            methodName,
                                            serverName,
                                            serviceName);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId,
                                                methodName,
                                                serverName,
                                                serviceName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error,
                                               methodName,
                                               serverName,
                                               serviceName);
        }
    }
}
