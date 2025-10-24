/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.automatedcuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CatalogTemplate extends RefDataElementBase
{
    private String name = null;
    private String description = null;
    private String versionIdentifier = null;


    public CatalogTemplate()
    {
    }


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getVersionIdentifier()
    {
        return versionIdentifier;
    }

    public void setVersionIdentifier(String versionIdentifier)
    {
        this.versionIdentifier = versionIdentifier;
    }


    /**
     * Generate a string containing the properties.
     *
     * @return string value
     */
    @Override
    public String toString()
    {
        return "CatalogTemplate{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", versionIdentifier='" + versionIdentifier + '\'' +
                "} " + super.toString();
    }
}
