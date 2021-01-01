/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GAFErrorCode;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GovernanceServiceException;

/**
 * The remediation governance service is responsible for correct omissions and errors in
 * open metadata and the associated digital landscape.
 */
public abstract class RemediationGovernanceService extends GovernanceService
{
    protected RemediationGovernanceContext governanceContext = null;


    /**
     * Set up details of the the governance action request and access to the metadata store.
     * This method is called before start and should not be null
     *
     * @param governanceContext specialist context for this type of governance action.
     */
    public synchronized void setGovernanceContext(RemediationGovernanceContext governanceContext)
    {
        this.governanceContext = governanceContext;
    }


    /**
     * Indicates that the governance service is completely configured and can begin processing.
     * This is where the watchdog governance service registers its listener.  The watchdog
     * will
     *
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() in your version.
     *
     * @throws ConnectorCheckedException there is a problem within the governance service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        if (governanceContext == null)
        {
            throw new GovernanceServiceException(GAFErrorCode.NULL_GOVERNANCE_CONTEXT.getMessageDefinition(governanceServiceName),
                                                 this.getClass().getName(),
                                                 methodName);
        }
    }


    /**
     * Free up any resources held since the connector is no longer needed.  This is a standard
     * method from the Open Connector Framework (OCF).  If you need to override this method
     * be sure to call super.disconnect() in your version.
     *
     * @throws ConnectorCheckedException there is a problem within the governance service.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();
    }
}
