/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opengovernance;


import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * GovernanceActionService describes the base class for a governance action service connector that is responsible for preforming
 * specific governance actions on demand.  There are five types of actions that s governance action service performs:
 *
 * <ul>
 *     <li><b>Watchdog</b> - monitors for changes to the metadata elements and initiates other governance actions depending on the nature of the change.</li>
 *     <li><b>Verification</b> - tests values in the metadata elements to detect errors or to classify the status of the metadata elements.</li>
 *     <li><b>Triage</b> - manages the choices on how to resolve a situation, often involving a human decision maker.</li>
 *     <li><b>Remediation</b> - maintains the metadata elements.</li>
 *     <li><b>Provisioning</b> - provisions resources in the digital landscape and maintains lineage.</li>
 * </ul>
 *
 * Each type of action is supported through the governance context that provides it with the metadata methods it needs for its specific role.
 * When you build a governance action service, you extend this governance action service class and access services through the context.
 */
public abstract class GeneralGovernanceActionService extends GovernanceActionServiceConnector
{
    protected GovernanceActionContext governanceContext = null;


    /**
     * Set up details of the governance action request and access to the metadata store.
     * This method is called before start and should not be null
     *
     * @param governanceContext specialist context for this type of governance action.
     */
    @Override
    public void setGovernanceContext(GovernanceActionContext governanceContext)
    {
        this.governanceContext = governanceContext;
    }


    /**
     * Retrieve the first action target element that matches the name.
     *
     * @param actionTargetName name of the action target
     * @return property value
     */
    protected ActionTargetElement getActionTarget(String actionTargetName)
    {
        if (governanceContext.getActionTargetElements() != null)
        {
            for (ActionTargetElement actionTargetElement : governanceContext.getActionTargetElements())
            {
                if (actionTargetElement != null)
                {
                    if ((actionTargetName == null) && (actionTargetElement.getActionTargetName() == null))
                    {
                        return actionTargetElement;
                    }
                    else if ((actionTargetName != null) && (actionTargetName.equals(actionTargetElement.getActionTargetName())))
                    {
                        return actionTargetElement;
                    }
                }
            }
        }

        return null;
    }


    /**
     * Retrieve the all the action target elements that matches the name.
     *
     * @param actionTargetName name of the action target
     * @return property value
     */
    protected List<ActionTargetElement> getAllActionTargets(String actionTargetName)
    {
        if (governanceContext.getActionTargetElements() != null)
        {
            List<ActionTargetElement> results = new ArrayList<>();

            for (ActionTargetElement actionTargetElement : governanceContext.getActionTargetElements())
            {
                if (actionTargetElement != null)
                {
                    if ((actionTargetName == null) && (actionTargetElement.getActionTargetName() == null))
                    {
                        results.add(actionTargetElement);
                    }
                    else if ((actionTargetName != null) && (actionTargetName.equals(actionTargetElement.getActionTargetName())))
                    {
                        results.add(actionTargetElement);
                    }
                }
            }

            if (! results.isEmpty())
            {
                return results;
            }
        }

        return null;
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
        Map<String, Object> configurationProperties = connectionBean.getConfigurationProperties();

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
     * be sure to call super.start() in your version.
     *
     * @throws ConnectorCheckedException a problem within the governance action service.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();
        super.validateContext(governanceContext);
    }


    /**
     * Disconnect is called either because this governance action service called governanceContext.recordCompletionStatus()
     * or the administrator requested this governance action service stop running or the hosting server is shutting down.
     * If disconnect completes before the governance action service records
     * its completion status then the governance action service is restarted either at the administrator's request
     * or the next time the server starts.
     * If you do not want this governance action service restarted, be sure to record the completion status in disconnect().
     * The disconnect() method is a standard method from the Open Connector Framework (OCF).  If you need to override this method
     * be sure to call super.disconnect() in your version.
     *
     * @throws ConnectorCheckedException a problem within the governance action service.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();
    }
}
