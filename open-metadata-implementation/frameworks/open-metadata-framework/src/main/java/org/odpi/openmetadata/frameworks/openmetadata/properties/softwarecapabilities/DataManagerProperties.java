/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EngineProperties describes the properties of an engine.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
// todo this mapping is not complete
@JsonSubTypes({
        @JsonSubTypes.Type(value = DatabaseManagerProperties.class, name = "DatabaseManagerProperties"),
        @JsonSubTypes.Type(value = DataAccessManagerProperties.class, name = "DataAccessManagerProperties"),

})
public class DataManagerProperties extends SoftwareCapabilityProperties
{
    /**
     * Default constructor
     */
    public DataManagerProperties()
    {
        super();
        super.typeName = OpenMetadataType.DATA_MANAGER.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DataManagerProperties(DataManagerProperties template)
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
        return "EngineProperties{} " + super.toString();
    }
}
