/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;

import java.util.List;

/**
 * GovernanceActionServiceProviderBase implements the base class for the connector provider for a governance action service.
 */
public abstract class GovernanceActionServiceProviderBase extends ConnectorProviderBase
{
    protected List<String> supportedRequestTypes       = null;
    protected List<String> supportedRequestParameters  = null;
    protected List<String> supportedRequestSourceNames = null;
    protected List<String> supportedTargetActionNames  = null;
    protected List<String> supportedGuards             = null;


    /**
     * The request types returned are those that affect the governance action service's behaviour.  Other request types may be used
     * to call the governance action service but they result in default behaviour.
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
     * source names may be used in a call the governance action service but they result in default behaviour.
     *
     * @return list of request sources with special meaning
     */
    public List<String> supportedRequestSourceNames()
    {
        return supportedRequestSourceNames;
    }


    /**
     * The action target names returned are those that affect the governance action service's behaviour.  Other action target names may be used
     * in a call the governance action service but they result in default behaviour.
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
}
