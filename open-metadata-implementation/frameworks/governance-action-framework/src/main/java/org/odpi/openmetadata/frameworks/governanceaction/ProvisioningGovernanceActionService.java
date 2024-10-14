/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

import java.util.Map;

/**
 * The provisioning governance action service is responsible for provisioning real-world resources in the digital
 * landscape and maintaining the Assets and lineage associated with them.
 * To create your own provisioning governance action service, create a new class that extends this class
 * along with another class that extends the GovernanceActionServiceProviderBase class to act as its connector provider.
 * Add a start() method that begins by calling super.start() and then includes the provisioning logic.
 * You may also wish to add the metadata for the new assets, a process to represent this governance action service's
 * activity that are linked with the source asset(s) using LineageMapping relationships:
 * <b>sourceAsset -LineageMapping- provisioningProcess -LineageMapping- targetAsset</b>
 * This is done using the methods available through the governanceContext which is set up just before your start() method is called.
 * Once the provisioning work is complete, and any lineage metadata is created, your start() method should call
 * governanceContext.recordCompletionStatus() and then return.
 */
public abstract class ProvisioningGovernanceActionService extends GovernanceActionServiceConnector
{
    protected ProvisioningGovernanceContext governanceContext = null;


    /**
     * Set up details of the governance action request and access to the metadata store.
     * This method is called before start and should not be null
     *
     * @param governanceContext specialist context for this type of governance action.
     */
    public void setGovernanceContext(GovernanceActionContext governanceContext)
    {
        this.governanceContext = governanceContext;
    }


    /**
     * Retrieve the property value from the values passed to this governance action service.
     *
     * @param propertyName name of the property
     * @param defaultValue default value
     * @return property value
     */
    protected String getProperty(String propertyName, String defaultValue)
    {
        Map<String, String> requestParameters       = governanceContext.getRequestParameters();
        Map<String, Object> configurationProperties = connectionProperties.getConfigurationProperties();

        String propertyValue = defaultValue;

        if ((requestParameters != null) && (requestParameters.get(propertyName) != null))
        {
            propertyValue = requestParameters.get(propertyName);
        }
        else
        {
            if ((configurationProperties != null) && (configurationProperties.get(propertyName) != null))
            {
                propertyValue = configurationProperties.get(propertyName).toString();
            }
        }

        return propertyValue;
    }


    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
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
     * or the administrator requested this governance action service stop running or the hosting server is shutting down.
     * If disconnect completes before the governance action service records
     * its completion status then the governance action service is restarted either at the administrator's request or the next time the server starts.
     * If you do not want this governance action service restarted, be sure to record the completion status in disconnect().
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
