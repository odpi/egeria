/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.platformservices.properties;

import java.util.Objects;

/**
 * The name, description and organization for the platform which is configured in the application.properties
 * file.
 */
public class PublicProperties
{
    private String displayName = null;
    private String description = null;
    private String organizationName = null;

    public PublicProperties()
    {
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getOrganizationName()
    {
        return organizationName;
    }

    public void setOrganizationName(String organizationName)
    {
        this.organizationName = organizationName;
    }

    @Override
    public String toString()
    {
        return "AboutProperties{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", organizationName='" + organizationName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        PublicProperties that = (PublicProperties) objectToCompare;
        return Objects.equals(displayName, that.displayName) && Objects.equals(description, that.description) && Objects.equals(organizationName, that.organizationName);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(displayName, description, organizationName);
    }
}
