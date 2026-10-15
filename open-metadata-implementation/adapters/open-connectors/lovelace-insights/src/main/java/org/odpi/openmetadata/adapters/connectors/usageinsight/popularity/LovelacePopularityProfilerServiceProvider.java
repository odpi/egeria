/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.usageinsight.popularity;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.opengovernance.controls.Guard;

import java.util.List;

/**
 * LovelacePopularityProfilerServiceProvider is the connector provider for the Lovelace Popularity Profiler
 * governance action service.
 */
public class LovelacePopularityProfilerServiceProvider extends GovernanceActionServiceProviderBase
{
    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public LovelacePopularityProfilerServiceProvider()
    {
        super(EgeriaOpenConnectorDefinition.POPULARITY_PROFILER_LOVELACE_SERVICE,
              LovelacePopularityProfilerService.class.getName(),
              null);

        super.supportedRequestTypes = null;
        super.supportedRequestParameters = null;
        super.supportedActionTargetTypes = null;
        super.producedGuards = List.of(Guard.SERVICE_COMPLETED.getGuardType(),
                                       Guard.SERVICE_FAILED.getGuardType());
    }
}
