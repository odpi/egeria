/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.admin;

import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.RegisteredGovernanceServiceElement;
import org.odpi.openmetadata.accessservices.governanceengine.properties.RegisteredGovernanceService;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesErrorCode;

import java.util.Map;

/**
 * GovernanceServiceCache maintains the information about a registered governance service.  A single governance service may be registered using
 * different request types and analysis parameter pairs.
 */
public class GovernanceServiceCache
{
    private Connector                          nextGovernanceService;
    private RegisteredGovernanceServiceElement element;
    private RegisteredGovernanceService        properties;
    private Map<String, Map<String, String>>   requestTypeMapping;


    /**
     * Sets up the cache
     *
     * @param governanceServerName name of this server.
     * @param governanceEngineName name of this engine.
     * @param element registered properties of the governance services
     * @throws InvalidParameterException there is a problem with the connection used to create the
     * governance service instance or the governance service properties are null
     * @throws PropertyServerException problem with the governance service connector or related config
     */
    GovernanceServiceCache(String                              governanceServerName,
                           String                              governanceEngineName,
                           RegisteredGovernanceServiceElement  element) throws InvalidParameterException,
                                                                               PropertyServerException
    {
        final String methodName = "GovernanceServiceCache constructor";

        if ((element != null) && (element.getElementHeader() != null) && (element.getProperties() != null))
        {
            this.element            = element;
            this.properties         = element.getProperties();
            this.requestTypeMapping = properties.getRequestTypes();

            getNextGovernanceService(); /* validate that the connection works */
        }
        else
        {
            throw new PropertyServerException(EngineHostServicesErrorCode.NULL_GOVERNANCE_SERVICE.getMessageDefinition(methodName,
                                                                                                                       governanceEngineName,
                                                                                                                       governanceServerName),
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
        return properties.getQualifiedName();
    }


    /**
     * Simple getter for the governance service's unique identifier (GUID).
     *
     * @return string guid
     */
    public String getGovernanceServiceGUID()
    {
        return element.getElementHeader().getGUID();
    }


    /**
     * Return the analysis parameters to use if none supplied from the caller - these can be null too.
     *
     * @param requestType name of the request type
     * @return map of string property name to string property value
     */
    public Map<String, String> getRequestParameters(String requestType)
    {
        return requestTypeMapping.get(requestType);
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
            ConnectorBroker connectorBroker = new ConnectorBroker();

            nextGovernanceService = connectorBroker.getConnector(properties.getConnection());
        }
        catch (ConnectionCheckedException error)
        {
            throw new InvalidParameterException(error.getReportedErrorMessage(), error, properties.getQualifiedName() + "GovernanceService Connection");
        }
        catch (ConnectorCheckedException error)
        {
            throw new PropertyServerException(error);
        }

        return returnValue;
    }
}
