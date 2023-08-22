/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;

import java.util.ArrayList;

/**
 * OriginSeekerGovernanceActionProvider is the OCF connector provider for the Origin Seeker Governance Action Service.
 * This is a Remediation Governance Action Service.
 */
public class OriginSeekerGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "1c6939c4-de2c-44aa-a044-0ec64df0560f";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Remediation:OriginSeeker";
    private static final String  connectorTypeDisplayName = "Origin Seeker Governance Action Service";
    private static final String  connectorTypeDescription = "Follows the lineage mapping for an action target element to determine its origin.";

    static final String ORIGIN_ASSIGNED_GUARD           = "origin-assigned";
    static final String ORIGIN_ALREADY_ASSIGNED_GUARD   = "origin-already-assigned";
    static final String MULTIPLE_ORIGINS_DETECTED_GUARD = "multiple-origins-detected";
    static final String NO_ORIGINS_DETECTED_GUARD       = "no-origins-detected";
    static final String NO_TARGETS_DETECTED_GUARD       = "no-targets-detected";
    static final String MULTIPLE_TARGETS_DETECTED_GUARD = "multiple-targets-detected";
    static final String ORIGIN_SEEKING_FAILED_GUARD     = "origin-seeking-failed";

    private static final String connectorClassName = OriginSeekerGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public OriginSeekerGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedGuards = new ArrayList<>();
        supportedGuards.add(ORIGIN_ASSIGNED_GUARD);
        supportedGuards.add(ORIGIN_ALREADY_ASSIGNED_GUARD);
        supportedGuards.add(MULTIPLE_ORIGINS_DETECTED_GUARD);
        supportedGuards.add(NO_ORIGINS_DETECTED_GUARD);
        supportedGuards.add(NO_TARGETS_DETECTED_GUARD);
        supportedGuards.add(MULTIPLE_TARGETS_DETECTED_GUARD);
        supportedGuards.add(ORIGIN_SEEKING_FAILED_GUARD);

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
