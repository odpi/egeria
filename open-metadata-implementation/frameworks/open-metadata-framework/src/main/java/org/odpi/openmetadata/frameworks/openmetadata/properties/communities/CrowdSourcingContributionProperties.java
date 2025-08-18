/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.communities;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CrowdSourcingRole;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CrowdSourcingContributionProperties provides details of the role played by an actor in building new contribution.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CrowdSourcingContributionProperties extends RelationshipBeanProperties
{
    CrowdSourcingRole roleType = null;

    /**
     * Default constructor
     */
    public CrowdSourcingContributionProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.CROWD_SOURCING_CONTRIBUTION_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CrowdSourcingContributionProperties(CrowdSourcingContributionProperties template)
    {
        super(template);

        if (template != null)
        {
            this.roleType = template.getRoleType();
        }
    }


    /**
     * Return the expectations with respect to the scope.
     *
     * @return type of assignment
     */
    public CrowdSourcingRole getRoleType()
    {
        return roleType;
    }


    /**
     * Set up the expectations with respect to the scope.
     *
     * @param roleType type of assignment
     */
    public void setRoleType(CrowdSourcingRole roleType)
    {
        this.roleType = roleType;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CrowdSourcingContributionProperties{" +
                "roleType=" + roleType +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        CrowdSourcingContributionProperties that = (CrowdSourcingContributionProperties) objectToCompare;
        return Objects.equals(roleType, that.roleType);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), roleType);
    }
}
