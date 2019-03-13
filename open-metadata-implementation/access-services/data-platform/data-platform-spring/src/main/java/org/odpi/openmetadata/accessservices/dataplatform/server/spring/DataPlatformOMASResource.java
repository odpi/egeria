/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.server.spring;

import org.odpi.openmetadata.accessservices.dataplatform.server.DataPlatformRestServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-platform/users/{userId}/")
public class DataPlatformOMASResource {

    private final DataPlatformRestServices restAPI = new DataPlatformRestServices();

    public DataPlatformOMASResource() {
    }
}