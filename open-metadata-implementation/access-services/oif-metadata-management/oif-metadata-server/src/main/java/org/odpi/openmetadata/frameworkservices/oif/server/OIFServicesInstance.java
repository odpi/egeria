/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.oif.server;


import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIDummyBeanConverter;
import org.odpi.openmetadata.commonservices.generichandlers.SoftwareCapabilityHandler;
import org.odpi.openmetadata.commonservices.multitenant.AccessServerServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworkservices.oif.ffdc.OpenIntegrationErrorCode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * OIFServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 * It is created by the admin class during server start up and
 */
public class OIFServicesInstance extends AccessServerServiceInstance
{
    private final static AccessServiceDescription myDescription = AccessServiceDescription.OIF_METADATA_MANAGEMENT;

    private final SoftwareCapabilityHandler<Object> metadataSourceHandler;

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
    public OIFServicesInstance(OMRSRepositoryConnector repositoryConnector,
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
              maxPageSize);

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
}
