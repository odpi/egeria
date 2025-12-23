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
 * Business imperatives define a business goal that is critical to the success of the organization.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class BusinessImperativeProperties extends GovernanceDriverProperties
{
    /**
     * Default Constructor
     */
    public BusinessImperativeProperties()
    {
        super();
        super.typeName = OpenMetadataType.BUSINESS_IMPERATIVE.typeName;
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public BusinessImperativeProperties(BusinessImperativeProperties template)
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
        return "BusinessImperativeProperties{" +
                "} " + super.toString();
    }
}
