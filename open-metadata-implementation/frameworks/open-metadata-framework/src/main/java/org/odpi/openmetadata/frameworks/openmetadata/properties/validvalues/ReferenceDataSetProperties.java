/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ReferenceDataSetProperties provides the properties for a top level reference data set.
 * This element indicates that the elements linked below are part of a collections that should be synchronized together.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReferenceDataSetProperties extends ReferenceDataValueProperties
{
    /**
     * Constructor
     */
    public ReferenceDataSetProperties()
    {
        super();
        super.typeName = OpenMetadataType.REFERENCE_DATA_SET.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ReferenceDataSetProperties(ValidValueDefinitionProperties template)
    {
        super(template);
    }


    /**
     * Generate a string containing the properties.
     *
     * @return string value
     */
    @Override
    public String toString()
    {
        return "ReferenceDataSetProperties{" + '}';
    }
}
