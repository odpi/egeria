/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.provision;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

import java.util.Arrays;
import java.util.Collections;

/**
 * ProvisionUnityCatalogGovernanceActionProvider is the OCF connector provider for the "provision-unity-catalog"
 * Governance Action Service.
 */
public class ProvisionUnityCatalogGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String connectorClassName = ProvisionUnityCatalogGovernanceActionConnector.class.getName();

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public ProvisionUnityCatalogGovernanceActionProvider()
    {
        super(EgeriaOpenConnectorDefinition.PROVISION_UNITY_CATALOG_GOVERNANCE_ACTION_SERVICE,
              connectorClassName,
              null);

        super.supportedRequestParameters = ProvisionUnityCatalogRequestParameter.getRequestParameterTypes();
        super.producedGuards = ProvisionUnityCatalogGuard.getGuardTypes();
        super.producedActionTargetTypes = Collections.singletonList(ActionTarget.NEW_ASSET.getActionTargetType());
        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{
                UnityCatalogDeployedImplementationType.OSS_UC_CATALOG,
                UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA, UnityCatalogDeployedImplementationType.OSS_UC_VOLUME,
                UnityCatalogDeployedImplementationType.OSS_UC_TABLE, UnityCatalogDeployedImplementationType.OSS_UC_FUNCTION});
    }
}
