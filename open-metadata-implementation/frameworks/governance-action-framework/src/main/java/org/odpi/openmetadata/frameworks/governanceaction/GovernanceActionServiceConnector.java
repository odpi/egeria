/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GAFErrorCode;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GovernanceServiceException;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;

import java.util.ArrayList;
import java.util.Arrays;
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
public abstract class GovernanceActionServiceConnector extends ConnectorBase implements GovernanceActionService,
                                                                                        AuditLoggingComponent
{
    protected String          governanceServiceName = "<Unknown>";
    protected AuditLog        auditLog = null;
    protected PropertyHelper  propertyHelper = new PropertyHelper();

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
     * Retrieve a request parameter that is a comma-separated list of strings.
     *
     * @param propertyName name of property
     * @param requestParameters request parameters
     * @param defaultValue value to use if the property is not specified.
     * @return list of strings or null if not set
     */
    protected List<String> getArrayRequestParameter(String              propertyName,
                                                    Map<String, String> requestParameters,
                                                    List<String>        defaultValue)
    {
        if (requestParameters != null)
        {
            if (requestParameters.containsKey(propertyName))
            {
                Object arrayOption = requestParameters.get(propertyName);

                String[] options = arrayOption.toString().split(",");

                return new ArrayList<>(Arrays.asList(options));
            }
        }

        return defaultValue;
    }


    /**
     * Retrieve a request parameter that is a boolean.  If any non-null value is set it returns true unless
     * the value is set to FALSE, False or false.
     *
     * @param propertyName name of property
     * @param requestParameters request parameter
     * @return boolean flag or false if not set
     */
    protected boolean getBooleanRequestParameter(String              propertyName,
                                                 Map<String, String> requestParameters)
    {
        if (requestParameters != null)
        {
            if (requestParameters.containsKey(propertyName))
            {
                String booleanOption = requestParameters.get(propertyName);

                return ((! "FALSE".equals(booleanOption)) && (! "false".equals(booleanOption)) && (! "False".equals(booleanOption)));
            }
        }

        return false;
    }


    /**
     * Retrieve a request parameter that is an integer.
     *
     * @param propertyName name of property
     * @param requestParameters request parameter
     * @return integer value or zero if not supplied
     */
    protected int getIntRequestParameter(String              propertyName,
                                         Map<String, String> requestParameters)
    {
        if (requestParameters != null)
        {
            if (requestParameters.get(propertyName) != null)
            {
                String integerOption = requestParameters.get(propertyName);

                if (integerOption != null)
                {
                    return Integer.parseInt(integerOption);
                }
            }
        }

        return 0;
    }


    /**
     * Retrieve a request parameter that is a long.
     *
     * @param propertyName name of property
     * @param requestParameters request parameter
     * @return long value or zero if not supplied
     */
    protected long getLongRequestParameter(String              propertyName,
                                           Map<String, String> requestParameters)
    {
        if (requestParameters != null)
        {
            if (requestParameters.get(propertyName) != null)
            {
                String integerOption = requestParameters.get(propertyName);

                if (integerOption != null)
                {
                    return Long.parseLong(integerOption);
                }
            }
        }

        return 0L;
    }


    /**
     * Retrieve a request parameter that is a string or null if not set.
     *
     * @param propertyName name of property
     * @param requestParameters request parameter
     * @return string value of property or null if not supplied
     */
    protected String getStringRequestParameter(String              propertyName,
                                               Map<String, String> requestParameters)
    {
        if (requestParameters != null)
        {
            if (requestParameters.get(propertyName) != null)
            {
                return requestParameters.get(propertyName);
            }
        }

        return null;
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        super.disconnectConnectors(this.embeddedConnectors);
        super.disconnect();
    }
}
