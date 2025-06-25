/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NewElementRequestBody provides a structure used when creating actor profiles.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = NewAgreementRequestBody.class, name = "NewAgreementRequestBody"),
                @JsonSubTypes.Type(value = NewDigitalProductRequestBody.class, name = "NewDigitalProductRequestBody"),
                @JsonSubTypes.Type(value = NewGovernanceDefinitionRequestBody.class, name = "NewGovernanceDefinitionRequestBody"),
                @JsonSubTypes.Type(value = NewSolutionElementRequestBody.class, name = "NewSolutionElementRequestBody"),
        })
public class NewElementRequestBody extends NewElementOptionsRequestBody
{
    private OpenMetadataRootProperties properties = null;


    /**
     * Default constructor
     */
    public NewElementRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewElementRequestBody(NewElementRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.properties = template.getProperties();
        }
    }


    /**
     * Return the properties of the new element.
     *
     * @return properties
     */
    public OpenMetadataRootProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties of the new element.
     *
     * @param properties properties
     */
    public void setProperties(OpenMetadataRootProperties properties)
    {
        this.properties = properties;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "NewElementRequestBody{" +
                "properties=" + properties +
                "} " + super.toString();
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof NewElementRequestBody that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(properties, that.properties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), properties);
    }
}
