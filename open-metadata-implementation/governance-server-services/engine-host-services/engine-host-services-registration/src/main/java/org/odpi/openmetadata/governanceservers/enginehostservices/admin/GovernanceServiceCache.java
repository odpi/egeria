/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.admin;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RegisteredGovernanceServiceElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RegisteredGovernanceServiceProperties;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesErrorCode;

import java.util.HashMap;
import java.util.Map;

/**
 * GovernanceServiceCache maintains the information about a registered governance service.  A single governance service may be registered using
 * different request types and analysis parameter pairs.
 */
public class GovernanceServiceCache
{
    private final String              governanceServiceGUID;
    private final String              governanceServiceName;
    private final String              serviceRequestType;
    private final Map<String, String> requestParameters;
    private final Connection          serviceConnection;
    private final AuditLog            auditLog;

    private Connector                 nextGovernanceService;


    /**
     * Sets up the cache
     *
     * @param governanceServerName name of this server
     * @param governanceEngineName name of this engine
     * @param element registered properties of the governance services
     * @param auditLog logging destination for governance services
     * @throws InvalidParameterException there is a problem with the connection used to create the
     * governance service instance or the governance service properties are null
     * @throws PropertyServerException problem with the governance service connector or related config
     */
    GovernanceServiceCache(String                              governanceServerName,
                           String                              governanceEngineName,
                           RegisteredGovernanceServiceElement  element,
                           String                              requestType,
                           AuditLog                            auditLog) throws InvalidParameterException,
                                                                                PropertyServerException
    {
        final String methodName = "GovernanceServiceCache constructor";

        this.auditLog = auditLog;

        if ((requestType != null) &&
                    (element != null) && (element.getElementHeader() != null) && (element.getProperties() != null) &&
                    (element.getProperties().getRequestTypes() != null) && (! element.getProperties().getRequestTypes().isEmpty()))
        {
            this.governanceServiceGUID = element.getElementHeader().getGUID();
            this.governanceServiceName = element.getProperties().getQualifiedName();
            this.serviceConnection = element.getProperties().getConnection();

            RegisteredGovernanceServiceProperties registeredGovernanceServiceProperties = element.getProperties().getRequestTypes().get(requestType);

            if (registeredGovernanceServiceProperties.getServiceRequestType() != null)
            {
                this.serviceRequestType = registeredGovernanceServiceProperties.getServiceRequestType();
            }
            else
            {
                this.serviceRequestType = requestType;
            }

            this.requestParameters  = registeredGovernanceServiceProperties.getRequestParameters();

            getNextGovernanceService(); /* validate that the connection works */
        }
        else
        {
            String elementString = "<null>";
            if (element != null)
            {
                elementString = element.toString();
            }
            throw new PropertyServerException(EngineHostServicesErrorCode.NULL_GOVERNANCE_SERVICE.getMessageDefinition(methodName,
                                                                                                                       governanceEngineName,
                                                                                                                       governanceServerName,
                                                                                                                       elementString),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Simple getter for the governance service name - used in messages.
     *
     * @return name
     */
    public String getGovernanceServiceName()
    {
        return this.governanceServiceName;
    }


    /**
     * Simple getter for the governance service's unique identifier (GUID).
     *
     * @return string guid
     */
    public String getGovernanceServiceGUID()
    {
        return governanceServiceGUID;
    }


    /**
     * Simple getter for the requestType to pass to governance service.
     *
     * @return string name
     */
    public String getServiceRequestType()
    {
        return serviceRequestType;
    }


    /**
     * Return the request/analysis parameters to use if none supplied from the caller - these can be null too.
     *
     * @param suppliedRequestParameters request parameters from the caller
     * @return map of string property name to string property value
     */
    public Map<String, String> getRequestParameters(Map<String, String> suppliedRequestParameters)
    {
        Map<String, String> runRequestParameters;

        if (this.requestParameters == null)
        {
            runRequestParameters = new HashMap<>();
        }
        else
        {
            runRequestParameters = new HashMap<>(this.requestParameters);
        }

        if (suppliedRequestParameters != null)
        {
            runRequestParameters.putAll(suppliedRequestParameters);
        }

        if (runRequestParameters.isEmpty())
        {
            runRequestParameters = null;
        }

        return runRequestParameters;
    }


    /**
     * Return a governance service connector instance using the registered properties for the governance service.
     *
     * @return connector
     * @throws InvalidParameterException bad connection
     * @throws PropertyServerException problem with the governance service connector
     */
    public synchronized Connector  getNextGovernanceService() throws InvalidParameterException,
                                                                     PropertyServerException
    {
        Connector  returnValue = nextGovernanceService;

        try
        {
            ConnectorBroker connectorBroker = new ConnectorBroker(auditLog);

            nextGovernanceService = connectorBroker.getConnector(serviceConnection);
        }
        catch (ConnectionCheckedException error)
        {
            throw new InvalidParameterException(error.getReportedErrorMessage(), error, governanceServiceName + "GovernanceService Connection");
        }
        catch (ConnectorCheckedException error)
        {
            throw new PropertyServerException(error);
        }

        return returnValue;
    }
}
