/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

/**
 * The triage governance action service is responsible for determining which of the possible courses of action to take for a specific situation.
 * It may involve a human decision maker.
 *
 * To create your own triage governance action service, create a new class that extends this class
 * along with another class that extends the GovernanceActionServiceProviderBase class to act as its connector provider.
 * Add a start() method that begins by calling super.start() and then includes the logic to either make the decision or initiate action to
 * request a third party make the decision.
 * This is done either by calling a third party technology or using the methods available through the governanceContext which is set up
 * just before your start() method is called.  The requestSourceElements describe the situation that the governance action service is assessing
 * and the actionTargetElements describe the elements that should/could be acted upon.
 *
 * Once the set up is complete, or the decision is made, your start() method should call
 * governanceContext.recordCompletionStatus() and then return. The recordCompletionStatus() call will include guards that are used to determine
 * the next step.  This may be to enact the decision or monitor for the completion of the task pushed to an external decision maker.
 * It is through these guards that either the triage decision is communicated, or another governance action service is initiated to
 * take the next step.
 */
public abstract class TriageGovernanceActionService extends GovernanceActionServiceConnector
{
    protected TriageGovernanceContext governanceContext = null;


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
