/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalModelSourceProperties stores information about a link to an external analytical/AI model that is
 * relevant to open metadata.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExternalModelSourceProperties extends ExternalReferenceProperties
{
    /**
     * Default constructor
     */
    public ExternalModelSourceProperties()
    {
        super();
        super.typeName = OpenMetadataType.EXTERNAL_MODEL_SOURCE.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public ExternalModelSourceProperties(ExternalReferenceProperties template)
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
        return "ExternalModelSourceProperties{} " + super.toString();
    }
}