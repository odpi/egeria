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
    /**
     * The request types returned are those that affect the governance action service's behaviour.  Other request types may be used
     * to call the governance action service but they result in default behaviour.
     *
     * @return list of request types with special meaning
     */
    public abstract List<String> supportedRequestTypes();


    /**
     * The request parameters returned are used by the governance action service to control its behaviour.
     *
     * @return list of parameter names with special meaning
     */
    public abstract List<String> supportedRequestParameters();


    /**
     * The request source names returned are the request source names that affect the governance action service's behaviour.  Other request
     * source names may be used in a call the governance action service but they result in default behaviour.
     *
     * @return list of request source names with special meaning
     */
    public abstract List<String> supportedRequestSourceNames();


    /**
     * The action target names returned are those that affect the governance action service's behaviour.  Other action target names may be used
     * in a call the governance action service but they result in default behaviour.
     *
     * @return list of action target names with special meaning
     */
    public abstract List<String> supportedActionTargetNames();


    /**
     * The guards describe the output assessment from the governance action service.  The list returned is the complete list of
     * guards to expect from the governance action service.  They are used when defining governance action processes that choreograph
     * the execution of governance action services using the guards to determine the path in the process to take.
     *
     * @return list of guards produced by this service
     */
    public abstract List<String> supportedGuards();
}
