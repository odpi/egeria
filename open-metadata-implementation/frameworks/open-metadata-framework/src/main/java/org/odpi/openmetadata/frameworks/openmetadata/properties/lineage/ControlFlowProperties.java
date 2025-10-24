/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.lineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ControlFlowProperties describe the properties for a control flow relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ControlFlowProperties extends LineageRelationshipProperties
{
    private String  guard          = null;
    private boolean mandatoryGuard = false;


    /**
     * Default constructor
     */
    public ControlFlowProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor.  Retrieves values from the supplied template
     *
     * @param template element to copy
     */
    public ControlFlowProperties(ControlFlowProperties template)
    {
        super(template);

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
        return "ControlFlowProperties{" +
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
        ControlFlowProperties that = (ControlFlowProperties) objectToCompare;
        return mandatoryGuard == that.mandatoryGuard && Objects.equals(guard, that.guard);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), guard, mandatoryGuard);
    }
}