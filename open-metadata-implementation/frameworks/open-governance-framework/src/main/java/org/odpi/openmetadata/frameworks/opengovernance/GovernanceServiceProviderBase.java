/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opengovernance;

import org.odpi.openmetadata.frameworks.connectors.OpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.GuardType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.RequestParameterType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.RequestTypeType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

import java.util.List;

/**
 * GovernanceServiceProviderBase implements the base class for the connector provider for a governance service.
 */
public abstract class GovernanceServiceProviderBase extends OpenConnectorProviderBase
{
    /**
     * The type name of the asset that this connector supports.  Initialized to generic type - should be overridden
     * by subclasses.
     */
    protected static String supportedAssetTypeName = DeployedImplementationType.GOVERNANCE_SERVICE.getAssociatedTypeName();
    protected static String supportedDeployedImplementationType = DeployedImplementationType.GOVERNANCE_SERVICE.getDeployedImplementationType();

    /**
     * List of supported request types, along with their descriptions.
     */
    protected List<RequestTypeType> supportedRequestTypes = null;

    /**
     * List of request parameters to use when triggering this governance service, along with their descriptions.
     */
    protected List<RequestParameterType> supportedRequestParameters = null;

    /**
     * List of action targets to use when triggering this governance service, along with their descriptions.
     */
    protected List<ActionTargetType> supportedActionTargetTypes = null;

    /**
     * List of request parameters produced by this governance service, along with their descriptions.
     */
    protected List<RequestParameterType> producedRequestParameters = null;

    /**
     * List of action targets produced by this governance service, along with their descriptions.
     */
    protected List<ActionTargetType> producedActionTargetTypes = null;

    /**
     * List of guards produced by this governance service, along with their descriptions.
     */
    protected List<GuardType> producedGuards = null;


    /**
     * The request types returned are those that affect the governance service's behaviour.
     * Other request types may be used to call the governance service, but they result in default behaviour.
     *
     * @return list of request types
     */
    public List<RequestTypeType> getSupportedRequestTypes()
    {
        return supportedRequestTypes;
    }


    /**
     * Return the list of request parameters to use when triggering this governance service,
     * along with their descriptions.
     *
     * @return list of parameter names
     */
    public List<RequestParameterType> getSupportedRequestParameters()
    {
        return supportedRequestParameters;
    }


    /**
     * Return the list of action targets to use when triggering this governance service, along with their descriptions.
     *
     * @return list of action targets
     */
    public List<ActionTargetType> getSupportedActionTargetTypes() { return supportedActionTargetTypes; }


    /**
     * Return list of request parameters produced by this governance service, along with their descriptions.
     *
     * @return list
     */
    public List<RequestParameterType> getProducedRequestParameters()
    {
        return producedRequestParameters;
    }


    /**
     * Return action targets produced by this governance service, along with their descriptions.
     *
     * @return list
     */
    public List<ActionTargetType> getProducedActionTargetTypes()
    {
        return producedActionTargetTypes;
    }


    /**
     * The guards describe the output from the governance service.  The list returned is the complete list of
     * guards to expect from the governance service along with a description of how the guard is used.
     * These guards are used when defining governance action processes that choreograph
     * the execution of governance services using the guards to determine the path in the process to take.
     *
     * @return list of guards
     */
    public  List<GuardType> getProducedGuards()
    {
        return producedGuards;
    }


    /**
     * Constructor where subclass sets up the connector provider.
     */
    public GovernanceServiceProviderBase()
    {
        super();
    }


    /**
     * Constructor for an open connector provider.
     *
     * @param openConnectorDescription             connector definition
     * @param connectorClassName                   connector class name
     * @param recognizedConfigurationPropertyNames list of recognized configuration property names
     */
    public GovernanceServiceProviderBase(OpenConnectorDefinition openConnectorDescription,
                                         String                  connectorClassName,
                                         List<String>            recognizedConfigurationPropertyNames)
    {
        super(openConnectorDescription, connectorClassName, recognizedConfigurationPropertyNames);
    }
}
