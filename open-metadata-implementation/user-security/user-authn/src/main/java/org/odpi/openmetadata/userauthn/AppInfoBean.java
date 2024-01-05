/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userauthn;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;

/**
 * The AppInfoBean is a spring bean used to store the title and description of this runtime.
 */
@Component
public class AppInfoBean implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Title value from application.properties
     */
    private final String title;

    /**
     * Description value from application.properties
     */
    private final String description;


    /**
     * Constructor called when the application's properties are processed.
     *
     * @param title title
     * @param description description
     */
    public AppInfoBean(@Value("${app.title: }")       String title,
                       @Value("${app.description: }") String description)
    {
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
