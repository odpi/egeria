/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

/**
 * The remediation governance actin service is responsible for correct omissions and errors in
 * open metadata and the associated digital landscape.
 *
 * To create your own remediation governance action service, create a new class that extends this class
 * along with another class that extends the GovernanceActionServiceProviderBase class to act as its connector provider.
 * Add a start() method that begins by calling super.start() and then includes the logic to correct the metadata elements.
 * This is done using the methods available through the governanceContext which is set up just before your start() method is called.
 *
 * Once the remediation work is complete, your start() method should call
 * governanceContext.recordCompletionStatus() and then return.
 */
public abstract class RemediationGovernanceActionService extends GovernanceActionServiceConnector
{
    protected RemediationGovernanceContext governanceContext = null;


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
