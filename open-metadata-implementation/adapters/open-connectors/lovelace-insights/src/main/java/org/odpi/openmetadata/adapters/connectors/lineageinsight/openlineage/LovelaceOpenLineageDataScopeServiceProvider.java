/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.lineageinsight.openlineage;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.opengovernance.controls.Guard;

import java.util.List;

/**
 * LovelaceOpenLineageDataScopeServiceProvider is the connector provider for the LovelaceOpenLineageDataScopeService
 * governance action service.  It declares the log store action target and the analysis window request parameter
 * that all the OpenLineage analysis services share.
 */
public class LovelaceOpenLineageDataScopeServiceProvider extends GovernanceActionServiceProviderBase
{
    /**
     * Constructor used to initialize the connector provider with the Java class name of the specific
     * store implementation.
     */
    public LovelaceOpenLineageDataScopeServiceProvider()
    {
        super(EgeriaOpenConnectorDefinition.OPEN_LINEAGE_DATA_SCOPE_LOVELACE_SERVICE,
              LovelaceOpenLineageDataScopeService.class.getName(),
              null);

        super.supportedRequestTypes      = null;
        super.supportedRequestParameters = OpenLineageAnalysisRequestParameter.getRequestParameterTypes();
        super.supportedActionTargetTypes = OpenLineageAnalysisActionTarget.getActionTargetTypes();
        super.producedGuards             = List.of(Guard.SERVICE_COMPLETED.getGuardType(),
                                                   Guard.SERVICE_FAILED.getGuardType());
    }
}
