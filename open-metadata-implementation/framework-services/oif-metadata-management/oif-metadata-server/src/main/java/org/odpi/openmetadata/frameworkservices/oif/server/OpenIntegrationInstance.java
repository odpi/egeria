/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.oif.server;


import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIDummyBeanConverter;
import org.odpi.openmetadata.commonservices.generichandlers.SoftwareCapabilityHandler;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworkservices.oif.ffdc.OpenIntegrationErrorCode;
import org.odpi.openmetadata.frameworkservices.oif.handlers.OpenIntegrationHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * OpenIntegrationInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 * It is created by the admin class during server start up and
 */
public class OpenIntegrationInstance extends OMASServiceInstance
{
    private final static CommonServicesDescription myDescription = CommonServicesDescription.OIF_METADATA_MANAGEMENT;

    private final SoftwareCapabilityHandler<Object> metadataSourceHandler;
    private final OpenIntegrationHandler               openIntegrationHandler;

    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize max number of results to return on single request.
     *
     * @throws NewInstanceException a problem occurred during initialization
     */
    public OpenIntegrationInstance(OMRSRepositoryConnector repositoryConnector,
                                   AuditLog                auditLog,
                                   String                  localServerUserId,
                                   int                     maxPageSize) throws NewInstanceException
    {
        super(myDescription.getServiceName(),
              repositoryConnector,
              null,
              null,
              null,
              auditLog,
              localServerUserId,
              maxPageSize,
              null,
              null,
              null,
              null);

        final String methodName = "new ServiceInstance";

        if (repositoryHandler != null)
        {
            this.metadataSourceHandler = new SoftwareCapabilityHandler<>(new OpenMetadataAPIDummyBeanConverter<>(repositoryHelper, serviceName, serverName),
                                                                         Object.class,
                                                                         serviceName,
                                                                         serverName,
                                                                         invalidParameterHandler,
                                                                         repositoryHandler,
                                                                         repositoryHelper,
                                                                         localServerUserId,
                                                                         securityVerifier,
                                                                         supportedZones,
                                                                         defaultZones,
                                                                         publishZones,
                                                                         auditLog);


            this.openIntegrationHandler = new OpenIntegrationHandler(serviceName,
                                                                     serverName,
                                                                     invalidParameterHandler,
                                                                     repositoryHandler,
                                                                     repositoryHelper,
                                                                     localServerUserId,
                                                                     securityVerifier,
                                                                     supportedZones,
                                                                     defaultZones,
                                                                     publishZones,
                                                                     auditLog);
        }
        else
        {
            throw new NewInstanceException(OpenIntegrationErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);
        }
    }


    /**
     * Return the handler for managing software server capability objects representing the integrator.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    SoftwareCapabilityHandler<Object> getMetadataSourceHandler() throws PropertyServerException
    {
        final String methodName = "getMetadataSourceHandler";

        validateActiveRepository(methodName);

        return metadataSourceHandler;
    }


    /**
     * Return the handler for open integration requests.
     *
     * @return handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    OpenIntegrationHandler getOpenIntegrationHandler() throws PropertyServerException
    {
        final String methodName = "getOpenIntegrationHandler";

        validateActiveRepository(methodName);

        return openIntegrationHandler;
    }
}
