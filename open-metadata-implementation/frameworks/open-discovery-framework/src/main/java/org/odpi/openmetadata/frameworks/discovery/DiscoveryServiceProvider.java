/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;

import java.util.List;

/**
 * DiscoveryServiceProvider implements the base class for the connector provider for a discovery service.
 */
public abstract class DiscoveryServiceProvider extends ConnectorProviderBase
{
    /*
     * The type name of the asset that this connector supports.
     */
    protected static final String supportedAssetTypeName = "OpenDiscoveryService";

    /*
     * Additional configuration to aid choreographing this service in a Governance Action Process.
     */
    protected List<String> supportedRequestTypes       = null;
    protected List<String> supportedAnalysisParameters = null;
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
     * The analysis parameters returned are used by the open discovery service to control its behaviour.
     *
     * @return list of parameter names used if the connector is provisioning
     */
    public List<String> supportedAnalysisParameters()
    {
        return supportedAnalysisParameters;
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
