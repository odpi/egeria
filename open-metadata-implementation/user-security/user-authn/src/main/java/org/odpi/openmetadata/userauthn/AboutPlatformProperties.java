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
public class AboutPlatformProperties implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Display name value from application.properties
     */
    private final String displayName;

    /**
     * Description value from application.properties
     */
    private final String description;

    /**
     * Organization.name value from application.properties
     */
    private final String organizationName;


    /**
     * Constructor called when the application's properties are processed.
     *
     * @param displayName displayName
     * @param description description
     * @param organizationName name of organization
     */
    public AboutPlatformProperties(@Value("${platform.name: }")  String displayName,
                                   @Value("${platform.description: }")  String description,
                                   @Value("${platform.organization.name: }") String organizationName)
    {
        this.displayName      = displayName;
        this.description      = description;
        this.organizationName = organizationName;
    }

    /**
     * @return display name field
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return description field
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return organization name
     */
    public String getOrganizationName()
    {
        return organizationName;
    }
}
