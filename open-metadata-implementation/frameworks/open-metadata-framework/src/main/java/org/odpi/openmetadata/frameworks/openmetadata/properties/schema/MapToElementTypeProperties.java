/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.schema;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MapToElementTypeProperties describes the properties for the MapToElementType relationship between a map schema type
 * and a mapped schema type.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MapToElementTypeProperties extends RelationshipBeanProperties
{
    /**
     * Default constructor
     */
    public MapToElementTypeProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.MAP_TO_ELEMENT_TYPE_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public MapToElementTypeProperties(MapToElementTypeProperties template)
    {
        super(template);
    }

    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "MapToElementTypeProperties{} " + super.toString();
    }
}
