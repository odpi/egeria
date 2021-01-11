/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.admin;

import org.odpi.openmetadata.accessservices.governanceengine.properties.GovernanceEngineProperties;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;


/**
 * GovernanceServiceHandler provides the thread to run a governance service.  A new instance is created for each request.
 * The subclasses implement the run method.
 */
public abstract class GovernanceServiceHandler implements Runnable
{
    protected GovernanceEngineProperties governanceActionEngineProperties;
    protected String                     governanceActionEngineGUID;
    protected String                     governanceActionServiceName;

    private   Connector governanceService;
    protected String    governanceActionGUID;
    protected String    requestType;
    protected AuditLog  auditLog;


    /**
     * Constructor sets up the key parameters for running the governance action service.
     * This call is made on the REST call's thread so the properties are just cached.
     * The action happens in the run() method.
     *
     * @param governanceActionEngineProperties properties of the governance action engine - used for message logging
     * @param governanceActionEngineGUID unique Identifier of the governance action engine - used for message logging
     * @param governanceActionGUID unique identifier of the governance action that triggered this governance service
     * @param requestType incoming request type
     * @param governanceActionServiceName name of this governance action service - used for message logging
     * @param auditLog destination for log messages
     */
    protected GovernanceServiceHandler(GovernanceEngineProperties governanceActionEngineProperties,
                                       String                     governanceActionEngineGUID,
                                       String                     governanceActionGUID,
                                       String                     requestType,
                                       String                     governanceActionServiceName,
                                       Connector                  governanceService,
                                       AuditLog                   auditLog)
    {
        this.governanceActionEngineProperties = governanceActionEngineProperties;
        this.governanceActionEngineGUID       = governanceActionEngineGUID;
        this.governanceActionServiceName      = governanceActionServiceName;
        this.governanceActionGUID             = governanceActionGUID;
        this.requestType                      = requestType;
        this.governanceService                = governanceService;
        this.auditLog                         = auditLog;
    }


    /**
     * Disconnect the governance action service.  Called because the governance action service had set a completion status or
     * the server is shutting down.
     *
     * @throws ConnectorCheckedException connector is in trouble
     */
    public void disconnect() throws ConnectorCheckedException
    {
        if (governanceService != null)
        {
            governanceService.disconnect();
        }
    }
}
