/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.security;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceZoneProperties describes a governance zone which is a grouping of assets that are used for a specific
 * purpose.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceZoneProperties extends SecurityAccessControlProperties
{
    private String criteria         = null;


    /**
     * Default constructor
     */
    public GovernanceZoneProperties()
    {
        super();
        super.typeName = OpenMetadataType.GOVERNANCE_ZONE.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceZoneProperties(GovernanceZoneProperties template)
    {
        super(template);

        if (template != null)
        {
            this.criteria = template.getCriteria();
        }
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
        return "GovernanceZoneProperties{" +
                "criteria='" + criteria + '\'' +
                "} " + super.toString();
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
        GovernanceZoneProperties that = (GovernanceZoneProperties) objectToCompare;
        return Objects.equals(criteria, that.criteria);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), criteria);
    }
}
