/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server.spring;

import org.odpi.openmetadata.accessservices.assetlineage.server.AssetLineageRestServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-lineage/users/{userId}/")
public class AssetLineageResource {

    private final AssetLineageRestServices restAPI = new AssetLineageRestServices();



}
