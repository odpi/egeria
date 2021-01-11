/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.events;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceActionElement;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceEngineConfigurationEvent is used to inform the governance server that the configuration of
 * one of its governance engines has changed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceActionEvent extends GovernanceEngineEvent
{
    private static final long serialVersionUID = 1L;

    private GovernanceActionElement governanceActionElement = null;
    private String                  governanceEngineName    = null;

    /**
     * Default constructor
     */
    public GovernanceActionEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public GovernanceActionEvent(GovernanceActionEvent template)
    {
        super(template);

        if (template != null)
        {
            governanceActionElement = template.getGovernanceActionElement();
        }
    }


    /**
     * Return the description of the governance action.
     *
     * @return governance action element
     */
    public GovernanceActionElement getGovernanceActionElement()
    {
        return governanceActionElement;
    }


    /**
     * Set up the description of the governance action..
     *
     * @param governanceActionElement governance action element
     */
    public void setGovernanceActionElement(GovernanceActionElement governanceActionElement)
    {
        this.governanceActionElement = governanceActionElement;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceActionEvent{" +
                       "governanceActionElement=" + governanceActionElement +
                       ", governanceEngineName='" + governanceEngineName + '\'' +
                       ", eventVersionId=" + getEventVersionId() +
                       ", eventType=" + getEventType() +
                       '}';
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
        GovernanceActionEvent that = (GovernanceActionEvent) objectToCompare;
        return Objects.equals(governanceActionElement, that.governanceActionElement) &&
                       Objects.equals(governanceEngineName, that.governanceEngineName);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), governanceActionElement, governanceEngineName);
    }
}
