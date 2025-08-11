/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Many regulations and industry bodies define certifications that can confirm a level of support, capability
 * or competence in an aspect of a digital organization's operation.  Having certifications may be
 * necessary to operating legally or may be a business advantage.   The certifications awarded can be captured
 * in the metadata repository to enable both use and management of the certification process.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CertificationTypeProperties extends TermsAndConditionsProperties
{
    /**
     * Default Constructor
     */
    public CertificationTypeProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.CERTIFICATION_TYPE.typeName);
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public CertificationTypeProperties(TermsAndConditionsProperties template)
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
        return "CertificationTypeProperties{" +
                "} " + super.toString();
    }
}
