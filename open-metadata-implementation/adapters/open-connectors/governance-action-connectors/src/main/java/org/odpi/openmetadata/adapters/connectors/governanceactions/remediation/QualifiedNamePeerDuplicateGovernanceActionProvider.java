/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;

import java.util.ArrayList;

/**
 * QualifiedNamePeerDuplicateGovernanceActionProvider is the OCF connector provider for the Deduplication Governance Action Service.
 * This is a Remediation Governance Action Service.
 */
public class QualifiedNamePeerDuplicateGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "346939c4-de2c-44aa-a044-0ec64df0560f";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Remediation:Deduplication";
    private static final String  connectorTypeDisplayName = "Deduplication Governance Action Service";
    private static final String  connectorTypeDescription = "Checks the qualified name of an action target element to determine its duplicates.";

    static final String DUPLICATE_ASSIGNED_GUARD = "duplicate-detected";
    static final String DUPLICATE_ALREADY_ASSIGNED_GUARD = "duplicate-already-assigned";
    static final String NO_DUPLICATION_DETECTED_GUARD = "no-duplication-detected";
    static final String DUPLICATE_DETECTION_FAILED_GUARD = "duplicate-detection-failed";
    static final String NO_TARGETS_DETECTED_GUARD       = "no-targets-detected";

    private static final String connectorClassName = QualifiedNamePeerDuplicateGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * connector implementation.
     */
    public QualifiedNamePeerDuplicateGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedGuards = new ArrayList<>();
        supportedGuards.add(DUPLICATE_ASSIGNED_GUARD);
        supportedGuards.add(DUPLICATE_ALREADY_ASSIGNED_GUARD);
        supportedGuards.add(NO_DUPLICATION_DETECTED_GUARD);
        supportedGuards.add(DUPLICATE_DETECTION_FAILED_GUARD);
        supportedGuards.add(NO_TARGETS_DETECTED_GUARD);

        super.setConnectorClassName(connectorClassName);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
    }
}
