/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.dataplatformservices.api;

import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformDeployedDatabaseSchema;
import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformSoftwareServerCapability;
import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformTabularColumn;
import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformTabularSchema;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFRuntimeException;

import java.util.Date;

public class DataPlatformConnectorBase extends ConnectorBase implements DataPlatformMetadataExtractor{

    public DataPlatformConnectorBase() { super(); }

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

    public Date getChangesLastSynced() {
        return null;
    }

    public void setChangesLastSynced(Date time) throws OCFRuntimeException {

    }
}
