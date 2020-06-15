/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetlineage.server.AssetLineageRestServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-lineage/users/{userId}/")

@Tag(name = "Asset Lineage OMAS", description = "The Asset Lineage OMAS provides services to query the lineage of business terms and data assets.", externalDocs = @ExternalDocumentation(description = "Asset Lineage Open Metadata Access Service (OMAS)", url = "https://egeria.odpi.org/open-metadata-implementation/access-services/asset-lineage/"))
public class AssetLineageResource {

    private final AssetLineageRestServices restAPI = new AssetLineageRestServices();

    /**
     * Scan the cohort for Glossary Terms available
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     *
     * @return a list of unique identifiers (guids) of the available Glossary Terms as a response
     */
    @GetMapping(path = "scan-glossary-terms")
    public GUIDListResponse scanGlossaryTerms(@PathVariable String serverName,
                                              @PathVariable String userId) {
        return restAPI.scanGlossaryTerms(serverName, userId);
    }
}
