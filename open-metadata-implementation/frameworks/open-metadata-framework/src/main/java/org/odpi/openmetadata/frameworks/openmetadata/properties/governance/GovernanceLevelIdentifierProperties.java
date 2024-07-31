/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;

import java.util.Objects;

/**
 * GovernanceLevelIdentifierProperties describes a single level identifier for a specific governance classification.
 */
public class GovernanceLevelIdentifierProperties extends ReferenceableProperties
{
    private int    levelIdentifier = 0;
    private String displayName     = null;
    private String description     = null;


    /**
     * Default constructor
     */
    public GovernanceLevelIdentifierProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceLevelIdentifierProperties(GovernanceLevelIdentifierProperties template)
    {
        super(template);

        if (template != null)
        {
            this.levelIdentifier = template.getLevelIdentifier();
            this.displayName = template.getDisplayName();
            this.description = template.getDescription();
        }
    }


    /**
     * Return the identifier of the governance level.
     *
     * @return int
     */
    public int getLevelIdentifier()
    {
        return levelIdentifier;
    }


    /**
     * Set up the identifier of the governance level.
     *
     * @param levelIdentifier int
     */
    public void setLevelIdentifier(int levelIdentifier)
    {
        this.levelIdentifier = levelIdentifier;
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
        return "GovernanceLevelIdentifierProperties{" +
                       "levelIdentifier=" + levelIdentifier +
                       ", displayName='" + displayName + '\'' +
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
        GovernanceLevelIdentifierProperties that = (GovernanceLevelIdentifierProperties) objectToCompare;
        return levelIdentifier == that.levelIdentifier &&
                       Objects.equals(displayName, that.displayName) &&
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
        return Objects.hash(super.hashCode(), getLevelIdentifier(), getDisplayName(), getDescription());
    }
}
