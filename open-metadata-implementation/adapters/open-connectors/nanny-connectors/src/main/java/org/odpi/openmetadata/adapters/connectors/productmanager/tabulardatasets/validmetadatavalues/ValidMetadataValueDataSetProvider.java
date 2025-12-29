/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.productmanager.tabulardatasets.validmetadatavalues;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;


/**
 * ValidValueDataSetProvider is the connector provider for the ValidValueDataSet connector that manages the members of
 * a valid values set as a tabular data set.
 */
public class ValidMetadataValueDataSetProvider extends ConnectorProviderBase
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 716;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "381d1d1a-d498-4e47-a555-7004984a63c7";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:ResourceConnector:TabularDataSet:ValidMetadataValues";
    private static final String connectorDisplayName   = "Valid Metadata Values Tabular Data Set Connector";
    private static final String connectorDescription   = "Connector manages an open metadata valid value set for a particular property as if it was a tabular data set.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/resource/tabular-data-set/valid-metadata-values/";


    /*
     * Class of the connector.
     */
    private static final String connectorClassName      = ValidMetadataValueDataSetConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public ValidMetadataValueDataSetProvider()
    {
        super();

        /*
         * Set up the class name of the connector that this provider creates.
         */
        super.setConnectorClassName(connectorClassName);

        /*
         * Set up the connector type that should be included in a connection used to configure this connector.
         */
        ConnectorType connectorType = new ConnectorType();
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorQualifiedName);
        connectorType.setDisplayName(connectorDisplayName);
        connectorType.setDescription(connectorDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.STABLE);
        componentDescription.setComponentName(connectorQualifiedName);
        componentDescription.setComponentDescription(connectorDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);
    }
}
