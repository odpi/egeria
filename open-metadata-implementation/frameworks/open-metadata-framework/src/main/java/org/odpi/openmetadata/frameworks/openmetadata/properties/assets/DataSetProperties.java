/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.databases.DatabaseSchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.display.FormProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.display.QueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.display.ReportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.topics.TopicProperties;

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
                @JsonSubTypes.Type(value = DatabaseSchemaProperties.class, name = "DatabaseSchemaProperties"),
                @JsonSubTypes.Type(value = FormProperties.class, name = "FormProperties"),
                @JsonSubTypes.Type(value = ReportProperties.class, name = "ReportProperties"),
                @JsonSubTypes.Type(value = QueryProperties.class, name = "QueryProperties"),
                @JsonSubTypes.Type(value = TopicProperties.class, name = "TopicProperties"),
        })
public class DataSetProperties extends DataAssetProperties
{
    /**
     * Default constructor
     */
    public DataSetProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DataSetProperties(DataSetProperties template)
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
        return "DataSetProperties{} " + super.toString();
    }
}
