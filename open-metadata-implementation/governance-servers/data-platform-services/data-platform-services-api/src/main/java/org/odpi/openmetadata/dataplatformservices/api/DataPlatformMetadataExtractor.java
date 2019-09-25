/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.dataplatformservices.api;

import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformDeployedDatabaseSchema;
import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformSoftwareServerCapability;
import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformTabularColumn;
import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformTabularSchema;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFRuntimeException;

import java.util.Date;

public interface DataPlatformMetadataExtractor {

    DataPlatformSoftwareServerCapability getDataPlatformSoftwareServerCapability();

    DataPlatformDeployedDatabaseSchema getDataPlatformDeployedDatabaseSchema();

    DataPlatformTabularSchema getDataPlatformTabularSchema();

    DataPlatformTabularColumn getDataPlatformTabularColumn();

    Date getChangesLastSynced();

    void setChangesLastSynced(Date time) throws OCFRuntimeException;

}
