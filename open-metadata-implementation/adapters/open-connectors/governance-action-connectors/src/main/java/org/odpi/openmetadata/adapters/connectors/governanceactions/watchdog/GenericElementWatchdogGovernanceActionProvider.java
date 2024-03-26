/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;
import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

/**
 * GenericElementWatchdogGovernanceActionProvider is the OCF connector provider for the generic element monitor governance action service.
 * This is a Watchdog Governance Action Service.  The provider initializes the ConnectorProviderBase with the connector's class name,
 * sets up the default connector type for this service and the definitions of its request types, properties and guards that
 * define and control its behaviour.
 */
public class GenericElementWatchdogGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    private static final String  connectorTypeGUID = "8145967e-bb83-44b2-bc8c-68112c6a5a06";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Watchdog:GenericElement";
    private static final String  connectorTypeDisplayName = "Generic Element Watchdog Governance Action Service";
    private static final String  connectorTypeDescription =
            """
                    A Watchdog Governance Action Service that detects changes to requested elements and initiates a governance action process when they occur.  It has two modes of operation: listening for a single event and then terminating when it occurs or continuously listening for multiple events.  These modes of operation are controlled by the request types.  Then there are properties that can be set up in its connection's configuration properties and overridden by the request parameters.

                    The interestingTypeName property takes the name of an element type.  If set, it determines which types of elements are to be monitored.  This monitoring includes all subtypes of this interesting type.  If interestingTypeName is not set the default value is OpenMetadataRoot - effectively all elements with an open metadata type.

                    The instanceToMonitor property takes the unique identifier of a metadata element.  If set, this service will only consider events for this instance.  If it is not set then all elements of the interesting type are monitored unless there are one or more action targets that are labelled with instanceToMonitor when this service starts.If the action targets are set up then these are the instances that are monitored.

                    The rest of the properties are the governance action processes to call for specific types of events.  The property is set to the qualified name of the process to run if the type of event occurs on the metadata instance(s) being monitored.  If the property is not set, the type of event it refers to is ignored.

                    This service will only complete and produce a guard if it encounters an unrecoverable error or it is set up to listen for a single event and that event occurs.""";



    /*
     * This is the name of the connector that this provider will create
     */
    private static final String connectorClassName = GenericElementWatchdogGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation. It also set up details of the properties that controls its behavior.
     */
    public GenericElementWatchdogGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestTypes = GenericElementRequestType.getRequestTypeTypes();

        supportedRequestParameters = GenericElementRequestParameter.getRequestParameterTypes();


        supportedActionTargetTypes = new ArrayList<>();
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setName(GenericElementRequestParameter.INSTANCE_TO_MONITOR.getName());
        actionTargetType.setDescription(GenericElementRequestParameter.INSTANCE_TO_MONITOR.getDescription());
        actionTargetType.setTypeName(OpenMetadataType.OPEN_METADATA_ROOT.typeName);

        super.supportedActionTargetTypes.add(actionTargetType);

        producedGuards = GenericWatchdogGuard.getGuardTypes();

        super.setConnectorClassName(connectorClassName);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setDeployedImplementationType(supportedDeployedImplementationType);

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(GenericElementRequestParameter.INTERESTING_TYPE_NAME.getName());
        recognizedConfigurationProperties.add(GenericElementRequestParameter.INSTANCE_TO_MONITOR.getName());
        recognizedConfigurationProperties.add(GenericElementRequestParameter.NEW_ELEMENT_PROCESS_NAME.getName());
        recognizedConfigurationProperties.add(GenericElementRequestParameter.UPDATED_ELEMENT_PROCESS_NAME.getName());
        recognizedConfigurationProperties.add(GenericElementRequestParameter.DELETED_ELEMENT_PROCESS_NAME.getName());
        recognizedConfigurationProperties.add(GenericElementRequestParameter.CLASSIFIED_ELEMENT_PROCESS_NAME.getName());
        recognizedConfigurationProperties.add(GenericElementRequestParameter.RECLASSIFIED_ELEMENT_PROCESS_NAME.getName());
        recognizedConfigurationProperties.add(GenericElementRequestParameter.DECLASSIFIED_ELEMENT_PROCESS_NAME.getName());
        recognizedConfigurationProperties.add(GenericElementRequestParameter.NEW_RELATIONSHIP_PROCESS_NAME.getName());
        recognizedConfigurationProperties.add(GenericElementRequestParameter.UPDATED_RELATIONSHIP_PROCESS_NAME.getName());
        recognizedConfigurationProperties.add(GenericElementRequestParameter.DELETED_RELATIONSHIP_PROCESS_NAME.getName());
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }
}
