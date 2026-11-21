/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.datavalues;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataValueSourceProperties describes the link between a data value and the asset for the digital resource where the data is stored.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataValueSourceProperties extends LabeledRelationshipProperties
{
    /**
     * Default constructor
     */
    public DataValueSourceProperties()
    {
        super();
        super.typeName = OpenMetadataType.DATA_VALUE_SOURCE_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public DataValueSourceProperties(DataValueSourceProperties template)
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
        return "DataValueSourceProperties{" +
                "} " + super.toString();
    }
}
