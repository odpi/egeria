/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.server;

import org.odpi.openmetadata.accessservices.assetconsumer.client.AssetConsumer;
import org.odpi.openmetadata.accessservices.assetconsumer.client.OpenIntegrationServiceClient;
import org.odpi.openmetadata.accessservices.assetconsumer.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDef;
import org.odpi.openmetadata.viewservices.assetcatalog.beans.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AssetCatalogInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class AssetCatalogInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.ASSET_CATALOG;

    private final AssetConsumer                assetConsumerClient;
    private final OpenIntegrationServiceClient openIntegrationServiceClient;
    private final OpenMetadataStoreClient      openMetadataStoreClient;
    private final List<Type>                   supportedAssetTypes;


    /**
     * Set up the Asset Catalog OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException invalid parameter - probably null by could be an invalid type name
     * @throws PropertyServerException problem communicating with the remote metadata repository
     * @throws UserNotAuthorizedException problem with the userId
     */
    public AssetCatalogInstance(String       serverName,
                                AuditLog     auditLog,
                                String       localServerUserId,
                                int          maxPageSize,
                                String       remoteServerName,
                                String       remoteServerURL,
                                List<String> supportedAssetTypeNames) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        super(serverName,
              myDescription.getViewServiceName(),
              auditLog,
              localServerUserId,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        this.assetConsumerClient = new AssetConsumer(remoteServerName, remoteServerURL, auditLog);
        this.openIntegrationServiceClient = new OpenIntegrationServiceClient(remoteServerName, remoteServerURL, maxPageSize);
        this.openMetadataStoreClient = new OpenMetadataStoreClient(remoteServerName, remoteServerURL, maxPageSize);

        this.supportedAssetTypes = this.setupSupportedAssetTypes(supportedAssetTypeNames);
    }


    /**
     * Determine the full list of types request  for this view service search requests.
     *
     * @param supportedAssetTypeNames list of type names or null
     * @return list of types formatted using Asset Catalog beans
     * @throws InvalidParameterException invalid parameter - probably null by could be an invalid type name
     * @throws PropertyServerException problem communicating with the remote metadata repository
     * @throws UserNotAuthorizedException problem with the userId
    */
    private List<Type> setupSupportedAssetTypes(List<String> supportedAssetTypeNames) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        List<Type> results = new ArrayList<>();

        if (supportedAssetTypeNames == null)
        {
            results.addAll(this.getTypesForTypeName(localServerUserId, OpenMetadataType.ASSET.typeName));
        }
        else
        {
            for (String supportedAssetTypeName : supportedAssetTypeNames)
            {
                results.addAll(this.getTypesForTypeName(localServerUserId, supportedAssetTypeName));
            }
        }

        /*
         * Deduplicate the list of types
         */
        Map<String, Type> resultMap = new HashMap<>();

        for (Type result : results)
        {
            resultMap.put(result.getName(), result);
        }

        return new ArrayList<>(resultMap.values());
    }



    /**
     * Retrieve the open metadata type and its subtypes from the open metadata store and convert them to
     * Asset Catalog OMVS type beans.
     *
     * @param userId calling user
     * @param typeName type name to process
     * @return list of types
     * @throws InvalidParameterException invalid parameter - probably null by could be an invalid type name
     * @throws PropertyServerException problem communicating with the remote metadata repository
     * @throws UserNotAuthorizedException problem with the userId
     */
    private List<Type> getTypesForTypeName(String userId,
                                           String typeName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        List<Type> results = new ArrayList<>();

        OpenMetadataTypeDef openMetadataTypeDef = openMetadataStoreClient.getTypeDefByName(userId, typeName);

        if (openMetadataTypeDef != null)
        {
            results.add(this.convertOpenMetadataType(openMetadataTypeDef));

            List<OpenMetadataTypeDef> subTypes = openMetadataStoreClient.getSubTypes(userId, typeName);

            if (subTypes != null)
            {
                for (OpenMetadataTypeDef subType : subTypes)
                {
                    if (subType != null)
                    {
                        results.add(this.convertOpenMetadataType(subType));
                    }
                }
            }
        }

        return results;
    }


    /**
     * Convert an open metadata type definition into an Asset Catalog OMVS Type bean.
     *
     * @param openMetadataType open metadata type definition to convert.
     * @return Asset Catalog OMVS Type bean
     */
    private Type convertOpenMetadataType(OpenMetadataTypeDef openMetadataType)
    {
        if (openMetadataType != null)
        {
            Type type = new Type();

            type.setName(openMetadataType.getName());
            type.setDescription(openMetadataType.getDescription());
            type.setVersion(openMetadataType.getVersion());

            if (openMetadataType.getSuperType() != null)
            {
                type.setSuperType(openMetadataType.getSuperType().getName());
            }

            return type;
        }

        return null;
    }


    /**
     * Return the values from the SupportedTypesForSearch view service option.
     *
     * @return list of strings.  If null then all asset types are supported.
     */
    public List<Type> getSupportedAssetTypes()
    {
        return supportedAssetTypes;
    }


    /**
     * Return the main Asset Consumer OMAS client.
     *
     * @return client
     */
    public AssetConsumer getAssetConsumerClient()
    {
        return assetConsumerClient;
    }


    /**
     * Return the open integration client.  This client is from the Open Integration Framework (OIF).
     *
     * @return client
     */
    public OpenIntegrationServiceClient getOpenIntegrationServiceClient()
    {
        return openIntegrationServiceClient;
    }


    /**
     * Return the open metadata store client.  This client is from the Governance Action Framework (GAF).
     *
     * @return client
     */
    public OpenMetadataStoreClient getOpenMetadataStoreClient()
    {
        return openMetadataStoreClient;
    }
}
