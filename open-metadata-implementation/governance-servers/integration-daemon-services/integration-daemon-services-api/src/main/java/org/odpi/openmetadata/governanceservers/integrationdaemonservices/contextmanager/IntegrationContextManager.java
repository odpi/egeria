/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.integrationdaemonservices.contextmanager;

import org.odpi.openmetadata.adminservices.configuration.properties.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnector;

import java.util.Map;

/**
 * IntegrationContextManager is the base class for the context manager that is implemented
 */
public abstract class IntegrationContextManager
{
    protected String   partnerOMASPlatformRootURL  = null;
    protected String   partnerOMASServerName       = null;
    protected String   localServerUserId           = null;
    protected String   localServerPassword         = null;
    protected int      maxPageSize                 = 0;
    protected AuditLog auditLog                    = null;


    /**
     * Default constructor
     */
    protected IntegrationContextManager()
    {
    }


    /**
     * Initialize server properties for the context manager.
     *
     * @param partnerOMASServerName name of the server to connect to
     * @param partnerOMASPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param maxPageSize maximum number of results that can be returned on a single REST call
     * @param auditLog logging destination
     */
    public void initializeContextManager(String   partnerOMASServerName,
                                         String   partnerOMASPlatformRootURL,
                                         String   userId,
                                         String   password,
                                         int      maxPageSize,
                                         AuditLog auditLog)
    {
        this.partnerOMASPlatformRootURL = partnerOMASPlatformRootURL;
        this.partnerOMASServerName      = partnerOMASServerName;
        this.localServerUserId          = userId;
        this.localServerPassword        = password;
        this.maxPageSize                = maxPageSize;
        this.auditLog                   = auditLog;
    }


    /**
     * Suggestion for subclass to create client(s) to partner OMAS.
     *
     * @throws InvalidParameterException the subclass is not able to create one of its clients
     */
    public abstract void createClients() throws InvalidParameterException;


    /**
     * Set up the context in the supplied connector. This is called between initialize() and start() on the connector.
     *
     * @param connectorId unique identifier of the connector (used to configure the event listener)
     * @param connectorName name of connector from config
     * @param metadataSourceQualifiedName unique name of the software server capability that represents the metadata source.
     * @param integrationConnector connector created from connection integration service configuration
     * @param permittedSynchronization controls the direction(s) that metadata is allowed to flow
     * @param serviceOptions options from the integration service's configuration
     *
     * @throws InvalidParameterException the connector is not of the correct type
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public abstract void setContext(String                   connectorId,
                                    String                   connectorName,
                                    String                   metadataSourceQualifiedName,
                                    IntegrationConnector     integrationConnector,
                                    PermittedSynchronization permittedSynchronization,
                                    Map<String, Object>      serviceOptions) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;
}
