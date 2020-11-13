/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.cohortregistrystore.file;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.OMRSCohortRegistryStoreProviderBase;

/**
 * FileBasedRegistryStoreProvider is the OCF connector provider for the file based cohort registry store.
 */
public class FileBasedRegistryStoreProvider extends OMRSCohortRegistryStoreProviderBase
{
    static final String  connectorTypeGUID = "108b85fe-d7a8-45c3-9f88-742ac4e4fd14";
    static final String  connectorTypeName = "File Based Cohort Registry Store Connector";
    static final String  connectorTypeDescription = "Connector supports storing of the open metadata cohort registry in a file.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * registry store implementation.
     */
    public FileBasedRegistryStoreProvider()
    {
        Class<?>    connectorClass = FileBasedRegistryStoreConnector.class;

        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
    }
}
