/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ProvisioningActionProcessProperties describes a governance action process that provisions a digital
 * resource to a target environment.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProvisioningActionProcessProperties extends GovernanceActionProcessProperties
{
    /**
     * Default constructor
     */
    public ProvisioningActionProcessProperties()
    {
        super();
        super.typeName = OpenMetadataType.PROVISIONING_ACTION_PROCESS.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProvisioningActionProcessProperties(GovernanceActionProcessProperties template)
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
        return "ProvisioningActionProcessProperties{} " + super.toString();
    }
}
