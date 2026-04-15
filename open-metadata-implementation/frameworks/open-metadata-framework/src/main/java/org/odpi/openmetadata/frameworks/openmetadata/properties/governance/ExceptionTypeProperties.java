/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The exception type describes a way that an element may be exempt from, or in violation of a governance policy.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExceptionTypeProperties extends GovernanceControlProperties
{
    /**
     * Default Constructor
     */
    public ExceptionTypeProperties()
    {
        super();
        super.typeName = OpenMetadataType.EXCEPTION_TYPE.typeName;
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public ExceptionTypeProperties(ExceptionTypeProperties template)
    {
        super(template);
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public ExceptionTypeProperties(TermsAndConditionsProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.CERTIFICATION_TYPE.typeName;
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "CertificationTypeProperties{" +
                "} " + super.toString();
    }
}
