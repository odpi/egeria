/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NextGovernanceActionProcessStepProperties identifies the properties of a NextGovernanceActionProcessStep relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NextGovernanceActionProcessStepProperties extends RelationshipBeanProperties
{
    private String  guard          = null;
    private boolean mandatoryGuard = false;

    /**
     * Typical Constructor
     */
    public NextGovernanceActionProcessStepProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.NEXT_GOVERNANCE_ACTION_PROCESS_STEP_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone Constructor the resulting object will return true if tested with this.equals(template) as
     * long as the template object is not null;
     *
     * @param template object being copied
     */
    public NextGovernanceActionProcessStepProperties(NextGovernanceActionProcessStepProperties template)
    {
        if (template != null)
        {
            guard            = template.getGuard();
            mandatoryGuard   = template.getMandatoryGuard();
        }
    }

    /**
     * Return the guard of the relationship.
     *
     * @return string guard
     */
    public String getGuard()
    {
        return guard;
    }


    /**
     * Set up the guard of the relationship.
     *
     * @param guard string name
     */
    public void setGuard(String guard)
    {
        this.guard = guard;
    }


    /**
     * Return whether the guard was mandatory.
     *
     * @return boolean
     */
    public boolean getMandatoryGuard()
    {
        return mandatoryGuard;
    }


    /**
     * Set up whether the guard was mandatory.
     *
     * @param mandatoryGuard boolean
     */
    public void setMandatoryGuard(boolean mandatoryGuard)
    {
        this.mandatoryGuard = mandatoryGuard;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "NextGovernanceActionProcessStepProperties{" +
                "guard='" + guard + '\'' +
                ", mandatoryGuard=" + mandatoryGuard +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        NextGovernanceActionProcessStepProperties that = (NextGovernanceActionProcessStepProperties) objectToCompare;
        return mandatoryGuard == that.mandatoryGuard && Objects.equals(guard, that.guard);
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), mandatoryGuard, guard);
    }
}
