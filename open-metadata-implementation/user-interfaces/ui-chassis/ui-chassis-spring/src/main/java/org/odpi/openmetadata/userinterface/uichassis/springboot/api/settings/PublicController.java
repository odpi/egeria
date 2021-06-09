/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    AppBean app;

    /**
     *
     * @param request the http servlet request
     * @return an AppBean that contains the app build information
     */
    @GetMapping( path = "/app/info")
    public AppBean getAppTitle(HttpServletRequest request){
        return app;
    }
}
