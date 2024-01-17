/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.actiontargettype.ActionTargetType;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * GenericElementWatchdogGovernanceActionProvider is the OCF connector provider for the generic element monitor governance action service.
 * This is a Watchdog Governance Action Service.  The provider initializes the ConnectorProviderBase with the connector's class name,
 * sets up the default connector type for this service and the definitions of its request types, properties and guards that
 * define and control its behaviour.
 */
public class GenericElementWatchdogGovernanceActionProvider extends GenericWatchdogGovernanceActionProvider
{
    private static final String  connectorTypeGUID = "8145967e-bb83-44b2-bc8c-68112c6a5a06";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Watchdog:GenericElement";
    private static final String  connectorTypeDisplayName = "Generic Element Watchdog Governance Action Service";
    private static final String  connectorTypeDescription =
            "A Watchdog Governance Action Service that detects changes to requested elements and initiates a governance action process when they " +
            "occur.  It has two modes of operation: listening for a single event and then terminating when it occurs or continuously listening " +
            "for multiple events.  These modes of operation are controlled by the request types.  Then there are properties that can be set up " +
            "in its connection's configuration properties and overridden by the request parameters." +
            "\n\n" +
            "The interestingTypeName property takes the name of an element type.  If set, it determines which types of elements are to be " +
            "monitored.  This monitoring includes all subtypes of this interesting type.  If interestingTypeName is not set " +
            "the default value is OpenMetadataRoot - effectively all elements with an open metadata type." +
            "\n\n" +
            "The instanceToMonitor property takes the unique identifier of a metadata element.  If set, this service will " +
            "only consider events for this instance.  If it is not set then all elements of the interesting type are " +
            "monitored unless there are one or more action targets that are labelled with instanceToMonitor when this service starts." +
            "If the action targets are set up then these are the instances that are monitored." +
            "\n\n" +
            "The rest of the properties are the governance action processes to call for specific types of events.  The property is set to the " +
            "qualified name of the process to run if the type of event occurs on the metadata instance(s) being monitored.  If the property is not " +
            "set, the type of event it refers to is ignored." +
            "\n\n" +
            "This service will only complete and produce a guard if it encounters an unrecoverable error or it is set up to listen for a single " +
            "event and that event occurs.";


    /*
     * These request types indicate whether the monitor looks for a single event or multiple.
     */
    static final String PROCESS_SINGLE_EVENT    = "process-single-event";
    static final String PROCESS_MULTIPLE_EVENTS = "process-multiple-events";


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

        supportedRequestTypes = new ArrayList<>();
        supportedRequestTypes.add(PROCESS_SINGLE_EVENT);
        supportedRequestTypes.add(PROCESS_MULTIPLE_EVENTS);

        supportedRequestParameters = new ArrayList<>();
        supportedRequestParameters.add(CHANGED_PROPERTY_NAMES);
        supportedRequestParameters.add(INTERESTING_TYPE_NAME_PROPERTY);
        supportedRequestParameters.add(INSTANCE_TO_MONITOR_PROPERTY);
        supportedRequestParameters.add(NEW_ELEMENT_PROCESS_NAME_PROPERTY);
        supportedRequestParameters.add(UPDATED_ELEMENT_PROCESS_NAME_PROPERTY);
        supportedRequestParameters.add(DELETED_ELEMENT_PROCESS_NAME_PROPERTY);
        supportedRequestParameters.add(CLASSIFIED_ELEMENT_PROCESS_NAME_PROPERTY);
        supportedRequestParameters.add(RECLASSIFIED_ELEMENT_PROCESS_NAME_PROPERTY);
        supportedRequestParameters.add(DECLASSIFIED_ELEMENT_PROCESS_NAME_PROPERTY);
        supportedRequestParameters.add(NEW_RELATIONSHIP_PROCESS_NAME_PROPERTY);
        supportedRequestParameters.add(UPDATED_RELATIONSHIP_PROCESS_NAME_PROPERTY);
        supportedRequestParameters.add(DELETED_RELATIONSHIP_PROCESS_NAME_PROPERTY);

        supportedTargetActionNames = new ArrayList<>();
        supportedTargetActionNames.add(INSTANCE_TO_MONITOR_PROPERTY);

        supportedGuards = new ArrayList<>();
        supportedGuards.add(MONITORING_COMPLETE);
        supportedGuards.add(MONITORING_FAILED);
        supportedGuards.add(MONITORING_STOPPED);

        super.setConnectorClassName(connectorClassName);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(INTERESTING_TYPE_NAME_PROPERTY);
        recognizedConfigurationProperties.add(INSTANCE_TO_MONITOR_PROPERTY);
        recognizedConfigurationProperties.add(NEW_ELEMENT_PROCESS_NAME_PROPERTY);
        recognizedConfigurationProperties.add(UPDATED_ELEMENT_PROCESS_NAME_PROPERTY);
        recognizedConfigurationProperties.add(DELETED_ELEMENT_PROCESS_NAME_PROPERTY);
        recognizedConfigurationProperties.add(CLASSIFIED_ELEMENT_PROCESS_NAME_PROPERTY);
        recognizedConfigurationProperties.add(RECLASSIFIED_ELEMENT_PROCESS_NAME_PROPERTY);
        recognizedConfigurationProperties.add(DECLASSIFIED_ELEMENT_PROCESS_NAME_PROPERTY);
        recognizedConfigurationProperties.add(NEW_RELATIONSHIP_PROCESS_NAME_PROPERTY);
        recognizedConfigurationProperties.add(UPDATED_RELATIONSHIP_PROCESS_NAME_PROPERTY);
        recognizedConfigurationProperties.add(DELETED_RELATIONSHIP_PROCESS_NAME_PROPERTY);
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;

        actionTargetTypes = new HashMap<>();
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setTypeName(OpenMetadataType.OPEN_METADATA_ROOT.typeName);

        super.actionTargetTypes.put(INSTANCE_TO_MONITOR_PROPERTY, actionTargetType);
    }
}
