/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Referenceable;

import java.util.Objects;

/**
 * GovernanceZone describes a governance zone which is a grouping of assets that are used for a specific
 * purpose.
 */
public class GovernanceZone extends Referenceable
{
    private String displayName = null;
    private String description = null;
    private String criteria = null;

    private static final long     serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public GovernanceZone()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceZone(GovernanceZone  template)
    {
        super(template);

        if (template != null)
        {
            this.displayName = template.getDisplayName();
            this.description = template.getDescription();
            this.criteria = template.getCriteria();
        }
    }


    /**
     * Return the short name for the governance zone.
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the short name for the governance zone.
     *
     * @param displayName string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description for the governance zone
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the governance zone
     *
     * @param description text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return details of the criteria that Assets have when they are placed in these zones.
     *
     * @return text
     */
    public String getCriteria()
    {
        return criteria;
    }


    /**
     * Set up the details of the criteria that Assets have when they are placed in these zones.
     *
     * @param criteria text
     */
    public void setCriteria(String criteria)
    {
        this.criteria = criteria;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceZone{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", criteria='" + criteria + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", meanings=" + getMeanings() +
                ", type=" + getType() +
                ", GUID='" + getGUID() + '\'' +
                ", URL='" + getURL() + '\'' +
                ", classifications=" + getClassifications() +
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
        GovernanceZone that = (GovernanceZone) objectToCompare;
        return Objects.equals(getDisplayName(), that.getDisplayName()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getCriteria(), that.getCriteria());
    }



    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDisplayName(), getDescription(), getCriteria());
    }
}
