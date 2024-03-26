/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceserver.events;

import com.fasterxml.jackson.annotation.*;

import java.io.Serial;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EngineActionEvent is used to inform the engine that there is an engine action to run.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EngineActionEvent extends GovernanceEngineConfigurationEvent
{
    @Serial
    private static final long serialVersionUID = 1L;

    private String engineActionGUID = null;

    /**
     * Default constructor
     */
    public EngineActionEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public EngineActionEvent(EngineActionEvent template)
    {
        super(template);

        if (template != null)
        {
            engineActionGUID = template.getEngineActionGUID();
        }
    }


    /**
     * Return the unique identifier of the governance action.
     *
     * @return string guid
     */
    public String getEngineActionGUID()
    {
        return engineActionGUID;
    }


    /**
     * Set up the unique identifier of the engine action.
     *
     * @param engineActionGUID string guid
     */
    public void setEngineActionGUID(String engineActionGUID)
    {
        this.engineActionGUID = engineActionGUID;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "EngineActionEvent{" +
                       "engineActionGUID=" + engineActionGUID +
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
        EngineActionEvent that = (EngineActionEvent) objectToCompare;
        return Objects.equals(engineActionGUID, that.engineActionGUID);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), engineActionGUID);
    }
}
