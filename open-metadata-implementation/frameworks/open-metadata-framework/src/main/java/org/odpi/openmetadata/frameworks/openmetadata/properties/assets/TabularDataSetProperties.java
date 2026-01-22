/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.referencedata.ReferenceCodeMappingTableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.referencedata.ReferenceCodeTableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataSetProperties is a class for representing a generic data set.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ReferenceCodeMappingTableProperties.class, name = "ReferenceCodeMappingProperties"),
                @JsonSubTypes.Type(value = ReferenceCodeTableProperties.class, name = "ReferenceCodeTableProperties"),
        })
public class TabularDataSetProperties extends DataSetProperties
{
    /**
     * Default constructor
     */
    public TabularDataSetProperties()
    {
        super();
        super.typeName = OpenMetadataType.TABULAR_DATA_SET.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public TabularDataSetProperties(TabularDataSetProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public TabularDataSetProperties(DataSetProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.TABULAR_DATA_SET.typeName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "TabularDataSetProperties{} " + super.toString();
    }
}
