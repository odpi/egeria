/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CohortMemberProperties describe the properties needed to describe a server's membership of a cohort.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CohortMemberProperties extends SoftwareCapabilityProperties
{
    /**
     * Default constructor.
     */
    public CohortMemberProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.COHORT_MEMBER.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public CohortMemberProperties(SoftwareServiceProperties template)
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
        return "CohortMemberProperties{} " + super.toString();
    }
}
