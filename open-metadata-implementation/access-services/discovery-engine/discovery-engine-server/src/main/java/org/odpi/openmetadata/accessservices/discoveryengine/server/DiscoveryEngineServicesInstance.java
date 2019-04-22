/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.server;


import org.odpi.openmetadata.accessservices.discoveryengine.ffdc.DiscoveryEngineErrorCode;
import org.odpi.openmetadata.accessservices.discoveryengine.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.accessservices.discoveryengine.handlers.DiscoveryConfigurationServerHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * DiscoveryEngineServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class DiscoveryEngineServicesInstance
{
    private static AccessServiceDescription myDescription = AccessServiceDescription.DISCOVERY_ENGINE_OMAS;

    private List<String>                        supportedZones;
    private OMRSRepositoryConnector             repositoryConnector;
    private OMRSMetadataCollection              metadataCollection;
    private DiscoveryConfigurationServerHandler discoveryConfigurationHandler;
    private String                              serverName;
    private OMRSAuditLog                        auditLog;


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that DiscoveryEngine is allowed to serve Assets from.
     * @throws NewInstanceException a problem occurred during initialization
     */
    public DiscoveryEngineServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                           List<String>            supportedZones,
                                           OMRSAuditLog            auditLog) throws NewInstanceException
    {
        final String methodName = "new ServiceInstance";

        if (repositoryConnector != null)
        {
            try
            {
                this.repositoryConnector = repositoryConnector;
                this.serverName = repositoryConnector.getServerName();
                this.metadataCollection = repositoryConnector.getMetadataCollection();
                this.supportedZones = supportedZones;
                this.auditLog = auditLog;

                this.discoveryConfigurationHandler = new DiscoveryConfigurationServerHandler(myDescription.getAccessServiceName(),
                                                                                             repositoryConnector);

                DiscoveryEngineServicesInstanceMap.setNewInstanceForJVM(serverName, this);
            }
            catch (Throwable error)
            {
                DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.OMRS_NOT_INITIALIZED;
                String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

                throw new NewInstanceException(errorCode.getHTTPErrorCode(),
                                               this.getClass().getName(),
                                               methodName,
                                               errorMessage,
                                               errorCode.getSystemAction(),
                                               errorCode.getUserAction());

            }
        }
        else
        {
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.OMRS_NOT_INITIALIZED;
            String                errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new NewInstanceException(errorCode.getHTTPErrorCode(),
                                           this.getClass().getName(),
                                           methodName,
                                           errorMessage,
                                           errorCode.getSystemAction(),
                                           errorCode.getUserAction());

        }
    }


    /**
     * Return the handler for configuration requests.
     *
     * @return handler object
     */
    DiscoveryConfigurationServerHandler getDiscoveryConfigurationHandler()
    {
        return discoveryConfigurationHandler;
    }


    /**
     * Return the list of zones that this instance of the Asset Consumer OMAS should support.
     *
     * @return list of zone names.
     */
    public List<String> getSupportedZones()
    {
        return supportedZones;
    }


    /**
     * Return the audit log for this access service.
     *
     * @return audit log
     */
    public OMRSAuditLog getAuditLog()
    {
        return auditLog;
    }


    /**
     * Return the server name.
     *
     * @return serverName name of the server for this instance
     * @throws NewInstanceException a problem occurred during initialization
     */
    public String getServerName() throws NewInstanceException
    {
        final String methodName = "getServerName";

        if (serverName != null)
        {
            return serverName;
        }
        else
        {
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.OMRS_NOT_AVAILABLE;
            String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new NewInstanceException(errorCode.getHTTPErrorCode(),
                                           this.getClass().getName(),
                                           methodName,
                                           errorMessage,
                                           errorCode.getSystemAction(),
                                           errorCode.getUserAction());
        }
    }


    /**
     * Return the local metadata collection for this server.
     *
     * @return OMRSMetadataCollection object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    public OMRSMetadataCollection getMetadataCollection() throws PropertyServerException
    {
        final String methodName = "getMetadataCollection";

        if ((repositoryConnector != null) && (metadataCollection != null) && (repositoryConnector.isActive()))
        {
            return metadataCollection;
        }
        else
        {
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.OMRS_NOT_AVAILABLE;
            String                 errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Return the repository connector for this server.
     *
     * @return OMRSRepositoryConnector object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    public OMRSRepositoryConnector  getRepositoryConnector() throws PropertyServerException
    {
        final String methodName = "getRepositoryConnector";

        if ((repositoryConnector != null) && (metadataCollection != null) && (repositoryConnector.isActive()))
        {
            return repositoryConnector;
        }
        else
        {
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.OMRS_NOT_AVAILABLE;
            String                 errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }


    /**
     * Unregister this instance from the instance map.
     */
    public void shutdown() {
        DiscoveryEngineServicesInstanceMap.removeInstanceForJVM(serverName);
    }
}
