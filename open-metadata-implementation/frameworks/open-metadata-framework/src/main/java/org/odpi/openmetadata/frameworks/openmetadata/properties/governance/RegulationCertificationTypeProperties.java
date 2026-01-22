/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A regulation article  is an article in a regulation. Dividing the regulation into articles can help when
 * planning compliance approaches and reporting on the effectiveness of the associated activities.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RegulationCertificationTypeProperties extends LabeledRelationshipProperties
{
    /**
     * Default Constructor
     */
    public RegulationCertificationTypeProperties()
    {
        super();
        super.typeName = OpenMetadataType.REGULATION_CERTIFICATION_TYPE.typeName;
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public RegulationCertificationTypeProperties(RegulationCertificationTypeProperties template)
    {
        super(template);
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "RegulationCertificationTypeProperties{" +
                "} " + super.toString();
    }
}
