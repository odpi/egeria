/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EmbeddedProcessProperties defines the properties of a running process that is inder the control of another process.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TransientEmbeddedProcessProperties.class, name = "TransientEmbeddedProcessProperties"),
})
public class EmbeddedProcessProperties extends ActionProperties
{
    /**
     * Default constructor
     */
    public EmbeddedProcessProperties()
    {
        super();
        super.typeName = OpenMetadataType.EMBEDDED_PROCESS.typeName;
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public EmbeddedProcessProperties(EmbeddedProcessProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public EmbeddedProcessProperties(ActionProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.EMBEDDED_PROCESS.typeName;
    }



    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "EmbeddedProcessProperties{} " + super.toString();
    }
}
