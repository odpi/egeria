/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.LocationMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Location;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;

/**
 * LocationHandler manages Location objects.  It runs server-side in
 * the OMAG Server Platform and retrieves Location entities through the OMRSRepositoryConnector.
 */
public class LocationHandler extends AttachmentHandlerBase
{
    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param lastAttachmentHandler handler for recording last attachment
     */
    public LocationHandler(String                  serviceName,
                           String                  serverName,
                           InvalidParameterHandler invalidParameterHandler,
                           RepositoryHandler       repositoryHandler,
                           OMRSRepositoryHelper    repositoryHelper,
                           LastAttachmentHandler   lastAttachmentHandler)
    {
        super(serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, lastAttachmentHandler);
    }


    /**
     * Count the number of locations attached to an anchor entity.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return count of attached objects
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int countKnownLocations(String   userId,
                                   String   anchorGUID,
                                   String   methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        return super.countAttachments(userId,
                                      anchorGUID,
                                      ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                      LocationMapper.ASSET_LOCATION_TYPE_GUID,
                                      LocationMapper.ASSET_LOCATION_TYPE_NAME,
                                      methodName);
    }


    /**
     * Return the locations attached to an anchor entity.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the identifier is attached to
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
    public List<Location>  getLocations(String   userId,
                                        String   anchorGUID,
                                        int      startingFrom,
                                        int      pageSize,
                                        String   methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        // todo
        return null;
    }
}
