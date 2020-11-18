/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
public class InfoController {

    @Value("${omas.server.name}")
    String serverName;

    @Value("${omas.server.url}")
    String serverUrl;

    @Autowired
    AppBean app;

    /**
     *
     * @return a redirectView to the theme URI css file
     */
    @GetMapping( path = "/api/src/app/info")
    public AppBean getAppTitle(HttpServletRequest request){
        return app;
    }

    /**
     *
     * @return omas settings object
     */
    @GetMapping( value = "/api/omas/settings")
    public OmasSettings getOmasSettings(){
        return new OmasSettings(serverName,serverUrl);
    }
}
