package org.odpi.openmetadata.virtualdataconnector.virtualiser.admin;

import org.odpi.openmetadata.adminservices.OMAGServerAdminServices;
import org.odpi.openmetadata.adminservices.OMAGServerOperationalServices;
import org.odpi.openmetadata.adminservices.configuration.properties.CohortConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.LocalRepositoryConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.properties.OMAGServerConfigResponse;
import org.odpi.openmetadata.adminservices.properties.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * VirtualizerAdminResource provides the spring annotations for the server-side
 * implementation of the administrative interface for
 * the Virtualizer
 */

@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")
public class AdminResource {
    private OMAGServerAdminServices adminServices = new OMAGServerAdminServices();
    private OMAGServerOperationalServices operationalServices = new OMAGServerOperationalServices();

    @RequestMapping(method = RequestMethod.GET, path = "/server-origin")
    public String getServerOrigin() {
        return "Virtualizer";
    }
}
