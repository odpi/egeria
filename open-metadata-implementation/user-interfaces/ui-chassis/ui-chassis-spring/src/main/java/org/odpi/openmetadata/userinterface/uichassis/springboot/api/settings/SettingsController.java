/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
public class SettingsController {

    @Value("${omas.server.name}")
    String serverName;

    @Value("${omas.server.url}")
    String serverUrl;

    /**
     *
     * @return omas settings object
     */
    @GetMapping( value = "/api/omas/settings")
    public OmasSettings getOmasSettings(){
        return new OmasSettings(serverName,serverUrl);
    }
}
