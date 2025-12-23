/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.actors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataItemOwnerProperties describes a role for a person who is responsible for a data item as it flows between systems.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataItemOwnerProperties extends GovernanceRoleProperties
{
    /**
     * Default constructor
     */
    public DataItemOwnerProperties()
    {
        super();
        super.typeName = OpenMetadataType.DATA_ITEM_OWNER.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DataItemOwnerProperties(GovernanceRoleProperties template)
    {
        super(template);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataItemOwnerProperties{} " + super.toString();
    }
}