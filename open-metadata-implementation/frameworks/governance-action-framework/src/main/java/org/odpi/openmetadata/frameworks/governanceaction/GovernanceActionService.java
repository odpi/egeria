/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;


import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;


/**
 * GovernanceActionService describes the base class for a specific type of connector that is responsible for preforming
 * specific governance actions on demand.  There are five types of governance action service:
 *
 * <ul>
 *     <li><b>WatchdogGovernanceActionService</b> - monitors for changes to the metadata elements and initiates other governance actions depending on the nature of the change.</li>
 *     <li><b>VerificationGovernanceActionService</b> - tests values in the metadata elements to detect errors or to classify the status of the metadata elements.</li>
 *     <li><b>TriageGovernanceActionService</b> - manages the choices on how to resolve a situation, often involving a human decision maker.</li>
 *     <li><b>RemediationGovernanceActionService</b> - maintains the metadata elements.</li>
 *     <li><b>ProvisioningGovernanceActionService</b> - provisions resources in the digital landscape and maintains lineage.</li>
 * </ul>
 *
 * Each type of governance action service is passed a specialized context that provides it with the metadata methods it needs for its specific role.
 * When you build a governance action service, you extend the governance action service class that matches the purpose of your governance action
 * to ensure your code receives a context with the appropriate interface.
 */
public abstract class GovernanceActionService extends GovernanceActionServiceConnector
{
    protected GovernanceActionContext governanceContext = null;


    /**
     * Set up details of the governance action request and access to the metadata store.
     * This method is called before start and should not be null
     *
     * @param governanceContext specialist context for this type of governance action.
     */
    public synchronized void setGovernanceContext(GovernanceActionContext governanceContext)
    {
        this.governanceContext = governanceContext;
    }


    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     *
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() in your version.
     *
     * @throws ConnectorCheckedException there is a problem within the governance action service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();
        super.validateContext(governanceContext);
    }


    /**
     * Disconnect is called either because this governance action service called governanceContext.recordCompletionStatus()
     * or the administer requested this governance action service stop running or the hosting server is shutting down.
     *
     * If disconnect completes before the governance action service records
     * its completion status then the governance action service is restarted either at the administrator's request
     * or the next time the server starts.
     * If you do not want this governance action service restarted, be sure to record the completion status in disconnect().
     *
     * The disconnect() method is a standard method from the Open Connector Framework (OCF).  If you need to override this method
     * be sure to call super.disconnect() in your version.
     *
     * @throws ConnectorCheckedException there is a problem within the governance action service.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();
    }
}
