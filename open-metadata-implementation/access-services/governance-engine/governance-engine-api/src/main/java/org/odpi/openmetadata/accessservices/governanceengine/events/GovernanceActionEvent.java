/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.events;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceActionElement;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceActionEvent is used to inform the governance server that there is a governance action to run.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceActionEvent extends GovernanceEngineEvent
{
    private static final long serialVersionUID = 1L;

    private String governanceActionGUID = null;

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
            governanceActionGUID = template.getGovernanceActionGUID();
        }
    }


    /**
     * Return the unique identifier of the governance action.
     *
     * @return string guid
     */
    public String getGovernanceActionGUID()
    {
        return governanceActionGUID;
    }


    /**
     * Set up the unique identifier of the governance action.
     *
     * @param governanceActionGUID string guid
     */
    public void setGovernanceActionGUID(String governanceActionGUID)
    {
        this.governanceActionGUID = governanceActionGUID;
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
                       "governanceActionGUID=" + governanceActionGUID +
                       ", eventVersionId=" + getEventVersionId() +
                       ", eventType=" + getEventType() +
                       ", governanceEngineGUID='" + getGovernanceEngineGUID() + '\'' +
                       ", governanceEngineName='" + getGovernanceEngineName() + '\'' +
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
        return Objects.equals(governanceActionGUID, that.governanceActionGUID);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), governanceActionGUID);
    }
}
