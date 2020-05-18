/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.ReferenceableConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Referenceable;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * ReferenceableHandler manages methods on generic referenceables.
 */
public class ReferenceableHandler
{
    private String                  serviceName;
    private String                  serverName;
    private OMRSRepositoryHelper    repositoryHelper;
    private RepositoryHandler       repositoryHandler;
    private InvalidParameterHandler invalidParameterHandler;


    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     */
    public ReferenceableHandler(String                  serviceName,
                                String                  serverName,
                                InvalidParameterHandler invalidParameterHandler,
                                RepositoryHandler       repositoryHandler,
                                OMRSRepositoryHelper    repositoryHelper)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHandler = repositoryHandler;
        this.repositoryHelper = repositoryHelper;
    }


    /**
     * Returns the list of related assets for the asset.
     *
     * @param userId       String   userId of user making request.
     * @param elementGUID    String   unique id for asset.
     * @param startFrom int      starting position for fist returned element.
     * @param pageSize  int      maximum number of elements to return on the call.
     * @param methodName String calling method
     *
     * @return a list of assets or
     * @throws InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * @throws PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * @throws UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public List<Referenceable> getMoreInformation(String  userId,
                                                  String  elementGUID,
                                                  int     startFrom,
                                                  int     pageSize,
                                                  String  methodName)  throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        int queryPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        List<EntityDetail>  entitiesForRelationshipType = repositoryHandler.getEntitiesForRelationshipType(userId,
                                                                                                           elementGUID,
                                                                                                           ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                                                                           ReferenceableMapper.REFERENCEABLE_TO_MORE_INTO_TYPE_GUID,
                                                                                                           ReferenceableMapper.REFERENCEABLE_TO_MORE_INFO_TYPE_NAME,
                                                                                                           startFrom,
                                                                                                           queryPageSize,
                                                                                                           methodName);

        if (entitiesForRelationshipType != null)
        {
            List<Referenceable>  moreInfoElements = new ArrayList<>();

            for (EntityDetail  entity : entitiesForRelationshipType)
            {
                if (entity != null)
                {
                    ReferenceableConverter converter = new ReferenceableConverter(entity,
                                                                                  repositoryHelper,
                                                                                  serviceName);

                    moreInfoElements.add(converter.getBean());
                }
            }

            if (moreInfoElements.isEmpty())
            {
                return null;
            }
            else
            {
                return moreInfoElements;
            }
        }

        return null;
    }


    /**
     * Create the property facet for the vendor properties.
     *
     * @param userId calling user
     * @param referenceableGUID unique identifier of the metadata element
     * @param qualifiedName qualified name of the metadata element
     * @param vendorProperties properties for the vendor
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void setVendorProperties(String              userId,
                                    String              referenceableGUID,
                                    String              qualifiedName,
                                    Map<String, String> vendorProperties,
                                    String              methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        // todo
    }


    /**
     * Retrieve the property facet for the vendor properties.
     *
     * @param userId calling user
     * @param referenceableGUID unique identifier of the metadata element
     * @param qualifiedName qualified name of the metadata element
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public Map<String, String> getVendorProperties(String userId,
                                                   String referenceableGUID,
                                                   String qualifiedName,
                                                   String methodName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        // todo
        return null;
    }
}
