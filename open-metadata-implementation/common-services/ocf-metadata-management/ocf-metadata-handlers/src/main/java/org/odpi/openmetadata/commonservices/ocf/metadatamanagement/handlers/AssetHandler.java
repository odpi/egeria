/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.*;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.*;

/**
 * AssetHandler manages Asset objects and optionally connections in the property server.  It runs server-side in
 * the OMAG Server Platform and retrieves Assets and Connections through the OMRSRepositoryConnector.
 */
public class AssetHandler extends ReferenceableHandler
{
    protected List<String>            supportedZones;
    protected List<String>            publishZones;
    protected List<String>            defaultZones;



    /**
     * Construct the asset handler with information needed to work with Asset objects.
     *
     * @param serviceName name of this service
     * @param serverName name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler handler for interfacing with the repository services
     * @param repositoryHelper    helper utilities for managing repository services objects
     * @param supportedZones list of zones that the service is allowed to serve Assets from.
     * @param publishZones list of zones that the service should set in Assets when they are published.
     * @param defaultZones list of zones that the service should set in all new Assets and when they are withdrawn.
     */
    public AssetHandler(String                    serviceName,
                        String                    serverName,
                        InvalidParameterHandler   invalidParameterHandler,
                        RepositoryHandler         repositoryHandler,
                        OMRSRepositoryHelper      repositoryHelper,
                        List<String>              supportedZones,
                        List<String>              publishZones,
                        List<String>              defaultZones)
    {
        super(serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper);

        this.supportedZones            = supportedZones;
        this.publishZones              = publishZones;
        this.defaultZones              = defaultZones;
    }


    /**
     * Reclassifies an asset based on the new properties.
     *
     * @param userId userId
     * @param originalAsset current content of the asset
     * @param updatedAsset new asset values
     * @param zoneMembershipProperties zone membership properties
     * @param ownerProperties owner properties
     * @param methodName calling method
     *
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    @Deprecated
    public void reclassifyAsset(String             userId,
                                Asset              originalAsset,
                                Asset              updatedAsset,
                                InstanceProperties zoneMembershipProperties,
                                InstanceProperties ownerProperties,
                                String             methodName) throws UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        if ((originalAsset == null) || (originalAsset.getZoneMembership() == null))
        {
            if (updatedAsset.getZoneMembership() != null)
            {
                repositoryHandler.classifyEntity(userId,
                                                 null,
                                                 null,
                                                 updatedAsset.getGUID(),
                                                 AssetMapper.ASSET_ZONES_CLASSIFICATION_GUID,
                                                 AssetMapper.ASSET_ZONES_CLASSIFICATION_NAME,
                                                 null,
                                                 null,
                                                 zoneMembershipProperties,
                                                 methodName);
            }
        }
        else
        {
            try
            {
                if (updatedAsset.getZoneMembership() == null)
                {
                    repositoryHandler.declassifyEntity(userId,
                                                       null,
                                                       null,
                                                       updatedAsset.getGUID(),
                                                       AssetMapper.ASSET_ZONES_CLASSIFICATION_GUID,
                                                       AssetMapper.ASSET_ZONES_CLASSIFICATION_NAME,
                                                       null,
                                                       methodName);
                }
                else if (!(originalAsset.getZoneMembership().equals(updatedAsset.getZoneMembership())))
                {
                    repositoryHandler.reclassifyEntity(userId,
                                                       null,
                                                       null,
                                                       updatedAsset.getGUID(),
                                                       AssetMapper.ASSET_ZONES_CLASSIFICATION_GUID,
                                                       AssetMapper.ASSET_ZONES_CLASSIFICATION_NAME,
                                                       null,
                                                       zoneMembershipProperties,
                                                       methodName);
                }
            }
            catch (InvalidParameterException error)
            {
                throw new PropertyServerException(error);
            }
        }

        if ((originalAsset == null) || (originalAsset.getOwner() == null))
        {
            if (updatedAsset.getOwner() != null)
            {
                repositoryHandler.classifyEntity(userId,
                                                 null,
                                                 null,
                                                 updatedAsset.getGUID(),
                                                 AssetMapper.ASSET_OWNERSHIP_CLASSIFICATION_GUID,
                                                 AssetMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME,
                                                 null,
                                                 null,
                                                 ownerProperties,
                                                 methodName);
            }
        }
        else
        {
            try
            {
                if (updatedAsset.getOwner() == null)
                {
                    repositoryHandler.declassifyEntity(userId,
                                                       null,
                                                       null,
                                                       originalAsset.getGUID(),
                                                       AssetMapper.ASSET_OWNERSHIP_CLASSIFICATION_GUID,
                                                       AssetMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME,
                                                       null,
                                                       methodName);
                }
                else if (!originalAsset.getOwner().equals(updatedAsset.getOwner()))
                {
                    repositoryHandler.reclassifyEntity(userId,
                                                       null,
                                                       null,
                                                       originalAsset.getGUID(),
                                                       AssetMapper.ASSET_OWNERSHIP_CLASSIFICATION_GUID,
                                                       AssetMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME,
                                                       null,
                                                       ownerProperties,
                                                       methodName);
                }
            }
            catch (InvalidParameterException error)
            {
                throw new PropertyServerException(error);
            }
        }
    }
}
