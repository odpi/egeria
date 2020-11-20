/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;


@RestController
public class PublicController {

    @Value("${theme:default}")
    String theme;

    @Autowired
    AppBean app;

    /**
     *
     * @return an AppBean that contains the app build information
     */
    @GetMapping( path = "/api/public/app/info")
    public AppBean getAppTitle(HttpServletRequest request){
        return app;
    }

    /**
     *
     * @return a redirectView to the theme URI css file
     */
    @GetMapping( path = "/api/css/theme")
    public RedirectView getThemeCss(){
        return new RedirectView("/api/themes/" + theme + "/css/style.css", true);
    }

    @GetMapping( path = "/api/js/global")
    public String getRootPath(HttpServletRequest request){
        return "window.MyAppGlobals = { rootPath: '" + request.getContextPath() + "/' };";
    }
}
