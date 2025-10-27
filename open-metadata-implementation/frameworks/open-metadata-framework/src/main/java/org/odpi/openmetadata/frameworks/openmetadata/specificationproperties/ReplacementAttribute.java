/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.specificationproperties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SpecificationPropertyType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ReplacementAttribute characterises one of the attribute values that should be provided when using a specific
 * template.  A replacement attribute value is ued to update the principle entity of a template.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReplacementAttribute extends ReplacementAttributeType
{
    /**
     * Default constructor
     */
    public ReplacementAttribute()
    {
        super();
        super.setSpecificationPropertyType(SpecificationPropertyType.REPLACEMENT_ATTRIBUTE);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ReplacementAttribute(ReplacementAttributeType template)
    {
        super(template);
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ReplacementAttribute{" +
                "} " + super.toString();
    }
}
