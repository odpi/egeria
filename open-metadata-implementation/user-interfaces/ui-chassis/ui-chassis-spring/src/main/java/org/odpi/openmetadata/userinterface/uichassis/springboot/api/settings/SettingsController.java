/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.settings;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SettingsController {

    @Value("${omas.server.name}")
    String serverName;

    @Value("${omas.server.url}")
    String serverUrl;

    @Value("${omas.asset.catalog.page.size}")
    String pageSize;

    /**
     * @return omas settings object
     */
    @GetMapping(value = "/api/ui/settings")
    public UISettings getUISettings() {
        return new UISettings(serverName, serverUrl, pageSize);
    }
}
