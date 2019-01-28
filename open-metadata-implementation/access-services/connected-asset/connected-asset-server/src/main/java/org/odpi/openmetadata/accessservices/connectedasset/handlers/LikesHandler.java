/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.handlers;

import org.odpi.openmetadata.accessservices.connectedasset.converters.LikeConverter;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.UnrecognizedGUIDException;
import org.odpi.openmetadata.accessservices.connectedasset.mappers.AssetMapper;
import org.odpi.openmetadata.accessservices.connectedasset.mappers.LikeMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Like;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.ArrayList;
import java.util.List;

/**
 * LikesHandler is responsible for retrieving a list of Likes from the open metadata repositories
 * taking into account the paging requirements.
 */
public class LikesHandler
{
    List<Like>  returnedList = null;

    /**
     * Construct the asset handler with a link to the property server's connector and this access service's
     * official name.  Then retrieve the asset and its relationships.
     *
     * @param serviceName  name of this service
     * @param serverName  name of this server
     * @param repositoryConnector  connector to the property server.
     * @param userId        userId of user making request.
     * @param assetGUID     unique id for asset.
     * @param elementStart  starting element to return (may be so many elements that paging is needed).
     * @param maxElements  Maximum number of elements to return (may be so many elements that paging is needed).
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property (metadata) server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public LikesHandler(String                  serviceName,
                        String                  serverName,
                        OMRSRepositoryConnector repositoryConnector,
                        String                  userId,
                        String                  assetGUID,
                        int                     elementStart,
                        int                     maxElements) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        RepositoryHandler    repositoryHandler = new RepositoryHandler(serviceName, serverName, repositoryConnector);

        try
        {
            List<Relationship>   retrievedRelationships = repositoryHandler.retrieveRelationships(userId, AssetMapper.TYPE_NAME, assetGUID, LikeMapper.RELATIONSHIP_TYPE_NAME, elementStart, maxElements);

            returnedList = new ArrayList<>();

            if (retrievedRelationships != null)
            {
                for (Relationship  relationship : retrievedRelationships)
                {
                    if (relationship != null)
                    {
                        EntityProxy  entityProxy = relationship.getEntityTwoProxy();
                        EntityDetail elementEntity = repositoryHandler.retrieveEntity(userId, LikeMapper.TYPE_NAME, entityProxy.getGUID());

                        LikeConverter  converter = new LikeConverter(elementEntity, relationship, repositoryConnector.getRepositoryHelper(), serviceName);
                        Like           bean = converter.getBean();

                        if (bean != null)
                        {
                            returnedList.add(bean);
                        }
                    }
                }
            }
        }
        catch (UnrecognizedGUIDException error)
        {
            throw new InvalidParameterException(error.getReportedHTTPCode(),
                                                error.getReportingClassName(),
                                                error.getReportingActionDescription(),
                                                error.getErrorMessage(),
                                                error.getReportedSystemAction(),
                                                error.getReportedUserAction(),
                                                assetGUID);
        }
    }


    /**
     * Return the requested list of annotations to the caller.
     *
     * @return list
     */
    public List<Like> getList()
    {
        if (returnedList == null)
        {
            return null;
        }
        else if (returnedList.isEmpty())
        {
            return null;
        }
        else
        {
            return returnedList;
        }
    }
}
