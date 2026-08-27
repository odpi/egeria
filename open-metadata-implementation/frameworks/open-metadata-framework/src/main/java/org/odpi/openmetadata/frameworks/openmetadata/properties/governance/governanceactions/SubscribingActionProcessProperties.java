/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SubscribingActionProcessProperties describes a governance action process that creates a subscription to a digital resource.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SubscribingActionProcessProperties extends GovernanceActionProcessProperties
{
    /**
     * Default constructor
     */
    public SubscribingActionProcessProperties()
    {
        super();
        super.typeName = OpenMetadataType.SUBSCRIBING_ACTION_PROCESS.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SubscribingActionProcessProperties(GovernanceActionProcessProperties template)
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
        return "SubscribingActionProcessProperties{} " + super.toString();
    }
}
