/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.provision;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

import java.util.Arrays;

/**
 * ProvisionUnityCatalogGovernanceActionProvider is the OCF connector provider for the "provision-unity-catalog"
 * Governance Action Service.
 */
public class ProvisionUnityCatalogGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int     connectorComponentId   = 698;

    /*
     * Unique identifier for the connector type.
     */
    private static final String  connectorTypeGUID = "58681cd6-ded2-488b-be8d-031e42cb345c";
    private static final String connectorTypeQualifiedName = "Egeria:GovernanceActionService:UnityCatalog:Provision";
    private static final String connectorTypeDisplayName   = "Provision Unity Catalog Governance Action Service";
    private static final String connectorTypeDescription   = "Governance Action Service that creates descriptions of a new unity catalog resource that is used to provision an equivalent resource in a Unity Catalog (UC) server.";
    private static final String connectorWikiPage = "https://egeria-project.org/connectors/unity-catalog/catalog-survey-service/";
    private static final String connectorClassName = ProvisionUnityCatalogGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public ProvisionUnityCatalogGovernanceActionProvider()
    {
        super();

        super.supportedRequestParameters = ProvisionUnityCatalogRequestParameter.getRequestParameterTypes();
        super.producedGuards = ProvisionUnityCatalogGuard.getGuardTypes();
        super.producedActionTargetTypes = Arrays.asList(new ActionTargetType[]{ActionTarget.NEW_ASSET.getActionTargetType()});

        super.setConnectorClassName(connectorClassName);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setSupportedDeployedImplementationType(supportedDeployedImplementationType);

        super.connectorTypeBean = connectorType;

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{
                UnityCatalogDeployedImplementationType.OSS_UC_CATALOG,
                UnityCatalogDeployedImplementationType.OSS_UC_SCHEMA, UnityCatalogDeployedImplementationType.OSS_UC_VOLUME,
                UnityCatalogDeployedImplementationType.OSS_UC_TABLE, UnityCatalogDeployedImplementationType.OSS_UC_FUNCTION});

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.TECHNICAL_PREVIEW);
        componentDescription.setComponentName(connectorTypeDisplayName);
        componentDescription.setComponentDescription(connectorTypeDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);

    }
}
