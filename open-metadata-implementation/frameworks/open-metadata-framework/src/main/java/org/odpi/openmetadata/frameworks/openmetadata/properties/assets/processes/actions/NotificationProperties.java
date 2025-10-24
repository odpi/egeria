/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The "Notification" describes information that needs to be passed to an actor
 * (see AssignmentScope). It is typically assigned to a person role.  The actor
 * then acts on the information and closes it when no longer relevant.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NotificationProperties extends ActionProperties
{
    private String systemAction = null;
    private String userResponse = null;


    /**
     * Default constructor
     */
    public NotificationProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.NOTIFICATION.typeName);;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NotificationProperties(NotificationProperties template)
    {
        super(template);

        if (template != null)
        {
            systemAction = template.getSystemAction();
            userResponse = template.getUserResponse();
        }
    }


    /**
     * Return the action taken by the producer of this notification.
     *
     * @return string
     */
    public String getSystemAction()
    {
        return systemAction;
    }


    /**
     * Set up the action taken by the producer of this notification.
     *
     * @param systemAction string
     */
    public void setSystemAction(String systemAction)
    {
        this.systemAction = systemAction;
    }


    /**
     * Return the suggested action that the receiver of this notification should take.
     *
     * @return string
     */
    public String getUserResponse()
    {
        return userResponse;
    }


    /**
     * Set up the suggested action that the receiver of this notification should take.
     *
     * @param userResponse string
     */
    public void setUserResponse(String userResponse)
    {
        this.userResponse = userResponse;
    }

    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "NotificationProperties{" +
                "systemAction='" + systemAction + '\'' +
                ", userResponse='" + userResponse + '\'' +
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
        NotificationProperties that = (NotificationProperties) objectToCompare;
        return Objects.equals(systemAction, that.systemAction) && Objects.equals(userResponse, that.userResponse);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), systemAction, userResponse);
    }
}
