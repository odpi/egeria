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
 * TargetForGovernanceActionProperties identifies the properties of an TargetForGovernanceAction relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TargetForGovernanceActionProperties extends RelationshipBeanProperties
{
    private String         actionTargetName  = null;

    /**
     * Typical Constructor
     */
    public TargetForGovernanceActionProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone Constructor the resulting object will return true if tested with this.equals(template) as
     * long as the template object is not null;
     *
     * @param template object being copied
     */
    public TargetForGovernanceActionProperties(TargetForGovernanceActionProperties template)
    {
        if (template != null)
        {
            actionTargetName  = template.getActionTargetName();
        }
    }


    /**
     * Return the name assigned to this action target.  This name helps to guide the actor in its processing of this action target.
     *
     * @return string name
     */
    public String getActionTargetName()
    {
        return actionTargetName;
    }


    /**
     * Set up the name assigned to this action target.  This name helps to guide the actor in its processing of this action target.
     *
     * @param actionTargetName string name
     */
    public void setActionTargetName(String actionTargetName)
    {
        this.actionTargetName = actionTargetName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "TargetForGovernanceActionProperties{" +
                "actionTargetName='" + actionTargetName + '\'' +
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
        TargetForGovernanceActionProperties that = (TargetForGovernanceActionProperties) objectToCompare;
        return Objects.equals(actionTargetName, that.actionTargetName);
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), actionTargetName);
    }
}
