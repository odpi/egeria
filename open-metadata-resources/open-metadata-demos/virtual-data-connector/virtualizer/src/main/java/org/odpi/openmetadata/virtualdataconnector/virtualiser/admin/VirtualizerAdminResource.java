/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.virtualdataconnector.virtualiser.admin;

import org.odpi.openmetadata.adminservices.OMAGServerAdminServices;
import org.odpi.openmetadata.adminservices.OMAGServerOperationalServices;
import org.odpi.openmetadata.adminservices.properties.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * VirtualizerAdminResource provides the spring annotations for the server-side
 * implementation of the administrative interface for
 * the Virtualizer
 */

@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")
public class VirtualizerAdminResource {

    private OMAGServerAdminServices adminServices = new OMAGServerAdminServices();
    private OMAGServerOperationalServices operationalServices = new OMAGServerOperationalServices();

    @RequestMapping(method = RequestMethod.GET, path = "/server-origin")
    public String getServerOrigin() {
        return "Virtualizer";
    }

    @RequestMapping(method = RequestMethod.POST, path = "/server-url-root")
    public VoidResponse setServerURLRoot(@PathVariable String userId,
                                         @PathVariable String serverName,
                                         @RequestParam String url){
        return adminServices.setServerURLRoot(userId, serverName, url);
    }


}
