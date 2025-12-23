/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AuthoredReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionPortProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DesignModelElement describes an element in a design.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ConceptModelElementProperties.class, name = "ConceptModelElementProperties"),
                @JsonSubTypes.Type(value = SolutionComponentProperties.class, name = "SolutionComponentProperties"),
                @JsonSubTypes.Type(value = SolutionPortProperties.class, name = "SolutionPortProperties"),
        })
public class DesignModelElementProperties extends AuthoredReferenceableProperties
{
    private String       canonicalName     = null;

    /**
     * Default constructor
     */
    public DesignModelElementProperties()
    {
        super();
        super.typeName = OpenMetadataType.DESIGN_MODEL_ELEMENT.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DesignModelElementProperties(DesignModelElementProperties template)
    {
        super(template);

        if (template != null)
        {
            this.canonicalName = template.getCanonicalName();
        }
    }

    /**
     * Return the canonical name used to generate technically compliant names in different implementation
     * technologies. The canonical name has each word capitalized with a space between each word.
     *
     * @return string
     */
    public String getCanonicalName()
    {
        return canonicalName;
    }


    /**
     * Set up the canonical name used to generate technically compliant names in different implementation
     * technologies. The canonical name has each word capitalized with a space between each word.
     *
     * @param canonicalName string
     */
    public void setCanonicalName(String canonicalName)
    {
        this.canonicalName = canonicalName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DesignModelElementProperties{" +
                "canonicalName='" + canonicalName + '\'' +
                "} " + super.toString();
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
        if (! (objectToCompare instanceof DesignModelElementProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(canonicalName, that.canonicalName);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), canonicalName);
    }
}
