/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.settings;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;


@RestController
public class SettingsController {

    @Value("${theme:default}")
    String theme;

    @Value("${omas.server.name}")
    String serverName;

    @Value("${omas.server.url}")
    String serverUrl;

    /**
     *
     * @return a redirectView to the theme URI css file
     */
    @GetMapping( path = "/css/theme")
    public RedirectView getThemeCss(){
        return new RedirectView("/themes/" + theme + "/css/style.css", true);
    }

    @GetMapping( path = "/js/global")
    public String getRootPath(HttpServletRequest request){
        return "window.MyAppGlobals = { rootPath: '"+request.getContextPath()+"/' };";
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
