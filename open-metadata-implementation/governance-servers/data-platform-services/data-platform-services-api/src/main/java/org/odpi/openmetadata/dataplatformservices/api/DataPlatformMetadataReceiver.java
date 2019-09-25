/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.dataplatformservices.api;

import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformDeployedDatabaseSchema;
import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformSoftwareServerCapability;
import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformTabularColumn;
import org.odpi.openmetadata.dataplatformservices.api.model.DataPlatformTabularSchema;

public interface DataPlatformMetadataReceiver {

    DataPlatformSoftwareServerCapability getDataPlatformSoftwareServerCapability();

    DataPlatformDeployedDatabaseSchema getDataPlatformDeployedDatabaseSchema();

    DataPlatformTabularSchema getDataPlatformTabularSchema();

    DataPlatformTabularColumn getDataPlatformTabularColumn();

}
