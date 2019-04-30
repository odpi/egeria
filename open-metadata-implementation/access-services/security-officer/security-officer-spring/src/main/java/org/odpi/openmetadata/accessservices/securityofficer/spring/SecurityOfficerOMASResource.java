/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */
package org.odpi.openmetadata.accessservices.securityofficer.spring;

import org.odpi.openmetadata.accessservices.securityofficer.server.admin.services.SecurityOfficerService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/security-officer/users/{userId}")
public class SecurityOfficerOMASResource {

    private SecurityOfficerService service = new SecurityOfficerService();

    /**
     * Returns the security tag for the given asset
     *
     * @param serverName    server name
     * @param userId        String - userId of user making request.
     * @param assetId       the type of the entities that are returned
     */
    @RequestMapping(method = RequestMethod.GET, path = "/security-tag/assets/{assetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    private void getSecurityTagByAssetIdentifier(@PathVariable String serverName, @PathVariable String userId, @PathVariable String assetId) {
        service.getSecurityTagByAssetId(serverName, userId, assetId);
    }
}
