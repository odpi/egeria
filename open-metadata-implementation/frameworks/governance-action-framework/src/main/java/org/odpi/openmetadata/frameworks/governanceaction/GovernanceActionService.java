/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GAFErrorCode;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GovernanceServiceException;

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
 * Each type of governance action service is passed a specialize context that provides it with the metadata methods it needs for its specific role.
 * When you build a governance action service, you extend the governance action service class that matches the purpose of your governance action
 * to ensure your code receives a context with the appropriate interface.
 */
public abstract class GovernanceActionService extends ConnectorBase implements AuditLoggingComponent
{
    protected String   governanceServiceName = "<Unknown>";
    protected AuditLog auditLog = null;

    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    public void setAuditLog(AuditLog auditLog)
    {
        this.auditLog = auditLog;
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
}
