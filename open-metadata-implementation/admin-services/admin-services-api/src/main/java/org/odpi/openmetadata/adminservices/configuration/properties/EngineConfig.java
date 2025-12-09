/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EngineConfig provides the properties to configure a single governance engine in an service (in an engine hosting server).
 * The configuration for each of these engines is extracted from the partner OMAS using
 * the engine name as the qualified name.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EngineConfig extends OMAGServerClientConfig
{
    private String engineId            = UUID.randomUUID().toString();
    private String engineQualifiedName = null;
    private String engineUserId        = null;


    /**
     * Default constructor
     */
    public EngineConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public EngineConfig(EngineConfig template)
    {
        super(template);

        if (template != null)
        {
            engineId            = template.getEngineId();
            engineQualifiedName = template.getEngineQualifiedName();
            engineUserId        = template.getEngineUserId();
        }
    }


    /**
     * Return the unique identifier for this governance engine.  It is used when registering a listener with the one of
     * the OutTopics of the metadata services access services.
     *
     * @return String identifier
     */
    public String getEngineId()
    {
        return engineId;
    }


    /**
     * Set up the unique identifier for this governance engine.  It is used when registering a listener with the one of
     * the OutTopics of the metadata services access services.
     *
     * @param engineId String identifier
     */
    public void setEngineId(String engineId)
    {
        this.engineId = engineId;
    }


    /**
     * Return the name of the governance engine.  This is the qualified name of the GovernanceEngine entity in the metadata repository that
     * represents the engine.
     *
     * @return String name
     */
    public String getEngineQualifiedName()
    {
        return engineQualifiedName;
    }


    /**
     * Set up the name of the governance engine.   This is the qualified name of the GovernanceEngine entity in the metadata repository that
     * represents the engine.
     *
     * @param engineQualifiedName String name
     */
    public void setEngineQualifiedName(String engineQualifiedName)
    {
        this.engineQualifiedName = engineQualifiedName;
    }


    /**
     * Return the userId that the governance engine should use when calling the metadata server. (Null means use the Engine Host's userId.)
     *
     * @return string userId
     */
    public String getEngineUserId()
    {
        return engineUserId;
    }


    /**
     * Set up the userId that the governance engine should use when calling the metadata server. (Null means use the Engine Host's userId.)
     *
     * @param engineUserId string userId
     */
    public void setEngineUserId(String engineUserId)
    {
        this.engineUserId = engineUserId;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "EngineConfig{" +
                       "engineId=" + engineId +
                       ", engineQualifiedName='" + engineQualifiedName + '\'' +
                       ", engineUserId='" + engineUserId + '\'' +
                       '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
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
        EngineConfig that = (EngineConfig) objectToCompare;
        return Objects.equals(engineId, that.engineId) &&
                       Objects.equals(engineQualifiedName, that.engineQualifiedName) &&
                       Objects.equals(engineUserId, that.engineUserId);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getEngineId(), getEngineQualifiedName(), getEngineUserId());
    }
}
