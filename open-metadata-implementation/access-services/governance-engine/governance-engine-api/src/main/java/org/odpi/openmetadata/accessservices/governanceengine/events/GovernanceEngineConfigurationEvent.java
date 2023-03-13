/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.events;

import com.fasterxml.jackson.annotation.*;

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
public class GovernanceEngineConfigurationEvent extends GovernanceEngineEvent
{
    private static final long serialVersionUID = 1L;



    /**
     * Default constructor
     */
    public GovernanceEngineConfigurationEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public GovernanceEngineConfigurationEvent(GovernanceEngineConfigurationEvent template)
    {
        super(template);
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceEngineConfigurationEvent{" +
                       "eventVersionId=" + getEventVersionId() +
                       ", eventType=" + getEventType() +
                       ", governanceEngineGUID='" + getGovernanceEngineGUID() + '\'' +
                       ", governanceEngineName='" + getGovernanceEngineName() + '\'' +
                       '}';
    }
}
