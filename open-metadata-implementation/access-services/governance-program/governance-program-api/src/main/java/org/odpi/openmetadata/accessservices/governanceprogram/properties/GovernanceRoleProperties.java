/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The GovernanceRoleProperties describes a role within the governance program.  Initially, the role is defined and then
 * a person is assigned to the role.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceRoleProperties extends PersonRoleProperties
{
    private static final long    serialVersionUID = 1L;

    private int headCount = 1;


    /**
     * Default Constructor
     */
    public GovernanceRoleProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public GovernanceRoleProperties(GovernanceRoleProperties template)
    {
        super(template);

        if (template != null)
        {
            this.headCount = template.getHeadCount();
        }
    }


    /**
     * Return the number of people that can be appointed to this role.
     *
     * @return int
     */
    public int getHeadCount()
    {
        return headCount;
    }


    /**
     * Set up the number of people that can be appointed to this role.
     *
     * @param headCount int
     */
    public void setHeadCount(int headCount)
    {
        this.headCount = headCount;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "GovernanceRoleProperties{" +
                       "headCount=" + headCount +
                       ", domainIdentifier=" + getDomainIdentifier() +
                       ", appointmentId='" + getRoleId() + '\'' +
                       ", appointmentContext='" + getScope() + '\'' +
                       ", appointmentTitle='" + getTitle() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        GovernanceRoleProperties that = (GovernanceRoleProperties) objectToCompare;
        return headCount == that.headCount;
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), headCount);
    }
}
