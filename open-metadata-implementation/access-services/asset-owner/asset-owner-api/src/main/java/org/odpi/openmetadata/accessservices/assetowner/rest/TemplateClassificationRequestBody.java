/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TemplateClassificationRequestBody carries the parameters for classifying an asset as suitable to use for a template.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TemplateClassificationRequestBody extends AssetOwnerOMASAPIRequestBody
{
    private static final long    serialVersionUID = 1L;

    private String              name                 = null;
    private String              description          = null;
    private Map<String, String> additionalProperties = null;


    /**
     * Default constructor
     */
    public TemplateClassificationRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TemplateClassificationRequestBody(TemplateClassificationRequestBody template)
    {
        super(template);

        if (template != null)
        {
            name = template.getName();
            description = template.getDescription();
            additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Returns the name property for the template.
     * If no name is available then null is returned.
     *
     * @return String name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name property for the template.
     *
     * @param name String name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Returns the description property for the template.
     * If no description is provided then null is returned.
     *
     * @return description String text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description property associated with the template.
     *
     * @param description String text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return a copy of the additional properties.  Null means no additional properties are available.
     *
     * @return AdditionalProperties
     */
    public Map<String, String> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalProperties);
        }
    }


    /**
     * Set up additional properties.
     *
     * @param additionalProperties Additional properties object
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }




    /**
     * Generate a string containing the properties.
     *
     * @return string value
     */
    @Override
    public String toString()
    {
        return "TemplateClassificationRequestBody{" +
                       "name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       ", additionalProperties=" + additionalProperties +
                       '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        TemplateClassificationRequestBody that = (TemplateClassificationRequestBody) objectToCompare;
        return Objects.equals(name, that.name) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(additionalProperties, that.additionalProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), name, description, additionalProperties);
    }
}