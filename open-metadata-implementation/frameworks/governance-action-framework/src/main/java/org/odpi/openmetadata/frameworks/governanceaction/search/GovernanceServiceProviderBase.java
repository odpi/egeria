/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.search;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.governanceaction.actiontargettype.ActionTargetType;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GovernanceServiceProviderBase implements the base class for the connector provider for a governance service.
 */
public abstract class GovernanceServiceProviderBase extends ConnectorProviderBase
{
    /**
     * The type name of the asset that this connector supports.  Initialized to generic type - should be overridden
     * by subclasses.
     */
    protected static String supportedAssetTypeName = OpenMetadataType.GOVERNANCE_SERVICE.typeName;

    /**
     * Additional configuration to aid configuring this service.  It is set up by the concrete implementation
     * of the governance service.
     */
    protected List<String> supportedRequestTypes       = null;

    /**
     * Additional configuration to aid configuring this service.  It is set up by the concrete implementation
     * of the governance service.
     */
    protected List<String> supportedRequestParameters  = null;

    /**
     * Additional configuration to aid configuring this service.  It is set up by the concrete implementation
     * of the governance service.
     */
    protected List<String> supportedRequestSourceNames = null;

    /**
     * Additional configuration to aid configuring this service.  It is set up by the concrete implementation
     * of the governance service.
     */
    protected List<String> supportedTargetActionNames  = null;

    /**
     * Additional configuration to aid configuring this service.  It is set up by the concrete implementation
     * of the governance service.
     */
    protected List<String> supportedGuards = null;

    /**
     * Map of the supported action target names to the types of entity to map it to.
     */
    protected Map<String, ActionTargetType> actionTargetTypes = null;


    /**
     * The request types returned are those that affect the governance action service's behaviour.  Other request types may be used
     * to call the governance action service, but they result in default behaviour.
     *
     * @return list of request types
     */
    public List<String> supportedRequestTypes()
    {
        return supportedRequestTypes;
    }


    /**
     * The request parameters returned are used by the governance action service to control its behaviour.
     *
     * @return list of parameter names used if the connector is provisioning
     */
    public List<String> supportedRequestParameters()
    {
        return supportedRequestParameters;
    }


    /**
     * The request source names returned are the request source names that affect the governance action service's behaviour.  Other request
     * source names may be used in a call the governance action service, but they result in default behaviour.
     *
     * @return list of request sources with special meaning
     */
    public List<String> supportedRequestSourceNames()
    {
        return supportedRequestSourceNames;
    }


    /**
     * The action target names returned are those that affect the governance action service's behaviour.  Other action target names may be used
     * in a call the governance action service, but they result in default behaviour.
     *
     * @return list of action target names with special meaning
     */
    public List<String> supportedActionTargetNames()
    {
        return supportedTargetActionNames;
    }


    /**
     * The guards describe the output assessment from the governance action service.  The list returned is the complete list of
     * guards to expect from the governance action service.  They are used when defining governance action processes that choreograph
     * the execution of governance action services using the guards to determine the path in the process to take.
     *
     * @return list of guards
     */
    public  List<String> supportedGuards()
    {
        return supportedGuards;
    }


    /**
     * Return the map of supported action target types for this governance service.
     *
     * @return map of action target name to open metadata type name.  Map is empty if no action target types are defined.
     */
    public Map<String, ActionTargetType> getActionTargetTypes() { return actionTargetTypes; }
}
