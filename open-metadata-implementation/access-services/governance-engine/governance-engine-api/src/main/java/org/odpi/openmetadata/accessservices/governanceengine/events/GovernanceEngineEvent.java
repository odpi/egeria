/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.events;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceEngineEvent provides a common base for all events from the access service.
 * It implements Serializable and a version identifier.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
                      @JsonSubTypes.Type(value = GovernanceEngineConfigurationEvent.class, name = "GovernanceEngineConfigurationEvent"),
                      @JsonSubTypes.Type(value = GovernanceServiceConfigurationEvent.class, name = "GovernanceServiceConfigurationEvent"),
                      @JsonSubTypes.Type(value = WatchdogGovernanceServiceEvent.class, name = "WatchdogGovernanceServiceEvent"),
                      @JsonSubTypes.Type(value = EngineActionEvent.class, name = "EngineActionEvent")
})
public abstract class GovernanceEngineEvent implements Serializable
{
    private static final long serialVersionUID = 1L;

    private long                      eventVersionId       = 1L;
    private GovernanceEngineEventType eventType            = null;
    private String                    governanceEngineGUID = null;
    private String                    governanceEngineName = null;

    /**
     * Default Constructor sets the properties to nulls
     */
    GovernanceEngineEvent()
    {
        /*
         * Nothing to do.
         */
    }


    /**
     * Copy/clone constructor set values from the template
     *
     * @param template object to copy
     */
    GovernanceEngineEvent(GovernanceEngineEvent template)
    {
        if (template != null)
        {
            this.eventVersionId = template.getEventVersionId();
            this.eventType = template.getEventType();
            this.governanceEngineGUID = template.getGovernanceEngineGUID();
            this.governanceEngineName = template.getGovernanceEngineName();
        }
    }


    /**
     * Return the event version id for this event structure.
     *
     * @return long
     */
    public long getEventVersionId()
    {
        return eventVersionId;
    }


    /**
     * Set up the event version id for this event structure.
     *
     * @param eventVersionId long
     */
    public void setEventVersionId(long eventVersionId)
    {
        this.eventVersionId = eventVersionId;
    }


    /**
     * Return the type of event.
     *
     * @return event type enum
     */
    public GovernanceEngineEventType getEventType()
    {
        return eventType;
    }


    /**
     * Set up the type of event.
     *
     * @param eventType - event type enum
     */
    public void setEventType(GovernanceEngineEventType eventType)
    {
        this.eventType = eventType;
    }


    /**
     * Return the unique identifier of the governance engine that has a configuration change.
     *
     * @return string guid
     */
    public String getGovernanceEngineGUID()
    {
        return governanceEngineGUID;
    }


    /**
     * Set up the unique identifier of the governance engine that has a configuration change.
     *
     * @param governanceEngineGUID string guid
     */
    public void setGovernanceEngineGUID(String governanceEngineGUID)
    {
        this.governanceEngineGUID = governanceEngineGUID;
    }


    /**
     * Return the unique name of the governance engine that has a configuration change.
     *
     * @return string name
     */
    public String getGovernanceEngineName()
    {
        return governanceEngineName;
    }


    /**
     * Set up the unique name of the governance engine that has a configuration change.
     *
     * @param governanceEngineName string name
     */
    public void setGovernanceEngineName(String governanceEngineName)
    {
        this.governanceEngineName = governanceEngineName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceEngineEvent{" +
                       "eventVersionId=" + eventVersionId +
                       ", eventType=" + eventType +
                       ", governanceEngineGUID='" + governanceEngineGUID + '\'' +
                       ", governanceEngineName='" + governanceEngineName + '\'' +
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
        GovernanceEngineEvent that = (GovernanceEngineEvent) objectToCompare;
        return eventVersionId == that.eventVersionId &&
                       eventType == that.eventType &&
                       Objects.equals(governanceEngineGUID, that.governanceEngineGUID) &&
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
        return Objects.hash(eventVersionId, eventType, governanceEngineGUID, governanceEngineName);
    }
}
