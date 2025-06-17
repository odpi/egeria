/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.actors;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceRoleProperties describes a person role defined to support governance.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceRoleProperties extends PersonRoleProperties
{
    private int domainIdentifier = 0; /* Zero means not specific to a governance domain */


    /**
     * Default constructor
     */
    public GovernanceRoleProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.GOVERNANCE_ROLE.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceRoleProperties(GovernanceRoleProperties template)
    {
        super(template);

        if (template != null)
        {
            this.domainIdentifier     = template.getDomainIdentifier();
        }
    }


    /**
     * Return the identifier of the governance domain that this role belongs to.  Zero means that the
     * role is not specific to any domain.
     *
     * @return int
     */
    public int getDomainIdentifier()
    {
        return domainIdentifier;
    }


    /**
     * Set up the identifier of the governance domain that this role belongs to.  Zero means that the
     * role is not specific to any domain.
     *
     * @param domainIdentifier int
     */
    public void setDomainIdentifier(int domainIdentifier)
    {
        this.domainIdentifier = domainIdentifier;
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
                "domainIdentifier=" + domainIdentifier +
                "} " + super.toString();
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
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
        return domainIdentifier == that.domainIdentifier;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), domainIdentifier);
    }
}
