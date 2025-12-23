/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A governance procedure describes a manual procedure that is performed under certain situations. An example would
 * be a visual check of a person's government identity document such as a passport or driving license.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = MethodologyProperties.class, name = "MethodologyProperties"),
        })
public class GovernanceProcedureProperties extends GovernanceControlProperties
{
    /**
     * Default Constructor
     */
    public GovernanceProcedureProperties()
    {
        super();
        super.typeName = OpenMetadataType.GOVERNANCE_PROCEDURE.typeName;
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public GovernanceProcedureProperties(GovernanceProcedureProperties template)
    {
        super(template);
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "GovernanceProcedureProperties{" +
                "} " + super.toString();
    }
}
