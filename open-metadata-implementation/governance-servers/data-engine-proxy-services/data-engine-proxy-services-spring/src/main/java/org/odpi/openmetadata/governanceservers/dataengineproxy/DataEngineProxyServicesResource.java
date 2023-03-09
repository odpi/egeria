/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.governanceservers.dataengineproxy.model.ProcessLoadResponse;
import org.odpi.openmetadata.governanceservers.dataengineproxy.rest.DataEngineProxyRestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/servers/{serverName}/open-metadata/data-engine-proxy/users/{userId}")
@Tag(name = "Data Engine Proxy Service", description = "The Data Engine Proxy Service offers a rest interface to trigger processing by data engine proxy.", externalDocs = @ExternalDocumentation(description = "Open Lineage Services", url = "https://egeria-project.org/services//data-engine-proxy-services/"))
public class DataEngineProxyServicesResource {

    DataEngineProxyRestService dataEngineProxyRestService = new DataEngineProxyRestService();

    @GetMapping("/load")
    public ProcessLoadResponse load(@PathVariable("serverName") String serverName,
                                           @PathVariable("userId") String userId) {
        return dataEngineProxyRestService.load(serverName, userId);
    }

/*    @PostMapping("/load-process/{processId}")
    public ProcessLoadResponse loadProcess(@PathVariable("serverName") String serverName,
                                           @PathVariable("userId") String userId,
                                           @PathVariable("processId") String processId) {
        return dataEngineProxyRestService.getProcessChanges(serverName, userId, processId);
    }*/
}
