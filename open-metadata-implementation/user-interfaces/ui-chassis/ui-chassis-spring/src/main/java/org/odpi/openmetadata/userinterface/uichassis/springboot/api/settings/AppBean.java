/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.settings;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * App bean used to store app description
 */
@Component
public class AppBean implements Serializable {
    private final String title;
    private final String description;

    public AppBean(@Value("${app.title: }") String title,
                   @Value("${app.description: }") String description){
        this.title = title;
        this.description = description;
    }

    /**
     * @return title field
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return description field
     */
    public String getDescription() {
        return description;
    }
}
