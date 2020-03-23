/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.server;


import org.odpi.openmetadata.accessservices.assetowner.ffdc.AssetOwnerErrorCode;
import org.odpi.openmetadata.accessservices.assetowner.handlers.FileSystemHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * AssetOwnerServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class AssetOwnerServicesInstance extends OCFOMASServiceInstance
{
    private static AccessServiceDescription myDescription = AccessServiceDescription.ASSET_OWNER_OMAS;

    private FileSystemHandler     fileSystemHandler;

    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that AssetOwner is allowed to serve Assets from.
     * @param defaultZones list of zones that AssetOwner sets up in new Asset instances.
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize max number of results to return on single request.
     * @throws NewInstanceException a problem occurred during initialization
     */
    public AssetOwnerServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                      List<String>            supportedZones,
                                      List<String>            defaultZones,
                                      AuditLog                auditLog,
                                      String                  localServerUserId,
                                      int                     maxPageSize) throws NewInstanceException
    {
        super(myDescription.getAccessServiceFullName(),
              repositoryConnector,
              supportedZones,
              defaultZones,
              auditLog,
              localServerUserId,
              maxPageSize);

        if (repositoryHandler != null)
        {
            this.fileSystemHandler = new FileSystemHandler(serviceName,
                                                           serverName,
                                                           supportedZones,
                                                           assetHandler,
                                                           schemaTypeHandler,
                                                           invalidParameterHandler,
                                                           repositoryHandler,
                                                           repositoryHelper);
        }
        else
        {
            final String methodName = "new ServiceInstance";

            throw new NewInstanceException(AssetOwnerErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);

        }
    }


    /**
     * Return the handler for file system requests.
     *
     * @return handler object
     */
    FileSystemHandler getFileSystemHandler()
    {
        return fileSystemHandler;
    }
}
