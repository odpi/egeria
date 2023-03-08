/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.projectmanagement.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * StakeholderProperties provides a details of a stakeholder for an initiative.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class StakeholderProperties extends RelationshipProperties
{
    private static final long    serialVersionUID = 1L;

    String stakeholderRole = null;

    /**
     * Default constructor
     */
    public StakeholderProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public StakeholderProperties(StakeholderProperties template)
    {
        super(template);

        if (template != null)
        {
            this.stakeholderRole = template.getStakeholderRole();
        }
    }


    /**
     * Return the role of the stakeholder to the initiative.
     *
     * @return role type
     */
    public String getStakeholderRole()
    {
        return stakeholderRole;
    }


    /**
     * Set up the role of the stakeholder to the initiative.
     *
     * @param stakeholderRole role type
     */
    public void setStakeholderRole(String stakeholderRole)
    {
        this.stakeholderRole = stakeholderRole;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "StakeholderProperties{" +
                       "effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", stakeholderRole='" + stakeholderRole + '\'' +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        StakeholderProperties that = (StakeholderProperties) objectToCompare;
        return Objects.equals(stakeholderRole, that.stakeholderRole);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), stakeholderRole);
    }
}
