/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.server.spring;


import org.odpi.openmetadata.accessservices.informationview.events.ReportRequestBody;
import org.odpi.openmetadata.accessservices.informationview.responses.VoidResponse;
import org.odpi.openmetadata.accessservices.informationview.server.InformationViewOMASRegistration;
import org.odpi.openmetadata.accessservices.informationview.server.InformationViewRestServices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/information-view/users/{userId}/")
public class InformationViewOMASResource {
    private final InformationViewOMASRegistration registration;

    private InformationViewRestServices restAPI = new InformationViewRestServices();

    public InformationViewOMASResource() {
        registration = new InformationViewOMASRegistration();
    }

    /**
     *
     * @param serverName unique identifier for requested server.
     * @param userId the unique identifier for the user
     * @param requestBody The json representing the structure of the report and basic report properties
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, path = "/report")
    public VoidResponse submitReport(@PathVariable("serverName") String serverName,
                                     @PathVariable("userId") String userId,
                                     @RequestBody ReportRequestBody requestBody) {
        return restAPI.submitReport(serverName, userId, requestBody);
    }

}
