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

    public AppBean(@Value("${app.title: }") String title){
        this.title = title;
    }

    /**
     *
     * @return title field
     */
    public String getTitle() {
        return title;
    }
}
