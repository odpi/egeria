/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetlineage.server.AssetLineageRestServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-lineage/users/{userId}/")

@Tag(name="Asset Lineage OMAS", description="The Asset Lineage OMAS provides services to query the lineage of business terms and data assets.", externalDocs=@ExternalDocumentation(description="Asset Lineage Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/asset-lineage/"))
public class AssetLineageResource {

    private final AssetLineageRestServices restAPI = new AssetLineageRestServices();



}
