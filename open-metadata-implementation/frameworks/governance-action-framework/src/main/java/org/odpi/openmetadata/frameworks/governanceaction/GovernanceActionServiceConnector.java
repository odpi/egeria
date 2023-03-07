/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.VirtualConnectorExtension;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GAFErrorCode;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GovernanceServiceException;

import java.util.List;

/**
 * GovernanceActionServiceConnector describes the base class for a specific type of connector that is responsible for preforming
 * specific governance actions on demand.  There are six types of governance action service:
 *
 * <ul>
 *     <li><b>WatchdogGovernanceActionService</b> - monitors for changes to the metadata elements and initiates other governance actions depending on the nature of the change.</li>
 *     <li><b>VerificationGovernanceActionService</b> - tests values in the metadata elements to detect errors or to classify the status of the metadata elements.</li>
 *     <li><b>TriageGovernanceActionService</b> - manages the choices on how to resolve a situation, often involving a human decision maker.</li>
 *     <li><b>RemediationGovernanceActionService</b> - maintains the metadata elements.</li>
 *     <li><b>ProvisioningGovernanceActionService</b> - provisions resources in the digital landscape and maintains lineage.</li>
 *     <li><b>GeneralGovernanceActionService</b> - combines all of the capability of the specialist services above.</li>
 * </ul>
 *
 * Each type of governance action service is passed a specialized context that provides it with the metadata methods it needs for its specific role.
 * When you build a governance action service, you extend the governance action service class that matches the purpose of your governance action
 * to ensure your code receives a context with the appropriate interface.
 *
 * In addition, there is a generic governance action service called <b>GeneralGovernanceActionService</b> that combines all the functions of the
 * five specialist types of governance action service.  It is used when if is more efficient to combine the functions into one execution.
 */
public abstract class GovernanceActionServiceConnector extends ConnectorBase implements GovernanceActionService,
                                                                                        AuditLoggingComponent,
                                                                                        VirtualConnectorExtension
{
    protected String          governanceServiceName = "<Unknown>";
    protected AuditLog        auditLog = null;
    protected List<Connector> embeddedConnectors = null;

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
    public void initializeEmbeddedConnectors(List<Connector> embeddedConnectors)
    {
        this.embeddedConnectors = embeddedConnectors;
    }


    /**
     * Set up the governance action service name.  This is used in error messages.
     *
     * @param governanceServiceName name of the service
     */
    public void setGovernanceServiceName(String governanceServiceName)
    {
        this.governanceServiceName = governanceServiceName;
    }


    /**
     * Provide a common exception for unexpected errors.
     *
     * @param methodName calling method
     * @param error caught exception
     * @throws GovernanceServiceException wrapped exception
     */
    protected void handleUnexpectedException(String      methodName,
                                             Throwable   error) throws ConnectorCheckedException
    {
        throw new GovernanceServiceException(GAFErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(governanceServiceName,
                                                                                                    error.getClass().getName(),
                                                                                                    methodName,
                                                                                                    error.getMessage()),
                                             this.getClass().getName(),
                                             methodName);
    }


    /**
     * Set up details of the governance action request and access to the metadata store.
     * This method is called before start and should not be null
     *
     * @param governanceContext specialist context for this type of governance action.
     */
    public abstract void setGovernanceContext(GovernanceActionContext governanceContext);


    /**
     * Verify that the context has been set up in the subclass
     *
     * @param governanceContext context from the subclass
     * @throws ConnectorCheckedException error to say that the connector (governance action service) is not able to proceed because
     * it has not been set up correctly.
     */
    public void validateContext(GovernanceContext governanceContext) throws ConnectorCheckedException
    {
        final String methodName = "start";

        if (governanceContext == null)
        {
            throw new GovernanceServiceException(GAFErrorCode.NULL_GOVERNANCE_CONTEXT.getMessageDefinition(governanceServiceName),
                                                 this.getClass().getName(),
                                                 methodName);
        }
    }




    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public  synchronized void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();

        if (this.embeddedConnectors != null)
        {
            for (Connector embeddedConnector : this.embeddedConnectors)
            {
                if (embeddedConnector != null)
                {
                    try
                    {
                        embeddedConnector.disconnect();
                    }
                    catch (Exception error)
                    {
                        // keep going
                    }
                }
            }
        }
    }
}
