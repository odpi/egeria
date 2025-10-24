/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DesignModel describes a collection of design model elements that make up a model of a design.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ConceptModelProperties.class, name = "ConceptModelProperties"),
                @JsonSubTypes.Type(value = SolutionBlueprintProperties.class, name = "SolutionBlueprintProperties"),
        })
public class DesignModelProperties extends CollectionProperties
{
    private String       userDefinedStatus = null;
    private List<String> authors           = null;

    /**
     * Default constructor
     */
    public DesignModelProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.DESIGN_MODEL.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DesignModelProperties(DesignModelProperties template)
    {
        super(template);

        if (template != null)
        {
            this.userDefinedStatus = template.getUserDefinedStatus();
            this.authors = template.getAuthors();
        }
    }


    /**
     * Return the status of the element.
     *
     * @return string
     */
    public String getUserDefinedStatus()
    {
        return userDefinedStatus;
    }


    /**
     * Set up the status of the element
     *
     * @param userDefinedStatus string
     */
    public void setUserDefinedStatus(String userDefinedStatus)
    {
        this.userDefinedStatus = userDefinedStatus;
    }


    /**
     * Return the list of authors for this design.
     *
     * @return list
     */
    public List<String> getAuthors()
    {
        return authors;
    }

    /**
     * Set up the list of authors for this design.
     *
     * @param authors list
     */
    public void setAuthors(List<String> authors)
    {
        this.authors = authors;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DesignModelProperties{" +
                "userDefinedStatus='" + userDefinedStatus + '\'' +
                ", authors=" + authors +
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
        if (! (objectToCompare instanceof DesignModelProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(userDefinedStatus, that.userDefinedStatus) &&
                Objects.equals(authors, that.authors);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), userDefinedStatus, authors);
    }
}
