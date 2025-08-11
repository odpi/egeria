/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ReportTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * DataAssetProperties is a java bean used to create assets associated with data.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DataStoreProperties.class, name = "DataStoreProperties"),
        @JsonSubTypes.Type(value = DataSetProperties.class, name = "DataSetProperties"),
        @JsonSubTypes.Type(value = DataFeedProperties.class, name = "DataFeedProperties"),
        @JsonSubTypes.Type(value = ReportTypeProperties.class, name = "ReportTypeProperties"),
})
public class DataAssetProperties extends AssetProperties
{
    /**
     * Default constructor
     */
    public DataAssetProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.DATA_ASSET.typeName);
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public DataAssetProperties(DataAssetProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DataAssetProperties(AssetProperties template)
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
        return "DataAssetProperties{} " + super.toString();
    }
}
