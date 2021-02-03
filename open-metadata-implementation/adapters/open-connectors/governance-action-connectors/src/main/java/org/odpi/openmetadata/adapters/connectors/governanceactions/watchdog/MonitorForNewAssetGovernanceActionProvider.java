/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;

import java.util.ArrayList;
import java.util.List;

/**
 * MonitorForNewAssetGovernanceActionProvider is the OCF connector provider for the Monitor for New Asset Governance Action Service.
 * This is is a Watchdog Governance Action Service.
 */
public class MonitorForNewAssetGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "8145967e-bb83-44b2-bc8c-68112c6a5a06";
    private static final String  connectorTypeName = "Monitor for New Asset Governance Action Service";
    private static final String  connectorTypeDescription = "Watchdog Governance Action Service that detects new assets.";

    static final String INTERESTING_ASSET_TYPE_NAME_PROPERTY                = "interestingAssetTypeName";

    static final String NO_LINEAGE_CONFIGURATION_PROPERTY                   = "createLineage";
    static final String LINEAGE_PROCESS_NAME_CONFIGURATION_PROPERTY         = "processName";
    static final String TARGET_FILE_NAME_PATTERN_CONFIGURATION_PROPERTY     = "targetFileNamePattern";

    static final String PROCESS_SINGLE_EVENT    = "process-single-event";
    static final String PROCESS_MULTIPLE_EVENTS = "process-multiple-events";

    static final String MONITORING_COMPLETE = "monitoring-complete";
    static final String MONITORING_SHUTDOWN = "monitoring-shutdown";
    static final String MONITORING_FAILED   = "monitoring-failed";

    private static final String connectorClassName = MonitorForNewAssetGovernanceActionConnector.class.getName();

    private List<String> supportedGuards = new ArrayList<>();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public MonitorForNewAssetGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedGuards.add(MONITORING_COMPLETE);
        supportedGuards.add(MONITORING_SHUTDOWN);
        supportedGuards.add(MONITORING_FAILED);

        super.setConnectorClassName(connectorClassName);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
    }


    /**
     * The request types returned are those that affect the governance action service's behaviour.  Other request types may be used
     * to call the governance action service but they result in default behaviour.
     *
     * @return list of request types
     */
    @Override
    public List<String> supportedRequestTypes()
    {
        return null;
    }


    /**
     * The request parameters returned are used by the governance action service to control its behaviour.
     *
     * @return list of parameter names used if the connector is provisioning
     */
    @Override
    public List<String> supportedRequestParameters()
    {
        return null;
    }


    /**
     * The request source names returned are the request source names that affect the governance action service's behaviour.  Other request
     * source names may be used in a call the governance action service but they result in default behaviour.
     *
     * @return null since request sources are ignored
     */
    @Override
    public List<String> supportedRequestSourceNames()
    {
        return null;
    }


    /**
     * The action target names returned are those that affect the governance action service's behaviour.  Other action target names may be used
     * in a call the governance action service but they result in default behaviour.
     *
     * @return list of action target names with special meaning
     */
    @Override
    public List<String> supportedActionTargetNames()
    {
        return null;
    }


    /**
     * The guards describe the output assessment from the governance action service.  The list returned is the complete list of
     * guards to expect from the governance action service.  They are used when defining governance action processes that choreograph
     * the execution of governance action services using the guards to determine the path in the process to take.
     *
     * @return list of guards
     */
    @Override
    public  List<String> supportedGuards()
    {
        return supportedGuards;
    }
}
