/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NewActionTarget identifies an element that a governance action service should process.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NewActionTarget
{
    private String actionTargetName = null;
    private String actionTargetGUID = null;


    /**
     * Typical Constructor
     */
    public NewActionTarget()
    {
    }


    /**
     * Copy/clone Constructor the resulting object will return true if tested with this.equals(template) as
     * long as the template object is not null;
     *
     * @param template object being copied
     */
    public NewActionTarget(NewActionTarget template)
    {
        if (template != null)
        {
            actionTargetName = template.getActionTargetName();
            actionTargetGUID = template.getActionTargetGUID();
        }
    }


    /**
     * Return the name assigned to this action target.  This name helps to guide the governance service in its processing of this action target.
     *
     * @return string name
     */
    public String getActionTargetName()
    {
        return actionTargetName;
    }


    /**
     * Set up the name assigned to this action target.  This name helps to guide the governance service in its processing of this action target.
     *
     * @param actionTargetName string name
     */
    public void setActionTargetName(String actionTargetName)
    {
        this.actionTargetName = actionTargetName;
    }


    /**
     * Return the unique identifier for this action target.
     *
     * @return string identifier
     */
    public String getActionTargetGUID()
    {
        return actionTargetGUID;
    }


    /**
     * Set up the unique identifier for this action target.
     *
     * @param actionTargetGUID string identifier
     */
    public void setActionTargetGUID(String actionTargetGUID)
    {
        this.actionTargetGUID = actionTargetGUID;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "NewActionTarget{" +
                       "actionTargetName='" + actionTargetName + '\'' +
                       ", actionTargetGUID='" + actionTargetGUID + '\'' +
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
        NewActionTarget that = (NewActionTarget) objectToCompare;
        return Objects.equals(actionTargetName, that.actionTargetName) &&
                       Objects.equals(actionTargetGUID, that.actionTargetGUID);
    }



    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(actionTargetName, actionTargetGUID);
    }
}
