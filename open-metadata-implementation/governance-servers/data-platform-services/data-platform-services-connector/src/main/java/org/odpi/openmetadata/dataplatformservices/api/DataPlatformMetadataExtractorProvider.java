/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.dataplatformservices.api;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;

/**
 * The DataPlatformMetadataExtractorBase provides a base class for the connector provider supporting different
 * Data Platform Connectors.
 * It adds no function but provides a placeholder for additional function if needed for the creation of
 * any Data Platform Connectors.
 * <p>
 * It extends ConnectorProviderBase which does the creation of connector instances.  The subclasses of
 * DataPlatformMetadataExtractorBase must initialize ConnectorProviderBase with the Java class
 * name of the Data Platform Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class DataPlatformMetadataExtractorProvider extends ConnectorProviderBase {

    /**
     * Instantiates a new Data platform metadata extractor provider.
     */
    public DataPlatformMetadataExtractorProvider() { super(); }

}