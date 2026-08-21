/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.devopspipeline.server;

import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.AssetHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.NetworkHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.OperatingPlatformHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SoftwareCapabilityHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SoftwareDevelopmentHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.StorageVolumeHandler;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;

/**
 * DevopsPipelineInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class DevopsPipelineInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.DEVOPS_PIPELINE;

    private final StorageVolumeHandler storageVolumeHandler;
    private final NetworkHandler       networkHandler;
    private final AssetHandler         assetHandler;
    private final OperatingPlatformHandler operatingPlatformHandler;
    private final SoftwareCapabilityHandler softwareCapabilityHandler;
    private final SoftwareDevelopmentHandler softwareDevelopmentHandler;



    /**
     * Set up the Devops Pipeline OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param localServerSecretsStoreProvider secrets store connector for bearer token
     * @param localServerSecretsStoreLocation secrets store location for bearer token
     * @param localServerSecretsStoreCollection secrets store collection for bearer token
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public DevopsPipelineInstance(String   serverName,
                                  AuditLog auditLog,
                                  String   localServerUserId,
                                  String   localServerSecretsStoreProvider,
                                  String   localServerSecretsStoreLocation,
                                  String   localServerSecretsStoreCollection,
                                  int      maxPageSize,
                                  String   remoteServerName,
                                  String   remoteServerURL) throws InvalidParameterException
    {
        super(serverName,
              myDescription.getViewServiceFullName(),
              auditLog,
              localServerUserId,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        OpenMetadataClient openMetadataClient = new EgeriaOpenMetadataStoreClient(remoteServerName,
                                                                                  remoteServerURL,
                                                                                  localServerSecretsStoreProvider,
                                                                                  localServerSecretsStoreLocation,
                                                                                  localServerSecretsStoreCollection,
                                                                                  maxPageSize,
                                                                                  auditLog);

        storageVolumeHandler = new StorageVolumeHandler(serverName,
                                                        auditLog,
                                                        myDescription.getViewServiceFullName(),
                                                        openMetadataClient);

        networkHandler = new NetworkHandler(serverName,
                                            auditLog,
                                            myDescription.getViewServiceFullName(),
                                            openMetadataClient);

        assetHandler = new AssetHandler(serverName,
                                        auditLog,
                                        myDescription.getViewServiceFullName(),
                                        openMetadataClient);

        operatingPlatformHandler = new OperatingPlatformHandler(serverName,
                                                                auditLog,
                                                                myDescription.getViewServiceFullName(),
                                                                openMetadataClient);

        softwareCapabilityHandler = new SoftwareCapabilityHandler(serverName,
                                                                  auditLog,
                                                                  myDescription.getViewServiceFullName(),
                                                                  openMetadataClient);

        softwareDevelopmentHandler = new SoftwareDevelopmentHandler(serverName,
                                                                    auditLog,
                                                                    myDescription.getViewServiceFullName(),
                                                                    openMetadataClient);
    }


    /**
     * Return the open metadata handler.
     *
     * @return client
     */
    public StorageVolumeHandler getStorageVolumeHandler()
    {
        return storageVolumeHandler;
    }


    /**
     * Return the open metadata handler.
     *
     * @return client
     */
    public NetworkHandler getNetworkHandler()
    {
        return networkHandler;
    }


    /**
     * Return the open metadata handler.
     *
     * @return client
     */
    public AssetHandler getAssetHandler()
    {
        return assetHandler;
    }


    /**
     * Return the open metadata handler.
     *
     * @return client
     */
    public OperatingPlatformHandler getOperatingPlatformHandler()
    {
        return operatingPlatformHandler;
    }


    /**
     * Return the open metadata handler.
     *
     * @return client
     */
    public SoftwareCapabilityHandler getSoftwareCapabilityHandler()
    {
        return softwareCapabilityHandler;
    }


    /**
     * Return the open metadata handler.
     *
     * @return client
     */
    public SoftwareDevelopmentHandler getSoftwareDevelopmentHandler()
    {
        return softwareDevelopmentHandler;
    }
}
