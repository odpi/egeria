/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.service;

import org.odpi.openmetadata.accessservices.dataengine.server.util.ExceptionHandler;

/**
 * The AssetCatalogService provides the server-side implementation of the Asset Catalog Open Metadata
 * Assess Service (OMAS).
 * This service provide the functionality to fetch asset's header, classification and properties.
 */
public class DataEngineService {

    private static DataEngineInstanceHandler instanceHandler = new DataEngineInstanceHandler();

    private ExceptionHandler exceptionUtil = new ExceptionHandler();

    public DataEngineService() {
    }
}
