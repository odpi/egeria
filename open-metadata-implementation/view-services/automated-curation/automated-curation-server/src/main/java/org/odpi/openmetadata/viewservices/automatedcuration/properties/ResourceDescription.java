/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.automatedcuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ResourceDescription extends RefDataElementBase
{
    private String              resourceUse = null;
    private String              resourceUseDescription = null;

    /**
     * Default constructor
     */
    public ResourceDescription()
    {
    }

    /**
     *
     * @return
     */
    public String getResourceUse()
    {
        return resourceUse;
    }

    public void setResourceUse(String resourceUse)
    {
        this.resourceUse = resourceUse;
    }

    public String getResourceUseDescription()
    {
        return resourceUseDescription;
    }

    public void setResourceUseDescription(String resourceUseDescription)
    {
        this.resourceUseDescription = resourceUseDescription;
    }


    /**
     * Generate a string containing the properties.
     *
     * @return string value
     */
    @Override
    public String toString()
    {
        return "ResourceDescription{" +
                "resourceUse='" + resourceUse + '\'' +
                ", resourceUseDescription='" + resourceUseDescription + '\'' +
                "} " + super.toString();
    }
}
