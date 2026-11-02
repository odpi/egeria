/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.WatchdogActionTarget;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.WatchdogRequestParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * GenericElementWatchdogGovernanceActionProvider is the OCF connector provider for the generic element watchdog action service.
 * This is a Watchdog Action Service.  The provider initializes the ConnectorProviderBase with the connector's class name,
 * sets up the default connector type for this service and the definitions of its request types, properties and guards that
 * define and control its behaviour.
 */
public class GenericElementWatchdogGovernanceActionProvider extends GenericWatchdogGovernanceActionProvider
{
    private static final String  connectorTypeGUID = "fa209667-5ca0-40f4-b998-5a449c35c147";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Watchdog:OpenMetadataRootElement";
    private static final String  connectorTypeDisplayName = "Generic Element Watchdog Action Service";
    private static final String  connectorTypeDescription =
            """
                    A Watchdog Action Service that detects changes to requested elements and initiates a governance action process when they occur.  It has two modes of operation: listening for a single event and then terminating when it occurs or continuously listening for multiple events.  These modes of operation are controlled by the request types.  Then there are properties that can be set up in its connection's configuration properties and overridden by the request parameters.

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
        supportedActionTargetTypes = WatchdogActionTarget.getInstanceActionTargetTypes();

        ConnectorType connectorType = new ConnectorType();
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(DeployedImplementationType.WATCHDOG_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName());
        connectorType.setSupportedDeployedImplementationType(DeployedImplementationType.WATCHDOG_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType());

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(WatchdogRequestParameter.INTERESTING_TYPE_NAME.getName());
        recognizedConfigurationProperties.add(WatchdogRequestParameter.INSTANCE_TO_MONITOR.getName());
        recognizedConfigurationProperties.add(WatchdogRequestParameter.NEW_ELEMENT_PROCESS_NAME.getName());
        recognizedConfigurationProperties.add(WatchdogRequestParameter.UPDATED_ELEMENT_PROCESS_NAME.getName());
        recognizedConfigurationProperties.add(WatchdogRequestParameter.DELETED_ELEMENT_PROCESS_NAME.getName());
        recognizedConfigurationProperties.add(WatchdogRequestParameter.CLASSIFIED_ELEMENT_PROCESS_NAME.getName());
        recognizedConfigurationProperties.add(WatchdogRequestParameter.RECLASSIFIED_ELEMENT_PROCESS_NAME.getName());
        recognizedConfigurationProperties.add(WatchdogRequestParameter.DECLASSIFIED_ELEMENT_PROCESS_NAME.getName());
        recognizedConfigurationProperties.add(WatchdogRequestParameter.NEW_RELATIONSHIP_PROCESS_NAME.getName());
        recognizedConfigurationProperties.add(WatchdogRequestParameter.UPDATED_RELATIONSHIP_PROCESS_NAME.getName());
        recognizedConfigurationProperties.add(WatchdogRequestParameter.DELETED_RELATIONSHIP_PROCESS_NAME.getName());
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }
}
