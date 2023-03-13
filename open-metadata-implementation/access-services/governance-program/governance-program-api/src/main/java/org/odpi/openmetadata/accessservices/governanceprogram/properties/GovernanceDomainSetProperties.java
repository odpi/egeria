/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import java.util.Objects;

/**
 * GovernanceDomainSetProperties describes a collection of related governance domains.
 */
public class GovernanceDomainSetProperties extends ReferenceableProperties
{
    private String displayName      = null;
    private String description      = null;

    private static final long     serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public GovernanceDomainSetProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceDomainSetProperties(GovernanceDomainSetProperties template)
    {
        super(template);

        if (template != null)
        {
            this.displayName = template.getDisplayName();
            this.description = template.getDescription();
        }
    }


    /**
     * Return the short name for the governance domain set.
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the short name for the governance domain set.
     *
     * @param displayName string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description for the governance domain set
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the governance domain set
     *
     * @param description text
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
        return "GovernanceDomainSetProperties{" +
                       "displayName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", typeName='" + getTypeName() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", extendedProperties=" + getExtendedProperties() +
                       '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        GovernanceDomainSetProperties that = (GovernanceDomainSetProperties) objectToCompare;
        return Objects.equals(displayName, that.displayName) &&
                       Objects.equals(description, that.description);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDisplayName(), getDescription());
    }
}
