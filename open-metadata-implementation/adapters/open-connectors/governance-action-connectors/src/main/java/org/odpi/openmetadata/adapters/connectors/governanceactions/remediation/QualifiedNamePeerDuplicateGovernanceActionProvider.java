/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * QualifiedNamePeerDuplicateGovernanceActionProvider is the OCF connector provider for the Deduplication Governance Action Service.
 * This is a Remediation Governance Action Service.
 */
public class QualifiedNamePeerDuplicateGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "346939c4-de2c-44aa-a044-0ec64df0560f";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Remediation:Deduplication";
    private static final String  connectorTypeDisplayName = "Deduplication Governance Action Service";
    private static final String  connectorTypeDescription = "Checks the qualified name of an action target element to determine its duplicates.  Any duplicated found are linked to the action target element.";

    static final String ACTION_TARGET_NAME = "elementGUID";

    private static final String connectorClassName = QualifiedNamePeerDuplicateGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * connector implementation.
     */
    public QualifiedNamePeerDuplicateGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        producedGuards = QualifiedNamePeerDuplicateGuard.getGuardTypes();

        supportedActionTargetTypes = new ArrayList<>();
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setName(ACTION_TARGET_NAME);
        actionTargetType.setTypeName(OpenMetadataType.REFERENCEABLE.typeName);

        super.supportedActionTargetTypes.add(actionTargetType);

        super.setConnectorClassName(connectorClassName);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);

        super.connectorTypeBean = connectorType;
    }
}
