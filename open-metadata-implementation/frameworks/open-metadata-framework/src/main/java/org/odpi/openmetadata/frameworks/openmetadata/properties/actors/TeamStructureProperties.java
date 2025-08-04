/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.actors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TeamStructureProperties describes the properties for the TeamStructure relationship between 2 teams.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TeamStructureProperties extends RelationshipBeanProperties
{
    private boolean delegationEscalationAuthority = true;


    /**
     * Default constructor
     */
    public TeamStructureProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.TEAM_STRUCTURE_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TeamStructureProperties(TeamStructureProperties template)
    {
        super(template);

        if (template != null)
        {
            delegationEscalationAuthority = template.getDelegationEscalationAuthority();
        }
    }


    /**
     * Return whether delegations and escalations flow along this relationship.
     *
     * @return boolean
     */
    public boolean getDelegationEscalationAuthority()
    {
        return delegationEscalationAuthority;
    }


    /**
     * Set up whether delegations and escalations flow along this relationship.
     *
     * @param delegationEscalationAuthority boolean
     */
    public void setDelegationEscalationAuthority(boolean delegationEscalationAuthority)
    {
        this.delegationEscalationAuthority = delegationEscalationAuthority;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "TeamStructureProperties{" +
                "delegationEscalationAuthority=" + delegationEscalationAuthority +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        TeamStructureProperties that = (TeamStructureProperties) objectToCompare;
        return Objects.equals(delegationEscalationAuthority, that.delegationEscalationAuthority);
    }


    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), delegationEscalationAuthority);
    }
}
