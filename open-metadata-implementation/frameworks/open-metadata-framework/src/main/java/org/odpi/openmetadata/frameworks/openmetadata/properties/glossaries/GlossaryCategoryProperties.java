/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GlossaryCategoryProperties describes grouping of glossary terms that have similar characteristics.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GlossaryCategoryProperties extends ReferenceableProperties
{
    private String displayName = null;
    private String description = null;


    /**
     * Default constructor
     */
    public GlossaryCategoryProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public GlossaryCategoryProperties(GlossaryCategoryProperties template)
    {
        super(template);

        if (template != null)
        {
            displayName = template.getDisplayName();
            description = template.getDescription();
        }
    }


    /**
     * Return a human memorable name for the glossary category.
     *
     * @return string  name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up a human memorable name for the glossary category.
     *
     * @param displayName string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description of the glossary category.
     *
     * @return string text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the glossary category.
     *
     * @param description string text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GlossaryCategoryProperties{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }
}
