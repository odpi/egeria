/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.server;

public class DataPlatformInstanceHandler {
    private static DataPlatformServicesInstanceMap instanceMap = new DataPlatformServicesInstanceMap();

    /**
     * Default constructor registers the access service
     */
    DataPlatformInstanceHandler() {
        new DataPlatformOMASRegistration();
    }

}
