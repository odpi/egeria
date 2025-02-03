/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.informationsupplychains;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * InformationSupplyChainLinkProperties identifies a relationship that is part of an information supply chain.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class InformationSupplyChainLinkProperties extends RelationshipProperties
{
    private String label = null;
    private String description = null;


    /**
     * Default constructor
     */
    public InformationSupplyChainLinkProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public InformationSupplyChainLinkProperties(InformationSupplyChainLinkProperties template)
    {
        super(template);

        if (template != null)
        {
            this.label = template.getLabel();
            this.description = template.getDescription();
        }
    }


    /**
     * Return the display label for this relationship.
     *
     * @return string
     */
    public String getLabel()
    {
        return label;
    }


    /**
     * Set up the display label for this relationship.
     *
     * @param label string
     */
    public void setLabel(String label)
    {
        this.label = label;
    }


    /**
     * Return the description for this relationship.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description for this relationship.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "InformationSupplyChainLinkProperties{" +
                "label='" + label + '\'' +
                "description='" + description + '\'' +
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
        if (! (objectToCompare instanceof InformationSupplyChainLinkProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(label, that.label) &&
                Objects.equals(description, that.description);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), label, description);
    }
}
