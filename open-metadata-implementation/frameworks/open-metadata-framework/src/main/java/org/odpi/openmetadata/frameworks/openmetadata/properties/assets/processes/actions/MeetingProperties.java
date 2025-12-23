/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CommunityProperties describes the core properties of a community.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MeetingProperties extends ActionProperties
{
    private String       objective = null;
    private List<String> minutes   = null;
    private List<String> decisions   = null;



    /**
     * Default constructor
     */
    public MeetingProperties()
    {
        super();
        super.typeName = OpenMetadataType.MEETING.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public MeetingProperties(MeetingProperties template)
    {
        super(template);

        if (template != null)
        {
            objective = template.getObjective();
            minutes = template.getMinutes();
            decisions = template.getDecisions();
        }
    }


    /**
     * Return the objective of the meeting - what it is trying to achieve?
     *
     * @return text
     */
    public String getObjective()
    {
        return objective;
    }


    /**
     * Set up the objective of the meeting - what it is trying to achieve?
     *
     * @param objective text
     */
    public void setObjective(String objective)
    {
        this.objective = objective;
    }


    public List<String> getMinutes()
    {
        return minutes;
    }

    public void setMinutes(List<String> minutes)
    {
        this.minutes = minutes;
    }

    public List<String> getDecisions()
    {
        return decisions;
    }

    public void setDecisions(List<String> decisions)
    {
        this.decisions = decisions;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "MeetingProperties{" +
                "objective='" + objective + '\'' +
                ", minutes=" + minutes +
                ", decisions=" + decisions +
                "} " + super.toString();
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
        MeetingProperties meeting = (MeetingProperties) objectToCompare;
        return Objects.equals(objective, meeting.objective) && Objects.equals(minutes, meeting.minutes) && Objects.equals(decisions, meeting.decisions);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), objective, minutes, decisions);
    }
}
