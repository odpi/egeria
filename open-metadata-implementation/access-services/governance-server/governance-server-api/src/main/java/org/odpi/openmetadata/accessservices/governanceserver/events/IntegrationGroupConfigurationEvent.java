/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceserver.events;

import com.fasterxml.jackson.annotation.*;

import java.io.Serial;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * IntegrationGroupConfigurationEvent is used to inform listening integration daemons that the configuration of
 * one of the integration groups has changed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = GovernanceServiceConfigurationEvent.class, name = "GovernanceServiceConfigurationEvent"),
})
public class IntegrationGroupConfigurationEvent extends GovernanceServerEvent
{
    @Serial
    private static final long serialVersionUID = 1L;

    private String integrationGroupGUID = null;
    private String integrationGroupName = null;

    /**
     * Default constructor
     */
    public IntegrationGroupConfigurationEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public IntegrationGroupConfigurationEvent(IntegrationGroupConfigurationEvent template)
    {
        super(template);

        if (template != null)
        {
            this.integrationGroupGUID = template.getIntegrationGroupGUID();
            this.integrationGroupName = template.getIntegrationGroupName();
        }
    }


    /**
     * Return the unique identifier of the Integration Group that has a configuration change.
     *
     * @return string guid
     */
    public String getIntegrationGroupGUID()
    {
        return integrationGroupGUID;
    }


    /**
     * Set up the unique identifier of the Integration Group that has a configuration change.
     *
     * @param integrationGroupGUID string guid
     */
    public void setIntegrationGroupGUID(String integrationGroupGUID)
    {
        this.integrationGroupGUID = integrationGroupGUID;
    }


    /**
     * Return the unique name of the Integration Group that has a configuration change.
     *
     * @return string name
     */
    public String getIntegrationGroupName()
    {
        return integrationGroupName;
    }


    /**
     * Set up the unique name of the Integration Group that has a configuration change.
     *
     * @param integrationGroupName string name
     */
    public void setIntegrationGroupName(String integrationGroupName)
    {
        this.integrationGroupName = integrationGroupName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "IntegrationGroupConfigurationEvent{" +
                "governanceEngineGUID='" + integrationGroupGUID + '\'' +
                ", governanceEngineName='" + integrationGroupName + '\'' +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        IntegrationGroupConfigurationEvent that = (IntegrationGroupConfigurationEvent) objectToCompare;
        return Objects.equals(integrationGroupGUID, that.integrationGroupGUID) &&
                Objects.equals(integrationGroupName, that.integrationGroupName);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), integrationGroupGUID, integrationGroupName);
    }
}
