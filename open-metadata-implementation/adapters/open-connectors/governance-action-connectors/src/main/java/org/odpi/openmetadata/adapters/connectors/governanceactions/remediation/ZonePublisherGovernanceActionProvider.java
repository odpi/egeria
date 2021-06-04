/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.remediation;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;

import java.util.ArrayList;
import java.util.List;

/**
 * ZonePublisherGovernanceActionProvider is the OCF connector provider for the Zone Publisher Governance Action Service.
 * This is a Remediation Governance Action Service.
 */
public class ZonePublisherGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "add1ff1b-d32d-42e7-9ebf-a2d26276aafe";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Remediation:ZonePublisher";
    private static final String  connectorTypeDisplayName = "Zone Publisher Governance Action Service";
    private static final String  connectorTypeDescription = "Assigns the configured zone(s) to the requested asset.";

    static final String PUBLISH_ZONES_PROPERTY = "publishZones";


    static final String ZONE_ASSIGNED_GUARD          = "zone-assigned";
    static final String NO_ZONES_DETECTED_GUARD      = "no-zones-detected";
    static final String NO_TARGETS_DETECTED_GUARD    = "no-targets-detected";
    static final String ZONE_PUBLISHING_FAILED_GUARD = "zone-publishing-failed";

    private static final String connectorClassName = ZonePublisherGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public ZonePublisherGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestParameters = new ArrayList<>();
        supportedRequestParameters.add(PUBLISH_ZONES_PROPERTY);

        supportedGuards = new ArrayList<>();
        supportedGuards.add(ZONE_ASSIGNED_GUARD);
        supportedGuards.add(NO_ZONES_DETECTED_GUARD);
        supportedGuards.add(NO_TARGETS_DETECTED_GUARD);
        supportedGuards.add(ZONE_PUBLISHING_FAILED_GUARD);

        super.setConnectorClassName(connectorClassName);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(PUBLISH_ZONES_PROPERTY);
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }
}
