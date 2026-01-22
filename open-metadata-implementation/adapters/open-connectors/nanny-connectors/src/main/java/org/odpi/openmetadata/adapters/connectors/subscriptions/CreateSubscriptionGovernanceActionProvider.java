/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.subscriptions;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.opengovernance.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;

import java.util.List;

/**
 * CreateSubscriptionGovernanceActionProvider is the OCF connector provider for the "Create Asset"
 * Governance Action Service.
 */
public class CreateSubscriptionGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "488136c1-fded-4449-a820-60762b4f7fac";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:DigitalSubscription:Create";
    private static final String  connectorTypeDisplayName = "Create Digital Subscription Governance Action Service";
    private static final String  connectorTypeDescription = "Governance Action Service that creates a digital subscription and links it to the subscriber (if supplied).  The digital subscription's GUID is passed as a new action target.";

    private static final String connectorClassName = CreateSubscriptionGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public CreateSubscriptionGovernanceActionProvider()
    {
        super();

        super.supportedRequestParameters = ManageDigitalSubscriptionRequestParameter.getRequestParameterTypes();
        super.producedGuards = ManageDigitalSubscriptionGuard.getGuardTypes();
        super.producedActionTargetTypes = List.of(ActionTarget.NEW_DIGITAL_SUBSCRIPTION.getActionTargetType());
        super.supportedActionTargetTypes = ManageDigitalSubscriptionActionTarget.getCreateSubscriptionActionTargetTypes();

        super.setConnectorClassName(connectorClassName);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setSupportedDeployedImplementationType(supportedDeployedImplementationType);

        super.connectorTypeBean = connectorType;

        super.supportedTechnologyTypes = null;

    }
}
