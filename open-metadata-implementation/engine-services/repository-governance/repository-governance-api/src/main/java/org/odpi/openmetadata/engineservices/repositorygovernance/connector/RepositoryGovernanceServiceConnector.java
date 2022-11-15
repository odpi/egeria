/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.repositorygovernance.connector;

import org.odpi.openmetadata.engineservices.repositorygovernance.ffdc.RepositoryGovernanceErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.VirtualConnectorExtension;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import java.util.List;


/**
 * RepositoryGovernanceServiceConnector describes a specific type of connector that is responsible for managing the content
 * of a specific open metadata archive.  Information about the available metadata is passed in the archive context.
 * The returned archive context also contains the status of this service.
 */
public abstract class RepositoryGovernanceServiceConnector extends ConnectorBase implements RepositoryGovernanceService,
                                                                                            AuditLoggingComponent,
                                                                                            VirtualConnectorExtension
{
    /*
     * Controls for the connector
     */
    protected String                      repositoryGovernanceServiceName = "<Unknown>";
    protected RepositoryGovernanceContext repositoryGovernanceContext     = null;
    protected AuditLog                    auditLog                        = null;
    protected List<Connector>             embeddedConnectors              = null;



    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    @Override
    public void setAuditLog(AuditLog auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Return the component description that is used by this connector in the audit log.
     *
     * @return id, name, description, wiki page URL.
     */
    @Override
    public ComponentDescription getConnectorComponentDescription()
    {
        if ((this.auditLog != null) && (this.auditLog.getReport() != null))
        {
            return auditLog.getReport().getReportingComponent();
        }

        return null;
    }


    /**
     * Set up the list of connectors that this virtual connector will use to support its interface.
     * The connectors are initialized waiting to start.  When start() is called on the
     * virtual connector, it needs to pass start() to each of the embedded connectors. Similarly for
     * disconnect().
     *
     * @param embeddedConnectors  list of connectors
     */
    @Override
    public void initializeEmbeddedConnectors(List<Connector> embeddedConnectors)
    {
        this.embeddedConnectors = embeddedConnectors;
    }


    /**
     * Set up access to the open metadata ecosystem.
     *
     * @param repositoryGovernanceContext interface to the enterprise repository services and the ability to register a listener to the enterprise
     *                       OMRS topic.
     */
    public synchronized void setRepositoryGovernanceContext(RepositoryGovernanceContext repositoryGovernanceContext)
    {
        this.repositoryGovernanceContext = repositoryGovernanceContext;
    }


    /**
     * Set up the archive service name.  This is used in error messages.
     *
     * @param archiveServiceName name of the archive service
     */
    public void setRepositoryGovernanceServiceName(String archiveServiceName)
    {
        this.repositoryGovernanceServiceName = archiveServiceName;
    }


    /**
     * Return the archive context for this archive service.  This is typically called after the disconnect()
     * method is called.  If called before disconnect(), it may only contain partial results.
     *
     * @return archive context containing the results discovered (so far) by the archive service.
     */
    public synchronized RepositoryGovernanceContext getRepositoryGovernanceContext()
    {
        return repositoryGovernanceContext;
    }


    /**
     * Indicates that the archive service is completely configured and can begin processing.
     * Any embedded connectors are started.
     * This is the method where the function of the archive service is implemented in the subclass.
     *
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() in your version.
     *
     * @throws ConnectorCheckedException there is a problem within the archive service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        validateContext(repositoryGovernanceContext);

    }


    /**
     * Provide a common exception for unexpected errors.
     *
     * @param methodName calling method
     * @param error caught exception
     * @throws ConnectorCheckedException wrapped exception
     */
    protected void handleUnexpectedException(String    methodName,
                                             Exception error) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(RepositoryGovernanceErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryGovernanceServiceName,
                                                                                                                    error.getClass().getName(),
                                                                                                                    methodName,
                                                                                                                    error.getMessage()),
                                            this.getClass().getName(),
                                            methodName);
    }



    /**
     * Verify that the context has been set up for the subclass
     *
     * @param governanceContext context from the subclass
     * @throws ConnectorCheckedException error to say that the connector (governance action service) is not able to proceed because
     * it has not been set up correctly.
     */
    protected void validateContext(RepositoryGovernanceContext governanceContext) throws ConnectorCheckedException
    {
        final String methodName = "start";

        if (governanceContext == null)
        {
            throw new ConnectorCheckedException(RepositoryGovernanceErrorCode.NULL_REPOSITORY_GOVERNANCE_CONTEXT.getMessageDefinition(
                    repositoryGovernanceServiceName),
                                                this.getClass().getName(),
                                                methodName);
        }
    }
}
