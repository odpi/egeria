/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.dataplatformservices.api;

import org.odpi.openmetadata.accessservices.dataplatform.client.DataPlatformClient;
import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformDeployedDatabaseSchema;
import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformSoftwareServerCapability;
import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformTabularColumn;
import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformTabularSchema;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFRuntimeException;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;

import java.util.Date;


/**
 * Base implementation of a Data Platform Metadata Extractor Connector, implementing all of the required methods
 * from any Data Platform Metadata Extractor Connector is expected to provide (via the DataPlatformMetadataExtractor).
 * It is an abstract class as on its own it does absolutely nothing, and therefore a Data Platform Metadata Extractor
 * Connector needs to extend it and override at least one of the methods to actually do something.
 */
public class DataPlatformMetadataExtractorBase extends ConnectorBase implements DataPlatformMetadataExtractor{

    /* The Data Platform OMAS Client side implementation */
    private DataPlatformClient dataPlatformClient;

    /**
     * Instantiates a new Data platform metadata extractor base.
     */
    public DataPlatformMetadataExtractorBase() { super(); }

    public DataPlatformSoftwareServerCapability getDataPlatformSoftwareServerCapability() {
        return null;
    }

    public DataPlatformDeployedDatabaseSchema getDataPlatformDeployedDatabaseSchema() {
        return null;
    }

    public DataPlatformTabularSchema getDataPlatformTabularSchema() {
        return null;
    }

    public DataPlatformTabularColumn getDataPlatformTabularColumn() {
        return null;
    }

    /**
     * Gets data platform client.
     *
     * @return the data platform client
     */
    public DataPlatformClient getDataPlatformClient() {
        return dataPlatformClient;
    }

    /**
     * Sets data platform client.
     *
     * @param dataPlatformClient the data platform client
     */
    public void setDataPlatformClient(DataPlatformClient dataPlatformClient) {
        this.dataPlatformClient = dataPlatformClient;
    }
}
